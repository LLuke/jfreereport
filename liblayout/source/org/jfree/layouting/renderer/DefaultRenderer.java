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
 * $Id: DefaultRenderer.java,v 1.16 2006/10/27 18:25:50 taqua Exp $
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
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.type.GenericType;
import org.jfree.layouting.layouter.content.type.ResourceType;
import org.jfree.layouting.layouter.content.type.TextType;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.output.OutputProcessor;
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
import org.jfree.layouting.renderer.process.CleanPaginatedBoxesStep;
import org.jfree.layouting.renderer.process.ComputeICMMetricsStep;
import org.jfree.layouting.renderer.process.ComputeMarginsStep;
import org.jfree.layouting.renderer.process.ComputeStaticPropertiesStep;
import org.jfree.layouting.renderer.process.ComputeTableICMMetricsStep;
import org.jfree.layouting.renderer.process.FillPhysicalPagesStep;
import org.jfree.layouting.renderer.process.InfiniteMajorAxisLayoutStep;
import org.jfree.layouting.renderer.process.InfiniteMinorAxisLayoutStep;
import org.jfree.layouting.renderer.process.PaginationStep;
import org.jfree.layouting.renderer.process.ParagraphLineBreakStep;
import org.jfree.layouting.renderer.process.TableRowHeightStep;
import org.jfree.layouting.renderer.process.TableValidationStep;
import org.jfree.layouting.renderer.process.ValidateModelStep;
import org.jfree.layouting.renderer.text.DefaultRenderableTextFactory;
import org.jfree.layouting.renderer.text.RenderableTextFactory;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.ui.Drawable;
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
  protected static class DefaultFlowRendererState implements State
  {
    private ValidateModelStep validateModelStep;
    private TableValidationStep tableValidationStep;
    private ComputeStaticPropertiesStep staticPropertiesStep;
    private ComputeMarginsStep marginsStep;
    private ComputeICMMetricsStep icmMetricsStep;
    private ComputeTableICMMetricsStep tableICMMetricsStep;
    private ParagraphLineBreakStep paragraphLinebreakStep;
    private InfiniteMinorAxisLayoutStep minorAxisLayoutStep;
    private InfiniteMajorAxisLayoutStep majorAxisLayoutStep;
    private TableRowHeightStep tableRowHeightStep;
    private PaginationStep paginationStep;
    private BoxDefinitionFactory boxDefinitionFactory;
    private RenderPageContext pageContext;
    private int bufferLength;

    private LogicalPageBox logicalPageBox;

    public DefaultFlowRendererState(DefaultRenderer renderer)
    {
      this.boxDefinitionFactory = renderer.boxDefinitionFactory;
      this.validateModelStep = renderer.validateModelStep;
      this.tableValidationStep = renderer.tableValidationStep;
      this.staticPropertiesStep = renderer.staticPropertiesStep;

      this.marginsStep = renderer.marginsStep;
      this.icmMetricsStep = renderer.icmMetricsStep;
      this.tableICMMetricsStep = renderer.tableICMMetricsStep;
      this.paragraphLinebreakStep = renderer.paragraphLinebreakStep;
      this.minorAxisLayoutStep = renderer.minorAxisLayoutStep;
      this.majorAxisLayoutStep = renderer.majorAxisLayoutStep;
      this.tableRowHeightStep = renderer.tableRowHeightStep;
      this.paginationStep = renderer.paginationStep;
      this.pageContext = renderer.pageContext;
      this.bufferLength = renderer.buffer.getData().length;

      this.logicalPageBox = (LogicalPageBox) renderer.logicalPageBox.derive(true);
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
      final DefaultRenderer defaultRenderer = new DefaultRenderer(layoutProcess, false);
      defaultRenderer.buffer = new CodePointBuffer(bufferLength);

      defaultRenderer.boxDefinitionFactory = this.boxDefinitionFactory;
      defaultRenderer.validateModelStep = this.validateModelStep;
      defaultRenderer.tableValidationStep = this.tableValidationStep;
      defaultRenderer.staticPropertiesStep = this.staticPropertiesStep;
      defaultRenderer.marginsStep = this.marginsStep;
      defaultRenderer.icmMetricsStep = this.icmMetricsStep;
      defaultRenderer.tableICMMetricsStep = this.tableICMMetricsStep;
      defaultRenderer.paragraphLinebreakStep = this.paragraphLinebreakStep;
      defaultRenderer.minorAxisLayoutStep = this.minorAxisLayoutStep;
      defaultRenderer.majorAxisLayoutStep = this.majorAxisLayoutStep;
      defaultRenderer.tableRowHeightStep = this.tableRowHeightStep;
      defaultRenderer.paginationStep = this.paginationStep;
      defaultRenderer.pageContext = this.pageContext;
      defaultRenderer.logicalPageBox = (LogicalPageBox) this.logicalPageBox.derive(true);

      return defaultRenderer;
    }
  }


  // from restore
  private LayoutProcess layoutProcess;

  // statefull ..
  private LogicalPageBox logicalPageBox;

  // to be recreated
  private CodePointBuffer buffer;

  // Stateless components ..
  private RenderPageContext pageContext;
  private BoxDefinitionFactory boxDefinitionFactory;
  private ValidateModelStep validateModelStep;
  private TableValidationStep tableValidationStep;
  private ComputeStaticPropertiesStep staticPropertiesStep;
  private ComputeMarginsStep marginsStep;
  private ComputeICMMetricsStep icmMetricsStep;
  private ComputeTableICMMetricsStep tableICMMetricsStep;
  private ParagraphLineBreakStep paragraphLinebreakStep;
  private InfiniteMinorAxisLayoutStep minorAxisLayoutStep;
  private InfiniteMajorAxisLayoutStep majorAxisLayoutStep;
  private TableRowHeightStep tableRowHeightStep;
  private PaginationStep paginationStep;
  private FillPhysicalPagesStep fillPhysicalPagesStep;
  private CleanPaginatedBoxesStep cleanPaginatedBoxesStep;

  private StringStore stringsStore;
  private ContentStore elementsStore;
  private ContentStore pendingStore;

  private static class FlowContext
  {
    private RenderableTextFactory textFactory;
    private NormalFlowRenderBox currentFlow;

    public FlowContext(final RenderableTextFactory textFactory,
                       final NormalFlowRenderBox currentFlow)
    {
      this.textFactory = textFactory;
      this.currentFlow = currentFlow;
    }

    public RenderableTextFactory getTextFactory()
    {
      return textFactory;
    }

    public NormalFlowRenderBox getCurrentFlow()
    {
      return currentFlow;
    }
  }

  private Stack flowContexts;

  protected DefaultRenderer(final LayoutProcess layoutProcess, boolean init)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }

    this.layoutProcess = layoutProcess;
    this.flowContexts = new Stack();
    if (init)
    {
      this.boxDefinitionFactory =
          new DefaultBoxDefinitionFactory(new BorderFactory());

      this.validateModelStep = new ValidateModelStep();
      this.staticPropertiesStep = new ComputeStaticPropertiesStep();
      this.tableValidationStep = new TableValidationStep();
      this.marginsStep = new ComputeMarginsStep();
      this.paragraphLinebreakStep = new ParagraphLineBreakStep();
      this.icmMetricsStep = new ComputeICMMetricsStep();
      this.tableICMMetricsStep = new ComputeTableICMMetricsStep();
      this.minorAxisLayoutStep = new InfiniteMinorAxisLayoutStep();
      this.majorAxisLayoutStep = new InfiniteMajorAxisLayoutStep();
      this.tableRowHeightStep = new TableRowHeightStep();
      this.paginationStep = new PaginationStep();
      this.fillPhysicalPagesStep = new FillPhysicalPagesStep();
      this.stringsStore = new StringStore();
      this.elementsStore = new ContentStore();
      this.pendingStore = new ContentStore();
      this.cleanPaginatedBoxesStep = new CleanPaginatedBoxesStep();
    }
  }

  public DefaultRenderer(final LayoutProcess layoutProcess)
  {
    this(layoutProcess, true);
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
   * @throws NormalizationException
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

    tryToValidate();

  }

  private void tryToValidate() throws NormalizationException
  {
    if (validateModelStep.isLayoutable(logicalPageBox) == false)
    {
      return;
    }

    tableValidationStep.validate(logicalPageBox);

    staticPropertiesStep.compute(logicalPageBox);
    marginsStep.compute(logicalPageBox);
    paragraphLinebreakStep.compute(logicalPageBox);

    boolean repeat = true;
    while (repeat)
    {
      icmMetricsStep.compute(logicalPageBox);
      tableICMMetricsStep.compute(logicalPageBox);

      minorAxisLayoutStep.compute(logicalPageBox);
      majorAxisLayoutStep.compute(logicalPageBox);
      tableRowHeightStep.compute(logicalPageBox);

      if (paginationStep.performPagebreak(logicalPageBox) ||
          logicalPageBox.isOpen() == false)
      {
        // A new page has been started. Recover the page-grid, then restart
        // everything from scratch. (We have to recompute, as the pages may
        // be different now, due to changed margins or page definitions)
        final OutputProcessor outputProcessor = layoutProcess.getOutputProcessor();
        final long nextOffset = paginationStep.getNextOffset();
        if (outputProcessor.isPhysicalPageOutput())
        {
          final long pageOffset = logicalPageBox.getPageOffset();
          final LogicalPageBox box = fillPhysicalPagesStep.compute
                  (logicalPageBox, pageOffset, nextOffset);
          outputProcessor.processContent(box);
        }
        else
        {
          outputProcessor.processContent(logicalPageBox);
        }

        // Now fire the pagebreak. This goes through all layers and informs all
        // components, that a pagebreak has been encountered and possibly a
        // new page has been set. It does not save the state or perform other
        // expensive operations. However, it updates the 'isPagebreakEncountered'
        // flag, which will be active until the input-feed received a new event.
        repeat = logicalPageBox.isOpen();
        if (repeat)
        {
          Log.debug ("PAGEBREAK ENCOUNTERED");
          firePagebreak();

          logicalPageBox.setPageOffset(nextOffset);
          cleanPaginatedBoxesStep.compute(logicalPageBox);
        }
        else
        {
          Log.debug ("DOCUMENT FINISHED");
        }
      }
      else
      {
        repeat = false;
      }
    }
  }

  protected void firePagebreak() throws NormalizationException
  {
    // todo: Compute the current page and the pseudo-pages for this one.
    final PageContext pageContext = this.pageContext.getPageContext();
    final LayoutStyle style = pageContext.getStyle();

    // todo: Update the pseudo-pages (left | right, first)
    layoutProcess.pageBreakEncountered(null, new PseudoPage[0]);
  }

  protected RenderBox getInsertationPoint()
  {
    FlowContext context = (FlowContext) flowContexts.peek();
    return context.getCurrentFlow().getInsertationPoint();
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

    tryToValidate();

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

    tryToValidate();

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

    tryToValidate();

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

    tryToValidate();

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

    tryToValidate();

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

    tryToValidate();

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

    tryToValidate();

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

    tryToValidate();
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

    tryToValidate();
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

    tryToValidate();
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

    tryToValidate();
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
    tryToValidate();
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
    tryToValidate();
  }

  public void finishedBlock() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableCell() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableRow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableSection() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    tryToValidate();
  }

  public void finishedTable() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedFlow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();

    flowContexts.pop();

    tryToValidate();
  }

  public void finishedPhysicalPageFlow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryToValidate();
  }

  public void finishedDocument() throws NormalizationException
  {
    logicalPageBox.close();
    tryToValidate();
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

  public State saveState() throws StateException
  {
    return new DefaultFlowRendererState(this);
  }
}
