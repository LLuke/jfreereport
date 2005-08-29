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
 * -------------------
 * PercentageDemo.java
 * -------------------
 * (C)opyright 2003, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PercentageDemo.java,v 1.11 2005/08/08 15:36:27 taqua Exp $
 *
 * Changes
 * -------
 * 04-Apr-2003 : Version 1 (DG);
 *
 */

package org.jfree.report.demo.functions;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * A simple report where column 3 displays (column 1 / column 2) as a percentage.
 *
 * @author David Gilbert
 */
public class PercentageDemo extends AbstractXmlDemoHandler
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
  public PercentageDemo ()
  {
    data = createData();
  }

  public String getDemoName()
  {
    return "Percentage Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(this.data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("percentage.html", PercentageDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("percentage.xml", PercentageDemo.class);
  }

  /**
   * Creates a sample dataset. <!-- (Used in JUnitTest) -->
   *
   * @return A <code>TableModel</code>.
   */
  public static TableModel createData ()
  {
    final DefaultTableModel data = new DefaultTableModel();
    data.addColumn("A");
    data.addColumn("B");
    data.addRow(new Object[]{new Double(43.0), new Double(127.5)});
    data.addRow(new Object[]{new Double(57.0), new Double(108.5)});
    data.addRow(new Object[]{new Double(35.0), new Double(164.8)});
    data.addRow(new Object[]{new Double(86.0), new Double(164.0)});
    data.addRow(new Object[]{new Double(12.0), new Double(103.2)});
    return data;
  }

  public static void main (final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final PercentageDemo demoHandler = new PercentageDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(demoHandler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}