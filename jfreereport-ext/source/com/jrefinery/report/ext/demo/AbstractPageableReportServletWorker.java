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
 * $Id: AbstractPageableReportServletWorker.java,v 1.4 2003/03/02 19:19:24 taqua Exp $
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
  /** the used output target for repagination and report processing. */
  private OutputTarget outputTarget;

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
   * Gets the output target that should be used to process the report.
   *
   * @return the defined output target.
   */
  public OutputTarget getOutputTarget()
  {
    return outputTarget;
  }

  /**
   * Defines the output target that should be used to process the report.
   *
   * @param outputTarget the output target that should be used to repaginate
   * and process the report if necessary.
   */
  public void setOutputTarget(OutputTarget outputTarget)
  {
    this.outputTarget = outputTarget;
  }

  /**
   * Repaginates the report. If no output target is set, a IllegalStateException is
   * thrown.
   *
   * @throws ReportInitialisationException if the report could not be initialized.
   * @throws ReportProcessingException if the report processing failes.
   * @throws FunctionInitializeException if the output function could not be initalized.
   * @throws IllegalStateException if no output target is defined.
   */
  private void repaginateReport()
      throws ReportInitialisationException,
      ReportProcessingException, FunctionInitializeException
  {
    if (outputTarget == null)
    {
      throw new IllegalStateException("No Output target defined");
    }

    if (isSessionRequired())
    {
      HttpSession session = getSession();
      processor = new PageableReportProcessor(getReport());
      // set a dummy target for the repagination
      processor.setOutputTarget(getOutputTarget());

      pageStateList = (ReportStateList) session.getAttribute(getPropertyPrefix() + "PageStateList");
      if (pageStateList == null)
      {
        pageStateList = processor.repaginate();
        // a new report has been created, now store the attribute in the session for a
        // later use
        session.setAttribute(getPropertyPrefix() + "PageStateList", pageStateList);
      }
    }
    else
    {
      processor = new PageableReportProcessor(getReport());
      // set a dummy target for the repagination
      processor.setOutputTarget(getOutputTarget());
      pageStateList = processor.repaginate();
    }

    isPaginated = true;
  }

  /**
   * Returns the number of pages of the paginated report. If the pagination fails
   * one of the various exceptions is thrown.
   *
   * @return the number of pages for the report.
   * @throws ReportInitialisationException if the report could not be initialized.
   * @throws ReportProcessingException if the report processing failes.
   * @throws FunctionInitializeException if the output function could not be initalized.
   */
  public int getNumberOfPages ()
    throws  ReportInitialisationException, ReportProcessingException, FunctionInitializeException
  {
    if (!isPaginated)
    {
      repaginateReport();
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
      getOutputTarget().open();
      if (!isPaginated)
      {
        repaginateReport();
      }

      ReportState state = pageStateList.get(page);
      processor.processPage(state, getOutputTarget());
      getOutputTarget().close();
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
      PageableReportProcessor processor = new PageableReportProcessor(getReport());
      processor.setOutputTarget(getOutputTarget());
      getOutputTarget().open();
      processor.processReport();
      getOutputTarget().close();
    }
    catch (Exception e)
    {
      throw new ReportProcessingException("Failed", e);
    }
  }
}
