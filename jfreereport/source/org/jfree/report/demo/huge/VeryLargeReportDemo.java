package org.jfree.report.demo.huge;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.util.ObjectUtilities;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 28.08.2005, 20:13:43
 *
 * @author: Thomas Morgner
 */
public class VeryLargeReportDemo extends AbstractXmlDemoHandler
{
  private HugeLetterAndColorTableModel data;

  public VeryLargeReportDemo()
  {
    data = new HugeLetterAndColorTableModel();
  }

  public String getDemoName()
  {
    return "Very Large Report Demo";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("large-report.html", VeryLargeReportDemo.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("large-report.xml", VeryLargeReportDemo.class);
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

    final VeryLargeReportDemo handler = new VeryLargeReportDemo();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
