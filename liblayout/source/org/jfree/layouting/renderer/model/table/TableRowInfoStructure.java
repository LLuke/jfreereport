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
 * TableRowInfoStructure.java
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
package org.jfree.layouting.renderer.model.table;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.table.cells.TableCell;

/**
 * Creation-Date: 10.09.2006, 20:01:18
 *
 * @author Thomas Morgner
 */
public class TableRowInfoStructure
{
  private ArrayList cells;
  private boolean validationDone;
  private int rowNumber;

  public TableRowInfoStructure()
  {
    cells = new ArrayList();
  }

  public void addCell (TableCell cell)
  {
    if (cells.size() > 6)
    {
      throw new IllegalStateException();
    }
    cells.add(cell);
  }

  public int getCellCount ()
  {
    return cells.size();
  }

  public TableCell getCellAt (int col)
  {
    return (TableCell) cells.get(col);
  }

  public boolean isValidationDone()
  {
    return validationDone;
  }

  public void setValidationDone(final boolean validationDone)
  {
    this.validationDone = validationDone;
  }

  public int getRowNumber()
  {
    return rowNumber;
  }

  public void setRowNumber(final int rowNumber)
  {
    this.rowNumber = rowNumber;
  }
}
