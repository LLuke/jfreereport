/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * CountryDataTableModel.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo.world;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * A sample data source for the JFreeReport Demo Application.
 *
 * @author David Gilbert
 */
public class CountryDataTableModel extends AbstractTableModel
{

  /**
   * Storage for the data.
   */
  private ArrayList data;

  /**
   * Default constructor - builds the sample data source using incomplete (and
   * possibly inaccurate) data for countries of the world.
   */
  public CountryDataTableModel()
  {
    data = new ArrayList();
    data.add(new Object[]{"Morocco", "MA", "Africa", new Integer(29114497)});
    data.add(new Object[]{"South Africa", "ZA", "Africa", new Integer(40583573)});
    data.add(new Object[]{"China", "CN", "Asia", new Integer(1254400000)});
    data.add(new Object[]{"Iran", "IR", "Asia", new Integer(66000000)});
    data.add(new Object[]{"Iraq", "IQ", "Asia", new Integer(19700000)});
    data.add(new Object[]{"Australia", "AU", "Australia", new Integer(18751000)});
    data.add(new Object[]{"Austria", "AT", "Europe", new Integer(8015000)});
    data.add(new Object[]{"Belgium", "BE", "Europe", new Integer(10213752)});
    data.add(new Object[]{"Estonia", "EE", "Europe", new Integer(1445580)});
    data.add(new Object[]{"Finland", "FI", "Europe", new Integer(5171000)});
    data.add(new Object[]{"France", "FR", "Europe", new Integer(60186184)});
    data.add(new Object[]{"Germany", "DE", "Europe", new Integer(82037000)});
    data.add(new Object[]{"Hungary", "HU", "Europe", new Integer(10044000)});
    data.add(new Object[]{"Italy", "IT", "Europe", new Integer(57612615)});
    data.add(new Object[]{"Norway", "NO", "Europe", new Integer(4445460)});
    data.add(new Object[]{"Poland", "PL", "Europe", new Integer(38608929)});
    data.add(new Object[]{"Portugal", "PT", "Europe", new Integer(9918040)});
    data.add(new Object[]{"Spain", "ES", "Europe", new Integer(39669394)});
    data.add(new Object[]{"Sweden", "SE", "Europe", new Integer(8854322)});
    data.add(new Object[]{"Switzerland", "CH", "Europe", new Integer(7123500)});
    data.add(new Object[]{"Canada", "CA", "North America", new Integer(30491300)});
    data.add(new Object[]{"United States of America",
        "US", "North America", new Integer(273866000)});
    data.add(new Object[]{"Brazil", "BR", "South America", new Integer(165715400)});
//    final Object[] o = new Object[]{"Brazil", "BR", "South America", new Integer(165715400)};
//    for (int i = 0; i < 20000; i++)
//    {
//      data.add(o);
//    }
  }

  /**
   * Returns the number of rows in the table model.
   *
   * @return the row count.
   */
  public int getRowCount()
  {
    return data.size();
  }

  /**
   * Returns the number of columns in the table model.
   *
   * @return the column count.
   */
  public int getColumnCount()
  {
    return 4;
  }

  /**
   * Returns the class of the data in the specified column.
   *
   * @param column the column (zero-based index).
   * @return the column class.
   */
  public Class getColumnClass(final int column)
  {
    if (column == 3)
    {
      return Integer.class;
    }
    else
    {
      return String.class;
    }
  }

  /**
   * Returns the name of the specified column.
   *
   * @param column the column (zero-based index).
   * @return the column name.
   */
  public String getColumnName(final int column)
  {
    if (column == 0)
    {
      return "Country";
    }
    else if (column == 1)
    {
      return "ISO Code";
    }
    else if (column == 2)
    {
      return "Continent";
    }
    else if (column == 3)
    {
      return "Population";
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
  public Object getValueAt(final int row, final int column)
  {
    Object[] rowData = (Object[]) data.get(row);
    return rowData[column];
  }

}
