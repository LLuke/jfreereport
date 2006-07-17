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
 * PrintContentGenerator.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PrintContentGenerator.java,v 1.1 2006/07/11 13:45:08 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.normalizer.generator;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.displaymodel.DisplayBlockElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayContent;
import org.jfree.layouting.normalizer.displaymodel.DisplayElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayFlowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableSectionElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableRowElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayTableCellElement;
import org.jfree.layouting.normalizer.displaymodel.DisplayRootInlineElement;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.util.Log;

/**
 * Simply prints each incoming call.
 *
 * @author Thomas Morgner
 */
public class PrintContentGenerator implements ContentGenerator
{
  private static class PrintContentGeneratorState implements State
  {
    public PrintContentGeneratorState()
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
      return new PrintContentGenerator();
    }
  }

  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   */
  public void startedDocument(final PageContext pageContext)
  {
    Log.debug("<document>");
  }

  public void startedFlow(final DisplayFlowElement element)
  {
    Log.debug("<flow tag='" + element.getLayoutContext().getTagName() + "'>");
  }

  public void startedTable(final DisplayTableElement element)
  {
    Log.debug("<table>");
  }

  public void startedTableSection(final DisplayTableSectionElement element)
  {
    Log.debug("<table-section>");
  }

  public void startedTableRow(final DisplayTableRowElement element)
  {
    Log.debug("<table-row>");
  }

  public void startedTableCell(final DisplayTableCellElement element)
  {
    Log.debug("<table-cell>");
  }

  public void startedBlock(final DisplayBlockElement element)
  {
    final String tagName = element.getLayoutContext().getTagName();
    Log.debug("<block tag='" + tagName + "'>");
  }

  public void startedRootInline(final DisplayRootInlineElement element)
          throws NormalizationException
  {
    final String tagName = element.getLayoutContext().getTagName();
    Log.debug("<paragraph tag='" + tagName + "'>");
  }

  public void startedMarker(final DisplayElement element)
          throws NormalizationException
  {
    Log.debug("<marker>");
  }

  public void startedInline(final DisplayElement element)
  {
    final String tagName = element.getLayoutContext().getTagName();
    Log.debug("<inline tag='" + tagName + "'>");
  }

  public void addContent(final DisplayContent node)
  {
    Log.debug("<content>" + node + "</content>");
  }

  public void finishedInline()
  {
    Log.debug("</inline>");
  }

  public void finishedMarker() throws NormalizationException
  {
    Log.debug("</marker>");
  }

  public void finishedRootInline() throws NormalizationException
  {
    Log.debug("</paragraph>");
  }

  public void finishedBlock()
  {

    Log.debug("</block>");
  }

  public void finishedTableCell()
  {
    Log.debug("</table-cell>");
  }

  public void finishedTableRow()
  {
    Log.debug("</table-row>");
  }

  public void finishedTableSection()
  {
    Log.debug("</table-section>");
  }

  public void finishedTable()
  {
    Log.debug("</table>");
  }

  public void finishedFlow()
  {
    Log.debug("</flow>");
  }

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void finishDocument()
  {
    Log.debug("</document>");
  }

  public State saveState() throws StateException
  {
    return new PrintContentGeneratorState();
  }

  /**
   * Starts a special flow. A special flow receives content for the special and
   * page areas; the renderer may have to update the content area size.
   *
   * @param context
   */
  public void startedPhysicalPageFlow(final DisplayFlowElement element)
  {

  }

  public void handlePageBreak(final PageContext pageContext)
  {

  }

  public void finishedPhysicalPageFlow()
  {

  }

  public Renderer getRenderer()
  {
    throw new UnsupportedOperationException();
  }
}
