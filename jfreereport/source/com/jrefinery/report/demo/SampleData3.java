/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------
 * SampleData3.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 *
 * $Id: SampleData3.java,v 1.8 2002/12/12 12:26:55 mungady Exp $
 *
 */
package com.jrefinery.report.demo;

import javax.swing.table.AbstractTableModel;

/**
 * A sample data source for the JFreeReport Demo Application.
 *
 * @author Thomas Morgner
 */
public class SampleData3 extends AbstractTableModel
{
  /** Storage for the data. */
  private Object data[][];

  /**
   * Default constructor - builds a sample data source.
   *
   */
  public SampleData3()
  {
    data = new Object[][]
    {
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "01212",
       "Robert A. Heinlein - Starship Trooper", new Integer(1), new Double(12.49)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "01231",
       "Robert A. Heinlein - Glory Road", new Integer(1), new Double(12.99)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "12121",
       "Frank Herbert - Dune", new Integer(1), new Double(10.99)},
      {"Mr. Black", "1666 Pennsylvania Ave.", "012345 Washington", "A1232",
       "Bierce Ambrose - The Devils Dictionary", new Integer(2), new Double(19.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "12333",
       "Samuel Adams - How to sell tea ", new Integer(100), new Double(10.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "88812",
       "Adam Smith - The wealth of nations", new Integer(1), new Double(49.95)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "33123",
       "D. Khan - How to conquer friends", new Integer(1), new Double(15.99)},
      {"John F. Google", "12a Nowaday Road", "99999 Boston", "33123",
       "D. Khan - How to conquer friends", new Integer(1), new Double(19.49)},
      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123",
       "D. Khan - How to conquer friends", new Integer(1), new Double(15.99)},
      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123",
       "J. Ceaser - Choosing the right friends", new Integer(1), new Double(25.99)},
      {"Cleeve Johnson", "87 Oakham Drive", "99999 Boston", "33123",
       "Galileo - When to tell the truth", new Integer(1), new Double(29.59)}
    };
  }

  /**
   * Returns the number of rows in the table model.
   *
   * @return the row count.
   */
  public int getRowCount()
  {
    return data.length;
  }

  /**
   * Returns the number of columns in the table model.
   *
   * @return the column count.
   */
  public int getColumnCount()
  {
    return 8;
  }

  /**
   * Returns the class of the data in the specified column.
   *
   * @param column  the column (zero-based index).
   *
   * @return the column class.
   */
  public Class getColumnClass(int column)
  {
    if (column == 5)
    {
      return Integer.class;
    }
    else if (column == 6)
    {
      return Double.class;
    }
    else
    {
      return String.class;
    }
  }

  /**
   * Returns the name of the specified column.
   *
   * @param column  the column (zero-based index).
   *
   * @return the column name.
   */
  public String getColumnName(int column)
  {
    if (column == 0)
    {
      return "name";
    }
    else if (column == 1)
    {
      return "street";
    }
    else if (column == 2)
    {
      return "town";
    }
    else if (column == 3)
    {
      return "productcode";
    }
    else if (column == 4)
    {
      return "productname";
    }
    else if (column == 5)
    {
      return "count";
    }
    else if (column == 6)
    {
      return "price";
    }
    else if (column == 7)
    {
      return "total";
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns the data value at the specified row and column.
   *
   * @param row  the row index (zero based).
   * @param column  the column index (zero based).
   *
   * @return the value.
   */
  public Object getValueAt(int row, int column)
  {
    if (column == 7)
    {
      Integer i = (Integer) data[row][5];
      Double d = (Double) data[row][6];
      return new Double(i.intValue() * d.doubleValue());
    }
    else
    {
      return data[row][column];
    }
  }

}
