/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * ----------------
 * ReportEvent.java
 * ----------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportEvent.java,v 1.10 2002/11/07 21:45:27 taqua Exp $
 *
 * Changes (from 10-May-2002)
 * --------------------------
 *
 * 10-May-2002 : Created the EventInterface for JFreeReport
 * 05-Jun-2002 : cleared the interface,Documentation
 * 17-Jul-2002 : Updated header and Javadocs (DG);
 * 28-Jul-2002 : Added DataRow support
 * 28-Aug-2002 : Documentation update
 */
package com.jrefinery.report.event;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.states.ReportState;

import java.util.EventObject;

/**
 * Represents a report event.
 * <P>
 * Includes information regarding which ReportState generated the event.
 *
 * @author Thomas Morgner
 */
public class ReportEvent extends EventObject
{
  /**
   * Creates a new ReportEvent.
   *
   * @param state The current state of the processed report (null not allowed).
   */
  public ReportEvent (ReportState state)
  {
    super (state);
    if (state == null)
    {
     throw new NullPointerException ();
    }
  }

  /**
   * Returns the ReportState, which is the source of the event.
   * <P>
   * This function will never return null.
   *
   * @return The state.
   */
  public ReportState getState ()
  {
    return (ReportState) getSource ();
  }

  /**
   * Returns the report that generated the event.
   * <P>
   * This is a convenience method that extracts the report from the report state.
   *
   * @return The report.
   */
  public JFreeReport getReport ()
  {
    return getState ().getReport ();
  }

  /**
   * Returns the currently assigned dataRow for this event. The DataRow is used to access the
   * fields of the DataSource and other Functions and Expressions within the current row of the
   * report.
   *
   * @return the DataRow of the report
   */
  public DataRow getDataRow ()
  {
    return getState ().getDataRow ();
  }
}
