/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * SampleData3.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.demo;

import javax.swing.table.AbstractTableModel;

public class SampleData3 extends AbstractTableModel
{
  /** Storage for the data. */
  protected Object data[][];

  /**
   * Default constructor - builds a sample data source.
   *
   */
  public SampleData3 ()
  {
    data = new Object[][]
    {
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "01212", "Robert A. Heinlein - Starship Trooper", new Integer (1), new Double (12.49)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "01231", "Robert A. Heinlein - Glory Road", new Integer (1), new Double (12.99)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "12121", "Frank Herbert - Dune", new Integer (1), new Double (10.99)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "A1232", "Bierce Ambrose - The Devils Dictionary", new Integer (2), new Double (19.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "12333", "Samuel Adams - How to sell tea ", new Integer (100), new Double (10.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "88812", "Adam Smith - The wealth of nations", new Integer (1), new Double (49.95)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "33123", "D. Khan - How to conquer friends", new Integer (1), new Double (15.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "33123", "D. Khan - How to conquer friends", new Integer (1), new Double (19.49)},
      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123", "D. Khan - How to conquer friends", new Integer (1), new Double (15.99)},
      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123", "J. Ceaser - Choosing the right friends", new Integer (1), new Double (25.99)},
      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123", "Galileo - When to tell the truth", new Integer (1), new Double (29.59)}
    };
  }

  /**
   * Returns the number of rows in the table model.
   */
  public int getRowCount ()
  {
    return data.length;
  }

  /**
   * Returns the number of columns in the table model.
   */
  public int getColumnCount ()
  {
    return 8;
  }

  /**
   * Returns the class of the data in the specified column.
   */
  public Class getColumnClass (int columnIndex)
  {
    if (columnIndex == 5)
      return Integer.class;
    else if (columnIndex == 6)
      return Double.class;
    else
      return String.class;
  }

  /**
   * Returns the name of the specified column.
   */
  public String getColumnName (int columnIndex)
  {
    if (columnIndex == 0)
      return "name";
    else if (columnIndex == 1)
      return "street";
    else if (columnIndex == 2)
      return "town";
    else if (columnIndex == 3)
      return "productcode";
    else if (columnIndex == 4)
      return "productname";
    else if (columnIndex == 5)
      return "count";
    else if (columnIndex == 6)
      return "price";
    else if (columnIndex == 7)
      return "total";
    else
      return null;
  }

  /**
   * Returns the data value at the specified row and column.
   */
  public Object getValueAt (int row, int column)
  {
    if (column == 7)
    {
      Integer i = (Integer) data[row][5];
      Double  d = (Double) data[row][6];
      return new Double (i.intValue() * d.doubleValue());
    }
    else
    return data[row][column];
  }

}
