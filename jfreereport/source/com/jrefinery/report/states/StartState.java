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
 * StartState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.event.ReportEvent;

import java.util.Date;

/**
 * Initial state for a report. Prints the report header and proceeds to PostProcessHeader-State.
 * <p>
 * alias PreReportHeaderState<br>
 * advances to PostReportHeaderState<br>
 * before the print, a reportStarted event gets fired.
 */
public class StartState extends ReportState
{
  /**
   * Default constructor and the only constructor to create a state without cloning another.
   *
   * @param report  the report.
   */
  public StartState (JFreeReport report) throws ReportProcessingException
  {
    super (report);
  }

  public StartState (FinishState fstate, int level) throws ReportProcessingException
  {
    super (fstate);
    resetState();
    getFunctions().setLevel(level);
  }

  /**
   * Advances from the 'start' state to the 'pre-report-header' state.
   * <p>
   * Initialises the 'report.date' property, and fires a 'report-started' event.
   *
   * @param rpc  the report processor.
   *
   * @return the next state ('pre-report-header').
   */
  public ReportState advance (ReportProcessor rpc)
  {
    JFreeReport report = this.getReport ();
    this.setCurrentPage (1);

    // A PropertyHandler should set the properties.
    this.setProperty (JFreeReport.REPORT_DATE_PROPERTY, new Date ());

    // Initialize the report before any band (and especially before the pageheader)
    // is printed.
    ReportEvent event = new ReportEvent (this);
    this.fireReportStartedEvent (event);

    return new PreReportHeaderState (this);
  }

  /**
   * Returns <code>true</code> because this *is* the start state.
   *
   * @return true
   */
  public boolean isStart ()
  {
    return true;
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
