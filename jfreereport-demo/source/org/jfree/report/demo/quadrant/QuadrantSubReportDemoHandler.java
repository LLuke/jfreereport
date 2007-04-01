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
 * QuadrantSubReportDemoHandler.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo.quadrant;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

import org.jfree.report.demo.util.AbstractXmlDemoHandler;
import org.jfree.report.demo.util.ReportDefinitionException;
import org.jfree.report.flow.DefaultReportJob;
import org.jfree.report.flow.ReportJob;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 02.12.2006, 20:11:18
 *
 * @author Thomas Morgner
 */
public class QuadrantSubReportDemoHandler extends AbstractXmlDemoHandler
{
  public QuadrantSubReportDemoHandler()
  {
  }

  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName()
  {
    return "Quadrant Demo with SubReport";
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
    return new DefaultReportJob(parseReport());
  }

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("quadrant-subreport.html", QuadrantSubReportDemoHandler.class);
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
    return createDefaultTable(new DefaultTableModel());
  }

  /**
   * Returns the URL of the XML definition for this report.
   *
   * @return the URL of the report definition.
   */
  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("quad-subreport.xml", QuadrantSubReportDemoHandler.class);
  }
}
