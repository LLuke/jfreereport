/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: ReportEvent.java,v 1.2 2003/08/24 15:13:22 taqua Exp $
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

package org.jfree.report.event;

import java.util.EventObject;

import org.jfree.report.DataRow;
import org.jfree.report.ReportDefinition;
import org.jfree.report.states.ReportState;

/**
 * Represents a report event.
 * <P>
 * Includes information regarding which {@link ReportState} generated the event.
 *
 * @author Thomas Morgner
 */
public class ReportEvent extends EventObject
{
  /** The event type constant, that the report initialize event is invoked. */
  public static final int REPORT_INITIALIZED = 0x01;
  /** The event type constant, that the page start event is invoked. */
  public static final int PAGE_STARTED = 0x02;
  /** The event type constant, that the report start event is invoked. */
  public static final int REPORT_STARTED = 0x04;
  /** The event type constant, that a group start event is invoked. */
  public static final int GROUP_STARTED = 0x08;
  /** The event type constant, that the items started event is invoked. */
  public static final int ITEMS_STARTED = 0x10;
  /** The event type constant, that the items advanced event is invoked. */
  public static final int ITEMS_ADVANCED = 0x20;
  /** The event type constant, that the items finished event is invoked. */
  public static final int ITEMS_FINISHED = 0x40;
  /** The event type constant, that a group finished event is invoked. */
  public static final int GROUP_FINISHED = 0x80;
  /** The event type constant, that the report finished event is invoked. */
  public static final int REPORT_FINISHED = 0x100;
  /** The event type constant, that the report done event is invoked. */
  public static final int REPORT_DONE = 0x200;
  /** The event type constant, that the page finished event is invoked. */
  public static final int PAGE_FINISHED = 0x400;
  /** The event type constant, that the page finished event is invoked. */
  public static final int PAGE_CANCELED = 0x800;
  /** The event type constant, that the post group footer state will be processed. */
  public static final int POST_GROUP_FOOTER = 0x1000;
  /** The event type constant, that the post group header state will be processed. */
  public static final int POST_GROUP_HEADER = 0x4000;

  /**
   * The event type constant, that this event is a prepare event. Use this constant
   * in combination with one of the other event types to specify which kind of event
   * was prepared.
   */
  public static final int PREPARE_EVENT = 0x2000;

  /** The event type for this event. */
  private int type;

  /**
   * Creates a new <code>ReportEvent</code>.
   *
   * @param state  the current state of the processed report (<code>null</code> not permmitted).
   * @param type the event type for this event object.
   */
  public ReportEvent(final ReportState state, final int type)
  {
    super(state);
    if (state == null)
    {
      throw new NullPointerException("ReportEvent(ReportState) : null not permitted.");
    }
    if (type <= 0)
    {
      throw new IllegalArgumentException("This is not a valid EventType: " + type);
    }
    this.type = type;
  }

  /**
   * Returns the event type. The type is made up of a combination of several flags.
   *
   * @return the event type.
   */
  public int getType()
  {
    return type;
  }

  /**
   * Returns the <code>ReportState</code>, which is the source of the event.
   *
   * @return the state (never <code>null</code>).
   */
  public ReportState getState()
  {
    return (ReportState) getSource();
  }

  /**
   * Returns the report that generated the event.
   * <P>
   * This is a convenience method that extracts the report from the report state.
   *
   * @return the report.
   */
  public ReportDefinition getReport()
  {
    return getState().getReport();
  }

  /**
   * Returns the currently assigned dataRow for this event.
   * <p>
   * The {@link DataRow} is used to access the fields of the
   * {@link org.jfree.report.filter.DataSource} and other functions and expressions within the
   * current row of the report.
   *
   * @return the data row.
   */
  public DataRow getDataRow()
  {
    return getState().getDataRow();
  }

  /**
   * Returns the current function level.
   *
   * @return the function level.
   */
  public int getLevel()
  {
    return getState().getLevel();
  }
}
