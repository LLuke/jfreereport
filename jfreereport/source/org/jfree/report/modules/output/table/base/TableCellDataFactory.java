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
 * -------------------------
 * TableCellDataFactory.java
 * -------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableCellDataFactory.java,v 1.2 2003/08/24 15:06:10 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */
package org.jfree.report.modules.output.table.base;

import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;

/**
 * Creates TableCellData object from the given Elements and the element's bounds.
 * The contents and the behaviour of the TableCellDataFactory is dependent on the
 * TableWriter implementation.
 * <p>
 * The TableWriter will use the generated TableCellData to fill the TableGrid.
 *
 * @see TableWriter
 * @see TableCellData
 * @see TableGrid
 *
 * @author Thomas Morgner
 */
public interface TableCellDataFactory
{
  /**
   * Creates a {@link TableCellData} for an {@link Element}. The generated CellData
   * should contain copies of all needed element attributes, as the element instance
   * will be reused in the later report processing.
   * <p>
   * If the tablemodel does not support the element type, return <code>null</code>.
   * <p>
   * Bands should be marked by returning a TableBandArea - do not return a plain
   * TableCellData instance unless you want the band to override all other content
   * of the band.
   *
   * @param e  the element that should be converted into TableCellData.
   * @param rect  the element's bounds within the table (specified in points).
   *
   * @return The generated TableCellData object, or <code>null</code> if element type is
   *         not supported.
   */
  public TableCellData createCellData(Element e, Rectangle2D rect);
}
