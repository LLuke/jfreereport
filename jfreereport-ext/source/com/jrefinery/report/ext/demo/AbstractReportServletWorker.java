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
 * $Id: AbstractReportServletWorker.java,v 1.4 2003/03/02 21:36:41 taqua Exp $
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

/**
 * The report servlet worker provides the infrastructure needed to process the
 * report. The worker stores the processed data in the provided session.
 * 
 * @author Thomas Morgner
 */
public abstract class AbstractReportServletWorker
{
  /** the processed report. */
  private JFreeReport report;
  /** the current http session. */
  private HttpSession session;

  /**
   * Creates a new Report servlet worker for the given session.
   *
   * @param session the session.
   */
  public AbstractReportServletWorker (HttpSession session)
  {
    this.session = session;
  }

  /**
   * Loads the report. If a session is used, it is checked, whether a report is
   * bound to the session (using the attribute "Report"), if no report is bound
   * to the session or no session is used at all, the report is created using
   * <code>createReport</code>.
   *
   * @return the loaded or created report
   * @throws ReportInitialisationException if loading the report was not successfull.
   */
  public JFreeReport loadReport()
      throws ReportInitialisationException
  {
    JFreeReport report = null;
    if (isSessionRequired())
    {
      report = (JFreeReport) session.getAttribute(getPropertyPrefix() + "Report");
      if (report == null)
      {
        report = createReport();
        if (report == null)
          throw new ReportInitialisationException("Created report is null");

        session.setAttribute(getPropertyPrefix() + "Report", report);
      }
    }
    else
    {
      report = createReport();
      if (report == null)
        throw new ReportInitialisationException("Created report is null");

    }
    return report;
  }

  /**
   * Load or parses the report definition. The report is fully initialized.
   * The data model has to be assigned to the report.
   *
   * @return the initialized report definition.
   * @throws ReportInitialisationException
   */
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
   * Parses the report and returns the fully initialized report. A data model is
   * already assigned to the report.
   *
   * @return the created report.
   * @throws ReportInitialisationException if the report creation failed.
   */
  protected abstract JFreeReport createReport()
      throws ReportInitialisationException;


  /**
   * Checks, whether the session should be used to store some of the generated
   * data.
   *
   * @return true, if the session should be used to store the data, false otherwise.
   */
  protected boolean isSessionRequired()
  {
    return session != null;
  }

  /**
   * Gets the current session. The session can be null.
   *
   * @return the session or null, if no session is set.
   */
  protected HttpSession getSession()
  {
    return session;
  }

  /**
   * Get the page format that should be used to process the report.
   *
   * @return the report's page format.
   * @throws ReportInitialisationException if the report could not be initialized,
   * so that no page format could be read.
   */
  public PageFormat getReportPageFormat ()
    throws ReportInitialisationException
  {
    return getReport().getDefaultPageFormat();
  }

  /**
   * Processes the report. How this is done, is implementation specific.
   *
   * @throws ReportProcessingException if something went wrong during the report processing.
   */
  public abstract void processReport()
      throws ReportProcessingException;


  /**
   * Returns a property prefix for the various used report properties. This can
   * be used to host several reports on a single session.
   *
   * @return the property prefix, an empty string by default.
   */
  protected String getPropertyPrefix()
  {
    return "";
  }

}
