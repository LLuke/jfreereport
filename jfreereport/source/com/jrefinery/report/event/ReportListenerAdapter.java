/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * --------------------------
 * ReportListenerAdapter.java
 * --------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  Thomas Morgner
 * Contributor(s):   -;
 *
 * Changes (from 10-May-2002)
 * --------------------------
 *
 * 10-May-2002 : Created the EventInterface for JFreeReport
 * 17-Jul-2002 : Updated header and Javadocs (DG);
 *
 */
package com.jrefinery.report.event;

/**
 * A base class that provides empty implementations of the methods in the ReportListener interface.
 */
public class ReportListenerAdapter implements ReportListener
{
  /**
   * An empty implementation of the reportStarted method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void reportStarted (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the reportFinished method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void reportFinished (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the pageStarted method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void pageStarted (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the pageFinished method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void pageFinished (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the groupStarted method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void groupStarted (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the groupFinished method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void groupFinished (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the itemsStarted method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void itemsStarted (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the itemsFinished method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void itemsFinished (ReportEvent event)
  {
  }

  /**
   * An empty implementation of the itemsAdvanced method defined in the ReportListener interface.
   *
   * @param event The event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
  }

}
