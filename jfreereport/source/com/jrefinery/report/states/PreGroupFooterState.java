/**
 * ----------------------
 * PreGroupFooterState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.Group;
import com.jrefinery.report.function.FunctionCollection;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.event.ReportEvent;

/**
 * If there is not enough space to print the footer, the footer returns itself to
 * wait for the pageBreak. After the groupFinished Event has been fired and the footer
 * is printed, the PostGroupFooterState gets active.
 */
public class PreGroupFooterState extends ReportState
{
  /**
   * Creates a new 'pre-group-footer' report state.
   *
   * @param previous  the previous report state.
   */
  public PreGroupFooterState (ReportState previous)
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
    Group group = (Group) this.getReport ().getGroup (this.getCurrentGroupIndex ());

    GroupFooter footer = group.getFooter ();
    if (rpc.isSpaceFor (footer))
    {
      // There is a header and enough space to print it. The finishGroup event is
      // fired and PostGroupFooterState activated after all work is done.
      ReportEvent event = new ReportEvent (this);
      this.fireGroupFinishedEvent (event);
      this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
      rpc.printGroupFooter (footer);
      return new PostGroupFooterState (this);
    }
    else
    {
      // There is not enough space to print the footer. Wait for the pageBreak and
      // return later.
      return this;
    }
  }
}
