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
 * PreReportHeaderState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportHeader;

/**
 * Initial state for a report. Prints the report header and proceeds to PostProcessHeader-State.
 * <p>
 * alias PreReportHeaderState<br>
 * advances to PostReportHeaderState<br>
 * before the print, a reportStarted event gets fired.
 */
public class PreReportHeaderState extends ReportState
{
  /**
   * Default constructor and the only constructor to create a state without cloning another.
   *
   * @param previousState  the previous report state.
   */
  public PreReportHeaderState (ReportState previousState)
  {
    super (previousState);
  }

  /**
   * Advances from this state to the next.  In the process, the report header is printed.
   *
   * @param rpc  the report processor.
   *
   * @return the next state ('post-report-header').
   */
  public ReportState advance (ReportProcessor rpc)
  {
    this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
    JFreeReport report = this.getReport ();
    ReportHeader reportHeader = report.getReportHeader ();
    rpc.printReportHeader (reportHeader);
    return new PostReportHeaderState (this);
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet advanced
   * we perform a readAHead lookup when populating elements.
   *
   * @return true
   */
  public boolean isPrefetchState ()
  {
    return true;
  }
}
