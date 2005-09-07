package org.jfree.report.demo.onetomany;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 27.08.2005, 13:12:03
 *
 * @author: Thomas Morgner
 */
public class PeopleReportXmlDemoHandler extends AbstractXmlDemoHandler
{
  private PeopleReportTableModel tableModel;

  public PeopleReportXmlDemoHandler()
  {
    tableModel = new PeopleReportTableModel();
  }

  public String getDemoName()
  {
    return "One-To-Many-Elements Reports Demo (XML-Version)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(tableModel);
    return null;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("people-xml.html", PeopleReportXmlDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(tableModel);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("joined-report.xml", PeopleReportXmlDemoHandler.class);
  }
}
