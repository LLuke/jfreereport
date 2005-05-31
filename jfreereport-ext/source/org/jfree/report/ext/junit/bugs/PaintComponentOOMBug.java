/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PaintComponentOOMBug.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PaintComponentOOMBug.java,v 1.2 2005/04/15 20:21:02 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.bugs;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.ext.junit.TestSystem;
import org.jfree.report.modules.output.table.csv.CSVReportUtil;

public class PaintComponentOOMBug
{
  private static final String URLNAME = "org/jfree/report/ext/junit/bugs/resource/spanned-header.xml";
//  private static final String URLNAME = "/org/jfree/report/ext/junit/bugs/resource/csv-not-working.xml";

  public PaintComponentOOMBug ()
  {
  }

  public static void main (final String[] args)
          throws Exception
  {
    final String[] colnames = new String []{
      "GroupID", "NetID", "Nickname", "StudentID", "MiddleInitial",
      "FirstName", "LastName", "TransmitterID", "LineNumber", "Total_Value"

    };

    final String[][] data = new String[][]{
      colnames, colnames
    };
    final TableModel model = new DefaultTableModel(data, colnames);
//    final JFreeReport report = new SampleReport2().createReport();
    System.out.println (model.getRowCount());
    final JFreeReport report = TestSystem.loadReport(URLNAME, model);
//    FunctionalityTestLib.createXLS(report);
//    ExcelReportUtil.createXLS(report, "/tmp/export.xls");
//    HtmlReportUtil.createStreamHTML(report, "/tmp/export.html");
//    FunctionalityTestLib.createXLS(report);
    CSVReportUtil.createCSV(report, "/tmp/export.csv");
  }

}
