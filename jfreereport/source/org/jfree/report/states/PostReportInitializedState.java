/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------------------------
 * PostReportInitializedState.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PostReportInitializedState.java,v 1.5 2005/02/23 21:06:04 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.06.2003 : Initial version
 *
 */

package org.jfree.report.states;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;

/**
 * Prints the report header. This state is the second state in the report processing and
 * was created after the report was initialized.
 *
 * @author Thomas Morgner
 */
public class PostReportInitializedState extends ReportState
{
  /**
   * Constructs a ReportState from an existing ReportState.
   *
   * @param clone the existing state.
   */
  public PostReportInitializedState (final ReportState clone)
  {
    super(clone);
  }

  public int getEventCode ()
  {
    return ReportEvent.REPORT_STARTED;
  }

  /**
   * The advance method performs a transition from the current report state to the next
   * report state.  Each transition will usually involve some processing of the report.
   *
   * @return the next report state.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public ReportState advance ()
          throws ReportProcessingException
  {
    firePrepareEvent();

    // initialise the report before any band (and especially before the pageheader) is printed.
    fireReportStartedEvent();
    return new PreGroupHeaderState(this);
  }

  /**
   * Returns the corrected display item for this state. As the currentItem has not yet
   * advanced we perform a readAHead lookup when populating elements.
   *
   * @return true; Header related states preview the next itemband DataRow.
   */
  public boolean isPrefetchState ()
  {
    return true;
  }

}
