/**
 * ----------------------
 * PostGroupHeaderState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;

/**
 * The groupHeader has been printed. If there are more groups defined, activate them
 * and print their header. If no more groups can be activated, start printing the items.
 * Transition: PreGroupHeaderState or PreItemHeader
 * (this thing changes the currentGroup, but the other behaviour is
 * like StartGroup)
 */
public class PostGroupHeaderState extends ReportState
{

  /**
   * Creates a new 'post-group-header' state.
   *
   * @param previous  the previous state.
   */
  public PostGroupHeaderState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Checks whether there are more groups the work on.
   *
   * @return true, if the currentGroupIndex is smaller than the defined groups - 1
   */
  protected boolean hasMoreGroups ()
  {
    return this.getCurrentGroupIndex () < (this.getReport ().getGroupCount () - 1);
  }

  /**
   * Advances from this state to the next.
   * <p>
   * If there are more groups, activate the next PreGroupHeaderState state, else activate
   * the PreItemGroupState state.
   *
   * @param rpc  the report processor.
   *
   * @return the next state.
   */
  public ReportState advance (ReportProcessor rpc)
  {
    if (hasMoreGroups ())
    {
      // There are more groups defined.
      // Activate the next group and proceed to print it's header.
      return new PreGroupHeaderState (this);
    }
    else
    {
      // Prepare to print Items.
      return new PreItemGroupState (this);
    }
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
