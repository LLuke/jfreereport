/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------------
 * PostItemGroupState.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PostItemGroupState.java,v 1.7 2003/05/16 17:26:44 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.states;

import com.jrefinery.report.event.ReportEvent;

/**
 * A report state that is reached after the last row of data for one instance of the inner-most
 * group is processed.
 * <p>
 * The only purpose for this state is to fire the itemsFinished event. After that task is done,
 * a PreGroupFooterState gets active.
 *
 * @author David Gilbert
 */
public final class PostItemGroupState extends ReportState
{
  /**
   * Creates a new '<code>POST-ITEM-GROUP</code>' state.
   *
   * @param previous  the previous state.
   */
  public PostItemGroupState(ReportState previous)
  {
    super(previous);
  }

  /**
   * Advances from this state to the '<code>PRE-GROUP-FOOTER</code>' state.  Before changing
   * state, an 'items-finished' event is fired.
   *
   * @return the next report state ('<code>PRE-GROUP-FOOTER</code>').
   */
  public ReportState advance()
  {
    firePrepareEvent(ReportEvent.ITEMS_FINISHED);

    fireItemsFinishedEvent();
    return new PreGroupFooterState(this);
  }
}
