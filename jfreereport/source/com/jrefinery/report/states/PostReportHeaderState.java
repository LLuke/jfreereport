/**
 * ----------------------
 * PostReportHeaderState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;

/**
 * The report header has been printed. Proceed to the first group.
 */
public class PostReportHeaderState extends ReportState
{
  /**
   * Creates a 'post-report-header' state.
   *
   * @param reportstate the previous report state.
   */
  public PostReportHeaderState (ReportState reportstate)
  {
    super (reportstate);
  }

  /**
   * This state does nothing and advances directly to the first PreGroupHeaderState.
   *
   * @param rpc  the report processor.
   *
   * @return the next state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    return new PreGroupHeaderState (this);
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
