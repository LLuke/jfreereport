/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * -------------------------
 * PostGroupHeaderState.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PostGroupHeaderState.java,v 1.2 2003/08/24 15:13:23 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.states;

import org.jfree.report.event.ReportEvent;

/**
 * The groupHeader has been printed. If there are more groups defined, activate them
 * by creating a new PreGroupHeader state for the next group. If no more groups can
 * be activated, start printing the items by creating a PreItemGroupState.
 * <p>
 * Transition: PreGroupHeaderState or PreItemHeader
 *
 * @author David Gilbert
 */
public final class PostGroupHeaderState extends ReportState
{

  /**
   * Creates a new 'post-group-header' state.
   *
   * @param previous  the previous state.
   */
  public PostGroupHeaderState(final ReportState previous)
  {
    super(previous);
  }

  /**
   * Checks whether there are more groups the work on.
   *
   * @return true, if the currentGroupIndex is smaller than the defined groups - 1
   */
  protected boolean hasMoreGroups()
  {
    return this.getCurrentGroupIndex() < (this.getReport().getGroupCount() - 1);
  }

  /**
   * Advances from this state to the next.  If the reporting engine hasn't reached the inner-most
   * group yet, move to the '<code>PRE-GROUP-HEADER</code>' state again, otherwise move to the
   * '<code>PRE-ITEM-GROUP</code>' state.
   *
   * @return the next state.
   */
  public ReportState advance()
  {
    firePrepareEvent(ReportEvent.POST_GROUP_HEADER);
    if (hasMoreGroups())
    {
      // there are more groups defined, activate the next group and proceed to print it's header
      return new PreGroupHeaderState(this);
    }
    else
    {
      // we have reached the inner-most group, so prepare to print some data items
      return new PreItemGroupState(this);
    }
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet advanced
   * we perform a readAHead lookup when populating elements.
   *
   * @return true; the post group header previews the next data row.
   */
  public boolean isPrefetchState()
  {
    return true;
  }
}
