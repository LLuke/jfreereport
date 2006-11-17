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
 * AbstractRenderer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AbstractRenderer.java,v 1.5 2006/11/11 20:23:46 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import java.awt.Dimension;
import java.awt.Image;
import java.util.Stack;

import org.jfree.fonts.encoding.CodePointBuffer;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.type.GenericType;
import org.jfree.layouting.layouter.content.type.ResourceType;
import org.jfree.layouting.layouter.content.type.TextType;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
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
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
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
import org.jfree.ui.ExtendedDrawable;
import org.jfree.util.WaitingImageObserver;

/**
 * Creation-Date: 16.06.2006, 14:10:40
 *
 * @author Thomas Morgner
 */
public abstract class AbstractRenderer implements Renderer
{
  protected abstract static class RendererState implements State
  {
    private BoxDefinitionFactory boxDefinitionFactory;
    private RenderPageContext pageContext;
    private int bufferLength;
    private LogicalPageBox logicalPageBox;

    private StringStore stringsStore;
    private ContentStore elementsStore;
    private ContentStore pendingStore;

    // todo:!
    private FlowContext.FlowContextState[] flowContexts;

    public RendererState(AbstractRenderer renderer) throws StateException
    {
      this.boxDefinitionFactory = renderer.boxDefinitionFactory;
      this.pageContext = renderer.pageContext;

      if (renderer.buffer != null)
      {
        this.bufferLength = renderer.buffer.getData().length;
      }

      if (renderer.logicalPageBox != null)
      {
        this.logicalPageBox = (LogicalPageBox)
            renderer.logicalPageBox.derive(true);
      }

      try
      {
        this.stringsStore = (StringStore) renderer.stringsStore.clone();
        this.elementsStore = (ContentStore) renderer.elementsStore.clone();
        this.pendingStore = (ContentStore) renderer.pendingStore.clone();
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException();
      }

      final Stack renderFlowContexts = renderer.flowContexts;
      this.flowContexts = new FlowContext.FlowContextState
          [renderFlowContexts.size()];
      for (int i = 0; i < renderFlowContexts.size(); i++)
      {
        final FlowContext context = (FlowContext) renderFlowContexts.get(i);
        flowContexts[i] = context.saveState();
      }
    }


    protected void fill(AbstractRenderer renderer,
                        final LayoutProcess layoutProcess) throws StateException
    {
      if (bufferLength > 0)
      {
        renderer.buffer = new CodePointBuffer(bufferLength);
      }

      try
      {
        renderer.stringsStore = (StringStore) this.stringsStore.clone();
        renderer.elementsStore = (ContentStore) this.elementsStore.clone();
        renderer.pendingStore = (ContentStore) this.pendingStore.clone();
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException();
      }
      renderer.boxDefinitionFactory = this.boxDefinitionFactory;
      renderer.pageContext = this.pageContext;
      if (logicalPageBox != null)
      {
        renderer.logicalPageBox = (LogicalPageBox)
            this.logicalPageBox.derive(true);
      }

      renderer.flowContexts = new Stack();
      for (int i = 0; i < flowContexts.length; i++)
      {
        FlowContext.FlowContextState state = flowContexts[i];
        final Object currentFlowId = state.getCurrentFlowId();
        final State textFactoryState = state.getTextFactoryState();
        final RenderableTextFactory textFactory =
            (RenderableTextFactory) textFactoryState.restore(layoutProcess);
        final NormalFlowRenderBox box = (NormalFlowRenderBox)
            renderer.logicalPageBox.findNodeById(currentFlowId);
        renderer.flowContexts.push(new FlowContext(textFactory, box));
      }
    }
  }

  // from restore
  private LayoutProcess layoutProcess;
  // statefull ..
  private LogicalPageBox logicalPageBox;
  private StringStore stringsStore;
  private ContentStore elementsStore;
  private ContentStore pendingStore;
  private Stack flowContexts;

  // to be recreated
  private CodePointBuffer buffer;
  // Stateless components ..
  private RenderPageContext pageContext;
  private BoxDefinitionFactory boxDefinitionFactory;


  protected AbstractRenderer(final LayoutProcess layoutProcess,
                             final boolean init)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }

    this.layoutProcess = layoutProcess;
    this.flowContexts = new Stack();

    if (init)
    {
      this.stringsStore = new StringStore();
      this.elementsStore = new ContentStore();
      this.pendingStore = new ContentStore();
      this.boxDefinitionFactory =
          new DefaultBoxDefinitionFactory(new BorderFactory());
    }
  }

  public LogicalPageBox getLogicalPageBox()
  {
    return logicalPageBox;
  }

  public LayoutProcess getLayoutProcess()
  {
    return layoutProcess;
  }

  public RenderPageContext getRenderPageContext()
  {
    return pageContext;
  }

  public void startDocument(final PageContext pageContext)
  {
    if (pageContext == null)
    {
      throw new NullPointerException();
    }

    this.pageContext = new RenderPageContext(pageContext);
    // create the initial pagegrid.
    final PageGrid pageGrid =
        this.pageContext.createPageGrid(layoutProcess.getOutputMetaData());

    // initialize the logical page. The logical page needs the page grid,
    // as this contains the hints for the physical page sizes.
    logicalPageBox = new LogicalPageBox(pageGrid);
    logicalPageBox.setPageContext(this.pageContext.getPageContext());
  }

  /**
   * Chances are good, that this one is never used.
   *
   * @param context
   * @throws org.jfree.layouting.normalizer.content.NormalizationException
   *
   */
  public void startedPhysicalPageFlow(final LayoutContext context)
      throws NormalizationException
  {
    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final NormalFlowRenderBox item = new NormalFlowRenderBox(definition);
    item.appyStyle(context, layoutProcess.getOutputMetaData());

    this.pageContext = pageContext.update(context);
    item.setPageContext(pageContext.getPageContext());

    validateOutput();

  }

  protected abstract void validateOutput() throws NormalizationException;

  protected RenderBox getInsertationPoint()
  {
    FlowContext context = (FlowContext) flowContexts.peek();
    final RenderBox insertationPoint =
        context.getCurrentFlow().getInsertationPoint();

    // A small assertation game ..
    RenderBox root = insertationPoint;
    while (root != null)
    {
      if (root.getParent() == null)
      {
        break;
      }
      root = root.getParent();
    }
    if (root != logicalPageBox)
    {
      throw new IllegalStateException("Root: " + root + " " + logicalPageBox);
    }

    return insertationPoint;
  }

  public void startedFlow(final LayoutContext context)
      throws NormalizationException
  {
    this.pageContext = pageContext.update(context);

    if (logicalPageBox.isNormalFlowActive() == false)
    {
      // this is the first (the normal) flow. A document always starts
      // with a start-document and then a start-flow event.
      logicalPageBox.setNormalFlowActive(true);
      final DefaultRenderableTextFactory textFactory = new DefaultRenderableTextFactory(layoutProcess);
      textFactory.startText();

      FlowContext flowContext = new FlowContext
          (textFactory, logicalPageBox.getNormalFlow());

      flowContexts.push(flowContext);
    }
    else
    {
      // now check, what flow you are. A flow-top?
      // position: running(header); New headers replace old ones.
      // how to differentiate that (so that style-definitions are not that
      // complicated.

      // The receiving element would define the content property as
      // 'content: elements(header)'

      // A flow bottom?

      // or an ordinary flow?
      final BoxDefinition contentRoot =
          boxDefinitionFactory.createBlockBoxDefinition
              (context, layoutProcess.getOutputMetaData());

      NormalFlowRenderBox newFlow = new NormalFlowRenderBox(contentRoot);
      newFlow.appyStyle(context, layoutProcess.getOutputMetaData());
      newFlow.setPageContext(pageContext.getPageContext());

      RenderBox currentBox = getInsertationPoint();
      currentBox.addChild(newFlow.getPlaceHolder());
      currentBox.getNormalFlow().addFlow(newFlow);

      final DefaultRenderableTextFactory textFactory =
          new DefaultRenderableTextFactory(layoutProcess);
      textFactory.startText();

      FlowContext flowContext = new FlowContext
          (textFactory, newFlow);

      flowContexts.push(flowContext);
    }

    validateOutput();

  }

  public void startedTable(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    this.pageContext = pageContext.update(context);

    TableRenderBox tableRenderBox =
        new TableRenderBox(definition);
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());
    tableRenderBox.setPageContext(pageContext.getPageContext());

    getInsertationPoint().addChild(tableRenderBox);

    validateOutput();

  }

  private RenderableTextFactory getCurrentTextFactory()
  {
    final FlowContext context = (FlowContext) flowContexts.peek();
    return context.getTextFactory();
  }

  public void startedTableColumnGroup(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableColumnGroupNode columnGroupNode = new TableColumnGroupNode(definition);
    columnGroupNode.appyStyle(context, layoutProcess.getOutputMetaData());
    getInsertationPoint().addChild(columnGroupNode);

    validateOutput();

  }

  public void startedTableColumn(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableColumnNode columnGroupNode = new TableColumnNode(definition, context);
    getInsertationPoint().addChild(columnGroupNode);

    validateOutput();

  }

  public void startedTableSection(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableSectionRenderBox tableRenderBox =
        new TableSectionRenderBox(definition);
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());
    tableRenderBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(tableRenderBox);

    validateOutput();

  }

  public void startedTableRow(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableRowRenderBox tableRenderBox = new TableRowRenderBox(definition, false);
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());
    tableRenderBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(tableRenderBox);

    validateOutput();

  }

  public void startedTableCell(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    TableCellRenderBox tableRenderBox =
        new TableCellRenderBox(definition);
    tableRenderBox.setPageContext(pageContext.getPageContext());
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());

    getInsertationPoint().addChild(tableRenderBox);

    validateOutput();

  }

  public void startedBlock(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());

    BlockRenderBox blockBox = new BlockRenderBox(definition);
    blockBox.appyStyle(context, layoutProcess.getOutputMetaData());
    blockBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(blockBox);

    validateOutput();
  }

  public void startedMarker(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createInlineBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    MarkerRenderBox markerBox = new MarkerRenderBox(definition);
    markerBox.appyStyle(context, layoutProcess.getOutputMetaData());
    markerBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(markerBox);

    validateOutput();
  }

  public void startedRootInline(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    ParagraphRenderBox paragraphBox =
        new ParagraphRenderBox(definition);
    paragraphBox.appyStyle(context, layoutProcess.getOutputMetaData());
    paragraphBox.setPageContext(pageContext.getPageContext());

    getInsertationPoint().addChild(paragraphBox);

    validateOutput();
  }

  public void startedInline(final LayoutContext context)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createInlineBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    InlineRenderBox inlineBox = new InlineRenderBox(definition);
    inlineBox.appyStyle(context, layoutProcess.getOutputMetaData());
    inlineBox.setPageContext(pageContext.getPageContext());

    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChild(inlineBox);

    validateOutput();
  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
      throws NormalizationException
  {
    RenderableTextFactory textFactory = getCurrentTextFactory();
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
          validateOutput();
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
          validateOutput();
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
      validateOutput();
    }
  }

  private RenderNode[] createText(final String str,
                                  final LayoutContext context)
  {
    if (buffer != null)
    {
      buffer.setCursor(0);
    }
    buffer = Utf16LE.getInstance().decodeString(str, buffer);
    final int[] data = buffer.getBuffer();

    RenderableTextFactory textFactory = getCurrentTextFactory();
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

  public void finishedInline() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);
    insertationPoint.close();
    // currentBox = (RenderBox) currentBox.getParent();
    validateOutput();
  }

  public void finishedRootInline() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);
    insertationPoint.close();
  }

  public void finishedMarker() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);

    insertationPoint.close();
    validateOutput();
  }

  public void finishedBlock() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    validateOutput();
  }

  public void finishedTableCell() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    validateOutput();
  }

  public void finishedTableRow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    validateOutput();
  }

  public void finishedTableSection() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    validateOutput();
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    validateOutput();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    validateOutput();
  }

  public void finishedTable() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    validateOutput();
  }

  public void finishedFlow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();

    flowContexts.pop();

    validateOutput();
  }

  public void finishedPhysicalPageFlow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    validateOutput();
  }

  public void finishedDocument() throws NormalizationException
  {
    logicalPageBox.close();
    validateOutput();
    // At this point, we should have performed the necessary output.

    // Ok, lets play a little bit
    // todo: This is the end of the document, we should do some smarter things
    // here.
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    if (pageContext == null)
    {
      throw new NullPointerException();
    }

    this.pageContext = this.pageContext.update(pageContext);
    final PageGrid pageGrid =
        this.pageContext.createPageGrid(layoutProcess.getOutputMetaData());

    this.pendingStore = pendingStore.derive();
    this.elementsStore = elementsStore.derive();
    this.stringsStore = stringsStore.derive();

    this.logicalPageBox.updatePageArea(pageGrid);
  }
}
