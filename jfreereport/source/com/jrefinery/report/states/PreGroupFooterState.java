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
 * ------------------------
 * PreGroupFooterState.java
 * ------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: PreGroupFooterState.java,v 1.9 2003/05/16 17:26:45 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.states;

import com.jrefinery.report.event.ReportEvent;

/**
 * Fires the groupFinished Event and advances to PostGroupFooter.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public final class PreGroupFooterState extends ReportState
{
  /**
   * Creates a new '<code>PRE-GROUP-FOOTER</code>' report state.
   *
   * @param previous  the previous report state.
   */
  public PreGroupFooterState(ReportState previous)
  {
    super(previous);
  }

  /**
   * Advances from this state to the '<code>POST-GROUP-FOOTER</code>' state
   * after firing the GroupFinished Event.
   *
   * @return the next report state ('<code>POST-GROUP-FOOTER</code>').
   */
  public ReportState advance()
  {
    // There is a header and enough space to print it. The finishGroup event is
    // fired and PostGroupFooterState activated after all work is done.
    firePrepareEvent(ReportEvent.GROUP_FINISHED);

    fireGroupFinishedEvent();
    return new PostGroupFooterState(this);
  }
}
