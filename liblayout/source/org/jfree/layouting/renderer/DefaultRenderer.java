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
 * $Id: DefaultRenderer.java,v 1.12 2006/07/29 18:57:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.type.GenericType;
import org.jfree.layouting.layouter.content.type.ResourceType;
import org.jfree.layouting.layouter.content.type.TextType;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.output.pageable.graphics.PageDrawable;
import org.jfree.layouting.renderer.border.BorderFactory;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.BoxDefinitionFactory;
import org.jfree.layouting.renderer.model.DefaultBoxDefinitionFactory;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.MarkerRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.RenderNodeState;
import org.jfree.layouting.renderer.model.page.DefaultPageGrid;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.page.PrintSplitResult;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableColumnGroupNode;
import org.jfree.layouting.renderer.model.table.TableColumnNode;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.page.RenderPageContext;
import org.jfree.layouting.renderer.text.DefaultRenderableTextFactory;
import org.jfree.layouting.renderer.text.RenderableTextFactory;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.ui.Drawable;
import org.jfree.ui.DrawablePanel;
import org.jfree.ui.ExtendedDrawable;
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
  private RenderableTextFactory textFactory;
  private BoxDefinitionFactory boxDefinitionFactory;
  private CodePointBuffer buffer;
  private RenderPageContext pageContext;

  public DefaultRenderer(final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }

    this.layoutProcess = layoutProcess;
    this.boxDefinitionFactory =
            new DefaultBoxDefinitionFactory(new BorderFactory());
  }

  public void startDocument(final PageContext pageContext)
  {
    if (pageContext == null)
    {
      throw new NullPointerException();
    }

    Log.debug("<document>");
    this.pageContext = new RenderPageContext(pageContext);
    this.textFactory = new DefaultRenderableTextFactory(layoutProcess);
    final PageGrid pageGrid = new DefaultPageGrid(pageContext, layoutProcess.getOutputMetaData());

    // initialize the logical page. The logical page needs the page grid,
    // as this contains the hints for the physical page sizes.
    logicalPageBox = new LogicalPageBox(pageGrid);
    logicalPageBox.setRenderPageContext(this.pageContext);
  }

  public void startedPhysicalPageFlow(final LayoutContext context)
  {
    Log.debug("<special-flow>");
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    final CSSValue valign =
            context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN);
    final NormalFlowRenderBox item = new NormalFlowRenderBox(definition, valign);
    this.pageContext = pageContext.update(context);
    item.setRenderPageContext(pageContext);

    tryToValidate();

  }

  final boolean VALIDATE_AND_SPLIT = false;

  private void tryToValidate()
  {
    if (VALIDATE_AND_SPLIT)
    {

      if (logicalPageBox.isValidatable())
      {
        if (logicalPageBox.isOverflow())
        {
          Log.debug ("There is an overflow..");
          final PrintSplitResult printSplitResult = logicalPageBox.splitForPrint();
          logicalPageBox = printSplitResult.getPageBox();
        }
        else
        {
          Log.debug ("No overflow.");
        }

      }
      else
      {
        Log.debug ("Not validatable...");
      }
    }
    else
    {
    //  logicalPageBox.validate(RenderNodeState.FINISHED);
    }
  }

  protected RenderBox getInsertationPoint()
  {
//    NormalFlowRenderBox flow = (NormalFlowRenderBox) logicalPageBox.get();
    return logicalPageBox.getInsertationPoint();
  }

  public void startedFlow(final LayoutContext context)
  {
    //DefaultPendingContentStorage flow = new DefaultPendingContentStorage();

    textFactory.startText();

    Log.debug("<flow>");
    if (logicalPageBox.isNormalFlowActive())
    {
      // this is the first normal flow.
    }
    else
    {
      RenderBox currentBox = getInsertationPoint();
      currentBox.addChilds(textFactory.finishText());

      this.pageContext = pageContext.update(context);

      final BoxDefinition definition =
              boxDefinitionFactory.createBlockBoxDefinition
                      (context, layoutProcess.getOutputMetaData());
      final CSSValue valign =
              context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN);
      NormalFlowRenderBox newFlow = new NormalFlowRenderBox(definition, valign);
      newFlow.setRenderPageContext(pageContext);

      currentBox.addChild(newFlow.getPlaceHolder());
      currentBox.getNormalFlow().addFlow(newFlow);
    }

    tryToValidate();

  }

  public void startedTable(final LayoutContext context)
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<table>");
    textFactory.startText();

    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    final CSSValue borderSpacingVal =
            context.getStyle().getValue(TableStyleKeys.BORDER_SPACING);

    final RenderLength borderSpacing;
    final RenderLength rowSpacing;
    if (borderSpacingVal instanceof CSSValuePair)
    {
      CSSValuePair borderSpacingPair = (CSSValuePair) borderSpacingVal;
      rowSpacing = DefaultBoxDefinitionFactory.computeWidth
              (borderSpacingPair.getFirstValue(), context, layoutProcess.getOutputMetaData(), false, false);
      borderSpacing = DefaultBoxDefinitionFactory.computeWidth
              (borderSpacingPair.getSecondValue(), context, layoutProcess.getOutputMetaData(), false, false);
    }
    else
    {
      borderSpacing = RenderLength.EMPTY;
      rowSpacing = RenderLength.EMPTY;
    }
    this.pageContext = pageContext.update(context);

    TableRenderBox tableRenderBox =
            new TableRenderBox(definition, context, borderSpacing, rowSpacing);
    tableRenderBox.setRenderPageContext(pageContext);
    applyClear(context, tableRenderBox);

    getInsertationPoint().addChild(tableRenderBox);

    tryToValidate();

  }

  public void startedTableColumnGroup(final LayoutContext context)
          throws NormalizationException
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<table-col-group>");
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    TableColumnGroupNode columnGroupNode = new TableColumnGroupNode(definition, context);
    getInsertationPoint().addChild(columnGroupNode);

    tryToValidate();

  }

  public void startedTableColumn(final LayoutContext context)
          throws NormalizationException
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<table-col>");
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    TableColumnNode columnGroupNode = new TableColumnNode(definition, context);
    getInsertationPoint().addChild(columnGroupNode);

    tryToValidate();

  }

  public void startedTableSection(final LayoutContext context)
          throws NormalizationException
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<table-section>");
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final CSSValue displayRole =
            context.getStyle().getValue(BoxStyleKeys.DISPLAY_ROLE);
    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    TableSectionRenderBox tableRenderBox =
            new TableSectionRenderBox(definition, displayRole);
    tableRenderBox.setRenderPageContext(pageContext);
    getInsertationPoint().addChild(tableRenderBox);

    tryToValidate();

  }

  public void startedTableRow(final LayoutContext context)
          throws NormalizationException
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<table-row>");
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    TableRowRenderBox tableRenderBox = new TableRowRenderBox(definition, false);
    tableRenderBox.setRenderPageContext(pageContext);
    getInsertationPoint().addChild(tableRenderBox);

    tryToValidate();

  }

  public void startedTableCell(final LayoutContext context)
          throws NormalizationException
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<table-cell>");
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    TableCellRenderBox tableRenderBox =
            new TableCellRenderBox(definition, context);
    tableRenderBox.setRenderPageContext(pageContext);
    getInsertationPoint().addChild(tableRenderBox);

    tryToValidate();

  }

  public void startedBlock(final LayoutContext context)
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<block>");
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    final CSSValue valign =
            context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN);
    BlockRenderBox blockBox = new BlockRenderBox(definition, valign);
    applyClear(context, blockBox);
    blockBox.setRenderPageContext(pageContext);
    getInsertationPoint().addChild(blockBox);

    tryToValidate();
  }

  public void startedMarker(final LayoutContext context)
          throws NormalizationException
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<marker>");
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createInlineBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    MarkerRenderBox markerBox = new MarkerRenderBox(definition, context);
    markerBox.setRenderPageContext(pageContext);
    getInsertationPoint().addChild(markerBox);

    tryToValidate();
  }

  public void startedRootInline(final LayoutContext context)
          throws NormalizationException
  {
    getInsertationPoint().addChilds(textFactory.finishText());
    textFactory.startText();

    Log.debug("<paragraph>");
    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createBlockBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    ParagraphRenderBox paragraphBox =
            new ParagraphRenderBox(definition, context);
    paragraphBox.setRenderPageContext(pageContext);
    applyClear(context, paragraphBox);

    getInsertationPoint().addChild(paragraphBox);

    tryToValidate();
  }

  public void startedInline(final LayoutContext context)
  {
    getInsertationPoint().addChilds(textFactory.finishText());

    Log.debug("<inline>");
    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
            boxDefinitionFactory.createInlineBoxDefinition
                    (context, layoutProcess.getOutputMetaData());
    final CSSValue valign =
            context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN);
    InlineRenderBox inlineBox = new InlineRenderBox(definition, valign);
    inlineBox.setRenderPageContext(pageContext);
    applyClear(context, inlineBox);

    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChild(inlineBox);

    tryToValidate();
  }

  private void applyClear(final LayoutContext context,
                          final RenderBox box)
  {
    final CSSValue clearValue = context.getStyle().getValue(BoxStyleKeys.CLEAR);
//    Log.debug ("CLEAR VALUE: " + clearValue);
    if (Clear.BOTH.equals(clearValue))
    {
      box.setClearLeft(true);
      box.setClearRight(true);
    }
    else if (Clear.LEFT.equals(clearValue))
    {
      box.setClearLeft(true);
    }
    else if (Clear.RIGHT.equals(clearValue))
    {
      box.setClearRight(true);
    }
    else if (Clear.START.equals(clearValue))
    {
      box.setClearLeft(true);
    }
    else if (Clear.END.equals(clearValue))
    {
      box.setClearRight(true);
    }
  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
  {
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
        RenderableReplacedContent replacedContent =
                createImage((Image) raw, source, context);
        if (replacedContent != null)
        {
          getInsertationPoint().addChilds(textFactory.finishText());
          getInsertationPoint().addChild(replacedContent);
          tryToValidate();
          return;
        }
      }
      else if (raw instanceof Drawable)
      {
        RenderableReplacedContent replacedContent =
                createDrawable((Drawable) raw, source, context);
        if (replacedContent != null)
        {
          getInsertationPoint().addChilds(textFactory.finishText());
          getInsertationPoint().addChild(replacedContent);
          tryToValidate();
          return;
        }
      }
    }

    if (content instanceof TextType)
    {
      TextType textRaw = (TextType) content;
      final String textStr = textRaw.getText();
      final RenderNode[] text = createText(textStr, context);
      if (text.length == 0)
      {
        return;
      }

      final RenderBox insertationPoint = getInsertationPoint();
      insertationPoint.addChilds(text);
      tryToValidate();
    }
  }

  private RenderNode[] createText(final String str,
                                  final LayoutContext context)
  {
    Log.debug("Insertation Point: " + getInsertationPoint());
    Log.debug("Add Text " + str);
    if (buffer != null)
    {
      buffer.setCursor(0);
    }
    buffer = Utf16LE.getInstance().decodeString(str, buffer);
    final int[] data = buffer.getBuffer();

    return textFactory.createText(data, 0, data.length, context);
  }

  private RenderableReplacedContent createImage(Image image,
                                                ResourceKey source,
                                                LayoutContext context)
  {
    final WaitingImageObserver wobs = new WaitingImageObserver(image);
    wobs.waitImageLoaded();
    if (wobs.isError())
    {
      return null;
    }

    final CSSValue widthVal = context.getStyle().getValue(BoxStyleKeys.WIDTH);
    final RenderLength width = DefaultBoxDefinitionFactory.computeWidth
            (widthVal, context, layoutProcess.getOutputMetaData(), true, false);

    final CSSValue heightVal = context.getStyle().getValue(BoxStyleKeys.HEIGHT);
    final RenderLength height = DefaultBoxDefinitionFactory.computeWidth
            (heightVal, context, layoutProcess.getOutputMetaData(), true, false);
    final StrictDimension dims = StrictGeomUtility.createDimension
            (image.getWidth(null), image.getHeight(null));
    final CSSValue valign =
            context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN);
    return new RenderableReplacedContent(image, source, dims, width, height, valign);
  }


  private RenderableReplacedContent createDrawable(Drawable image,
                                                   ResourceKey source,
                                                   LayoutContext context)
  {
    final StrictDimension dims = new StrictDimension();

    if (image instanceof ExtendedDrawable)
    {
      ExtendedDrawable ext = (ExtendedDrawable) image;
      final Dimension preferredSize = ext.getPreferredSize();
      dims.setWidth(StrictGeomUtility.toInternalValue(preferredSize.getWidth()));
      dims.setHeight(StrictGeomUtility.toInternalValue(preferredSize.getHeight()));
    }

    final CSSValue widthVal = context.getStyle().getValue(BoxStyleKeys.WIDTH);
    final RenderLength width = DefaultBoxDefinitionFactory.computeWidth
            (widthVal, context, layoutProcess.getOutputMetaData(), true, false);

    final CSSValue heightVal = context.getStyle().getValue(BoxStyleKeys.HEIGHT);
    final RenderLength height = DefaultBoxDefinitionFactory.computeWidth
            (heightVal, context, layoutProcess.getOutputMetaData(), true, false);

    final CSSValue valign =
            context.getStyle().getValue(LineStyleKeys.VERTICAL_ALIGN);
    return new RenderableReplacedContent(image, source, dims, width, height, valign);
  }

  public void finishedInline()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);
    Log.debug("</inline>");
    insertationPoint.close();
    // currentBox = (RenderBox) currentBox.getParent();
    tryToValidate();
  }

  public void finishedRootInline() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);
    Log.debug("</paragraph>");
    insertationPoint.close();
  }

  public void finishedMarker() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);
    Log.debug("</marker>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedBlock()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</block>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableCell()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</table-cell>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableRow()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</table-row>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableSection()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</table-section>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</table-col-group>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</table-col>");
    tryToValidate();
  }

  public void finishedTable()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</table>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedFlow()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</flow>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedPhysicalPageFlow()
  {
    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChilds(textFactory.finishText());
    Log.debug("</special-flow>");
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedDocument()
  {
    Log.debug("</document>");
    logicalPageBox.close();

    // Ok, lets play a little bit
    LogicalPageBox rootBox = logicalPageBox;

    Log.debug("Pref H-Axis: " + rootBox.getEffectiveLayoutSize(RenderNode.HORIZONTAL_AXIS));
    Log.debug("Pref V-Axis: " + rootBox.getEffectiveLayoutSize(RenderNode.VERTICAL_AXIS));

    //tryToValidate();
    logicalPageBox.validate(RenderNodeState.FINISHED);

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
    if (pageContext == null)
    {
      throw new NullPointerException();
    }

    this.pageContext = this.pageContext.update(pageContext);

//    final PageGrid pageGrid =
//            new DefaultPageGrid(pageContext, layoutProcess.getOutputMetaData());

    // initialize the logical page. The logical page needs the page grid,
    // as this contains the hints for the physical page sizes.
//    logicalPageBox = new LogicalPageBox(pageGrid);

    // for now, we ignore manual pagebreaks. They create conflicting
    // situations inside tables and auto-layout elements and so we have
    // to ignore them.
  }

  public State saveState() throws StateException
  {
    return new DefaultFlowRendererState();
  }
}
