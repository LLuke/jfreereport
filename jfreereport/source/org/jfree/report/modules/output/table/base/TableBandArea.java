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
 * TableBandArea.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableBandArea.java,v 1.4 2003/10/10 18:46:48 taqua Exp $
 *
 * Changes
 * -------
 * 28-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;
import java.awt.Color;

/**
 * A band representation. Bands should create their own x- y- cuts so
 * that the layout is more accurate.
 *
 * @author Thomas Morgner.
 */
public class TableBandArea extends TableCellBackground
{
  /**
   * Creates a new TableBandArea with the given bounds.
   *
   * @param outerBounds the band bounds in the table,
   */
  public TableBandArea(final Rectangle2D outerBounds)
  {
    this(outerBounds, null);
  }

  /**
   * Creates a new TableBandArea with the given bounds.
   *
   * @param outerBounds the band bounds in the table
   * @param color the color of the band area (can be null).
   */
  protected TableBandArea(final Rectangle2D outerBounds, final Color color)
  {
    super(outerBounds, color);
  }

  /**
   * Creates a string representation of this band area.
   *
   * @return The string.
   */
  public String toString()
  {
    return "TableBandArea={bounds=" + getBounds() + "}";
  }
}
