/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -------------------
 * TableCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableCellData.java,v 1.4 2003/01/27 18:24:52 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;

/**
 * The TableCellData encapsulates cell information, either style information or
 * cell data, and the cell bounds. The cell bounds are used by the TableGridLayout
 * to place the cell into the TableGrid.
 * <p>
 * This class contains all data needed to successfully layout the table grid.
 * The cell style information is dependent on the concrete implementation and not
 * defined here.
 */
public abstract class TableCellData
{
  /** The position of the outer bounds of the cell */
  private Rectangle2D outerBounds;

  /**
   * Creates a new TableCellData object.
   *
   * @param outerBounds the bounds of this table cell data.
   */
  public TableCellData(Rectangle2D outerBounds)
  {
    if (outerBounds == null) throw new NullPointerException("OuterBounds is null");
    this.outerBounds = (Rectangle2D) outerBounds.clone();
  }

  /**
   * Gets the bounds of this table cell data.
   *
   * @return the bounds of this table cell data.
   */
  public Rectangle2D getBounds()
  {
    return (Rectangle2D) outerBounds.clone();
  }

  /**
   * Returns true, if this cell data definition is a background definition and does
   * not contain cell data.
   *
   * @return true for background cells and false for data cells.
   */
  public abstract boolean isBackground();
}
