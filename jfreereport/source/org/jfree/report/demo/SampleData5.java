/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------
 * SampleData5.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SampleData5.java,v 1.5 2005/01/24 23:58:41 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 *
 */

package org.jfree.report.demo;

import javax.swing.table.AbstractTableModel;

/**
 * A large sample data source for the JFreeReport Demo Application.
 *
 * @author Thomas Morgner
 */
public class SampleData5 extends AbstractTableModel
{

  /**
   * Default constructor - builds a sample data source.
   */
  public SampleData5 ()
  {
  }

  /**
   * Returns the number of rows in the table model.
   *
   * @return the row count.
   */
  public int getRowCount ()
  {
    return 120000;
  }

  /**
   * Returns the number of columns in the table model.
   *
   * @return the column count.
   */
  public int getColumnCount ()
  {
    return 5;
  }

  /**
   * Returns the class of the data in the specified column.
   *
   * @param column the column (zero-based index).
   * @return the column class.
   */
  public Class getColumnClass (final int column)
  {
    return Number.class;
  }

  /**
   * Returns the name of the specified column.
   *
   * @param column the column (zero-based index).
   * @return the column name.
   */
  public String getColumnName (final int column)
  {
    if (column == 0)
    {
      return "Name";
    }
    else if (column == 1)
    {
      return "Color";
    }
    else if (column == 2)
    {
      return "Letter";
    }
    else if (column == 3)
    {
      return "Integer";
    }
    else if (column == 4)
    {
      return "Double";
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns the data value at the specified row and column.
   *
   * @param row    the row index (zero based).
   * @param column the column index (zero based).
   * @return the value.
   */
  public strictfp Object getValueAt (final int row, final int column)
  {
    return new Long((long) (row / Math.pow(10, 4 - column)));
  }

}
