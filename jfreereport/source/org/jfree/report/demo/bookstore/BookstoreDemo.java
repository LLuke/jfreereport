/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * BookstoreDemo.java
 * ----------------
 * (C)opyright 2000-2005, by Simba Management Limited.
 *
 *
 * $Id: BookstoreTableModel.java,v 1.1 2005/08/29 17:30:33 taqua Exp $
 *
 */
package org.jfree.report.demo.bookstore;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A demo showing how to print simple invoices. Other invoice-like reports
 * can be found in the package {@link org.jfree.report.demo.invoice}.
 *
 * @author: Thomas Morgner
 */
public class BookstoreDemo extends AbstractXmlDemoHandler
{
  /** The data model to be used in the demo. */
  private TableModel data;

  /**
   * Default constructor.
   */
  public BookstoreDemo()
  {
    data = new BookstoreTableModel();
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName()
  {
    return "Bookstore (Invoice) Demo";
  }

  /**
   * Creates the report. This calls the standard parse method and then assigns
   * the table model to the report.
   *
   * @return the fully initialized JFreeReport object.
   * @throws ReportDefinitionException if an error occured preventing the
   * report definition.
   */
  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("bookstore.html", BookstoreDemo.class);
  }

  /**
   * Returns the presentation component for this demo. This component is
   * shown before the real report generation is started. Ususally it contains
   * a JTable with the demo data and/or input components, which allow to configure
   * the report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  /**
   * Returns the URL of the XML definition for this report.
   *
   * @return the URL of the report definition.
   */
  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("bookstore.xml", BookstoreDemo.class);
  }


  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final BookstoreDemo handler = new BookstoreDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
