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
 * -------------------------
 * EventMonitorFunction.java
 * -------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.function;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.util.Log;

/**
 * A function that logs each event that it receives.  This function can be used for debugging
 * purposes.
 *
 * @author Thomas Morgner
 */
public class EventMonitorFunction extends AbstractFunction
{
  /** Counts the number of times the reportStarted(...) method is called. */
  private int reportStartCount = 0;

  /**
   * Creates a new function.
   */
  public EventMonitorFunction()
  {
  }
  
  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    Log.info ("Report Started: Level = " + event.getState().getLevel() 
                      + " Prepare Run: " + event.getState().isPrepareRun());
    reportStartCount++;
    Log.info ("Report Started Count: " + reportStartCount);
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event  the event.
   */
  public void reportFinished(ReportEvent event)
  {
    Log.info ("Report Finished: Level = " + event.getState().getLevel() 
                       + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
    Log.info ("Page Started: Level = " + event.getState().getLevel() 
                    + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Page Started: " + event.getState().getCurrentPage());
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event  the event.
   */
  public void pageFinished(ReportEvent event)
  {
    Log.info ("Page Finished: Level = " + event.getState().getLevel() 
                     + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Page Finished: " + event.getState().getCurrentPage());
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    Log.info ("Group Started: Level = " + event.getState().getLevel() 
                     + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Group Started: " + event.getState().getCurrentGroupIndex());
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event  the event.
   */
  public void groupFinished(ReportEvent event)
  {
    Log.info ("Group Finished: Level = " + event.getState().getLevel() 
                      + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Group Finished: " + event.getState().getCurrentGroupIndex());
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    //Log.info ("Items Advanced: Level = " + event.getState().getLevel() 
    //                  + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   *
   * @param event  the event.
   */
  public void itemsStarted(ReportEvent event)
  {
    //Log.info ("Items Started: Level = " + event.getState().getLevel() 
    //                 + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Receives notification that a group of item bands has been completed.
   *
   * @param event  the event.
   */
  public void itemsFinished(ReportEvent event)
  {
    //Log.info ("Items Finished: Level = " + event.getState().getLevel() 
    //                  + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Returns <code>null</code> since this function is for generating log messages only.
   *
   * @return the value of the function (<code>null</code>).
   */
  public Object getValue()
  {
    return null;
  }
}
