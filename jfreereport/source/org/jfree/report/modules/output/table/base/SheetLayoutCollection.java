/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * SheetLayoutCollection.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: SheetLayoutCollection.java,v 1.6 2005/03/03 17:07:58 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12-Jul-2003 : Initial version
 * 27-Feb-2003 : TableLayoutInfo is now known as SheetLayoutCollection.
 */

package org.jfree.report.modules.output.table.base;

import java.util.ArrayList;

/**
 * The tablelayout info class is used to store the layout that was generated in the
 * repagination process. This layout can be shared over several pages to unify the look of
 * the tables.
 *
 * @author Thomas Morgner
 */
public class SheetLayoutCollection
{
  /**
   * A list of page layouts, one entry for every page.
   */
  private ArrayList pageLayouts;
  private SheetLayout globalLayout;
  private boolean useGlobalLayout;

  /**
   * Creates a new tablelayout info object to store the layout information.
   *
   * @param useGlobalLayout whether to use a global layout for all pages
   */
  public SheetLayoutCollection (final boolean useGlobalLayout)
  {
    this.pageLayouts = new ArrayList();
    this.useGlobalLayout = useGlobalLayout;
    this.globalLayout = null;
  }

  /**
   * Adds a layout for the next page to the layout information.
   *
   * @param bounds the layout.
   */
  public void addLayout (final SheetLayout bounds)
  {
    if (isGlobalLayout())
    {
      globalLayout = bounds;
    }
    else
    {
      pageLayouts.add(bounds);
    }
  }

  /**
   * Checks, whether to define a global layout.
   *
   * @return true, if the report uses an global layout, false otherwise.
   */
  public boolean isGlobalLayout ()
  {
    return useGlobalLayout;
  }

  /**
   * Returns the layout for a given page. This returns the same layout for all pages if
   * the globallayout feature is enabled.
   *
   * @param page the page for that the layout is requested.
   * @return the stored layout.
   *
   * @throws IndexOutOfBoundsException if the page is invalid.
   */
  public SheetLayout getLayoutForPage (final int page)
  {
    //Log.debug("Query Layout [" + isGlobalLayout() + "] for page " + page);
    if (isGlobalLayout())
    {
      if (globalLayout == null)
      {
        throw new IllegalStateException("No global layout defined.");
      }
      return globalLayout;
    }
    else
    {
      final SheetLayout layout = (SheetLayout) pageLayouts.get(page);
      if (layout == null)
      {
        throw new IllegalStateException("No sheet layout for page " + page);
      }
      return layout;
    }
  }

  /**
   * Return the number of pages stored in that list. This returns 1 if the global layout
   * is active.
   *
   * @return the number of pages.
   */
  public int getPageCount ()
  {
    if (!isGlobalLayout())
    {
      return pageLayouts.size();
    }
    if (globalLayout == null)
    {
      return 0;
    }
    return 1;
  }
}
