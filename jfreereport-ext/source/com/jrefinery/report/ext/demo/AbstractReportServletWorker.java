/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * -----------------------
 * AbstractReportServletWorker.java
 * -----------------------
 *
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: AbstractReportServletWorker.java,v 1.1 2003/01/25 02:56:17 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003: Initial version
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
