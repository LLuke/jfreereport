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
 * -------------------------
 * PreReportFooterState.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PreReportFooterState.java,v 1.14 2003/06/29 16:59:28 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.states;

import org.jfree.report.event.ReportEvent;

/**
 * At least the report has been finished. There is no more data to print, so just fire
 * that ReportFinishedEvent and advance to the state FinishState.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public final class PreReportFooterState extends ReportState
{
  /**
   * Creates a 'pre-report-footer' report state.
   *
   * @param previous  the previous report state.
   */
  public PreReportFooterState(final ReportState previous)
  {
    super(previous);
  }

  /**
   * Advances from this state to the next.
   *
   * @return the next report state.
   */
  public ReportState advance()
  {
    firePrepareEvent(ReportEvent.REPORT_FINISHED);

    fireReportFinishedEvent();
    return new PostReportFooterState(this);
  }
}
