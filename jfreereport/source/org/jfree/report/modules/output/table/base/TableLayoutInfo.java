/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * TableLayoutInfo.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableLayoutInfo.java,v 1.3 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------------------------
 * 12-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.table.base;

import java.awt.print.PageFormat;
import java.util.ArrayList;

/**
 * The tablelayout info class is used to store the layout that was generated
 * in the repagination process. This layout can be shared over several pages
 * to unify the look of the tables.
 *
 * @author Thomas Morgner
 */
public class TableLayoutInfo
{
  /** A flag defining whether to use the global layout. */
  private boolean globalLayout;
  /** A list of page layouts, one entry for every page. */
  private ArrayList pageLayouts;
  /** the page format used for the layouting. */
  private PageFormat pageFormat;

  /**
   * Creates a new tablelayout info object to store the layout information.
   *
   * @param globalLayout whether to use a global layout for all pages
   * @param format the page format used in the report.
   */
  public TableLayoutInfo(final boolean globalLayout, final PageFormat format)
  {
    if (format == null)
    {
      throw new NullPointerException("PageFormat is null");
    }
    this.pageLayouts = new ArrayList();
    this.globalLayout = globalLayout;
    this.pageFormat = format;
  }

  /**
   * Adds a layout for the next page to the layout information.
   *
   * @param bounds the layout.
   */
  public void addLayout(final TableGridBounds bounds)
  {
    if (isGlobalLayout())
    {
      if (pageLayouts.isEmpty())
      {
        pageLayouts.add(bounds);
      }
      else
      {
        pageLayouts.set(0, bounds);
      }
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
  public boolean isGlobalLayout()
  {
    return globalLayout;
  }

  /**
   * Returns the layout for a given page. This returns the same layout for
   * all pages if the globallayout feature is enabled.
   *
   * @param page the page for that the layout is requested.
   * @return the stored layout.
   * @throws IndexOutOfBoundsException if the page is invalid.
   */
  public TableGridBounds getLayoutForPage(final int page)
  {
    if (isGlobalLayout())
    {
      return (TableGridBounds) pageLayouts.get(0);
    }
    else
    {
      return (TableGridBounds) pageLayouts.get(page);
    }
  }

  /**
   * Return the number of pages stored in that list. This
   * returns 1 if the global layout is active.
   *
   * @return the number of pages.
   */
  public int getPageCount()
  {
    return pageLayouts.size();
  }


  /**
   * Returns the page format assigned with this layout.
   * This methods return type will change next release, when a better
   * way of defining page sizes is introduced.
   *
   * @return the page format.
   */
  public PageFormat getPageFormat()
  {
    return pageFormat;
  }

}
