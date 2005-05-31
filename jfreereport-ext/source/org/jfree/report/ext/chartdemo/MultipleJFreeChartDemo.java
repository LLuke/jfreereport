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
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.ext.chartdemo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
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
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.elementfactory.DrawableFieldElementFactory;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.FloatDimension;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractActionDowngrade;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.Rotation;

public class MultipleJFreeChartDemo extends AbstractDemoFrame
{
  protected class PreviewAPIReportAction extends AbstractActionDowngrade
  {
    public PreviewAPIReportAction()
    {
      putValue(Action.NAME, "Preview API Report");
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
      putValue(Action.NAME, actionName);
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

  private TableModel data;


  /**
   * Creates a new demo.
   */
  public MultipleJFreeChartDemo()
  {
    setTitle("JFreeChart demo (Multiple charts)");

    data = createTableModel();

    final JTabbedPane chartPanes = new JTabbedPane();
    for (int i = 0; i < 12; i++)
    {
      // create the chart...
      final JFreeChart chart = (JFreeChart) data.getValueAt(i, 0);

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
    // cheating: java has a higher chance to be the best language :)
    result.setValue("Java", new Integer((int) (Math.random() * 200)));
    result.setValue("Visual Basic", new Integer((int) (Math.random() * 50)));
    result.setValue("C/C++", new Integer((int) (Math.random() * 100)));
    result.setValue("PHP", new Integer((int) (Math.random() * 50)));
    result.setValue("Perl", new Integer((int) (Math.random() * 100)));
    return result;

  }

  /**
   * Creates a sample chart.
   *
   * @return A chart.
   */
  private JFreeChart createChart(final int year)
  {

    final JFreeChart chart = ChartFactory.createPieChart3D(
        "Programming Language of the Year " + year, // chart title
        createSampleDataset(), // data
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

    final MultipleJFreeChartDemo demo = new MultipleJFreeChartDemo();
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
    report.getItemBand().addElement(factory.createElement());

    report.setData(data);

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

  private TableModel createTableModel ()
  {
    final Object[][] data = new Object[12][];
    for (int i = 0; i < 12; i++)
    {
      data[i] = new Object[]{ createChart(i + 1995)};
    }

    final String[] colNames = {
      "Chart"
    };
    final DefaultTableModel model = new DefaultTableModel(data, colNames);
    return model;
  }
  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptXMLPreview(final String resourceName)
  {
    try
    {
      final URL url = ObjectUtilities.getResourceRelative(resourceName, MultipleJFreeChartDemo.class);
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

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview()
  {
    // empty as implementation side effect
  }
}
