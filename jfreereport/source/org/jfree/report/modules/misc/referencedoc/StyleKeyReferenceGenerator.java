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
 * -------------------------------
 * StyleKeyReferenceGenerator.java
 * -------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleKeyReferenceGenerator.java,v 1.4 2003/08/24 15:08:20 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.misc.referencedoc;

import java.net.URL;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.output.pageable.pdf.PDFReportUtil;
import org.jfree.report.modules.output.table.html.HtmlReportUtil;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;

/**
 * An application that generates a report that provides style key reference information.
 *
 * @author Thomas Morgner.
 */
public final class StyleKeyReferenceGenerator
{
  /** The report definition file. */
  private static final String REFERENCE_REPORT = "StyleKeyReferenceReport.xml";

  /**
   * DefaultConstructor.
   */
  private StyleKeyReferenceGenerator()
  {
  }

  /**
   * Creates the default tablemodel for the stylekey reference generator.
   *
   * @return the tablemodel for the stylekey reference generator.
   */
  public static TableModel createData()
  {
    final StyleKeyFactoryCollector cc = new StyleKeyFactoryCollector();
    cc.addFactory(new DefaultStyleKeyFactory());
    cc.addFactory(new PageableLayoutStyleKeyFactory());

    final StyleKeyReferenceTableModel model = new StyleKeyReferenceTableModel(cc);
    return model;
  }

  /**
   * The starting point for the application.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {

    final ReportGenerator gen = ReportGenerator.getInstance();
    final URL reportURL = StyleKeyReferenceGenerator.class.getResource(REFERENCE_REPORT);
    if (reportURL == null)
    {
      System.err.println("The report was not found in the classpath");
      System.err.println("File: " + REFERENCE_REPORT);
      System.exit(1);
      return;
    }

    final JFreeReport report;
    try
    {
      report = gen.parseReport(reportURL);
    }
    catch (Exception e)
    {
      System.err.println("The report could not be parsed.");
      System.err.println("File: " + REFERENCE_REPORT);
      e.printStackTrace(System.err);
      System.exit(1);
      return;
    }
    report.setData(createData());
    try
    {
      HtmlReportUtil.createStreamHTML(report, System.getProperty("user.home")
          + "/stylekey-reference.html");
      PDFReportUtil.createPDF(report, System.getProperty("user.home")
          + "/stylekey-reference.pdf");
    }
    catch (Exception e)
    {
      System.err.println("The report processing failed.");
      System.err.println("File: " + REFERENCE_REPORT);
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

}
