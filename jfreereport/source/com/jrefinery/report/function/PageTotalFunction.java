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
 * -----------------------
 * PageTotalFunction.java
 * -----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageTotalFunction.java,v 1.10 2002/12/12 12:26:56 mungady Exp $
 *
 * ChangeLog
 * ---------
 * 16-Oct-2002 : Initial version
 * 25-Oct-2002 : BugFix, grouped pagecounting was not working
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.util.Log;

import java.util.Hashtable;

/**
 * This function will only work as expected in group mode if the named group has pagebreak set to
 * true.  This function depends on pageEvents, so use it with PageableProcessor only.
 *
 * FunctionFlow:
 *
 * if autmatic pagebreak:
 *   fireGroupFinish
 *   -> PageFull, group footer cannot be printed yet
 *   firePageFinish
 *    -> groupFooter is printed
 *   fireGroupStarted
 *   firePageFinish // manual pagebreak
 *   -> groupHeader is printed
 *
 * if manual pagebreak only
 *    fireGroupFinish
 *    -> print GroupFooter
 *    fireGroupStarted
 *    firePageFinish // manual pagebreak
 *   -> groupHeader is printed
 *
 * @author Thomas Morgner
 */
public class PageTotalFunction extends PageFunction
{
  /**
   * The current number is a shared secret over multiple report states and is shared
   * among all states of a report (if global) or all states which belong to a group.
   */
  private static class PageStorage
  {
    /** The page. */
    private int page;

    /**
     * Creates a new page storage.
     *
     * @param page  the page.
     */
    public PageStorage(int page)
    {
      this.page = page;
    }

    /**
     * Returns the page number.
     *
     * @return the page number.
     */
    public int getPage()
    {
      return page;
    }

    /**
     * Sets the page number.
     *
     * @param page  the page number.
     */
    public void setPage(int page)
    {
      this.page = page;
    }
  }

  /** The page number. */
  private PageStorage pageStorage;

  /** The group pages. */
  private Hashtable groupPages;

  /** The group started flag. */
  private boolean isGroupStarted;

  /**
   * Creates a new page total function.
   */
  public PageTotalFunction()
  {
    groupPages = new Hashtable();
  }

  /**
   * Receives notification from the report engine that a new page is starting.  Grabs the page
   * number from the report state and stores it.
   * <p>
   * Prepared data is bound to the display item, the current displayed row.
   *
   * @param event Information about the event.
   */
  public void pageStarted(ReportEvent event)
  {
    Log.debug ("PageStarted Fired:" + event);
    if (event.getState().isPrepareRun() && event.getState().getLevel() < 0)
    {
      if (isGroupStarted)
      {
        this.pageStorage = new PageStorage(getStartPage());
        isGroupStarted = false;
      }
      else
      {
        this.setPage(getPage() + 1);
      }
      groupPages.put(new Integer(event.getState().getCurrentDisplayItem()), this.pageStorage);
    }
    else
    {
      if (event.getState().isPrepareRun() == false)
      {
        // restore the saved state
        this.pageStorage = (PageStorage)
            groupPages.get(new Integer(event.getState().getCurrentDisplayItem()));
        if (pageStorage == null)
        {
          throw new IllegalStateException("No page-storage for the current state: "
                                          + event.getState().getCurrentDataItem());
        }
      }
    }
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {
    if (getGroup() == null)
    {
      return;
    }

    JFreeReport report = event.getReport();
    Group group = report.getGroup(event.getState().getCurrentGroupIndex());
    if (getGroup().equals(group.getName()) == false)
    {
      return;
    }

    if (event.getState().isPrepareRun())
    {
      if (event.getState().getLevel() < 0)
      {
        // Correct the calculation:
        // at this point it is sure, that the previous group has finished. We cannot use the
        // group finished event, as this event is fired before the layouting is done. So the
        // groupfooter can move to the next page (without throwing another event), and a function
        // will not get informed of this.
        //this.setPage(getPage() - 1);

        isGroupStarted = true;
        // this PageStorage is only null, if the report has never reached the first report start
        // event
      }
    }
  }

  /**
   * Receives notification that the report has started.
   * <p>
   * Note: pageStorage is a shared object for all instances contained in the
   * start state, but reportStarted is called during the advancement. States
   * get cloned before the advancement is done ...
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    if (event.getState().isPrepareRun() && event.getState().getLevel() < 0)
    {
      this.groupPages.clear();
    }
  }

  /**
   * Sets the page number.
   *
   * @param page  the page number.
   */
  public void setPage(int page)
  {
    if (this.pageStorage != null)
    {
      this.pageStorage.setPage(page);
    }
  }

  /**
   * Returns the page number.
   *
   * @return the page number.
   */
  public int getPage()
  {
    if (this.pageStorage == null)
    {
      Log.warn ("CurrentPage is null, no repagination done?");
      return 0;
    }

    return this.pageStorage.getPage();
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
    pageStorage = new PageStorage(getStartPage() - 1);
  }

  /**
   * Returns the name of the group that this function acts upon.
   *
   * @return the group name.
   */
  public String getGroup()
  {
    return getProperty("group");
  }

  /**
   * Sets the name of the group that the function acts upon.
   *
   * @param group  the group name.
   */
  public void setGroup(String group)
  {
    setProperty("group", group);
  }

  /**
   * Returns the starting page number (defaults to 1).
   *
   * @return the starting page number.
   */
  public int getStartPage()
  {
    return Integer.parseInt(getProperty("start", "1"));
  }
}
