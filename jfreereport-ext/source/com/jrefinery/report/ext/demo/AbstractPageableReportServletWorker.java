/**
 * Date: Jan 24, 2003
 * Time: 10:45:50 PM
 *
 * $Id$
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

public abstract class AbstractPageableReportServletWorker
    extends AbstractReportServletWorker
{
  private PageableReportProcessor processor;
  private ReportStateList pageStateList;

  public AbstractPageableReportServletWorker(HttpSession session)
  {
    super(session);
  }

  public void repaginateReport(OutputTarget target)
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
  }

  public int getNumberOfPages ()
  {
    if (pageStateList == null)
      return 0;

    return pageStateList.size();
  }

  public void processPage (int page)
    throws ReportProcessingException
  {
    try
    {
      OutputTarget target = processor.getOutputTarget();
      target.open();
      ReportState state = pageStateList.get(page);
      processor.processPage(state, target);
      target.close();
    }
    catch (Exception e)
    {
      throw new ReportProcessingException("Failed", e);
    }
  }

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
}
