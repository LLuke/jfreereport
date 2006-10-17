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
 * $Id: EmptyRenderer.java,v 1.1 2006/07/17 16:50:42 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer;

import org.jfree.layouting.InstantiationState;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * Creation-Date: 17.07.2006, 17:43:21
 *
 * @author Thomas Morgner
 */
public class EmptyRenderer implements Renderer
{
  private LayoutProcess layoutProcess;

  public EmptyRenderer(final LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
  }

  /**
   * Starts the document and initalizes the default page context.
   *
   * @param pageContext
   */
  public void startDocument(final PageContext pageContext)
  {

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

  }

  public void startedTable(final LayoutContext layoutContext)
          throws NormalizationException
  {

  }

  public void startedTableSection(final LayoutContext layoutContext)
          throws NormalizationException
  {

  }

  public void startedTableRow(final LayoutContext layoutContext)
          throws NormalizationException
  {

  }

  public void startedTableCell(final LayoutContext layoutContext)
          throws NormalizationException
  {

  }

  public void startedBlock(final LayoutContext context)
          throws NormalizationException
  {

  }

  public void startedMarker(final LayoutContext context)
          throws NormalizationException
  {

  }

  public void startedRootInline(final LayoutContext context)
          throws NormalizationException
  {

  }

  public void startedInline(final LayoutContext context)
          throws NormalizationException
  {

  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
          throws NormalizationException
  {

  }

  public void finishedInline() throws NormalizationException
  {

  }

  public void finishedRootInline() throws NormalizationException
  {

  }

  public void finishedMarker() throws NormalizationException
  {

  }

  public void finishedBlock() throws NormalizationException
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

  public void finishedTable() throws NormalizationException
  {

  }

  public void finishedFlow() throws NormalizationException
  {

  }

  public void finishedPhysicalPageFlow() throws NormalizationException
  {

  }

  public void finishedDocument() throws NormalizationException
  {

  }

  public void startedTableColumnGroup(final LayoutContext context)
          throws NormalizationException
  {

  }

  public void startedTableColumn(final LayoutContext context)
          throws NormalizationException
  {

  }

  public void finishedTableColumnGroup() throws NormalizationException
  {

  }

  public void finishedTableColumn() throws NormalizationException
  {

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

  }

  public State saveState() throws StateException
  {
    return new InstantiationState(getClass());
  }
}
