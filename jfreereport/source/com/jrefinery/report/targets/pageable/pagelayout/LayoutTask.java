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
 * ---------------
 * LayoutTask.java
 * ---------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LayoutTask.java,v 1.3 2002/12/07 14:58:33 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.pagelayout;

import com.jrefinery.report.event.ReportEvent;

import java.util.Hashtable;

/**
 * A layout task. No real implementation ... next release ..
 *
 * @author Thomas Morgner
 */
public class LayoutTask
{
  /**
   * A layout flag.
   */
  public static class LayoutFlag implements Cloneable
  {
    /** The flag name. */
    private String flagName;
    
    /** The flag object. */
    private Object flagObject;

    /**
     * Creates a new layout flag. 
     *
     * @param name  the name (null not permitted).
     */
    public LayoutFlag (String name)
    {
      if (name == null)
      {
        throw new NullPointerException();
      }
      this.flagName = name;
    }

    /**
     * Returns the flag name.
     *
     * @return the flag name.
     */
    public String getName ()
    {
      return flagName;
    }

    /**
     * Returns the flag object.
     *
     * @return the flag object.
     */
    public Object getFlagObject()
    {
      return flagObject;
    }

    /**
     * Sets the flag object.
     *
     * @param flagObject  the flag object.
     */
    public void setFlagObject(Object flagObject)
    {
      this.flagObject = flagObject;
    }
  }

  /** The event type. */
  private EventType eventType;
  
  /** A 'done' flag. */
  private boolean done;
  
  /** The report event. */
  private ReportEvent event;
  
  /** Storage for progress flags. */
  private Hashtable progressFlags;

  /** 
   * Creates a new layout task.
   */
  public LayoutTask()
  {
    progressFlags = new Hashtable();
  }

  /**
   * Returns the event for this task.
   *
   * @return the event.
   */
  public ReportEvent getEvent()
  {
    return event;
  }

  /**
   * Sets the event for the layout task.
   *
   * @param event  the event.
   * @param type  the event type.
   */
  public void setEvent(ReportEvent event, EventType type)
  {
    this.event = event;
    this.eventType = type;
  }

  /**
   * Returns the event type.
   *
   * @return the event type.
   */
  public EventType getEventType()
  {
    return eventType;
  }

  /**
   * Returns the 'done' flag.
   *
   * @return true or false.
   */
  public boolean isDone ()
  {
    return done;
  }

  /**
   * Sets the 'done' flag.
   *
   * @param done  the new value of the flag.
   */
  public void setDone(boolean done)
  {
    this.done = done;
  }

  /**
   * Adds a progress flag.
   *
   * @param flag  the flag.
   */
  public void setFlag (LayoutFlag flag)
  {
    this.progressFlags.put (flag.getName(), flag);
  }

  /**
   * Gets a progress flag.
   *
   * @param name  the flag name.
   *
   * @return the layout flag.
   */
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
