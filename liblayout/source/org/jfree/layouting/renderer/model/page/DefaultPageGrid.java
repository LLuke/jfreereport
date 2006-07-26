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
 * DefaultPageGrid.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.model.page;

import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.input.style.keys.page.PageSize;

/**
 * Creation-Date: 15.06.2006, 16:16:00
 *
 * @author Thomas Morgner
 */
public class DefaultPageGrid implements PageGrid
{
  private int rows;
  private int columns;
  private PhysicalPageBox[] pages;

  public DefaultPageGrid(final PageContext pageContext)
  {
    this(pageContext, 1, 1);
  }

  public DefaultPageGrid(final PageContext pageContext,
                         final int rows,
                         final int columns)
  {
    this.rows = rows;
    this.columns = columns;
    this.pages = new PhysicalPageBox[rows * columns];
    for (int i = 0; i < pages.length; i++)
    {
      pages[i] = new PhysicalPageBox(pageContext,
              PageSize.A5.getWidth() * 1000, PageSize.A5.getHeight()  * 1000);
    }
  }

  public PhysicalPageBox getPage(int row, int col)
  {
    return pages[row * rows + col];
  }

  public int getRowCount()
  {
    return rows;
  }

  public int getColumnCount()
  {
    return columns;
  }
}
