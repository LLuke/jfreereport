/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ------------------------------
 * DataRowBackendTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 30.05.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.base.basic;

import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;
import com.jrefinery.report.DataRowBackend;

public class DataRowBackendTest extends TestCase
{
  public DataRowBackendTest(String s)
  {
    super(s);
  }

  public void testCreate()
  {
    DataRowBackend db = new DataRowBackend();
    assertTrue(db.getColumnCount() == 0);
    assertTrue(db.getCurrentRow() == -1);
    assertNull(db.getFunctions());
    assertNull(db.getReportProperties());
    assertNull(db.getTablemodel());
    assertTrue(db.isBeforeFirstRow());
    assertTrue(db.isLastRow());
  }

  public void testMethods ()
  {
    DataRowBackend db = new DataRowBackend();
    db.setCurrentRow(-1);
    DefaultTableModel model = new DefaultTableModel();
    Object[][] data = new Object[][]{
      { new Integer(1), "a" },
      { new Integer(2), "b" },
      { new Integer(3), "c" }
    };
    String[] columns = new String[]{
      "Number", "Letter"
    };
    model.setDataVector(data, columns);
    db.setTablemodel(model);
    assertTrue("" + db.getColumnCount(), db.getColumnCount() == data[0].length);
    db.setCurrentRow(0);
    db.setCurrentRow(1);
    db.setCurrentRow(2);
    try
    {
      db.setCurrentRow(3);
      fail();
    }
    catch (IllegalArgumentException e)
    {
    }

    for (int i = 0; i < data.length; i++)
    {
      db.setCurrentRow(i);
      assertEquals(db.get(0), data[i][0]);
      assertEquals(db.get("Number"), data[i][0]);
      assertEquals(db.get(1), data[i][1]);
      assertEquals(db.get("Letter"), data[i][1]);
    }
  }
}
