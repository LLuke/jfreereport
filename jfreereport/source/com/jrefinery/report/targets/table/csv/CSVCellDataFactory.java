/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------------
 * CSVCellDataFactory.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVCellDataFactory.java,v 1.8 2003/06/27 14:25:24 taqua Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version;
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package com.jrefinery.report.targets.table.csv;

import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;

/**
 * Creates a {@link TableCellData} object from the given {@link Element} and the element's bounds.
 * The factory handles text elements and ignores all other elements.
 *
 * @author Thomas Morgner
 */
public class CSVCellDataFactory implements TableCellDataFactory
{
  /**
   * Creates the {@link TableCellData} for the given {@link Element}. The CellData is created,
   * when the element value is a string and not <code>null</code>.
   * <p>
   * If the tablemodel does not support the element type, <code>null</code> is returned.
   *
   * @param e  the element that should be converted into {@link TableCellData}.
   * @param rect  the element's bounds within the table. The bounds are specified
   *              in points.
   *
   * @return The generated {@link TableCellData} object, or <code>null</code> if the element type
   *         is not supported.
   */
  public TableCellData createCellData(final Element e, final Rectangle2D rect)
  {
    final Object value = e.getValue();
    if ((value != null) && (value instanceof String))
    {
      return new CSVCellData((String) value, rect);
    }
    else
    {
      return null;
    }
  }
}
