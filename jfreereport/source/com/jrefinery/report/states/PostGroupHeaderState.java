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
 * PostGroupHeaderState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: PostGroupHeaderState.java,v 1.2 2002/11/07 21:45:28 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;

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
   * @return the next state.
   */
  public ReportState advance ()
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
