/**
 * ----------------------
 * PreGroupHeaderState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.Group;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.event.ReportEvent;

/**
 * Processes an groupheader. If there is not enough space to print the header,
 * no transition is done, else a PostGroupHeaderState-State gets activated.
 * Before the print, a groupStartEvent gets fired.
 * <p>
 * Before the group is printed, the group is activated and the currentGroup state
 * is adjusted using the enterGroup() function.
 */
public class PreGroupHeaderState extends ReportState
{
  /** Flag indicating whether or not the page break has been done. */
  private boolean handledPagebreak;

  /**
   * Creates a new 'pre-group-header' state.
   *
   * @param previous  the previous state.
   */
  public PreGroupHeaderState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Advances from this state to the next.
   *
   * @param rpc  the report processor.
   *
   * @return  the next state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    this.enterGroup ();

    Group group = (Group) this.getReport ().getGroup (this.getCurrentGroupIndex ());

    // if there is no header, fire the event and proceed to PostGroupHeaderState

    GroupHeader header = group.getHeader ();

    if (handledPagebreak == false && header.hasPageBreakBeforePrint ()
                                  && ((this.getCurrentGroupIndex() == 1
                                  && this.getCurrentPage() == 1) == false))
    {
      handledPagebreak = true;
      rpc.setPageDone ();
    }
    else
    {
    // there is a header, test if there is enough space to print it
      if (rpc.isSpaceFor (header))
      {
        // enough space, fire the events and proceed to PostGroupHeaderState
        ReportEvent event = new ReportEvent (this);
        this.fireGroupStartedEvent (event);
        this.getDataRowConnector ().setDataRowBackend (this.getDataRowBackend ());
        rpc.printGroupHeader (header);
        return new PostGroupHeaderState (this);
      }
      else
      {
        handledPagebreak = true;
      }
    }

    // Not enough space to print the header. Undo the GroupChange and wait until the
    // pagebreak has been done. After this the engine may return here to attemp another
    // print
    this.leaveGroup ();
    return this;
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
