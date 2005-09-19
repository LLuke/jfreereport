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
 * ReportRunner.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportRunner.java,v 1.1 2005/03/24 23:11:01 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.ext;

import org.jfree.report.JFreeReport;
import org.jfree.report.ext.junit.TestSystem;
import org.jfree.report.ext.junit.base.functionality.FunctionalityTestLib;
import org.jfree.report.modules.output.table.xls.ExcelReportUtil;

public class ReportRunner
{
  public ReportRunner ()
  {
  }

  public static void main (final String[] args)
          throws Exception
  {
    final FunctionalityTestLib.ReportTest test = FunctionalityTestLib.REPORTS[0];
    final JFreeReport report =
            TestSystem.loadReport(test.getReportDefinition(), test.getReportTableModel());
    ExcelReportUtil.createXLS(report, "/tmp/report1-strict.xls", true);
//    FunctionalityTestLib.createXLS(report);
  }
}
