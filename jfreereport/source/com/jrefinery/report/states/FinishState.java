/**
 * ----------------------
 * FinishState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;

/**
 * The report is done. No advance will be done, every call to advance will return this
 * FinishState-State.
 */
public class FinishState extends ReportState
{
  /**
   * Creates a new 'finish' report state.
   *
   * @param previous  the previous state.
   */
  public FinishState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances from this state to the next.  Since this is the 'finish' state, this method just
   * returns itself.
   *
   * @param rpc  the report processor.
   *
   * @return this state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    rpc.setPageDone ();
    return this;
  }

  /**
   * Returns true, to indicate that this is the 'finish' state.
   *
   * @return true, as this report is done and will no longer advance.
   */
  public boolean isFinish ()
  {
    return true;
  }

}
