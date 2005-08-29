package org.jfree.report.demo.world;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 28.08.2005, 20:13:43
 *
 * @author: Thomas Morgner
 */
public class CountryReportSecurityXMLDemoHandler extends AbstractXmlDemoHandler
{
  private CountryDataTableModel data;

  public CountryReportSecurityXMLDemoHandler()
  {
    data = new CountryDataTableModel();
  }

  public String getDemoName()
  {
    return "Country Report Demo (PDF Security)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("country-report-security.html", CountryReportSecurityXMLDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("country-report-security.xml", CountryReportSecurityXMLDemoHandler.class);
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

    final CountryReportSecurityXMLDemoHandler handler = new CountryReportSecurityXMLDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
