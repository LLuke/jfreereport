/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ----------------
 * CSVCellData.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVCellData.java,v 1.4 2003/02/24 15:06:31 mungady Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version;
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 * 
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.targets.table.TableCellData;

import java.awt.geom.Rectangle2D;

/**
 * The CSV extension of the {@link TableCellData} base class. The cell data implementation contains
 * the string value of the element value.
 * 
 * @author Thomas Morgner
 */
public class CSVCellData extends TableCellData
{
  /** The value. */
  private String value;

  /**
   * Creates a new <code>CSVCellData</code> object.
   *
   * @param value  the value of the cell (<code>null</code> not permitted).
   * @param outerBounds  the element bounds.
   */
  public CSVCellData(String value, Rectangle2D outerBounds)
  {
    super(outerBounds);
    if (value == null) 
    {
      throw new NullPointerException();
    }
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
   * Returns always <code>false</code>, as this is a data cell.
   *
   * @return <code>false</code>, as this is no background cell.
   */
  public boolean isBackground()
  {
    return false;
  }
}
