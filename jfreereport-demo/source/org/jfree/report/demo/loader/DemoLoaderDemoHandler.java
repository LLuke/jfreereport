/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * DemoLoaderDemoHandler.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo.loader;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.report.demo.util.AbstractDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.PreviewHandler;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.report.demo.JFreeReportDemoBoot;
import org.jfree.report.flow.DefaultReportJob;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.JFreeReport;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 03.12.2006, 14:29:35
 *
 * @author Thomas Morgner
 */
public class DemoLoaderDemoHandler extends AbstractDemoHandler
{
  public DemoLoaderDemoHandler()
  {
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName()
  {
    return "Load-Your-Own-Report Demo";
  }

  /**
   * Creates the report. For XML reports, this will most likely call the
   * ReportGenerator, while API reports may use this function to build and
   * return a new, fully initialized report object.
   *
   * @return the fully initialized JFreeReport object.
   * @throws org.jfree.report.demo.util.ReportDefinitionException
   *          if an error occured preventing the report definition.
   */
  public ReportJob createReport() throws ReportDefinitionException
  {
    // no real report here, as the report is loaded later ..
    // this method will never be called anyway
    return new DefaultReportJob(new JFreeReport());
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource()
  {
    return null;
  }

  /**
   * Returns the presentation component for this demo. This component is shown
   * before the real report generation is started. Ususally it contains a JTable
   * with the demo data and/or input components, which allow to configure the
   * report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent()
  {
    return new JPanel();
  }

  public PreviewHandler getPreviewHandler()
  {
    return new DemoLoaderPreviewHandler();
  }

  public static void main(String[] args)
  {
    JFreeReportDemoBoot.getInstance().start();

    SimpleDemoFrame demoFrame = new SimpleDemoFrame(new DemoLoaderDemoHandler());
    demoFrame.init();
    demoFrame.pack();
    RefineryUtilities.centerFrameOnScreen(demoFrame);
    demoFrame.setVisible(true);
  }
}
