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
 * PreGroupHeaderState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: PreGroupHeaderState.java,v 1.6 2002/11/25 23:20:43 taqua Exp $
 *
 * Changes
 * -------
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

    Group group = this.getReport ().getGroup (this.getCurrentGroupIndex ());
    GroupHeader header = group.getHeader ();

    // if the header has a pagebreak on the first row and the first group, then
    // ignore that pagebreak, it would create an empty page.
    if (handledPagebreak == false
        && header.hasPageBreakBeforePrint ()
        && ((this.getCurrentGroupIndex() != 1 || this.getCurrentDataItem() != -1)))
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
