/**
 * Date: Jan 24, 2003
 * Time: 11:47:55 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.io.ReportGenerator;
import com.jrefinery.report.targets.table.TableProcessor;

import javax.swing.table.TableModel;
import java.net.URL;
import java.io.IOException;

public class DefaultTableReportServletWorker extends AbstractTableReportServletWorker
{
  private URL url;
  private TableModel model;

  public DefaultTableReportServletWorker(URL url, TableModel model)
  {
    super();
    this.url = url;
    this.model = model;
  }

  /**
   * parses the report and returns the fully initialized report.
   * @return
   */
  protected JFreeReport createReport()
      throws ReportInitialisationException
  {
    try
    {
      JFreeReport report = parseReport(url);
      report.setData(model);
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
