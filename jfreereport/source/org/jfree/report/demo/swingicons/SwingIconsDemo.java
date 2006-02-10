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
 * -------------------
 * SwingIconsDemo.java
 * -------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: SwingIconsDemo.java,v 1.4 2006/02/10 13:21:34 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jul-2002 : Version 1 (DG);
 * 20-Nov-2002 : Corrected possible read error if the icon is not read completely from the zip file;
 * 27-Feb-2003 : Renamed First.java --> SwingIconsDemo.java (DG);
 *
 */

package org.jfree.report.demo.swingicons;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A demonstration application. <P> This demo is written up in the JFreeReport PDF
 * Documentation.  Please notify David Gilbert (david.gilbert@object-refinery.com) if you
 * need to make changes to this file. <P> To run this demo, you need to have the Java Look
 * and Feel Icons jar file on your classpath.
 *
 * @author David Gilbert
 */
public class SwingIconsDemo extends AbstractXmlDemoHandler
{
  private SwingIconsDemoPanel demoPanel;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public SwingIconsDemo ()
  {
    demoPanel = new SwingIconsDemoPanel();
  }

  public JComponent getPresentationComponent()
  {
    return demoPanel;
  }

  public String getDemoName()
  {
    return "Swing Icons Report";
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("swing-icons.html", SwingIconsDemo.class);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("swing-icons.xml", SwingIconsDemo.class);
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(demoPanel.getData());
    return report;
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

    final SwingIconsDemo handler = new SwingIconsDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
//    ExcelReportUtil.createXLS(handler.createReport(), "/tmp/icons.xls");
  }

}
