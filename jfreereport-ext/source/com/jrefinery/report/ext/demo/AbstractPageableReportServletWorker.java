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
 * AbstractPageableReportServletWorker.java
 * -----------------------
 *
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: AbstractPageableReportServletWorker.java,v 1.3 2003/03/02 04:10:28 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003: Initial version
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.PageableReportProcessor;
import com.jrefinery.report.targets.pageable.ReportStateList;

import javax.servlet.http.HttpSession;

/**
 * The report servlet worker provides the infrastructure needed to process the
 * report with a Pageable output target. The worker handles the repagination and
 * report initiatisation.
 */
public abstract class AbstractPageableReportServletWorker
    extends AbstractReportServletWorker
{
  /** the report processor that should be used to process the report. */
  private PageableReportProcessor processor;
  /** the cached page state list, which is created during the repagination. */
  private ReportStateList pageStateList;
  /** a flag to indicated, whether the pagination is already done. */
  private boolean isPaginated;

  /**
   * Creates a new AbstractPageableReportServletWorker for the given session.
   * The session may be null, if a complete report is processed instead of single
   * pages.
   *
   * @param session the used session or null, if no session processing is requested.
   */
  public AbstractPageableReportServletWorker(HttpSession session)
  {
    super(session);
  }

  /**
   * Repaginates the report.
   *
   * @param target the output target used for the repagination.
   * @throws ReportInitialisationException if the report could not be initialized.
   * @throws ReportProcessingException if the report processing failes.
   * @throws FunctionInitializeException if the output function could not be initalized.
   */
  private void repaginateReport(OutputTarget target)
      throws ReportInitialisationException,
      ReportProcessingException, FunctionInitializeException
  {
    if (isSessionRequired())
    {
      HttpSession session = getSession();
      processor = new PageableReportProcessor(getReport());
      // set a dummy target for the repagination
      processor.setOutputTarget(target);

      pageStateList = (ReportStateList) session.getAttribute("PageStateList");
      if (pageStateList == null)
      {
        pageStateList = processor.repaginate();
        // a new report has been created, now store the attribute in the session for a
        // later use
        session.setAttribute("PageStateList", pageStateList);
      }
    }
    else
    {
      processor = new PageableReportProcessor(getReport());
      // set a dummy target for the repagination
      processor.setOutputTarget(target);
      pageStateList = processor.repaginate();
    }

    isPaginated = true;
  }

  /**
   * Returns the number of pages of the paginated report. If the pagination fails
   * one of the various exceptions is thrown.
   *
   * @param target the output target that should be used to repaginate the report if necessary.
   * @return the number of pages for the report.
   * @throws ReportInitialisationException if the report could not be initialized.
   * @throws ReportProcessingException if the report processing failes.
   * @throws FunctionInitializeException if the output function could not be initalized.
   */
  public int getNumberOfPages (OutputTarget target)
    throws  ReportInitialisationException, ReportProcessingException, FunctionInitializeException
  {
    if (!isPaginated)
    {
      repaginateReport(target);
    }

    if (pageStateList == null)
    {
      return 0;
    }
    return pageStateList.size();
  }

  /**
   * Processes a single page of the report. If the report is not yet paginated,
   * the pagination is done before.
   *
   * @param page the page that should be processed.
   * @throws ReportProcessingException  if the report processing failed.
   * @throws IndexOutOfBoundsException if the page is invalid.
   */
  public void processPage (int page)
    throws ReportProcessingException
  {
    try
    {
      OutputTarget target = processor.getOutputTarget();
      target.open();
      if (!isPaginated)
      {
        repaginateReport(target);
      }

      ReportState state = pageStateList.get(page);
      processor.processPage(state, target);
      target.close();
    }
    catch (Exception e)
    {
      throw new ReportProcessingException("Failed", e);
    }
  }

  /**
   * Processes the complete report, this calls PageableReportProcessor.processReport()
   * and generates the content.
   *
   * @throws ReportProcessingException if something went wrong during the report processing.
   */
  public void processReport ()
      throws ReportProcessingException
  {
    try
    {
      OutputTarget target = processor.getOutputTarget();
      target.open();
      processor.processReport();
      target.close();
    }
    catch (Exception e)
    {
      throw new ReportProcessingException("Failed", e);
    }
  }

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
