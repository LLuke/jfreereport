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
 * EmptyRenderer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: EmptyRenderer.java,v 1.3 2006/10/27 18:25:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.util.Log;

/**
 * Creation-Date: 17.07.2006, 17:43:21
 *
 * @author Thomas Morgner
 */
public class PrintingRenderer implements Renderer
{
  private static class PrintingRendererState implements State
  {
    private State parentState;

    public PrintingRendererState(final Renderer parent) throws StateException
    {
      this.parentState = parent.saveState();
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws org.jfree.layouting.StateException
     *
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
        throws StateException
    {
      return new PrintingRenderer((Renderer) parentState.restore(layoutProcess));
    }
  }

  private Renderer parent;

  public PrintingRenderer(final Renderer parent)
  {
    this.parent = parent;
  }

  /**
   * Starts the document and initalizes the default page context.
   *
   * @param pageContext
   */
  public void startDocument(final PageContext pageContext)
  {
    Log.debug ("<document>");
    parent.startDocument(pageContext);
  }

  /**
   * Starts a special flow. A special flow receives content for the special and
   * page areas; the renderer may have to update the content area size.
   * <p/>
   * When this method is called, it is up to the renderer to find out which page
   * area needs to be updated. If the context cannot be resolved to a defined
   * special area, the content is handled as if it is a block content.
   * <p/>
   * The special flow always addresses physical page areas. The logical page
   * areas are not special in any way.
   *
   * @param context
   */
  public void startedPhysicalPageFlow(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<physical-page-flow>");
    parent.startedPhysicalPageFlow(context);
  }

  /**
   * Starts a floating, absolute or static element. This establishes a new
   * normal flow for the element.
   *
   * @param context
   */
  public void startedFlow(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<flow>");
    parent.startedFlow(context);
  }

  public void startedTable(final LayoutContext layoutContext)
          throws NormalizationException
  {
    Log.debug ("<table>");
    parent.startedTable(layoutContext);
  }

  public void startedTableSection(final LayoutContext layoutContext)
          throws NormalizationException
  {
    Log.debug ("<table-section>");
    parent.startedTableSection(layoutContext);
  }

  public void startedTableRow(final LayoutContext layoutContext)
          throws NormalizationException
  {
    Log.debug ("<table-row>");
    parent.startedTableRow(layoutContext);
  }

  public void startedTableCell(final LayoutContext layoutContext)
          throws NormalizationException
  {
    Log.debug ("<table-cell>");
    parent.startedTableCell(layoutContext);
  }

  public void startedBlock(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<block>");
    parent.startedBlock(context);
  }

  public void startedMarker(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<marker>");
    parent.startedMarker(context);
  }

  public void startedRootInline(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<paragraph>");
    parent.startedRootInline(context);
  }

  public void startedInline(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<inline>");
    parent.startedInline(context);
  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
          throws NormalizationException
  {
    Log.debug ("<content>" + content + "</content>");
    parent.addContent(context, content);
  }

  public void finishedInline() throws NormalizationException
  {
    Log.debug ("</inline>");
    parent.finishedInline();
  }

  public void finishedRootInline() throws NormalizationException
  {
    Log.debug ("</paragraph>");
    parent.finishedRootInline();
  }

  public void finishedMarker() throws NormalizationException
  {
    Log.debug ("</marker>");
    parent.finishedMarker();
  }

  public void finishedBlock() throws NormalizationException
  {
    Log.debug ("</block>");
    parent.finishedBlock();
  }

  public void finishedTableCell() throws NormalizationException
  {
    Log.debug ("</table-cell>");
    parent.finishedTableCell();
  }

  public void finishedTableRow() throws NormalizationException
  {
    Log.debug ("</table-row>");
    parent.finishedTableRow();
  }

  public void finishedTableSection() throws NormalizationException
  {
    Log.debug ("</table-section>");
    parent.finishedTableSection();
  }

  public void finishedTable() throws NormalizationException
  {
    Log.debug ("</table>");
    parent.finishedTable();
  }

  public void finishedFlow() throws NormalizationException
  {
    Log.debug ("</flow>");
    parent.finishedFlow();
  }

  public void finishedPhysicalPageFlow() throws NormalizationException
  {
    Log.debug ("</physical-flow>");
    parent.finishedPhysicalPageFlow();
  }

  public void finishedDocument() throws NormalizationException
  {
    Log.debug ("</document>");
    parent.finishedDocument();
  }

  public void startedTableColumnGroup(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<table-column-group>");
    parent.startedTableColumnGroup(context);
  }

  public void startedTableColumn(final LayoutContext context)
          throws NormalizationException
  {
    Log.debug ("<table-column>");
    parent.startedTableColumn(context);
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    Log.debug ("</table-column-group>");
    parent.finishedTableColumnGroup();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    Log.debug ("</table-column>");
    parent.finishedTableColumn();
  }

  /**
   * A call-back that informs the renderer, that a new page must be started.
   * This closes the old page context and copies all pending content to the new
   * context.
   *
   * @param pageContext
   */
  public void handlePageBreak(final PageContext pageContext)
  {
    Log.debug ("<!-- PAGEBREAK ENCOUNTERED -->");
    parent.handlePageBreak(pageContext);
  }

  public State saveState() throws StateException
  {
    return new PrintingRendererState(parent);
  }
}
