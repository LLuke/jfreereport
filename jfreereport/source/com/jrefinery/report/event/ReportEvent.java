/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * ------------
 * ReportEvent.java
 * ------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   -;
 *
 * Changes (from 10-May-2002)
 * --------------------------
 *
 * 10-May-2002 : Created the EventInterface for JFreeReport
 * 05-Jun-2002 : cleared the interface,Documentation
 */
package com.jrefinery.report.event;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportState;

import java.util.EventObject;

/**
 * The ReportEvent carries the information which reportstate generated the event.
 */
public class ReportEvent extends EventObject
{
  private ReportState state;

  /**
   * Creates a new ReportEvent. Neither report or state are allowed to be null.
   *
   * @param state the current state of the processed report.
   */
  public ReportEvent (ReportState state)
  {
    super (state);
    if (state == null) throw new NullPointerException ();
  }

  /**
   * Convience method to extract the report which was processed when the ReportEvent
   * was fired. This function will never return null
   *
   * @returns the report being processed.
   */
  public JFreeReport getReport ()
  {
    return getState ().getReport ();
  }

  /**
   * returns the ReportState, which is the source of the event. This function will never
   * return null.
   *
   * @return the current state of the report processing.
   */
  public ReportState getState ()
  {
    return (ReportState) getSource ();
  }
}
