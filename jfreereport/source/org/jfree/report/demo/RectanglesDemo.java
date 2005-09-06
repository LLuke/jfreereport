package org.jfree.report.demo;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.util.ObjectUtilities;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 04.09.2005, 14:52:06
 *
 * @author: Thomas Morgner
 */
public class RectanglesDemo extends AbstractXmlDemoHandler
{
  public RectanglesDemo()
  {
  }

  public String getDemoName()
  {
    return "Tables";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(new DefaultTableModel (10, 10));
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return null;
  }

  public JComponent getPresentationComponent()
  {
    return new JPanel();
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("report5.xml", RectanglesDemo.class);
  }

  public static void main(String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final RectanglesDemo handler = new RectanglesDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);

  }
}
