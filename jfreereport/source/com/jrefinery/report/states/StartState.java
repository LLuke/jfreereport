/**
 * ----------------------
 * StartState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
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
