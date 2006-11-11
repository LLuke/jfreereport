/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

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
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;

/**
 * This one does nothing. (It is used to swallow all calls during the restore
 * process.)
 *
 * @author Thomas Morgner
 */
public class EmptyContentGenerator implements ContentGenerator
{
  public EmptyContentGenerator()
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

  /**
   * Starts a special flow. A special flow receives content for the special and
   * page areas; the renderer may have to update the content area size.
   * <p/>
   * Todo: This is not yet implemented.
   *
   * @param context
   */
  public void startedPhysicalPageFlow(final DisplayFlowElement element)
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

  public void finishedPhysicalPageFlow() throws NormalizationException
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
}
