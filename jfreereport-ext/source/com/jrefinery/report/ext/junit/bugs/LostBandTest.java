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
 * LineBreakTest.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LostBandTest.java,v 1.2 2003/04/23 17:32:36 taqua Exp $
 *
 * Changes
 * -------
 * 11.04.2003 : Initial version
 */
package com.jrefinery.report.ext.junit.bugs;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.table.TableModel;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ext.junit.TestSystem;
import com.jrefinery.report.tablemodel.ResultSetTableModelFactory;

public class LostBandTest
{
  public static void main(String[] args)
      throws Exception
  {
    Class c = Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

    Driver d = (Driver) c.newInstance();
    Connection con = d.connect("jdbc:odbc:Arrgh", new Properties());
/*
    ResultSet resMeta = con.getMetaData().getTables(null, null, null, null);
    TableModel modMeta = ResultSetTableModelFactory.getInstance().generateDefaultTableModel(resMeta);
//    TableModelInfo.printTableModelContents(modMeta);
    resMeta.close();
*/
    Statement stmt = con.createStatement();
    ResultSet res = stmt.executeQuery("SELECT * FROM \"Sheet1$\"");
    TableModel mod = ResultSetTableModelFactory.getInstance().generateDefaultTableModel(res);
    res.close();
    con.close();

//    TableModelInfo.printTableModel(mod);

    JFreeReport report = TestSystem.loadReport("/com/jrefinery/report/ext/junit/bugs/resource/LostBands.xml", mod);
    if (report == null)
      System.exit(1);

    TestSystem.showPreviewFrame(report);

  }

}
