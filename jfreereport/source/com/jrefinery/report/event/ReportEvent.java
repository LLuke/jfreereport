/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, 2003 by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportEvent.java,v 1.15 2003/04/05 18:57:10 taqua Exp $
 *
 * Changes (from 10-May-2002)
 * --------------------------
 *
 * 10-May-2002 : Created the EventInterface for JFreeReport
 * 05-Jun-2002 : cleared the interface,Documentation
 * 17-Jul-2002 : Updated header and Javadocs (DG);
 * 28-Jul-2002 : Added DataRow support
 * 28-Aug-2002 : Documentation update
 * 03-Jan-2003 : Javadoc update (DG);
 * 
 */

package com.jrefinery.report.event;

import java.util.EventObject;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.ReportDefinition;
import com.jrefinery.report.states.ReportState;

/**
 * Represents a report event.
 * <P>
 * Includes information regarding which {@link ReportState} generated the event.
 *
 * @author Thomas Morgner
 */
public class ReportEvent extends EventObject
{
  public static final int REPORT_INITIALIZED = 0x01;
  public static final int PAGE_STARTED = 0x02;
  public static final int REPORT_STARTED = 0x04;
  public static final int GROUP_STARTED = 0x08;
  public static final int ITEMS_STARTED = 0x10;
  public static final int ITEMS_ADVANCED = 0x20;
  public static final int ITEMS_FINISHED = 0x40;
  public static final int GROUP_FINISHED = 0x80;
  public static final int REPORT_FINISHED = 0x100;
  public static final int REPORT_DONE = 0x200;
  public static final int PAGE_FINISHED = 0x400;
  public static final int PREPARE_EVENT = 0x2000;

  private int type;

  /**
   * Creates a new <code>ReportEvent</code>.
   *
   * @param state  the current state of the processed report (<code>null</code> not permmitted).
   */
  public ReportEvent (ReportState state, int type)
  {
    super (state);
    if (state == null)
    {
     throw new NullPointerException ("ReportEvent(ReportState) : null not permitted.");
    }
    this.type = type;
  }

  public int getType()
  {
    return type;
  }

  /**
   * Returns the <code>ReportState</code>, which is the source of the event.
   *
   * @return the state (never <code>null</code>).
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
   * @return the report.
   */
  public ReportDefinition getReport ()
  {
    return getState ().getReport ();
  }

  /**
   * Returns the currently assigned dataRow for this event. 
   * <p>
   * The {@link DataRow} is used to access the fields of the 
   * {@link com.jrefinery.report.filter.DataSource} and other functions and expressions within the 
   * current row of the report.
   *
   * @return the data row.
   */
  public DataRow getDataRow ()
  {
    return getState ().getDataRow ();
  }
}
