/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: AbstractPageableReportServletWorker.java,v 1.5 2005/08/09 15:44:01 taqua Exp $
 *
 * Changes
 * -------
 * 24-Jan-2003: Initial version
 */
package org.jfree.report.ext.servletdemo;

import javax.servlet.http.HttpSession;

import org.jfree.report.ReportInitialisationException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;

/**
 * The report servlet worker provides the infrastructure needed to process the
 * report with a Pageable output target. The worker handles the repagination and
 * report initiatisation.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractPageableReportServletWorker
    extends AbstractReportServletWorker
{
  /** the report processor that should be used to process the report. */
  private PageableReportProcessor processor;
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
  public AbstractPageableReportServletWorker(final HttpSession session)
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
  public void setOutputTarget(final OutputTarget outputTarget)
  {
    this.outputTarget = outputTarget;
  }

  /**
   * Repaginates the report. If no output target is set, a IllegalStateException is
   * thrown.
   *
   * @throws ReportInitialisationException if the report could not be initialized.
   * @throws ReportProcessingException if the report processing failes.
   * @throws IllegalStateException if no output target is defined.
   */
  private void repaginateReport()
      throws ReportInitialisationException,
      ReportProcessingException
  {
    if (outputTarget == null)
    {
      throw new IllegalStateException("No Output target defined");
    }

    if (isSessionRequired())
    {
      // todo: Store the report processor in the session?
      //final HttpSession session = getSession();
      processor = new PageableReportProcessor(getReport());
      // set a dummy target for the repagination
      processor.setOutputTarget(getOutputTarget());

      if (processor.isPaginated() == false)
      {
        processor.repaginate();
      }
    }
    else
    {
      processor = new PageableReportProcessor(getReport());
      // set a dummy target for the repagination
      processor.setOutputTarget(getOutputTarget());
      processor.repaginate();
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
   */
  public int getNumberOfPages ()
    throws  ReportInitialisationException, ReportProcessingException
  {
    if (!isPaginated)
    {
      repaginateReport();
    }

    return processor.getPageCount();
  }

  /**
   * Processes a single page of the report. If the report is not yet paginated,
   * the pagination is done before.
   *
   * @param page the page that should be processed.
   * @throws ReportProcessingException  if the report processing failed.
   * @throws IndexOutOfBoundsException if the page is invalid.
   */
  public void processPage (final int page)
    throws ReportProcessingException
  {
    try
    {
      getOutputTarget().open();
      if (!isPaginated)
      {
        repaginateReport();
      }


      processor.processPage(processor.createPageProcess(), page, true);
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
      final PageableReportProcessor processor = new PageableReportProcessor(getReport());
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
