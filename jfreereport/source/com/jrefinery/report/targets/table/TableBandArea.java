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
 * AbstractTableCellDataFactory.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableBandArea.java,v 1.1 2003/01/28 22:32:58 taqua Exp $
 *
 * Changes
 * -------
 * 28-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table;

import java.awt.geom.Rectangle2D;

/**
 * A band representation. Bands should create their own x- y- cuts so
 * that the layout is more accurate.
 */
public class TableBandArea extends TableCellBackground
{
  /**
   * Creates a new TableBandArea with the given bounds.
   *
   * @param outerBounds the band bounds in the table,
   */
  public TableBandArea(Rectangle2D outerBounds)
  {
    super(outerBounds, null);
  }

  /**
   * Creates a string representation of this band area. 
   *
   * @return a string representation of the band.
   */
  public String toString ()
  {
    return "TableBandArea={bounds=" + getBounds() + "}";
  }
}
