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
 * MultiPageCountryDataDemoHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MultiPageCountryDataDemoHandler.java,v 1.2 2005/10/11 19:27:56 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.world;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextOutputTarget;
import org.jfree.report.modules.output.pageable.plaintext.PrinterDriver;
import org.jfree.report.modules.output.pageable.plaintext.TextFilePrinterDriver;
import org.jfree.report.util.NullOutputStream;
import org.jfree.util.ObjectUtilities;

public class MultiPageCountryDataDemoHandler extends AbstractXmlDemoHandler
{
  /**
   * The data for the report.
   */
  private final TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public MultiPageCountryDataDemoHandler ()
  {
    this.data = new CountryDataTableModel();
  }


  public String getDemoName()
  {
    return "Page Spanning Country Report Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("multipage-country-report.html", CountryReportSecurityXMLDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("multipage-country-report.xml", CountryReportSecurityXMLDemoHandler.class);
  }


  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
          throws Exception
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final MultiPageCountryDataDemoHandler handler = new MultiPageCountryDataDemoHandler();
//    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
//    frame.init();
//    frame.pack();
//    RefineryUtilities.centerFrameOnScreen(frame);
//    frame.setVisible(true);
    final JFreeReport report = handler.createReport();
    final PageableReportProcessor pr = new PageableReportProcessor(report);
    final OutputStream fout = new BufferedOutputStream(
            new NullOutputStream());
    final PrinterDriver pc =
            new TextFilePrinterDriver(fout, 10, 15);
    final PlainTextOutputTarget target =
            new PlainTextOutputTarget(pc);

    pr.setOutputTarget(target);
    target.open();
    pr.processReport();
    target.close();
    fout.close();

  }
}
