/**
 * Date: Dec 1, 2002
 * Time: 5:56:19 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.pagelayout;

import com.jrefinery.report.Band;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.targets.pageable.Spool;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

/**
 *
 */
public class LayoutTask
{
  public static class LayoutFlag implements Cloneable
  {
    private String flagName;
    private Object flagObject;

    public LayoutFlag (String name)
    {
      if (name == null) throw new NullPointerException();
      this.flagName = name;
    }

    public String getName ()
    {
      return flagName;
    }

    public Object getFlagObject()
    {
      return flagObject;
    }

    public void setFlagObject(Object flagObject)
    {
      this.flagObject = flagObject;
    }
  }

  private EventType eventType;
  private boolean done;
  private ReportEvent event;
  private Hashtable progressFlags;

  public LayoutTask()
  {
    progressFlags = new Hashtable();
  }

  public ReportEvent getEvent()
  {
    return event;
  }

  public void setEvent(ReportEvent event, EventType type)
  {
    this.event = event;
    this.eventType = type;
  }

  public EventType getEventType()
  {
    return eventType;
  }

  public boolean isDone ()
  {
    return done;
  }

  public void setDone(boolean done)
  {
    this.done = done;
  }

  public void setFlag (LayoutFlag flag)
  {
    this.progressFlags.put (flag.getName(), flag);
  }

  public LayoutFlag getFlag (String name)
  {
    return (LayoutFlag) progressFlags.get (name);
  }

  /*
  public boolean pageBreakBefore;
  public boolean pageBreakAfter;
  public Rectangle2D bounds;
  public Band layoutComponent;
  public Spool layoutContent;
  */
}
