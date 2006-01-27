/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * PostReportHeader.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.01.2006 : Initial version
 */
package org.jfree.report.states;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;

/**
 * This is a helper state to allow the group header to be skipped when
 * creating report save states.
 *
 * @author Thomas Morgner
 */
public class PostReportHeaderState extends ReportState
{
  /**
   * Constructs a ReportState from an existing ReportState.
   *
   * @param clone the existing state.
   */
  public PostReportHeaderState(final ReportState clone)
  {
    super(clone);
  }

  /**
   * The advance method performs a transition from the current report state to
   * the next report state.  Each transition will usually involve some
   * processing of the report.
   *
   * @return the next report state.
   * @throws ReportProcessingException if there is a problem processing the
   *                                   report.
   */
  public ReportState advance() throws ReportProcessingException
  {
    return new PreGroupHeaderState(this);
  }

  /**
   * Returns the unique event code for this report state type.
   *
   * @return the event code for this state type.
   */
  public int getEventCode()
  {
    return ReportEvent.REPORT_STARTED;
  }
}
