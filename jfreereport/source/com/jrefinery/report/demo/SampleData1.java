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
 * SampleData1.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: SampleData1.java,v 1.3 2002/05/23 22:32:22 taqua Exp $
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
public class SampleData1 extends AbstractTableModel
{

  /** Storage for the data. */
  protected Object data[][];

  /**
   * Default constructor - builds the sample data source using incomplete (and possibly
   * inaccurate) data for countries of the world.
   */
  public SampleData1 ()
  {
    data = new Object[23][4];
    data[0] = new Object[]{"Morocco", "MA", "Africa", new Integer (29114497)};
    data[1] = new Object[]{"South Africa", "ZA", "Africa", new Integer (40583573)};
    data[2] = new Object[]{"China", "CN", "Asia", new Integer (1254400000)};
    data[3] = new Object[]{"Iran", "IR", "Asia", new Integer (66000000)};
    data[4] = new Object[]{"Iraq", "IQ", "Asia", new Integer (19700000)};
    data[5] = new Object[]{"Australia", "AU", "Australia", new Integer (18751000)};
    data[6] = new Object[]{"Austria", "AT", "Europe", new Integer (8015000)};
    data[7] = new Object[]{"Belgium", "BE", "Europe", new Integer (10213752)};
    data[8] = new Object[]{"Estonia", "EE", "Europe", new Integer (1445580)};
    data[9] = new Object[]{"Finland", "FI", "Europe", new Integer (5171000)};
    data[10] = new Object[]{"France", "FR", "Europe", new Integer (60186184)};
    data[11] = new Object[]{"Germany", "DE", "Europe", new Integer (82037000)};
    data[12] = new Object[]{"Hungary", "HU", "Europe", new Integer (10044000)};
    data[13] = new Object[]{"Italy", "IT", "Europe", new Integer (57612615)};
    data[14] = new Object[]{"Norway", "NO", "Europe", new Integer (4445460)};
    data[15] = new Object[]{"Poland", "PL", "Europe", new Integer (38608929)};
    data[16] = new Object[]{"Portugal", "PT", "Europe", new Integer (9918040)};
    data[17] = new Object[]{"Spain", "ES", "Europe", new Integer (39669394)};
    data[18] = new Object[]{"Sweden", "SE", "Europe", new Integer (8854322)};
    data[19] = new Object[]{"Switzerland", "CH", "Europe", new Integer (7123500)};
    data[20] = new Object[]{"Canada", "CA", "North America", new Integer (30491300)};
    data[21] = new Object[]{"United States of America", "US", "North America", new Integer (273866000)};
    data[22] = new Object[]{"Brazil", "BR", "South America", new Integer (165715400)};
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
    return 4;
  }

  /**
   * Returns the class of the data in the specified column.
   */
  public Class getColumnClass (int columnIndex)
  {
    if (columnIndex == 3)
      return Integer.class;
    else
      return String.class;
  }

  /**
   * Returns the name of the specified column.
   */
  public String getColumnName (int columnIndex)
  {
    if (columnIndex == 0)
      return "Country";
    else if (columnIndex == 1)
      return "ISO Code";
    else if (columnIndex == 2)
      return "Continent";
    else if (columnIndex == 3)
      return "Population";
    else
      return null;
  }

  /**
   * Returns the data value at the specified row and column.
   */
  public Object getValueAt (int row, int column)
  {
    return data[row][column];
  }

}
