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
 * -----------------
 * PageFunction.java
 * -----------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageFunction.java,v 1.15 2003/06/13 16:21:24 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.function;

import java.io.Serializable;

import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.event.PageEventListener;
import com.jrefinery.report.states.ReportState;

/**
 * A report function that counts pages. This method is only useable when a
 * report processor is used, which generated PageEvents. The PageableReportProcessor
 * is one of them.
 * <p>
 * As with all page dependent functions: The function will only be active, when
 * the page events get fired, this usually only happens during the last pagination
 * run and the printing. The function level will be negative when this happens.  
 *
 * @author Thomas Morgner
 */
public class PageFunction extends AbstractFunction
    implements Serializable, PageEventListener
{

  /** The page. */
  private int page;

  /** The 'group-started' flag. */
  private boolean isGroupStarted;

  /**
   * Constructs an unnamed function.
   * <P>
   * This constructor is intended for use by the SAX handler class only.
   */
  public PageFunction()
  {
  }

  /**
   * Constructs a named function.
   *
   * @param name  the function name.
   */
  public PageFunction(String name)
  {
    setName(name);
  }

  /**
   * Receives notification from the report engine that a new page is starting.  Grabs the page
   * number from the report state and stores it.
   *
   * @param event  the event.
   */
  public void pageStarted(ReportEvent event)
  {
    if (isGroupStarted)
    {
      this.setPage(getStartPage());
      isGroupStarted = false;
    }
    else
    {
      setPage(getPage() + 1);
    }
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor.
   *
   * @param event The event.
   */
  public void pageCanceled(ReportEvent event)
  {
    if (isIgnorePageCancelEvents())
    {
      return;
    }

    this.setPage(getPage() - 1);
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

    ReportState state = event.getState();
    Group group = event.getReport().getGroup(state.getCurrentGroupIndex());
    if (getGroup().equals(group.getName()))
    {
      isGroupStarted = true;
    }
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportInitialized(ReportEvent event)
  {
    this.setPage(getStartPage() - 1);
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

  /**
   * Returns the group name.
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
   * Returns the start page.
   *
   * @return the start page.
   */
  public int getStartPage()
  {
    return Integer.parseInt(getProperty("start", "1"));
  }

  /**
   * Returns whether this function will ignore PageCancel events.
   * This defaults to false, so that there will be no gaps between
   * the generated pages.
   *
   * @return true, if canceled pages should be ignored, false otherwise.
   */
  public boolean isIgnorePageCancelEvents()
  {
    return getProperty("ignore-cancel", "false").equalsIgnoreCase("true");
  }

  /**
   * Returns the current page.
   *
   * @return the current page.
   */
  protected int getPage()
  {
    return page;
  }

  /**
   * Sets the current page.
   *
   * @param page  the page.
   */
  protected void setPage(int page)
  {
    this.page = page;
  }
}
