/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * ------------
 * ReportListenerAdapter.java
 * ------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   -;
 *
 * Changes (from 10-May-2002)
 * --------------------------
 *
 * 10-May-2002 : Created the EventInterface for JFreeReport
 *
 */
package com.jrefinery.report.event;

public class ReportListenerAdapter implements ReportListener
{
  /**
   * Is called when the reportgeneration started. The Event carries an ReportState.Started
   * state. Use this to initialize the report.
   */
  public void reportStarted (ReportEvent event)
  {
  }

  /**
   * ReportFinished. The report generation has finished. The last record is read and all
   * groups are closed.
   */
  public void reportFinished (ReportEvent event)
  {
  }

  /**
   * A new page is beeing processed.
   */
  public void pageStarted (ReportEvent event)
  {
  }

  /**
   * The page is finished
   */
  public void pageFinished (ReportEvent event)
  {
  }

  /**
   * A new group has started. The group can be determined by the reportstate's getCurrentGroup()
   * function.
   */
  public void groupStarted (ReportEvent event)
  {
  }

  /**
   * The currentGroup is closed. The group can be determined by the reportstate's getCurrentGroup()
   */
  public void groupFinished (ReportEvent event)
  {
  }

  /**
   * The ItemsSection is beeing processed. The next events will be itemsAdvanced events until the
   * itemsFinished event is raised.
   */
  public void itemsStarted (ReportEvent event)
  {
  }

  /**
   * The itemBand is finished, the report starts to close open groups.
   */
  public void itemsFinished (ReportEvent event)
  {
  }

  /**
   * A new row is read. This event is raised before an ItemBand is printed.
   */
  public void itemsAdvanced (ReportEvent event)
  {
  }

}
