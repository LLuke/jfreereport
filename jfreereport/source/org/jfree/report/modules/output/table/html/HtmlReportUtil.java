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
 * HTMLReportUtil.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HTMLReportUtil.java,v 1.6 2003/11/21 14:48:17 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05-Jul-2003 : Initial version
 *
 */

package org.jfree.report.modules.output.table.html;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;

/**
 * Utility class to provide an easy to use default implementation of
 * html exports.
 *
 * @author Thomas Morgner
 */
public final class HtmlReportUtil
{
  /**
   * DefaultConstructor.
   *
   */
  private HtmlReportUtil()
  {
  }

  /**
   * Saves a report into a single HTML format.
   *
   * @param report  the report.
   * @param filename target file name.
   *
   * @throws ReportProcessingException if the report processing failed.
   * @throws java.io.IOException if there was an IOerror while processing the report.
   */
  public static void createStreamHTML(final JFreeReport report, final String filename)
      throws IOException, ReportProcessingException
  {
    final File file = new File (filename);
    final HtmlProcessor pr = new HtmlProcessor(report);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(file));
    pr.setFilesystem(new StreamHtmlFilesystem(fout, true, file.getParentFile().toURL()));
    pr.processReport();
    fout.close();
  }

  /**
   * Saves a report to HTML. The HTML file is stored in a directory.
   *
   * @param report  the report.
   * @param filename target file name.
   *
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createDirectoryHTML(final JFreeReport report, final String filename)
      throws IOException, ReportProcessingException
  {
    final HtmlProcessor pr = new HtmlProcessor(report);
    pr.setFilesystem(new DirectoryHtmlFilesystem(new File(filename)));
    pr.processReport();
  }

  /**
   * Saves a report in a ZIP file. The zip file contains a HTML document.
   *
   * @param report  the report.
   * @param filename target file name.
   *
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException if there was an IOerror while processing the report.
   */
  public static void createZIPHTML(final JFreeReport report, final String filename)
      throws IOException, ReportProcessingException
  {
    final HtmlProcessor pr = new HtmlProcessor(report);
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    pr.setFilesystem(new ZIPHtmlFilesystem(fout, "data"));
    pr.processReport();
    fout.close();
  }

}
