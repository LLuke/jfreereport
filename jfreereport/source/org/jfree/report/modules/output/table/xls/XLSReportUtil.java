/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * XLSReportUtil.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: XLSReportUtil.java,v 1.2 2003/08/18 18:28:01 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.xls;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.FunctionInitializeException;

/**
 * Utility class to provide an easy to use default implementation of
 * excel exports.
 * 
 * @author Thomas Morgner
 */
public final class XLSReportUtil
{  
  /**
   * DefaultConstructor.
   *
   */
  private XLSReportUtil ()
  {
  }

  /**
   * Saves a report to Excel format.
   *
   * @param report  the report.
   * @param filename target file name.
   *
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createXLS(final JFreeReport report, final String filename)
      throws IOException, FunctionInitializeException, ReportProcessingException
  {
    final ExcelProcessor pr = new ExcelProcessor(report);
    pr.setStrictLayout(false);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setOutputStream(fout);
    pr.processReport();
    fout.close();
  }

}
