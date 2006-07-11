/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultRenderer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Stack;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.Clear;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.type.GenericType;
import org.jfree.layouting.layouter.content.type.ResourceType;
import org.jfree.layouting.layouter.content.type.TextType;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.output.pageable.graphics.PageDrawable;
import org.jfree.layouting.renderer.border.BorderFactory;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.BoxDefinitionFactory;
import org.jfree.layouting.renderer.model.DefaultBoxDefinitionFactory;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableImage;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.page.FastPageGrid;
import org.jfree.layouting.renderer.page.PageGrid;
import org.jfree.layouting.renderer.text.DefaultRenderableTextFactory;
import org.jfree.layouting.renderer.text.RenderableTextFactory;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.ui.DrawablePanel;
import org.jfree.util.Log;
import org.jfree.util.WaitingImageObserver;

/**
 * A renderer which builds a rendered page model. This one is suitable to handle
 * all pageable content (the most complex case).
 *
 * @author Thomas Morgner
 */
public class DefaultRenderer implements Renderer
{
  private static class DefaultFlowRendererState implements State
  {
    public DefaultFlowRendererState()
    {
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
            throws StateException
    {
      return new DefaultRenderer(layoutProcess);
    }
  }


  private LayoutProcess layoutProcess;
  private LogicalPageBox logicalPageBox;
  private Stack openFlows;
  private RenderableTextFactory textFactory;
  private BoxDefinitionFactory boxDefinitionFactory;
  private CodePointBuffer buffer;

  public DefaultRenderer(final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }

    this.layoutProcess = layoutProcess;
    this.openFlows = new Stack();
    this.boxDefinitionFactory =
            new DefaultBoxDefinitionFactory(new BorderFactory());
  }

  public void startDocument(final PageContext pageContext)
  {
    Log.debug("<document>");
    this.textFactory = new DefaultRenderableTextFactory(layoutProcess);

    // initialize the initial page-grid
    // for the first try, use a hard-coded page size. We should change that
    // before we go public ...
    final PageGrid pageGrid = new FastPageGrid(500000, 500000);

    // initialize the logical page. The logical page needs the page grid,
    // as this contains the hints for the physical page sizes.
    logicalPageBox = new LogicalPageBox(pageGrid);
  }

  public void startedPhysicalPageFlow(final LayoutContext context)
  {
    Log.debug("<special-flow>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    openFlows.push(new NormalFlowRenderBox(definition));
  }

  protected RenderBox getInsertationPoint()
  {
    NormalFlowRenderBox flow = (NormalFlowRenderBox) openFlows.peek();
    return flow.getInsertationPoint();
  }

  public void startedFlow(final LayoutContext context)
  {
    //DefaultPendingContentStorage flow = new DefaultPendingContentStorage();
    Log.debug("<flow>");
    if (openFlows.isEmpty())
    {
      // this is the first normal flow.
      openFlows.push(logicalPageBox.getNormalFlow());
    }
    else
    {
      RenderBox currentBox = getInsertationPoint();
      final BoxDefinition definition =
              boxDefinitionFactory.createBlockBoxDefinition
              (context, layoutProcess.getOutputMetaData());
      NormalFlowRenderBox newFlow = new NormalFlowRenderBox(definition);
      currentBox.addChild(newFlow.getPlaceHolder());
      currentBox.getNormalFlow().addFlow(newFlow);
      openFlows.push(newFlow);
    }
  }

  public void startedTable(final LayoutContext context)
  {
    Log.debug("<table>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableRenderBox tableRenderBox = new TableRenderBox(definition);
    applyClear(context, tableRenderBox);

    getInsertationPoint().addChild(tableRenderBox);
  }

  public void startedTableSection(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug("<table-section>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableSectionRenderBox tableRenderBox = new TableSectionRenderBox(definition);
    getInsertationPoint().addChild(tableRenderBox);
  }

  public void startedTableRow(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug("<table-row>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableRowRenderBox tableRenderBox = new TableRowRenderBox(definition);
    getInsertationPoint().addChild(tableRenderBox);
  }

  public void startedTableCell(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug("<table-cell>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableCellRenderBox tableRenderBox = new TableCellRenderBox(definition);
    getInsertationPoint().addChild(tableRenderBox);
  }

  public void startedBlock(final LayoutContext context)
  {
    Log.debug("<block>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    BlockRenderBox blockBox = new BlockRenderBox(definition);
    applyClear(context, blockBox);

    getInsertationPoint().addChild(blockBox);
  }

  public void startedRootInline(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug("<paragraph>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    ParagraphRenderBox paragraphBox = new ParagraphRenderBox(definition);
    applyClear(context, paragraphBox);

    getInsertationPoint().addChild(paragraphBox);
  }

  public void startedInline(final LayoutContext context)
  {
    Log.debug("<inline>");
    final BoxDefinition definition =
            boxDefinitionFactory.createInlineBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    InlineRenderBox inlineBox = new InlineRenderBox(definition);
    applyClear(context, inlineBox);

    getInsertationPoint().addChild(inlineBox);
  }

  private void applyClear(final LayoutContext context,
                          final RenderBox inlineBox)
  {
    final CSSValue clearValue = context.getStyle().getValue(BoxStyleKeys.CLEAR);
//    Log.debug ("CLEAR VALUE: " + clearValue);
    if (Clear.BOTH.equals(clearValue))
    {
      inlineBox.setClearLeft(true);
      inlineBox.setClearRight(true);
    }
    else if (Clear.LEFT.equals(clearValue))
    {
      inlineBox.setClearLeft(true);
    }
    else if (Clear.RIGHT.equals(clearValue))
    {
      inlineBox.setClearRight(true);
    }
    else if (Clear.START.equals(clearValue))
    {
      inlineBox.setClearLeft(true);
    }
    else if (Clear.END.equals(clearValue))
    {
      inlineBox.setClearRight(true);
    }
  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
  {
//    Log.debug("CONTENT: " + content); // not yet
    if (content instanceof GenericType)
    {
      GenericType generic = (GenericType) content;
      ResourceKey source = null;
      if (content instanceof ResourceType)
      {
        ResourceType resourceType = (ResourceType) content;
        source = resourceType.getContent().getSource();
      }
      final Object raw = generic.getRaw();
      if (raw instanceof Image)
      {
        RenderableImage image = createImage((Image) raw, source);
        if (image != null)
        {
          getInsertationPoint().addChild(image);
          return;
        }
        else
        {
          // todo check, if we have alternate text for this.
          final RenderableText[] text = createText("IMAGE MISSING", context);
          getInsertationPoint().addChilds(text);
          return;
        }
      }
    }

    if (content instanceof TextType)
    {
      TextType textRaw = (TextType) content;
      final String textStr = textRaw.getText();
      final RenderableText[] text = createText(textStr, context);
//      Log.debug ("Text: " + textStr + " -> " + text[0].getRawText());
      getInsertationPoint().addChilds(text);
    }
  }

  private RenderableText[] createText(final String str,
                                    final LayoutContext context)
  {
    if (buffer != null)
    {
      buffer.setCursor(0);
    }
    buffer = Utf16LE.getInstance().decodeString(str, buffer);
    final int[] data = buffer.getBuffer();

    final RenderableText[] text = textFactory.createText(data, 0, data.length, context);
//    Log.debug("Generated Text " + text.length + " text token");
//
//    for (int x = 0; x < text.length; x++)
//    {
//      StringBuffer b = new StringBuffer();
//      RenderableText t = text[x];
//      Glyph[] gs = t.getGlyphs();
//      for (int i = t.getOffset(); i < t.getLength(); i++)
//      {
//        b.append((char) (0xffff & gs[i].getCodepoint()));
//      }
//      Log.debug("Generated Text: " + x + ": " + b.toString() +
//              " [" + gs.length + "]");
//    }
    return text;
  }

  private RenderableImage createImage(Image image, ResourceKey source)
  {
    final WaitingImageObserver wobs = new WaitingImageObserver(image);
    wobs.waitImageLoaded();
    if (wobs.isError())
    {
      return null;
    }
    else
    {
      final StrictDimension dims = StrictGeomUtility.createDimension
              (image.getWidth(null), image.getHeight(null));
      return new RenderableImage(image, source, dims);
    }
  }

  public void finishedInline()
  {
    Log.debug("</inline>");
    getInsertationPoint().close();
   // currentBox = (RenderBox) currentBox.getParent();
  }

  public void finishedRootInline() throws NormalizationException
  {
    Log.debug("</paragraph>");
    getInsertationPoint().close();
   // currentBox = (RenderBox) currentBox.getParent();
  }

  public void finishedBlock()
  {
    Log.debug("</block>");
    getInsertationPoint().close();
   // currentBox = (RenderBox) currentBox.getParent();
  }

  public void finishedTableCell()
  {
    Log.debug("</table-cell>");
    getInsertationPoint().close();
  //  currentBox = (RenderBox) currentBox.getParent();
  }

  public void finishedTableRow()
  {
    Log.debug("</table-row>");
    getInsertationPoint().close();
   // currentBox = (RenderBox) currentBox.getParent();
  }

  public void finishedTableSection()
  {
    Log.debug("</table-section>");
    getInsertationPoint().close();
 //   currentBox = (RenderBox) currentBox.getParent();
  }

  public void finishedTable()
  {
    Log.debug("</table>");
    getInsertationPoint().close();
  //  currentBox = (RenderBox) currentBox.getParent();
  }

  public void finishedFlow()
  {
    Log.debug("</flow>");
 //  NormalFlowRenderBox flow = (NormalFlowRenderBox) getInsertationPoint();
 //   currentBox = (RenderBox) flow.getPlaceHolder().getParent();
    getInsertationPoint().close();
    openFlows.pop();
  }

  public void finishedPhysicalPageFlow()
  {
    Log.debug("</special-flow>");
    getInsertationPoint().close();
    openFlows.pop();
  }

  public void finishedDocument()
  {
    Log.debug("</document>");
    logicalPageBox.close();

    // Ok, lets play a little bit
    LogicalPageBox rootBox = logicalPageBox;

    Log.debug("Min  H-Axis: " + rootBox.getMinimumSize(RenderNode.HORIZONTAL_AXIS));
    Log.debug("Min  V-Axis: " + rootBox.getMinimumSize(RenderNode.VERTICAL_AXIS));
    Log.debug("Pref H-Axis: " + rootBox.getPreferredSize(RenderNode.HORIZONTAL_AXIS));
    Log.debug("Pref V-Axis: " + rootBox.getPreferredSize(RenderNode.VERTICAL_AXIS));
    Log.debug("Max  H-Axis: " + rootBox.getMaximumSize(RenderNode.HORIZONTAL_AXIS));
    Log.debug("Max  V-Axis: " + rootBox.getMaximumSize(RenderNode.VERTICAL_AXIS));

    rootBox.setX(0);
    rootBox.setY(0);
    rootBox.setWidth(rootBox.getPreferredSize(RenderNode.HORIZONTAL_AXIS) / 2);
    rootBox.setHeight(rootBox.getMaximumSize(RenderNode.VERTICAL_AXIS));

    rootBox.validate();

    Log.debug("RootBox: (X,Y): " + rootBox.getX() + ", " + rootBox.getY());
    Log.debug("RootBox: (W,H): " + rootBox.getWidth() + ", " + rootBox.getHeight());

    final PageDrawable drawable = new PageDrawable(rootBox);
    drawable.print();

    final DrawablePanel comp = new DrawablePanel();
    comp.setDrawable(drawable);

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(comp, BorderLayout.CENTER);

    JDialog dialog = new JDialog();
    dialog.setModal(true);
    dialog.setContentPane(contentPane);
    dialog.setSize(800, 600);
    dialog.setVisible(true);
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    // initialize the initial page-grid
    // for the first try, use a hard-coded page size. We should change that
    // before we go public ...
    final PageGrid pageGrid = new FastPageGrid(500000, 500000);

    // initialize the logical page. The logical page needs the page grid,
    // as this contains the hints for the physical page sizes.
    logicalPageBox = new LogicalPageBox(pageGrid);
  }

  public State saveState() throws StateException
  {
    return new DefaultFlowRendererState();
  }
}
