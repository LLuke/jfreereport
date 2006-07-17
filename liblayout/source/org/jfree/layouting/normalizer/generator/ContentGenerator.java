package org.jfree.layouting.normalizer.generator;

import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.displaymodel.DisplayBlockElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayContent;
import org.jfree.layouting.normalizer.displaymodel.DisplayElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayFlowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableCellElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableRowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableSectionElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayRootInlineElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableColumnElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableColumnGroupElement;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.renderer.Renderer;

/**
 * The content generator is the third stage content processor.
 * This part is responsible to process the generated display model
 * and to prepare it for rendering it to the output medium.
 * <p/>
 * In the simplest case, the content generator will be able to pass the
 * display model without any changes. In the more complicated cases, the
 * display model needs to be flattened (as text processors cannot contain
 * paragraphs inside other paragraphs, for instance) before it can be rendered.
 */
public interface ContentGenerator extends StatefullComponent
{
  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   *
   * @param pageContext the page context for the default page.
   */
  public void startedDocument(final PageContext pageContext)
          throws NormalizationException;

  /**
   * Starts a special flow. A special flow receives content for the special
   * and page areas; the renderer may have to update the content area size.
   *
   * Todo: This is not yet implemented.
   *
   * @param context
   */
  public void startedPhysicalPageFlow(final DisplayFlowElement element)
          throws NormalizationException;

  public void startedFlow(final DisplayFlowElement element)
          throws NormalizationException;

  public void startedBlock(final DisplayBlockElement element)
          throws NormalizationException;

  public void startedRootInline(final DisplayRootInlineElement element)
          throws NormalizationException;

  public void startedTable (final DisplayTableElement element)
          throws NormalizationException;

  public void startTableColumnGroup(final DisplayTableColumnGroupElement element)
          throws NormalizationException;

  public void startTableColumn(final DisplayTableColumnElement element)
          throws NormalizationException;

  public void startedTableSection (final DisplayTableSectionElement element)
          throws NormalizationException;

  public void startedTableRow (final DisplayTableRowElement element)
          throws NormalizationException;

  public void startedTableCell (final DisplayTableCellElement element)
          throws NormalizationException;

  public void startedMarker(final DisplayElement element)
          throws NormalizationException;

  public void startedInline(final DisplayElement element)
          throws NormalizationException;

  public void addContent(final DisplayContent node)
          throws NormalizationException;

  public void finishedInline()
          throws NormalizationException;

  public void finishedMarker()
          throws NormalizationException;

  public void finishedTableCell()
          throws NormalizationException;

  public void finishedTableRow ()
          throws NormalizationException;

  public void finishedTableSection()
          throws NormalizationException;

  public void finishedTableColumn()
          throws NormalizationException;

  public void finishedTableColumnGroup()
          throws NormalizationException;

  public void finishedTable()
          throws NormalizationException;

  public void finishedRootInline()
          throws NormalizationException;

  public void finishedBlock ()
          throws NormalizationException;

  public void finishedFlow ()
          throws NormalizationException;

  public void finishedPhysicalPageFlow()
          throws NormalizationException;

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void finishDocument()
          throws NormalizationException;

  public void handlePageBreak(final PageContext pageContext);

  public Renderer getRenderer();

}
