/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ------------------------------
 * EventOrderFunction.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EventOrderFunction.java,v 1.5 2005/01/31 17:16:35 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 12.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.functionality;

import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.util.Log;

public class EventOrderFunction extends AbstractFunction implements PageEventListener
{
  private int lastEventType;
  private boolean pageOpen;

  public EventOrderFunction()
  {
  }

  public EventOrderFunction(final String name)
  {
    setName(name);
  }


  /**
   * Receives notification that report generation initializes the current run.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.REPORT_INITIALIZED) != ReportEvent.REPORT_INITIALIZED)
    {
      throw new IllegalArgumentException
        ("ReportEvent was expected to be of type REPORT_INITIALIZED");
    }

    if (lastEventType != ReportEvent.REPORT_DONE && lastEventType != 0)
    {
      throw new IllegalStateException("Unexpected Event: ReportInitialized: " + lastEventType);
    }

    lastEventType = ReportEvent.REPORT_INITIALIZED;
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.REPORT_STARTED) != ReportEvent.REPORT_STARTED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type REPORT_STARTED");
    }

    if (lastEventType != ReportEvent.REPORT_INITIALIZED)
    {
      throw new IllegalStateException("Unexpected Event: ReportStarted: " + lastEventType);
    }

    lastEventType = ReportEvent.REPORT_STARTED;
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.REPORT_FINISHED) != ReportEvent.REPORT_FINISHED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type REPORT_FINISHED");
    }

    if (lastEventType != ReportEvent.GROUP_FINISHED)
    {
      throw new IllegalStateException("Unexpected Event: ReportFinished: " + lastEventType);
    }

    lastEventType = ReportEvent.REPORT_FINISHED;
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.PAGE_STARTED) != ReportEvent.PAGE_STARTED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type PAGE_STARTED");
    }

    if (pageOpen)
    {
      throw new IllegalStateException("Unexpected Event: PageStarted: " + lastEventType);
    }
    pageOpen = true;
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor.
   * This method is called, when a page was removed from the report after
   * it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled(ReportEvent event)
  {
    if ((event.getType() & ReportEvent.PAGE_CANCELED) != ReportEvent.PAGE_CANCELED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type PAGE_CANCELED");
    }

    if (pageOpen)
    {
      throw new IllegalStateException("Unexpected Event: PageCanceled: " + lastEventType);
    }
  }

  /**
   * This event is fired, whenever an automatic pagebreak has been detected and the report
   * state had been reverted to the previous state.
   *
   * @param event
   */
  public void pageRolledBack (ReportEvent event)
  {
    if ((event.getType() & ReportEvent.PAGE_ROLLEDBACK) != ReportEvent.PAGE_ROLLEDBACK)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type PAGE_CANCELED");
    }

    if (pageOpen == false)
    {
      throw new IllegalStateException("Unexpected Event: PageRolledBack: " + lastEventType);
    }
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event  the event.
   */
  public void pageFinished(final ReportEvent event)
  {
    Log.error ("! EventOrderFunction: Page Finished called !");
    if ((event.getType() & ReportEvent.PAGE_FINISHED) != ReportEvent.PAGE_FINISHED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type PAGE_FINISHED: " +
          event.getType());
    }

    if (pageOpen == false)
    {
      throw new IllegalStateException("Unexpected Event: PageFinished: " + lastEventType);
    }
    pageOpen = false;
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    Log.error ("! EventOrderFunction: Group Started called !");
    if ((event.getType() & ReportEvent.GROUP_STARTED) != ReportEvent.GROUP_STARTED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type GROUP_STARTED");
    }
    Log.error ("! EventOrderFunction: Group Started called !");

    if (lastEventType != ReportEvent.GROUP_STARTED && lastEventType != ReportEvent.REPORT_STARTED)
    {
      Log.error (" ++! EventOrderFunction: Group Started called !", new Exception());
      throw new IllegalStateException("Unexpected Event: GroupStarted: " + lastEventType);
    }
    Log.error ("! EventOrderFunction: Group Started called !");

    lastEventType = ReportEvent.GROUP_STARTED;
    Log.error ("! EventOrderFunction: Group Started called !");
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.GROUP_FINISHED) != ReportEvent.GROUP_FINISHED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type GROUP_FINISHED");
    }

    if (lastEventType != ReportEvent.GROUP_FINISHED && lastEventType != ReportEvent.ITEMS_FINISHED)
    {
      throw new IllegalStateException("Unexpected Event: GroupFinished: " + lastEventType);
    }

    lastEventType = ReportEvent.GROUP_FINISHED;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.ITEMS_ADVANCED) != ReportEvent.ITEMS_ADVANCED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type ITEMS_ADVANCED");
    }

    if (lastEventType != ReportEvent.ITEMS_STARTED && lastEventType != ReportEvent.ITEMS_ADVANCED)
    {
      throw new IllegalStateException("Unexpected Event: ReportDone: " + lastEventType);
    }

    lastEventType = ReportEvent.ITEMS_ADVANCED;

  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.ITEMS_STARTED) != ReportEvent.ITEMS_STARTED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type ITEMS_STARTED");
    }

    if (lastEventType != ReportEvent.GROUP_STARTED)
    {
      throw new IllegalStateException("Unexpected Event: ItemsStarted: " + lastEventType);
    }

    lastEventType = ReportEvent.ITEMS_STARTED;

  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.ITEMS_FINISHED) != ReportEvent.ITEMS_FINISHED)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type ITEMS_FINISHED");
    }

    if (lastEventType != ReportEvent.ITEMS_ADVANCED)
    {
      throw new IllegalStateException("Unexpected Event: ItemsFinished: " + lastEventType);
    }

    lastEventType = ReportEvent.ITEMS_FINISHED;
  }

  /**
   * Receives notification that report generation has completed, the report footer was printed,
   * no more output is done. This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(final ReportEvent event)
  {
    if ((event.getType() & ReportEvent.REPORT_DONE) != ReportEvent.REPORT_DONE)
    {
      throw new IllegalArgumentException("ReportEvent was expected to be of type REPORT_DONE");
    }

    if (lastEventType != ReportEvent.REPORT_FINISHED)
    {
      throw new IllegalStateException("Unexpected Event: ReportDone: " + lastEventType);
    }

    lastEventType = ReportEvent.REPORT_DONE;
  }

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return new Integer(lastEventType);
  }
}
