/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * PlainTextReportUtil.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextReportUtil.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.pageable.plaintext;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;

/**
 * An utility class to write an report into a plain text file. If you need more 
 * control over the writing process, you will have to implement your own write
 * method.
 * 
 * @author Thomas Morgner
 */
public final class PlainTextReportUtil
{
  /**
   * Default Constructor.
   *
   */
  private PlainTextReportUtil()
  {
  }
  
  /**
   * Saves a report to plain text format.
   *
   * @param report  the report.
   * @param filename target file name.
   *
   * @throws ReportProcessingException if the report processing failed.
   * @throws FunctionInitializeException if the initialisation of the report processor failed.
   * @throws IOException if there was an IOerror while processing the report.
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public static void createPlainText(final JFreeReport report, final String filename)
      throws IOException, ReportProcessingException,
        FunctionInitializeException, OutputTargetException
  {
    final PageableReportProcessor pr = new PageableReportProcessor(report);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    final PrinterCommandSet pc = new PrinterCommandSet(fout, report.getDefaultPageFormat(), 6, 10);
    final PlainTextOutputTarget target = 
      new PlainTextOutputTarget(report.getDefaultPageFormat(), pc);
    pr.setOutputTarget(target);
    target.open();
    pr.processReport();
    target.close();
    fout.close();
  }


}
