/**
 * Date: Dec 1, 2002
 * Time: 7:16:30 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.pagelayout;

public class EventType
{
  public static final EventType PAGE_START   = new EventType("PAGE_START");
  public static final EventType PAGE_END     = new EventType("PAGE_END");
  public static final EventType GROUP_START  = new EventType("GROUP_START");
  public static final EventType GROUP_END    = new EventType("GROUP_END");
  public static final EventType REPORT_START = new EventType("REPORT_START");
  public static final EventType REPORT_END   = new EventType("REPORT_END");
  public static final EventType ITEM_START   = new EventType("ITEM_START");
  public static final EventType ITEM_END     = new EventType("ITEM_END");
  public static final EventType ITEM_ADVANCE = new EventType("ITEM_END");

  private final String myName; // for debug only

  private EventType(String name)
  {
    myName = name;
  }

  public String toString()
  {
    return myName;
  }
}
