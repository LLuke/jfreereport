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
 * ------------------
 * TableCellData.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableCellData.java,v 1.2 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;

/**
 * Encapsulates cell information, either style information or cell data, and the cell bounds.
 * The cell bounds are used by the {@link TableGridLayout} to place the cell into a
 * {@link TableGrid}.
 * <p>
 * This class contains all data needed to successfully layout the table grid.
 * The cell style information is dependent on the concrete implementation and not
 * defined here.
 *
 * @author Thomas Morgner.
 */
public abstract class TableCellData
{
  /** The outer bounds of the cell. */
  private Rectangle2D outerBounds;

  /**
   * Creates a new <code>TableCellData</code> object.
   *
   * @param outerBounds the bounds (<code>null</code> not permitted).
   */
  public TableCellData(final Rectangle2D outerBounds)
  {
    if (outerBounds == null)
    {
      throw new NullPointerException("TableCellData constructor : outerBounds is null");
    }
    this.outerBounds = (Rectangle2D) outerBounds.clone();
  }

  /**
   * Gets the bounds of this table cell data.
   *
   * @return The bounds.
   */
  public Rectangle2D getBounds()
  {
    return (Rectangle2D) outerBounds.clone();
  }

  /**
   * Redefines the bounds for this cell data. This should not be called from
   * user implementations - it is only needed to merge background cells and
   * should never used anywhere else.
   *
   * @param bounds the bounds.
   */
  protected void setBounds (Rectangle2D bounds)
  {
    outerBounds.setRect(bounds);
  }

  /**
   * Returns <code>true</code>, if this cell data definition is a background definition and does
   * not contain cell data.
   *
   * @return <code>true</code> for background cells and <code>false</code> for data cells.
   */
  public abstract boolean isBackground();
}
