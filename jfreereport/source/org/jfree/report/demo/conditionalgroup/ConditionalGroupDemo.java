/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ConditionalGroupDemo.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConditionalGroupDemo.java,v 1.7 2005/08/08 15:36:27 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 24.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.conditionalgroup;

import java.math.BigDecimal;
import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

public class ConditionalGroupDemo extends AbstractXmlDemoHandler
{
  private ConditionalGroupTableModel data;

  public ConditionalGroupDemo ()
  {
    this.data = new ConditionalGroupTableModel();
    this.data.addRecord("Income", "Account 1", null, new BigDecimal("9999.99"));
    this.data.addRecord("Income", "Account 2", null, new BigDecimal("9999.99"));
    this.data.addRecord("Expense", "Account A", "Account Z", new BigDecimal("9999.99"));
    this.data.addRecord("Expense", "Account A", "Account Y", new BigDecimal("9999.99"));
    this.data.addRecord("Expense", "Account B", null, new BigDecimal("9999.99"));
  }

  public String getDemoName()
  {
    return "Conditional Group Demo";
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("conditional.html", ConditionalGroupDemo.class);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("conditional-group-demo.xml", ConditionalGroupDemo.class);
  }


  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(data);
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

    final ConditionalGroupDemo handler = new ConditionalGroupDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
