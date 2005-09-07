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
 * OpenSourceXMLDemoHandler.java
 * -------------------
 * (C)opyright 2002, 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: OpenSourceXMLDemoHandler.java,v 1.1 2005/08/29 17:41:32 taqua Exp $
 *
 * Changes
 * -------
 * 29-Nov-2002 : Version 1 (DG);
 *
 */

package org.jfree.report.demo.opensource;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.WaitingImageObserver;

/**
 * A simple JFreeReport demonstration.  The generated report lists some free and open
 * source software projects for the Java programming language.
 *
 * @author David Gilbert
 */
public class OpenSourceXMLDemoHandler extends AbstractXmlDemoHandler
{
  /**
   * The data for the report.
   */
  private TableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public OpenSourceXMLDemoHandler ()
  {
    this.data = new OpenSourceProjects();
  }

  public String getDemoName()
  {
    return "Open Source Demo (XML)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(data);

    final URL imageURL = ObjectUtilities.getResourceRelative
            ("gorilla.jpg", OpenSourceXMLDemoHandler.class);
    final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();
    report.setProperty("logo", image);
    report.setPropertyMarked("logo", true);

    return report;
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("opensource-xml.html", OpenSourceXMLDemoHandler.class);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("opensource.xml", OpenSourceXMLDemoHandler.class);
  }
}