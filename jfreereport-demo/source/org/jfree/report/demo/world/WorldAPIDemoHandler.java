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
 * WorldAPIDemoHandler.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo.world;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.TableReportDataFactory;
import org.jfree.report.demo.util.AbstractDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.demo.util.SimpleDemoFrame;
import org.jfree.report.demo.JFreeReportDemoBoot;
import org.jfree.report.flow.DefaultReportJob;
import org.jfree.report.flow.ReportJob;
import org.jfree.util.ObjectUtilities;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 02.12.2006, 20:39:50
 *
 * @author Thomas Morgner
 */
public class WorldAPIDemoHandler extends AbstractDemoHandler
{
  private CountryDataTableModel data;

  public WorldAPIDemoHandler()
  {
    data = new CountryDataTableModel();
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName()
  {
    return "World Demo (API)";
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
    final WorldAPIDemoBuilder builder = new WorldAPIDemoBuilder();
    final DefaultReportJob job = new DefaultReportJob(builder.createReport());
    final TableReportDataFactory dataFactory =
        new TableReportDataFactory("default", new CountryDataTableModel());
    job.setDataFactory(dataFactory);
    return job;
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("world-demo-api.html", WorldDemoHandler.class);
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
    return createDefaultTable(data);
  }

  public static void main(String[] args)
  {
    JFreeReportDemoBoot.getInstance().start();

    SimpleDemoFrame demoFrame = new SimpleDemoFrame(new WorldAPIDemoHandler());
    demoFrame.init();
    demoFrame.pack();
    RefineryUtilities.centerFrameOnScreen(demoFrame);
    demoFrame.setVisible(true);

  }

}
