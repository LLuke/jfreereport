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
 * $Id: AbstractPageableReportServletWorker.java,v 1.1 2003/01/25 02:56:17 taqua Exp $
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
