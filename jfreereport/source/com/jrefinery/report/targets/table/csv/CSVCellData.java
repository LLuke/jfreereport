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
 * CSVCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVCellData.java,v 1.2 2003/01/25 20:34:11 taqua Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

/**
 * The CSV implementation of the TableCellData. The cell data implementation contains
 * the string value of the element value.
 */
public class CSVCellData extends TableCellData
{
  /** the value */
  private String value;

  /**
   * Creates a new CSVCellData object.
   *
   * @param value the value of the cell.
   * @param outerBounds the element bounds.
   */
  public CSVCellData(String value, Rectangle2D outerBounds)
  {
    super(outerBounds);
    if (value == null) throw new NullPointerException();
    this.value = value;
  }

  /**
   * Gets the value stored in this CSVCellData-object.
   *
   * @return the value of this cell data object.
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Returns always false, as this is a data cell.
   *
   * @return false, as this is no background cell.
   */
  public boolean isBackground()
  {
    return false;
  }
}
