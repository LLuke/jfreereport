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
 * PreGroupFooterState.java
 * --------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id$
 *
 * Changes
 * -------
 */
package com.jrefinery.report.states;

import com.jrefinery.report.ReportProcessor;
import com.jrefinery.report.Group;
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
