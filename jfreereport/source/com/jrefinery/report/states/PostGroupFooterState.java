/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * --------------------
 * ReportGenerator.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 * ----------------------
 * PostGroupFooterState.java
 * ----------------------
 *
 * ChangeLog
 * ---------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.Group;

/**
 * In this state the active group is closed. After that the next state gets activated:
 * <p>
 * If there is no more data and no more open groups, finish the report and activate the
 * PreReportFooterState state. If there is no more data but there are open groups, close them by
 * activating the next PreGroupFooterState state.
 * <p>
 * If there is more data, check whether there are any open groups. If there is no parent group
 * or the parent group is not finished, open the next sub group by activating PreGroupHeaderState.
 * If there is a parent group and this parent is finished, close the parent by activating
 * the PreGroupFooterState state.
 */
public class PostGroupFooterState extends ReportState
{
  /**
   * Creates a new 'post-group-footer' report state.
   *
   * @param previous  the previous report state.
   */
  public PostGroupFooterState (ReportState previous)
  {
    super (previous);
  }

  /**
   * Is there a next row to read?
   *
   * @return true, if there is at least one more row to read.
   */
  private boolean hasMoreData ()
  {
    return (this.getCurrentDataItem () < this.getReport ().getData ().getRowCount () - 1);
  }

  /**
   * Are there more groups active?
   *
   * @return true if this is the last group.
   */
  private boolean isLastGroup ()
  {
    return this.getCurrentGroupIndex () == BEFORE_FIRST_GROUP;
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
    // leave the current group and activate the parent group.
    // if this was the last active group, the group index is now BEFORE_FIRST_GROUP
    this.leaveGroup ();

    if (isLastGroup ())
    {
      // group finished, but there is more data - start a new group...
      if (hasMoreData ())
      {
        return new PreGroupHeaderState (this);
      }
      else
      {
        return new PreReportFooterState (this);
      }
    }
    else
    {
      // There are more groups active
      if (hasMoreData ())
      {
        // we have more data to work on
        // If the group is done, print the GroupFooter of the parent
        Group group = this.getReport ().getGroup (this.getCurrentGroupIndex ());
        if (group.isLastItemInGroup (this.getDataRowBackend (),
                                     this.getDataRowBackend ().previewNextRow ()))
        {
          // Parent is finished, print the footer
          return new PreGroupFooterState (this);
        }
        else
        {
          // more data in parent group, print the next header
          return new PreGroupHeaderState (this);
        }
      }
      else
      {
        // no more data, print the footer of the parent group
        return new PreGroupFooterState (this);
      }
    }
  }
}
