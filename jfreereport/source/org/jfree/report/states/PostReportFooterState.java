/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * --------------------------
 * PostReportFooterState.java
 * --------------------------
 * (C)opyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PostReportFooterState.java,v 1.4 2005/01/28 19:26:59 taqua Exp $
 *
 * Changes
 * -------
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.states;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;

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
   * @param previous the previous state.
   */
  public PostReportFooterState (final ReportState previous)
  {
    super(previous);
  }

  public int getEventCode ()
  {
    return ReportEvent.REPORT_DONE;
  }

  /**
   * Advance to the FinishState. The PostReportFooterState is used to catch a continued
   * reportfooter.
   *
   * @return a finish state, as there is nothing to be done after the report footer has
   *         been completed.
   *
   * @throws ReportProcessingException if advancing failed.
   */
  public ReportState advance ()
          throws ReportProcessingException
  {
    firePrepareEvent();

    fireReportDoneEvent();
    return new FinishState(this);
  }

}
