/**
 * ----------------------
 * PreReportHeaderState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
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
