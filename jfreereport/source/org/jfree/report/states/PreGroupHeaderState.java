/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * ------------------------
 * PreGroupHeaderState.java
 * ------------------------
 * (C)opyright 2000-2002, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PreGroupHeaderState.java,v 1.4 2004/05/07 08:14:22 mungady Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadoc comments (DG);
 */
package org.jfree.report.states;

import org.jfree.report.event.ReportEvent;

/**
 * Processes an groupheader. Activates the next group and fires the GroupStartEvent
 * for that group. then the PostGroupHeaderState-State gets activated.
 *
 * @author David Gilbert
 */
public final class PreGroupHeaderState extends ReportState
{
  /**
   * Creates a new '<code>PRE-GROUP-HEADER</code>' state.
   *
   * @param previous  the previous state.
   */
  public PreGroupHeaderState(final ReportState previous)
  {
    super(previous);
  }

  public int getEventCode ()
  {
    return ReportEvent.GROUP_STARTED;
  }

  /**
   * Advances from this state to the '<code>POST-GROUP-HEADER</code>' state after
   * fireing the GroupStartedEvent.
   *
   * @return  the next state ('<code>POST-GROUP-HEADER</code>').
   */
  public ReportState advance()
  {
    firePrepareEvent();

    enterGroup();
    // enough space, fire the events and proceed to PostGroupHeaderState
    fireGroupStartedEvent();
    return new PostGroupHeaderState(this);
  }

  /**
   * Activates the next group by incrementing the current group index.  The outer-most group is
   * given an index of zero, and this increases for each subgroup that is defined.
   */
  private void enterGroup()
  {
    setCurrentGroupIndex(getCurrentGroupIndex() + 1);
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet advanced
   * we perform a readAHead lookup when populating elements.
   *
   * @return true; Header related states preview the next itemband DataRow.
   */
  public boolean isPrefetchState()
  {
    return true;
  }
}
