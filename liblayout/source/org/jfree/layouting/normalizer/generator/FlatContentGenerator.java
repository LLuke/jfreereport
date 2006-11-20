package org.jfree.layouting.normalizer.generator;

import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.displaymodel.DisplayFlowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayBlockElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayRootInlineElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableColumnGroupElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableColumnElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableSectionElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableRowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableCellElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayContent;
import org.jfree.layouting.normalizer.displaymodel.DisplayPassThroughElement;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;

/**
 * Breaks down the content into a flat structure. Block content is no longer
 * contained in other block content, but tables and lines can still be nested.
 * <p/>
 * This transformation is needed for plain document output, like the StarWriter
 * export. It creates its own ambugities, as divs with multiple borders cannot
 * be expressed with this system. (Text processors have no paragraph stacking
 * at all).
 * <p/>
 * So a smart engine would interfere here and would generated nested tables
 * instead.
 * <p/>
 * Todo: Implement me
 */
public class FlatContentGenerator implements ContentGenerator
{
  public FlatContentGenerator()
  {
  }

  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   *
   * @param pageContext the page context for the default page.
   */
  public void startedDocument(final PageContext pageContext)
      throws NormalizationException
  {

  }

  public void startedFlow(final DisplayFlowElement element)
      throws NormalizationException
  {

  }

  public void startedBlock(final DisplayBlockElement element)
      throws NormalizationException
  {

  }

  public void startedRootInline(final DisplayRootInlineElement element)
      throws NormalizationException
  {

  }

  public void startedTable(final DisplayTableElement element)
      throws NormalizationException
  {

  }

  public void startTableColumnGroup(final DisplayTableColumnGroupElement element)
      throws NormalizationException
  {

  }

  public void startTableColumn(final DisplayTableColumnElement element)
      throws NormalizationException
  {

  }

  public void startedTableSection(final DisplayTableSectionElement element)
      throws NormalizationException
  {

  }

  public void startedTableRow(final DisplayTableRowElement element)
      throws NormalizationException
  {

  }

  public void startedTableCell(final DisplayTableCellElement element)
      throws NormalizationException
  {

  }

  public void startedMarker(final DisplayElement element)
      throws NormalizationException
  {

  }

  public void startedInline(final DisplayElement element)
      throws NormalizationException
  {

  }

  public void addContent(final DisplayContent node)
      throws NormalizationException
  {

  }

  public void finishedInline() throws NormalizationException
  {

  }

  public void finishedMarker() throws NormalizationException
  {

  }

  public void finishedTableCell() throws NormalizationException
  {

  }

  public void finishedTableRow() throws NormalizationException
  {

  }

  public void finishedTableSection() throws NormalizationException
  {

  }

  public void finishedTableColumn() throws NormalizationException
  {

  }

  public void finishedTableColumnGroup() throws NormalizationException
  {

  }

  public void finishedTable() throws NormalizationException
  {

  }

  public void finishedRootInline() throws NormalizationException
  {

  }

  public void finishedBlock() throws NormalizationException
  {

  }

  public void finishedFlow() throws NormalizationException
  {

  }

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void finishDocument() throws NormalizationException
  {

  }

  /**
   * This event handler is triggered by 'LayoutProcess.pageBreakEncountered()'.
   *
   * @param pageContext
   */
  public void handlePageBreak(final PageContext pageContext)
  {

  }

  public Renderer getRenderer()
  {
    return null;
  }

  public State saveState() throws StateException
  {
    return null;
  }

  public void startedPassThrough(final DisplayPassThroughElement element)
      throws NormalizationException
  {

  }

  public void addPassThroughContent(final DisplayContent node)
  {

  }

  public void finishPassThrough()
  {

  }
}
