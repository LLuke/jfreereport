/**
 * Date: Nov 29, 2002
 * Time: 12:41:31 PM
 *
 * $Id$
 */
package com.jrefinery.report.function;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.util.Log;

public class EventMonitorFunction extends AbstractFunction
{
  public EventMonitorFunction()
  {
  }

  int rStartC = 0;

  /**
   * Receives notification that the report has started.
   * <P>
   * Maps the reportStarted-method to the legacy function startReport ().
   *
   * @param event Information about the event.
   */
  public void reportStarted(ReportEvent event)
  {
    Log.info ("Report Started: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
    rStartC ++;
    Log.info ("Report Started Count: " + rStartC);
  }

  /**
   * Receives notification that the report has finished.
   * <P>
   * Maps the reportFinished-method to the legacy function endReport ().
   *
   * @param event Information about the event.
   */
  public void reportFinished(ReportEvent event)
  {
    Log.info ("Report Finished: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Receives notification that a page has started.
   * <P>
   * Maps the pageStarted-method to the legacy function startPage (int).
   *
   * @param event Information about the event.
   */
  public void pageStarted(ReportEvent event)
  {
    Log.info ("Page Started: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Page Started: " + event.getState().getCurrentPage());
  }

  /**
   * Receives notification that a page has ended.
   * <P>
   * Maps the pageFinished-method to the legacy function endPage (int).
   *
   * @param event Information about the event.
   */
  public void pageFinished(ReportEvent event)
  {
    Log.info ("Page Finished: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Page Finished: " + event.getState().getCurrentPage());
  }

  /**
   * Receives notification that a group has started.
   * <P>
   * Maps the groupStarted-method to the legacy function startGroup (int).
   *
   * @param event Information about the event.
   */
  public void groupStarted(ReportEvent event)
  {
    Log.info ("Group Started: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Group Started: " + event.getState().getCurrentGroupIndex());
  }

  /**
   * Receives notification that a group has finished.
   * <P>
   * Maps the groupFinished-method to the legacy function endGroup (int).
   *
   * @param event Information about the event.
   */
  public void groupFinished(ReportEvent event)
  {
    Log.info ("Group Finished: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
    Log.info ("Group Finished: " + event.getState().getCurrentGroupIndex());
  }

  /**
   * Receives notification that a row of data is being processed.
   * <P>
   * Maps the itemsAdvanced-method to the legacy function advanceItems (int).
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    //Log.info ("Items Advanced: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(ReportEvent event)
  {
    //Log.info ("Items Started: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(ReportEvent event)
  {
    //Log.info ("Items Finished: Level = " + event.getState().getLevel() + " Prepare Run: " + event.getState().isPrepareRun());
  }

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return The value of the function.
   */
  public Object getValue()
  {
    return null;
  }
}
