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
 * MultipleJFreeChartDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: MultipleJFreeChartDemo.java,v 1.1 2005/05/31 18:27:27 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.ext.chartdemo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Rotation;

public class TableJFreeChartDemo extends AbstractDemoFrame
{
  protected class PreviewXMLReportAction extends AbstractActionDowngrade
  {
    public PreviewXMLReportAction(final String actionName)
    {
      putValue(Action.NAME, actionName);
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      attemptPreview();
    }
  }

  private static final String SIMPLE_DEMO_DEF = "table-chart-simple.xml";

  private TableModel data;


  /**
   * Creates a new demo.
   */
  public TableJFreeChartDemo()
  {
    setTitle("JFreeChart demo (Multiple charts)");

    data = createTableModel();

    final JTabbedPane chartPanes = new JTabbedPane();
    for (int i = 0; i < 12; i++)
    {
      // create the chart...
      final JFreeChart chart = (JFreeChart) data.getValueAt(i * 5, 3);

      // add the chart to a panel...
      final ChartPanel chartPanel = new ChartPanel(chart);
      //chartPanel.setPreferredSize(new Dimension(500, 270));
      chartPanes.add(chartPanel, chart.getTitle().getText());
    }
    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(chartPanes);
    setContentPane(contentPane);
    buildMenu();
  }

  private void buildMenu()
  {
    final JMenu demoMenu = new JMenu("Demo");
    demoMenu.add(new ActionMenuItem(new PreviewXMLReportAction
        ("Preview")));

    demoMenu.addSeparator();
    demoMenu.add(new ActionMenuItem(getCloseAction()));

    final JMenuBar menubar = new JMenuBar();
    menubar.add(demoMenu);
    setJMenuBar(menubar);
  }

  /**
   * Creates a sample dataset for the demo.
   *
   * @return A sample dataset.
   */
  private PieDataset createSampleDataset(int[] votes)
  {
    final DefaultPieDataset result = new DefaultPieDataset();
    // cheating: java has a higher chance to be the best language :)
    result.setValue("Java", new Integer(votes[0]));
    result.setValue("Visual Basic", new Integer(votes[1]));
    result.setValue("C/C++", new Integer(votes[2]));
    result.setValue("PHP", new Integer(votes[3]));
    result.setValue("Perl", new Integer(votes[4]));
    return result;

  }

  /**
   * Creates a sample chart.
   *
   * @return A chart.
   */
  private JFreeChart createChart(final int year, int[] votes)
  {

    final JFreeChart chart = ChartFactory.createPieChart3D(
        "Programming Language of the Year " + (year), // chart title
        createSampleDataset(votes), // data
        true, // include legend
        true,
        false
    );

    // set the background color for the chart...
    final PiePlot3D plot = (PiePlot3D) chart.getPlot();
    plot.setStartAngle(270);
    plot.setDirection(Rotation.CLOCKWISE);
    plot.setForegroundAlpha(0.5f);
    plot.setNoDataMessage("No data to display");

    return chart;

  }

  /**
   * Starting point for the demonstration application.
   *
   * @param args  ignored.
   */
  public static void main(final String[] args)
  {
    JFreeReportBoot.getInstance().start();

    final TableJFreeChartDemo demo = new TableJFreeChartDemo();
    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);

  }

  private TableModel createTableModel ()
  {
    final Object[][] data = new Object[12 * 5][];
    final int[] votes = new int[5];
    for (int i = 0; i < 12; i++)
    {
      final Integer year = new Integer (1995 + i);
      votes[0] = (int) (Math.random() * 200);
      votes[1] = (int) (Math.random() * 50);
      votes[2] = (int) (Math.random() * 100);
      votes[3] = (int) (Math.random() * 50);
      votes[4] = (int) (Math.random() * 100);

      final JFreeChart chart = createChart(year.intValue(), votes);

      data[i * 5] = new Object[] {
        year, "Java", new Integer(votes[0]), chart
      };
      data[i * 5 + 1] = new Object[] {
        year, "Visual Basic", new Integer(votes[1]), chart
      };
      data[i * 5 + 2] = new Object[] {
        year, "C/C++", new Integer(votes[2]), chart
      };
      data[i * 5 + 3] = new Object[] {
        year, "PHP", new Integer(votes[3]), chart
      };
      data[i * 5 + 4] = new Object[] {
        year, "Perl", new Integer(votes[4]), chart
      };

    }

    final String[] colNames = {
      "year", "language", "votes", "chart"
    };

    return new DefaultTableModel(data, colNames);
  }
  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview()
  {
    try
    {
      final URL url = ObjectUtilities.getResourceRelative(SIMPLE_DEMO_DEF, TableJFreeChartDemo.class);
      final JFreeReport report = ReportGenerator.getInstance().parseReport(url);
      report.setData(data);

      // marking was done by declaring the property-ref

      final PreviewDialog dialog = new PreviewDialog(report);
      dialog.getBase().setToolbarFloatable(true);
      dialog.pack();
      RefineryUtilities.positionFrameRandomly(dialog);
      dialog.setVisible(true);
      dialog.requestFocus();
    }
    catch (Exception re)
    {
      showExceptionDialog("report.definitionfailure", re);
    }
  }
}
