/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ----------------------
 * PageTotalFunction.java
 * ----------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageTotalFunction.java,v 1.6 2005/01/25 00:00:18 taqua Exp $
 *
 * ChangeLog
 * ---------
 * 16-Oct-2002 : Initial version
 * 25-Oct-2002 : BugFix, grouped pagecounting was not working
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.function;

import java.io.Serializable;
import java.util.HashMap;

import org.jfree.report.event.ReportEvent;

/**
 * Prints the total number of pages of an report. If a group is specified, this function
 * expects the group to have the manual pagebreak enabled.
 * <p/>
 * This function will only work as expected in group mode if the named group has pagebreak
 * set to true.
 *
 * @author Thomas Morgner
 */
public class PageTotalFunction extends PageFunction
{
  /**
   * The current number is a shared secret over multiple report states and is shared among
   * all states of a report (if global) or all states which belong to a group.
   */
  private static class PageStorage implements Serializable
  {
    /**
     * The page.
     */
    private int page;

    /**
     * Creates a new page storage.
     *
     * @param page the page.
     */
    public PageStorage (final int page)
    {
      this.page = page;
    }

    /**
     * Returns the page number.
     *
     * @return the page number.
     */
    public int getPage ()
    {
      return page;
    }

    /**
     * Sets the page number.
     *
     * @param page the page number.
     */
    public void setPage (final int page)
    {
      this.page = page;
    }
  }

  /**
   * The page number.
   */
  private PageStorage pageStorage;

  /**
   * The group pages.
   */
  private HashMap groupPages;

  /**
   * Creates a new page total function.
   */
  public PageTotalFunction ()
  {
    this.groupPages = new HashMap();
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    // report started is no longer the first event. PageStarted is called first!
    if (pageStorage == null)
    {
      pageStorage = new PageStorage(getStartPage() - 1);
    }
    setIgnoreNextGroup(true);
    setWaitForFooterPrinted(false);
  }

  /**
   * Receives notification from the report engine that a new page is starting.  Grabs the
   * page number from the report state and stores it.
   * <p/>
   * Prepared data is bound to the display item, the current displayed row.
   *
   * @param event Information about the event.
   */
  public void pageStarted (final ReportEvent event)
  {
    if (event.getState().isPrepareRun() && event.getState().getLevel() < 0)
    {
      if (isGroupStarted())
      {
        this.pageStorage = new PageStorage(getStartPage());
        setGroupStarted(false);
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
   * Sets the page number.
   *
   * @param page the page number.
   */
  protected void setPage (final int page)
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
  protected int getPage ()
  {
    if (this.pageStorage == null)
    {
//      Log.warn ("CurrentPage is null, no repagination done?: " + this.hashCode() + " -> "
//                + (o == this));
      return 0;
    }

    return this.pageStorage.getPage();
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final PageTotalFunction function = (PageTotalFunction) super.getInstance();
    function.groupPages = new HashMap();
    function.pageStorage = null;
    return function;
  }

}
