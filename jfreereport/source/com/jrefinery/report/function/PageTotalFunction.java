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
 * -----------------------
 * PageTotalFunction.java
 * -----------------------
 *
 * ChangeLog
 * ----------------
 * 16-Oct-2002 : Initial version
 * 25-Oct-2002 : BugFix, grouped pagecounting was not working
 */
package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.util.Log;

import java.util.Hashtable;

/**
 * This function will only work as expected in group mode if the named group has pagebreak set to true.
 */
public class PageTotalFunction extends PageFunction
{
  /**
   * The current number is a shared secret over multiple report states and is shared
   * among all states of a report (if global) or all states which belong to a group
   */
  private static class PageStorage
  {
    private int page;

    public PageStorage(int page)
    {
      this.page = page;
    }

    public int getPage()
    {
      return page;
    }

    public void setPage(int page)
    {
      this.page = page;
    }
  }

  private PageStorage page;
  private Hashtable groupPages;

  public PageTotalFunction()
  {
    groupPages = new Hashtable();
  }

  /**
   * Receives notification from the report engine that a new page is starting.  Grabs the page
   * number from the report state and stores it.
   *
   * @param event Information about the event.
   */
  public void pageStarted(ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      this.setPage(getPage() + 1);
    }
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
    if (getGroup() == null) return;

    JFreeReport report = event.getReport();
    ReportState state = event.getState();
    Group group = report.getGroup(state.getCurrentGroupIndex());
    if (getGroup().equals(group.getName()))
    {
      if (event.getState().isPrepareRun())
      {
        // Correct the calculation:
        // at this point it is sure, that the previous group has finished. We cannot use the
        // group finished event, as this event is fired before the layouting is done. So the groupfooter
        // can move to the next page (without throwing another event), and a function will not get
        // informed of this.
        this.setPage(getPage() - 1);

        this.page = new PageStorage(getStartPage());
        groupPages.put(new Integer(event.getState().getCurrentDataItem()), this.page);
      }
      else
      {
        // restore the saved state
        this.page = (PageStorage) groupPages.get(new Integer(event.getState().getCurrentDataItem()));
        if (page == null)
        {
          throw new IllegalStateException("No page-storace for the current state: " + event.getState().getCurrentDataItem());
        }
      }
    }
  }

  /**
   * Receives notification that the report has started.
   * <P>
   * Maps the reportStarted-method to the legacy function startReport ().
   *
   * @param event Information about the event.
   */
  public void reportStarted(ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      this.page = new PageStorage(getStartPage() - 1);
      this.groupPages.clear();
    }
  }

  public void setPage(int page)
  {
    this.page.setPage(page);
  }

  public int getPage()
  {
    return this.page.getPage();
  }

  /**
   * Returns the page number (function value).
   *
   * @return the page number.
   */
  public Object getValue()
  {
    return new Integer(getPage());
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   * <P>
   * The default implementation checks that the function name is not null, and calls the
   * isInitialized() method (now deprecated).
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    try
    {
      getStartPage();
    }
    catch (Exception e)
    {
      throw new FunctionInitializeException(e.getMessage());
    }
  }

  public String getGroup()
  {
    return getProperty("group");
  }

  public void setGroup(String group)
  {
    setProperty("group", group);
  }

  public int getStartPage()
  {
    return Integer.parseInt(getProperty("start", "1"));
  }
}
