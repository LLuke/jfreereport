/**
 * ----------------------
 * PostItemGroupState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.event.ReportEvent;

/**
 * The only purpose for this state is to fire the itemsFinished event. After that task is done,
 * a PreGroupFooterState-State gets active.
 */
public class PostItemGroupState extends ReportState
{
  /**
   * Creates a new 'post-item-group' state.
   *
   * @param previous  the previous state.
   */
  public PostItemGroupState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances from this state to the next.
   * <p>
   * Just inform everybody that the itemband is no longer printed. Next state will be
   * PreGroupFooterState.
   *
   * @param rpc  the report processor.
   *
   * @return the next report state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    ReportEvent event = new ReportEvent (this);
    this.fireItemsFinishedEvent (event);
    return new PreGroupFooterState (this);
  }
}
