/**
 * ----------------------
 * PreItemGroupState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.event.ReportEvent;

/**
 * Prepare to print the items. This state fires the itemStarted-Event and advances to
 * the InItemGroupState state.
 */
public class PreItemGroupState extends ReportState
{

  /**
   * Creates a new 'pre-item-group' state.
   *
   * @param previous  the previous state.
   */
  public PreItemGroupState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances to the next state.
   *
   * @param rpc  the report processor.
   *
   * @return the next state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    // Inform everybody, that now items will be processed.

    ReportEvent event = new ReportEvent (this);
    this.fireItemsStartedEvent (event);
    if (this.getReport().getData().getRowCount() == 0)
    {
      return new PostItemGroupState(this);
    }
    return new InItemGroupState (this);
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet advanced
   * we perform a readAHead lookup when populating elements.
   *
   * @return true.
   */
  public boolean isPrefetchState ()
  {
    return true;
  }
}
