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
 * DataRowBackendPreviewTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataRowBackendPreviewTest.java,v 1.1 2003/07/08 14:21:47 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.05.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic;

import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;
import org.jfree.report.DataRowBackend;
import org.jfree.report.DataRowPreview;

public class DataRowBackendPreviewTest extends TestCase
{
  public DataRowBackendPreviewTest(final String s)
  {
    super(s);
  }

  public void testCreate()
  {
    final DataRowBackend db = new DataRowBackend();
    db.setCurrentRow(-1);
    final DefaultTableModel model = new DefaultTableModel();
    final Object[][] data = new Object[][]{
      {new Integer(1), "a"},
      {new Integer(2), "b"},
      {new Integer(3), "c"}
    };
    final String[] columns = new String[]{
      "Number", "Letter"
    };
    model.setDataVector(data, columns);
    db.setTablemodel(model);

    final DataRowPreview drv = new DataRowPreview(db);
    try
    {
      drv.setTablemodel(model);
      fail();
    }
    catch (IllegalStateException e)
    {
    }
    try
    {
      drv.setFunctions(null);
      fail();
    }
    catch (IllegalStateException e)
    {
    }
    try
    {
      drv.setReportProperties(null);
      fail();
    }
    catch (IllegalStateException e)
    {
    }
  }

  public void testFunctionality()
  {
    final DataRowBackend db = new DataRowBackend();
    db.setCurrentRow(-1);
    final DefaultTableModel model = new DefaultTableModel();
    final Object[][] data = new Object[][]{
      {new Integer(1), "a"},
      {new Integer(2), "b"},
      {new Integer(3), "c"}
    };
    final String[] columns = new String[]{
      "Number", "Letter"
    };
    model.setDataVector(data, columns);
    db.setTablemodel(model);

    for (int i = 0; i < data.length; i++)
    {
      db.setCurrentRow(i - 1);
      final DataRowBackend dbv = db.previewNextRow();
      assertEquals(dbv.get(0), data[i][0]);
      assertEquals(dbv.get("Number"), data[i][0]);
      assertEquals(dbv.get(1), data[i][1]);
      assertEquals(dbv.get("Letter"), data[i][1]);
    }

    db.setCurrentRow(data.length - 1);
    // there is no preview for the last row ...
    final DataRowBackend dbv = db.previewNextRow();
    assertNull(dbv);
  }
}
