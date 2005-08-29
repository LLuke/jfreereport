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
 * SportsCouncilDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SportsCouncilDemo.java,v 1.7 2005/08/08 15:36:29 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.demo.sportscouncil;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

public class SportsCouncilDemo extends AbstractXmlDemoHandler
{
  private SportsCouncilTableModel data;

  public SportsCouncilDemo()
  {
    data = SportsCouncilTableModel.createDefaultModel();
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("council.xml", SportsCouncilDemo.class);
  }

  public String getDemoName()
  {
    return "Sports-Council Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("sportscouncil.html", SportsCouncilDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
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

    final SportsCouncilDemo handler = new SportsCouncilDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
