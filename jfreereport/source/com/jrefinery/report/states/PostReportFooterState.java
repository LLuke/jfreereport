/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * --------------------
 * ReportGenerator.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PostReportFooterState.java,v 1.7 2003/05/16 17:26:44 taqua Exp $
 *
 * Changes
 * -------
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.event.ReportEvent;

/**
 * The report is done. No advance will be done, every call to advance will throw an
 * ReportProcessingException.
 *
 * @author David Gilbert
 */
public class PostReportFooterState extends ReportState
{
  /**
   * Creates a new 'finish' report state.
   *
   * @param previous  the previous state.
   */
  public PostReportFooterState(ReportState previous)
  {
    super(previous);
  }

  /**
   * Advance to the FinishState. The PostReportFooterState is used to catch a
   * continued reportfooter.
   *
   * @return a finish state, as there is nothing to be done after the report footer
   * has been completed.
   * @throws ReportProcessingException if advancing failed.
   */
  public ReportState advance() throws ReportProcessingException
  {
    firePrepareEvent(ReportEvent.REPORT_DONE);

    fireReportDoneEvent();
    return new FinishState(this);
  }

}
