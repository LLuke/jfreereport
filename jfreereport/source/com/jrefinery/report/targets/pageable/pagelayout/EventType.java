/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * --------------
 * EventType.java
 * --------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EventType.java,v 1.2 2002/12/04 16:20:57 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.pagelayout;

/**
 * A class that represents an event type. No real implementation ... next release ..
 *
 * @author Thomas Morgner
 */
public class EventType
{
  /** A 'page start' event. */
  public static final EventType PAGE_START   = new EventType("PAGE_START");
  
  /** A 'page end' event. */
  public static final EventType PAGE_END     = new EventType("PAGE_END");
  
  /** A 'group start' event. */
  public static final EventType GROUP_START  = new EventType("GROUP_START");
  
  /** A 'group end' event. */
  public static final EventType GROUP_END    = new EventType("GROUP_END");
  
  /** A 'report start' event. */
  public static final EventType REPORT_START = new EventType("REPORT_START");
  
  /** A 'report end' event. */
  public static final EventType REPORT_END   = new EventType("REPORT_END");
  
  /** An 'item start' event. */
  public static final EventType ITEM_START   = new EventType("ITEM_START");
  
  /** An 'item end' event. */
  public static final EventType ITEM_END     = new EventType("ITEM_END");
  
  /** An 'item advance' event. */
  public static final EventType ITEM_ADVANCE = new EventType("ITEM_ADVANCE");

  /** The name of the event type. */
  private final String myName; // for debug only

  /**
   * Creates a new event.  Since this constructor is private, you cannot create new
   * event types - only the event types defined by this class are available for use.
   *
   * @param name  the event name.
   */
  private EventType(String name)
  {
    myName = name;
  }

  /**
   * Returns the name of the event type.  Used for debug purposes.
   *
   * @return a string representation of the event type.
   */
  public String toString()
  {
    return myName;
  }
  
}
