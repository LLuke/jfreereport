/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 16.10.2002
 * Time: 19:07:38
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.util.Log;

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

  public PageTotalFunction()
  {
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
    if (event.getState().isPrepareRun())
    {
      if (getGroup() == null) return;

      JFreeReport report = event.getReport();
      ReportState state = event.getState();
      Group group = report.getGroup(state.getCurrentGroupIndex());
      if (getGroup().equals(group.getName()))
      {
        this.page = new PageStorage(getStartPage());
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

  /**
   * Receives notification that the report has finished.
   * <P>
   * Maps the reportFinished-method to the legacy function endReport ().
   *
   * @param event Information about the event.
   */
  public void reportFinished(ReportEvent event)
  {
    event.getState().setProperty(JFreeReport.REPORT_PAGECOUNT_PROPERTY, getValue());
  }
}
