package org.jfree.report.function;

import org.jfree.report.Band;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;

public abstract class AbstractElementFormatFunction extends AbstractFunction
        implements PageEventListener
{
  private String element;

  /**
   * Sets the element name. The name denotes an element within the item band. The element
   * will be retrieved using the getElement(String) function.
   *
   * @param name The element name.
   * @see org.jfree.report.Band#getElement(String)
   */
  public void setElement (final String name)
  {
    this.element = name;
  }

  /**
   * Returns the element name.
   *
   * @return The element name.
   */
  public String getElement ()
  {
    return element;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getItemBand();
    processRootBand(b);
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event the event.
   */
  public void reportFinished (final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getReportFooter();
    processRootBand(b);
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportStarted (final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getReportHeader();
    processRootBand(b);
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = FunctionUtilities.getCurrentGroup(event).getHeader();
    processRootBand(b);
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event the event.
   */
  public void groupFinished (final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = FunctionUtilities.getCurrentGroup(event).getFooter();
    processRootBand(b);
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor. This method is
   * called, when a page was removed from the report after it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled (final ReportEvent event)
  {
    // this can be ignored, as nothing is printed here...
  }

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished (final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getPageFooter();
    processRootBand(b);
  }

  /**
   * Receives notification that a new page is being started.
   *
   * @param event The event.
   */
  public void pageStarted (final ReportEvent event)
  {
    if (event.getState().isPrepareRun())
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getPageFooter();
    processRootBand(b);
  }

  /**
   * This event is fired, whenever an automatic pagebreak has been detected and the report
   * state had been reverted to the previous state.
   *
   * @param event
   */
  public void pageRolledBack (final ReportEvent event)
  {
    // don't have to care, page finished is the next event ..
  }

  protected abstract void processRootBand (Band b);
}
