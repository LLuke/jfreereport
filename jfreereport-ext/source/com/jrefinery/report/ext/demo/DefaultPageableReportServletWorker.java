/**
 * Date: Jan 24, 2003
 * Time: 10:35:18 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.net.URL;

public class DefaultPageableReportServletWorker
    extends AbstractPageableReportServletWorker
{
  private URL reportDefinition;
  private TableModel data;

  public DefaultPageableReportServletWorker(HttpSession session, URL report, TableModel data)
  {
    super(session);
    this.reportDefinition = report;
    this.data = data;
  }

  /**
   * parses the report and returns the fully initialized report.
   * @return
   */
  protected JFreeReport createReport() throws ReportInitialisationException
  {
    try
    {
      JFreeReport report = parseReport(reportDefinition);
      report.setData(data);
      return report;
    }
    catch (Exception e)
    {
      throw new ReportInitialisationException("Unable to create the report");
    }
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL  the template location.
   *
   * @return a report.
   */
  private JFreeReport parseReport(URL templateURL)
    throws IOException
  {
    JFreeReport result = null;
    ReportGenerator generator = ReportGenerator.getInstance();
    try
    {
        result = generator.parseReport(templateURL);
    }
    catch (Exception e)
    {
      Log.debug ("Cause: ", e);
      throw new IOException("Failed to parse the report");
    }
    return result;
  }
}
