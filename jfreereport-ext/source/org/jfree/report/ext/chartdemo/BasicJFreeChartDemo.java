/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * BasicJFreeChartDemo.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BasicJFreeChartDemo.java,v 1.2 2005/01/31 17:14:41 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.11.2003 : Initial version
 *
 */

package org.jfree.report.ext.chartdemo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.net.URL;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.elementfactory.DrawableFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.FloatDimension;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class BasicJFreeChartDemo extends AbstractDemoFrame
{
  protected class PreviewAPIReportAction extends AbstractActionDowngrade
  {
    public PreviewAPIReportAction()
    {
      putValue(NAME, "Preview API Report");
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      attemptAPIPreview();
    }
  }

  protected class PreviewXMLReportAction extends AbstractActionDowngrade
  {
    private String resourceName;

    public PreviewXMLReportAction(final String resourceName, final String actionName)
    {
      putValue(NAME, actionName);
      this.resourceName = resourceName;
    }

    /**
     * Receives notification of an action event.
     *
     * @param event  the event.
     */
    public void actionPerformed(final ActionEvent event)
    {
      attemptXMLPreview(resourceName);
    }
  }

  private static final String EXT_DEMO_DEF = "basic-chart-ext.xml";
  private static final String SIMPLE_DEMO_DEF = "basic-chart-simple.xml";

  /**
   * Creates a new demo.
   */
  public BasicJFreeChartDemo()
  {
    setTitle("JFreeChart demo");

    // create a dataset...
    final PieDataset dataset = createSampleDataset();

    // create the chart...
    final JFreeChart chart = createChart(dataset);

    // add the chart to a panel...
    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    setContentPane(chartPanel);

    buildMenu();
  }

  private void buildMenu()
  {
    final JMenu demoMenu = new JMenu("Demo");
    demoMenu.add(new ActionMenuItem(new PreviewAPIReportAction()));
    demoMenu.add(new ActionMenuItem(new PreviewXMLReportAction
        (EXT_DEMO_DEF, "Preview " + EXT_DEMO_DEF)));
    demoMenu.add(new ActionMenuItem(new PreviewXMLReportAction
        (SIMPLE_DEMO_DEF, "Preview " + SIMPLE_DEMO_DEF)));

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
  private PieDataset createSampleDataset()
  {

    final DefaultPieDataset result = new DefaultPieDataset();
    result.setValue("Java", new Double(43.2));
    result.setValue("Visual Basic", new Double(10.0));
    result.setValue("C/C++", new Double(17.5));
    result.setValue("PHP", new Double(32.5));
    result.setValue("Perl", new Double(0.0));
    return result;

  }

  /**
   * Creates a sample chart.
   *
   * @param dataset  the dataset.
   *
   * @return A chart.
   */
  private JFreeChart createChart(final PieDataset dataset)
  {

    final JFreeChart chart = ChartFactory.createPieChart3D(
        "Pie Chart 3D Demo 1", // chart title
        dataset, // data
        true, // include legend
        true,
        false
    );

    // set the background color for the chart...
    chart.setBackgroundPaint(Color.yellow);
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

    final BasicJFreeChartDemo demo = new BasicJFreeChartDemo();
    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);

  }

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptAPIPreview()
  {
    // empty as implementation side effect
    final JFreeReport report = new JFreeReport();

    final DrawableFieldElementFactory factory = new DrawableFieldElementFactory();
    factory.setName("drawable-field");
    factory.setAbsolutePosition(new Point2D.Float(7, 7));
    factory.setMinimumSize(new FloatDimension(400, 250));
    factory.setFieldname("Chart");
    report.getReportHeader().addElement(factory.createElement());

    // create a dataset...
    final PieDataset dataset = createSampleDataset();
    // create the chart...
    final JFreeChart chart = createChart(dataset);
    report.setProperty("Chart", chart);
    report.setPropertyMarked("Chart", true);

    try
    {
      final PreviewDialog dialog = new PreviewDialog(report);
      dialog.getBase().setToolbarFloatable(true);
      dialog.pack();
      RefineryUtilities.positionFrameRandomly(dialog);
      dialog.setVisible(true);
      dialog.requestFocus();
    }
    catch (ReportProcessingException re)
    {
      showExceptionDialog("report.definitionfailure", re);
    }
  }

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptXMLPreview(final String resourceName)
  {
    try
    {
      final URL url = getClass().getResource(resourceName);
      final JFreeReport report = ReportGenerator.getInstance().parseReport(url);

      // create a dataset...
      final PieDataset dataset = createSampleDataset();
      // create the chart...
      final JFreeChart chart = createChart(dataset);
      report.setProperty("Chart", chart);
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

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview()
  {
    // empty as implementation side effect
  }
}
