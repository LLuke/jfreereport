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
 * CSVCellDataFactory.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CSVCellDataFactory.java,v 1.4 2003/02/03 18:52:47 taqua Exp $
 *
 * Changes
 * -------
 * 21-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.csv;

import com.jrefinery.report.Element;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;

import java.awt.geom.Rectangle2D;

/**
 * Creates TableCellData object from the given Elements and the element's bounds.
 * The CellDataFactory handles TextElements and ignores all other elements.
 */
public class CSVCellDataFactory implements TableCellDataFactory
{
  /**
   * Creates the TableCellData for the given Element. The CellData is created,
   * when the element value is a string and not null.
   * <p>
   * If the tablemodel does not support the element type, null is returned.
   *
   * @param e the element that should be converted into TableCellData.
   * @param rect the elements bounds within the table. The bounds are specified
   * in points.
   * @return null if element type is not supported or the generated TableCellData object.
   */
  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    Object value = e.getValue();
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
