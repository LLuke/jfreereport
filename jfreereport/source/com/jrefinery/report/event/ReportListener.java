/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * -------------------
 * ReportListener.java
 * -------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   -;
 *
 * $Id: ReportListener.java,v 1.7 2003/02/12 09:59:47 taqua Exp $
 *
 * Changes (from 10-May-2002)
 * --------------------------
 * 10-May-2002 : Created the EventInterface for JFreeReport
 * 17-Jul-2002 : Updated header and Javadoc comments (DG);
 */
package com.jrefinery.report.event;

import java.util.EventListener;

/**
 * ReportListeners get informed whenever the state of a report changes.
 * <P>
 * You should be aware that most events occur more than once.  For example, the reportStarted event
 * will be triggered every time the report is regenerated.
 * <P>
 * When handling these events, use the reportState to track the current changes of the report.
 *
 * @author Thomas Morgner
 */
public interface ReportListener extends EventListener
{
  /**
   * Receives notification that report generation has started.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportStarted (ReportEvent event);

  /**
   * Receives notification that report generation has finished (the last record is read and all
   * groups are closed).
   *
   * @param event The event.
   */
  public void reportFinished (ReportEvent event);

  /**
   * Receives notification that report generation has completed, the report footer was printed,
   * no more output is done. This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone (ReportEvent event);

  /**
   * Receives notification that a new page is being started.
   *
   * @param event The event.
   */
  public void pageStarted (ReportEvent event);

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished (ReportEvent event);

  /**
   * Receives notification that a new group has started.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event The event.
   */
  public void groupStarted (ReportEvent event);

  /**
   * Receives notification that a group is finished.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event The event.
   */
  public void groupFinished (ReportEvent event);

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted (ReportEvent event);

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished (ReportEvent event);

  /**
   * Receives notification that a new row has been read.
   * <P>
   * This event is raised before an ItemBand is printed.
   *
   * @param event The event.
   */
  public void itemsAdvanced (ReportEvent event);

}
