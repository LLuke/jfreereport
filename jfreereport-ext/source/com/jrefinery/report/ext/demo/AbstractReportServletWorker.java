/**
 * Date: Jan 24, 2003
 * Time: 10:12:01 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.ReportProcessingException;

import javax.servlet.http.HttpSession;
import java.awt.print.PageFormat;

public abstract class AbstractReportServletWorker
{
  private JFreeReport report;
  private HttpSession session;

  public AbstractReportServletWorker (HttpSession session)
  {
    this.session = session;
  }

  public JFreeReport loadReport()
      throws ReportInitialisationException
  {
    JFreeReport report = null;
    if (isSessionRequired())
    {
      report = (JFreeReport) session.getAttribute("Report");
      if (report == null)
      {
        report = createReport();
        if (report == null)
          throw new ReportInitialisationException("Created report is null");

        session.setAttribute("Report", report);
      }
    }
    else
    {
      report = createReport();
    }
    return report;
  }



  public JFreeReport getReport()
    throws ReportInitialisationException
  {
    if (report == null)
    {
      report = loadReport();
    }
    return report;
  }

  /**
   * parses the report and returns the fully initialized report.
   * @return
   */
  protected abstract JFreeReport createReport()
      throws ReportInitialisationException;


  protected boolean isSessionRequired()
  {
    return session != null;
  }

  protected HttpSession getSession()
  {
    return session;
  }

  public PageFormat getReportPageFormat ()
    throws ReportInitialisationException
  {
    return getReport().getDefaultPageFormat();
  }

  public abstract void processReport()
      throws ReportProcessingException;

}
