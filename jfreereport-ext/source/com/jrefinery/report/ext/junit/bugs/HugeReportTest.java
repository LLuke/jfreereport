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
 * ----------------
 * HugeReportTest.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HugeReportTest.java,v 1.1 2003/04/11 19:32:37 taqua Exp $
 *
 * Changes
 * -------
 * 04.04.2003 : Initial version
 */
package com.jrefinery.report.ext.junit.bugs;

import javax.swing.table.AbstractTableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.demo.SampleData5;
import com.jrefinery.report.ext.junit.TestSystem;

public class HugeReportTest
{
  private static class TestTableModel extends AbstractTableModel
  {
    public TestTableModel()
    {
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount()
    {
      return 300000;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount()
    {
      return 2;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      if (columnIndex == 0)
      {
        return new Integer(rowIndex / 100);
      }
      return new Integer(rowIndex);
    }

    /**
     *  Returns a default name for the column using spreadsheet conventions:
     *  A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     *  returns an empty string.
     *
     * @param column  the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    public String getColumnName(int column)
    {
      if (column == 0)
      {
        return "GroupNumber";
      }
      else
      {
        return "RowNumber";
      }
    }
  }

  public static void main(String[] args)
      throws Exception
  {
    JFreeReport report = TestSystem.loadReport("/com/jrefinery/report/ext/junit/bugs/resource/countryreport.xml", new SampleData5());
    if (report == null)
      System.exit(1);


//    JOptionPane.showMessageDialog(null, "Start!");
    TestSystem.showPreviewFrame(report);
  }
}
