/**
 * ----------------------
 * PreReportFooterState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.function.FunctionCollection;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.event.ReportEvent;

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
   * @param rpc  the report processor.
   *
   * @return the next report state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    JFreeReport report = this.getReport ();
    ReportFooter reportFooter = report.getReportFooter ();

    if (rpc.isSpaceFor (reportFooter))
    {
      ReportEvent event = new ReportEvent (this);
      this.fireReportFinishedEvent (event);
      this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
      rpc.printReportFooter (reportFooter);
      return new FinishState (this);
    }
    else
    {
      return this;
    }
  }
}
