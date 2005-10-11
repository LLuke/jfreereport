package org.jfree.report.demo.stylesheets;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.report.demo.helper.SimpleDemoFrame;
import org.jfree.report.demo.world.CountryDataTableModel;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.util.ObjectUtilities;
import org.jfree.ui.RefineryUtilities;

/**
 * Creation-Date: 11.10.2005, 12:54:29
 *
 * @author Thomas Morgner
 */
public class StyleSheetDemoHandler extends AbstractXmlDemoHandler
{
  private TableModel data;

  public StyleSheetDemoHandler()
  {
    data = new CountryDataTableModel();
  }

  public String getDemoName()
  {
    return "WorldDemo using StyleSheets and Macros";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("stylesheets.html", StyleSheetDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative("stylesheets.xml", StyleSheetDemoHandler.class);
  }

  public static void main(String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final StyleSheetDemoHandler handler = new StyleSheetDemoHandler();
    final SimpleDemoFrame frame = new SimpleDemoFrame(handler);
    frame.init();
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }
}
