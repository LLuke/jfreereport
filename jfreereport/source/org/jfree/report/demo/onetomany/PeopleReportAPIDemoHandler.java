package org.jfree.report.demo.onetomany;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 27.08.2005, 13:17:33
 *
 * @author: Thomas Morgner
 */
public class PeopleReportAPIDemoHandler extends AbstractDemoHandler
{
  private PeopleReportTableModel tableModel;

  public PeopleReportAPIDemoHandler()
  {
    tableModel = new PeopleReportTableModel();
  }

  public String getDemoName()
  {
    return null;
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    final PeopleReportDefinition reportCreator = new PeopleReportDefinition();
    final JFreeReport report = reportCreator.getReport();
    report.setData(tableModel);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("people-api.html", PeopleReportAPIDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(tableModel);
  }

}
