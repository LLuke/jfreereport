/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * SampleData2.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: SampleData6.java,v 1.1 2002/05/31 19:15:13 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 *
 */

package com.jrefinery.report.demo;

import javax.swing.table.AbstractTableModel;

/**
 * A sample data source for the JFreeReport Demo Application.
 */
public class SampleData6 extends AbstractTableModel
{

  /**
   * Default constructor - builds a sample data source.
   */
  public SampleData6 ()
  {
  }

  /**
   * Returns the number of rows in the table model.
   */
  public int getRowCount ()
  {
    return 200000;
  }

  /**
   * Returns the number of columns in the table model.
   */
  public int getColumnCount ()
  {
    return 5;
  }

  /**
   * Returns the class of the data in the specified column.
   */
  public Class getColumnClass (int columnIndex)
  {
    if (columnIndex == 3)
      return Integer.class;
    else if (columnIndex == 4)
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
      return "Name";
    else if (columnIndex == 1)
      return "Color";
    else if (columnIndex == 2)
      return "Letter";
    else if (columnIndex == 3)
      return "Integer";
    else if (columnIndex == 4)
      return "Double";
    else
      return null;
  }

  /**
   * Returns the data value at the specified row and column.
   */
  public Object getValueAt (int row, int column)
  {
    Integer number = new Integer (row);
    Object[] rowdata = new Object[]{"One" + number.toString (), "Red" + number.toString (), "A" + number.toString (), new Integer (1), new Double (1.1)};
    return rowdata[column];
  }

}
