/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * PreReportFooterState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: PreReportFooterState.java,v 1.3 2002/11/07 21:45:28 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;

/**
 * At least the report has been finished. There is no more data to print, so just print
 * the reportHeader and advance to the state FinishState. Before printing the header fire the
 * reportFinished event.
 */
public class PreReportFooterState extends ReportState
{
  /**
   * Creates a 'pre-report-footer' report state.
   *
   * @param previous  the previous report state.
   */
  public PreReportFooterState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances from this state to the next.
   *
   * @return the next report state.
   */
  public ReportState advance ()
  {
    fireReportFinishedEvent ();
    return new FinishState (this);
  }
}
