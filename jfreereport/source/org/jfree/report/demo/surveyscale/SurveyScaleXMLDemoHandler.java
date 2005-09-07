package org.jfree.report.demo.surveyscale;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.demo.helper.AbstractXmlDemoHandler;
import org.jfree.report.demo.helper.ReportDefinitionException;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 27.08.2005, 16:27:19
 *
 * @author: Thomas Morgner
 */
public class SurveyScaleXMLDemoHandler extends AbstractXmlDemoHandler
{
  /**
   * The core data for the report.
   */
  private TableModel data;

  public SurveyScaleXMLDemoHandler()
  {
    data = new SurveyScaleDemoTableModel();
  }

  public String getDemoName()
  {
    return "Survey Scale Demo Report (XML)";
  }

  public JFreeReport createReport() throws ReportDefinitionException
  {
    JFreeReport report = parseReport();
    report.setData(data);
    return report;
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("survey-xml.html", SurveyScaleXMLDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  public URL getReportDefinitionSource()
  {
    return ObjectUtilities.getResourceRelative
            ("surveyscale.xml", SurveyScaleXMLDemoHandler.class);
  }
}
