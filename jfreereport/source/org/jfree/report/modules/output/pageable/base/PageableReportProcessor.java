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
 * ----------------------------
 * PageableReportProcessor.java
 * ----------------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageableReportProcessor.java,v 1.14 2004/03/27 20:21:15 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added Javadocs (DG);
 * 02-Feb-2003 : added Interrupt handling and removed log messages.
 * 04-Feb-2003 : Code-Optimizations
 * 12-Feb-2003 : BugFix: Page is ended when isPageEndedIsTrue(), dont wait for the next page.
 *               This caused lost rows.
 */

package org.jfree.report.modules.output.pageable.base;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.EmptyReportException;
import org.jfree.report.JFreeReport;
import org.jfree.report.PageDefinition;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.RepaginationListener;
import org.jfree.report.event.RepaginationState;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.modules.output.meta.MetaPage;
import org.jfree.report.modules.output.pageable.base.pagelayout.PageLayouter;
import org.jfree.report.modules.output.pageable.base.pagelayout.SimplePageLayouter;
import org.jfree.report.states.FinishState;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.ReportStateProgress;
import org.jfree.report.states.StartState;
import org.jfree.report.util.Log;

/**
 * A report processor for Pageable OutputTargets. The processor coordinates
 * and initializes the output process for all page- and print-oriented output
 * formats.
 *
 * Todo: The PageState list will no longer be accessible from outside
 *
 *
 * @author Thomas Morgner
 */
public class PageableReportProcessor
{
  /**
   * A compile time constant that defines how many events should be generated
   * during the report processing.
   */
  private static final int MAX_EVENTS_PER_RUN = 400;

  /** The level where the page function is executed. */
  private static final int PRINT_FUNCTION_LEVEL = -1;

  /** The page layout manager name. */
  public static final String LAYOUTMANAGER_NAME = "pageable.layoutManager";

  /** The report being processed. */
  private JFreeReport report;

  /** The output target. */
  private OutputTarget outputTarget;

  /** A flag defining whether to check for Thread-Interrupts. */
  private boolean handleInterruptedState;

  /** Storage for listener references. */
  private ArrayList listeners;

  /** The listeners as object array for faster access. */
  private Object[] listenersCache;

  private ReportStateList stateList;

  /**
   * Creates a new ReportProcessor.
   *
   * @param report  the report.
   *
   * @throws ReportProcessingException if the report cannot be cloned.
   */
  public PageableReportProcessor(final JFreeReport report)
      throws ReportProcessingException
  {
    try
    {
      // first cloning ... protect the page layouter function ...
      // and any changes we may do to the report instance.

      // a second cloning is done in the start state, to protect the
      // processed data.
      final JFreeReport internalReport = (JFreeReport) report.clone();
      final String layouter = internalReport.getReportConfiguration().
              getConfigProperty(LAYOUTMANAGER_NAME);
      final PageLayouter lm = createLayoutManager(layouter);
      internalReport.addExpression(lm);
      this.report = internalReport;
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Initial Clone of Report failed");
    }

  }

  /**
   * Adds a repagination listener. This listener will be informed of
   * pagination events.
   *
   * @param l  the listener.
   */
  public void addRepaginationListener(final RepaginationListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      listeners = new ArrayList(5);
    }
    listenersCache = null;
    listeners.add(l);
  }

  /**
   * Removes a repagination listener.
   *
   * @param l  the listener.
   */
  public void removeRepaginationListener(final RepaginationListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      return;
    }
    listenersCache = null;
    listeners.remove(l);
  }


  /**
   * Sends a repagination update to all registered listeners.
   *
   * @param state  the state.
   */
  protected void fireStateUpdate(final RepaginationState state)
  {
    if (listeners == null)
    {
      return;
    }
    if (listenersCache == null)
    {
      listenersCache = listeners.toArray();
    }
    for (int i = 0; i < listenersCache.length; i++)
    {
      final RepaginationListener l = (RepaginationListener) listenersCache[i];
      l.repaginationUpdate(state);
    }
  }

  /**
   * Returns whether the processor should check the threads interrupted state.
   * If this is set to true and the thread was interrupted, then the report processing
   * is aborted.
   *
   * @return true, if the processor should check the current thread state, false otherwise.
   */
  public boolean isHandleInterruptedState()
  {
    return handleInterruptedState;
  }

  /**
   * Defines, whether the processor should check the threads interrupted state.
   * If this is set to true and the thread was interrupted, then the report processing
   * is aborted.
   *
   * @param handleInterruptedState true, if the processor should check the current thread state,
   *                               false otherwise.
   */
  public void setHandleInterruptedState(final boolean handleInterruptedState)
  {
    this.handleInterruptedState = handleInterruptedState;
  }

  /**
   * Returns the output target.
   *
   * @return the output target.
   */
  public OutputTarget getOutputTarget()
  {
    return outputTarget;
  }

  /**
   * Sets the output target.
   *
   * @param outputTarget  the output target.
   */
  public void setOutputTarget(final OutputTarget outputTarget)
  {
    this.outputTarget = outputTarget;
  }

  /**
   * Returns the report object.
   *
   * @return the report.
   */
  protected JFreeReport getReport()
  {
    return report;
  }

  /**
   * Returns the layout manager.  If the key is <code>null</code>, an instance of the
   * <code>SimplePageLayouter</code> class is returned.
   *
   * @param key  the key (null permitted).
   *
   * @return the page layouter.
   *
   * @throws org.jfree.report.ReportProcessingException if there is a processing error.
   */
  private PageLayouter createLayoutManager(String key) throws ReportProcessingException
  {
    if (key == null)
    {
      key = SimplePageLayouter.class.getName();
    }
    try
    {
      final Class c = getClass().getClassLoader().loadClass(key);
      final PageLayouter lm = (PageLayouter) c.newInstance();
      lm.setName(LAYOUTMANAGER_NAME);
      return lm;
    }
    catch (Exception e)
    {
      throw new ReportProcessingException("Unable to create the layoutmanager", e);
    }
  }

  /**
   * Processes the report in two passes, the first pass calculates page boundaries and function
   * values, the second pass sends each page to the output target.
   * <p>
   * It is possible for the report processing to fail.  A base cause is that the report is
   * designed for a certain page size, but ends up being sent to an output target with a much
   * smaller page size.  If the headers and footers don't leave enough room on the page for at
   * least one row of data to be printed, then no progress is made.  An exception will be thrown
   * if this happens.
   *
   * @throws org.jfree.report.ReportProcessingException if the report did not proceed and got stuck.
   * @throws org.jfree.report.EmptyReportException if the repagination returned no pages.
   */
  public void processReport()
      throws ReportProcessingException, EmptyReportException
  {
    if (isPaginated() == false)
    {
      repaginate();
    }

    // do a repagination
    final ReportStateList list = stateList;
    if (list.size() == 0)
    {
      throw new EmptyReportException("EmptyReport: Repaginating returned no pages");
    }

    final boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();
    final RepaginationState repaginationState = new RepaginationState(this, 0, 0, 0, 0, false);
    final int pageCount = getReport().getPageDefinition().getPageCount();
    final PageProcess pageProcess = createPageProcess();

    for (int i = 0; i < list.size(); i++)
    {
      // todo warning: Not physical page!
      final ReportState rs = list.get(i);
      // fire an event for every generated page. It does not really matter
      // if that policy is not very informative, it is sufficient ...
      repaginationState.reuse(PRINT_FUNCTION_LEVEL,
          rs.getCurrentPage(), rs.getCurrentDataItem(), rs.getNumberOfRows(), false);
      fireStateUpdate(repaginationState);

      pageProcess.processPage(rs, failOnError);
      final MetaPage mp = pageProcess.getMetaPage();

      for (int p = 0; p < pageCount; p++)
      {
        performGenerate(mp, p);
      }
    }
  }

  public synchronized void repaginate () throws ReportProcessingException
  {
    stateList = performRepaginate();
  }

  /**
   * Processes the entire report and records the state at the end of every page.
   *
   * @return a list of report states (one for the beginning of each page in the report).
   *
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  private ReportStateList performRepaginate() throws ReportProcessingException
  {
    try
    {
      // every report processing starts with an StartState.
      final StartState startState = new StartState(getReport());
      ReportState state = startState;
      final int maxRows = getReport().getData().getRowCount();
      ReportStateList pageStates = null;

      // make sure the output target is configured
      getOutputTarget().configure(getReport().getReportConfiguration());

      // the report processing can be splitted into 2 separate processes.
      // The first is the ReportPreparation; all function values are resolved and
      // a dummy run is done to calculate the final layout. This dummy run is
      // also necessary to resolve functions which use or depend on the PageCount.

      // the second process is the printing of the report, this is done in the
      // processReport() method.

      // during a prepare run the REPORT_PREPARERUN_PROPERTY is set to true.
      state.setProperty(JFreeReport.REPORT_PREPARERUN_PROPERTY, Boolean.TRUE);

      // now process all function levels.
      // there is at least one level defined, as we added the PageLayouter
      // to the report.
      // the levels are defined from +inf to 0
      // we don't draw and we do not collect states in a StateList yet
      final Iterator it = startState.getLevels();
      if (it.hasNext() == false)
      {
        throw new IllegalStateException("No functions defined, invalid implementation.");
      }

      boolean hasNext;
      int level = ((Integer) it.next()).intValue();
      // outer loop: process all function levels
      do
      {
        // if the current level is the output-level, then save the report state.
        // The state is used later to restart the report processing.
        if (level == PRINT_FUNCTION_LEVEL)
        {
          pageStates = new ReportStateList(createPageProcess());
          state = processPrintedPages(state, pageStates, maxRows);
        }
        else
        {
          state = processPrepareLevels(state, level, maxRows);
        }

        // if there is an other level to process, then use the finish state to
        // create a new start state, which will continue the report processing on
        // the next higher level.
        hasNext = it.hasNext();
        if (hasNext)
        {
          level = ((Integer) it.next()).intValue();
          if (state instanceof FinishState)
          {
            state = new StartState((FinishState) state, level);
            // this is a paranoid check ...
            if (state.getCurrentPage() != ReportState.BEFORE_FIRST_PAGE)
            {
              throw new IllegalStateException("State was not set up properly");
            }
          }
          else
          {
            throw new IllegalStateException("Repaginate did not produce an finish state");
          }
        }
      }
      while (hasNext == true);

      //dummyOutput.close();
      state.setProperty(JFreeReport.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

      // finally return the saved page states.
      return pageStates;
    }
    catch (FunctionInitializeException fne)
    {
      throw new ReportProcessingException("Unable to initialize the functions/expressions.", fne);
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Unable to initialize the report, clone error", cne);
    }
  }

  /**
   * Processes the print level for the current report. This function will
   * fill the report state list while performing the repagination.
   *
   * @param state the start state for the print level.
   * @param pageStates the list of report states that should receive the created page states.
   * @param maxRows the number of rows in the report (used to estaminate the current progress).
   * @return the finish state for the report.
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  private ReportState processPrintedPages
          (ReportState state, final ReportStateList pageStates, final int maxRows)
      throws ReportProcessingException
  {
    final boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();
    ReportStateProgress progress = null;
    final RepaginationState repaginationState = new RepaginationState(this, 0, 0, 0, 0, true);

    // inner loop: process the complete report, calculate the function values
    // for the current level. Higher level functions are not available in the
    // dataRow.

    final PageProcess process = createPageProcess();

    while (!state.isFinish())
    {
      // fire an event for every generated page. It does not really matter
      // if that policy is not very informative, it is sufficient ...
      repaginationState.reuse(PRINT_FUNCTION_LEVEL,
          state.getCurrentPage(), state.getCurrentDataItem(), maxRows, true);
      fireStateUpdate(repaginationState);

      final ReportState oldstate = state;
      progress = state.createStateProgress(progress);
      state = process.processPage(state, failOnError);
      if (!state.isFinish())
      {
        // if the report processing is stalled, throw an exception; an infinite
        // loop would be caused.
        if (!state.isProceeding(progress))
        {
          throw new ReportProcessingException("State did not proceed, bailing out!");
        }
      }

      // if layout level has reached, and some content was generated, then add the page
      if (isEmptyPageGenerated(state) == false)
      {
        // add the page start event ..
        pageStates.add(oldstate);
      }
      else
      {
        // inform the next page, that the last one was canceled ...
        state.firePageCanceledEvent();
      }
    }
    return state;
  }

  /**
   * Processes all prepare levels to compute the function values.
   *
   * @param state the state state with which we beginn the processing.
   * @param level the current processing level.
   * @param maxRows the number of rows in the table model.
   * @return the finish state for the current level.
   * @throws ReportProcessingException if processing failed or if there are
   * exceptions during the function execution.
   */
  private ReportState processPrepareLevels
          (ReportState state, final int level, final int maxRows)
      throws ReportProcessingException
  {
    final boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();
    ReportStateProgress progress = null;

    int lastRow = -1;
    int eventCount = 0;
    final int eventTrigger = maxRows / MAX_EVENTS_PER_RUN;
    final RepaginationState repaginationState = new RepaginationState(this, 0, 0, 0, 0, true);
    // Function processing does not use the PageLayouter, so we don't need
    // the expensive cloning ...
    while (!state.isFinish())
    {
      checkInterrupted();
      if (lastRow != state.getCurrentDisplayItem())
      {
        lastRow = state.getCurrentDisplayItem();
        if (eventCount == 0)
        {
          repaginationState.reuse(level,
              state.getCurrentPage(), state.getCurrentDataItem(), maxRows, true);
          fireStateUpdate(repaginationState);
          eventCount += 1;
        }
        else
        {
          if (eventCount == eventTrigger)
          {
            eventCount = 0;
          }
          else
          {
            eventCount += 1;
          }
        }
      }

      progress = state.createStateProgress(progress);
      state = state.advance();
      if (failOnError)
      {
        if (state.isErrorOccured() == true)
        {
          throw new ReportEventException("Failed to dispatch an event.", state.getErrors());
        }
      }
      else
      {
        if (state.isErrorOccured() == true)
        {
          Log.error("Failed to dispatch an event.",
              new ReportEventException("Failed to dispatch an event.", state.getErrors()));
        }
      }
      if (!state.isFinish())
      {
        // if the report processing is stalled, throw an exception; an infinite
        // loop would be caused.
        if (!state.isProceeding(progress))
        {
          throw new ReportProcessingException("State did not proceed, bailing out!");
        }
      }
    }
    return state;
  }

  /**
   * Checks, whether the current thread is interrupted.
   *
   * @throws ReportInterruptedException if the thread is interrupted to
   * abort the report processing.
   */
  private void checkInterrupted() throws ReportInterruptedException
  {
    if (isHandleInterruptedState() && Thread.interrupted())
    {
      throw new ReportInterruptedException("Current thread is interrupted. Returning.");
    }
  }

  /**
   * Draws a single page of the report to the specified graphics device, and returns state
   * information.  The caller should check the returned state to ensure that some progress has
   * been made, because on some small paper sizes the report may get stuck (particularly if the
   * header and footer are large).
   * <p>
   * To check the progress, use ReportState.isProceeding(oldstate).
   *
   * @param page The page number of the page to be printed
   * @param failOnError if set to true, then errors in the report event handling will cause the
   *                    reporting to fail.
   *
   * @throws IllegalArgumentException if the given state is a start or a finish state.
   * @throws ReportProcessingException if there is a problem processing the report or the
   *                                   current thread has been interrupted.
   */
  public void processPage(final PageProcess pageProcess, final int page,
                             final boolean failOnError)
      throws ReportProcessingException
  {
    final ReportState pageState = getStateForPage(page);
    pageProcess.processPage(pageState, failOnError);
    final int pageCount = getReport().getPageDefinition().getPageCount();
    performGenerate(pageProcess.getMetaPage(), page % pageCount);
  }


  public PageProcess createPageProcess ()
  {
    final JFreeReport report = getReport();
    final PageProcess pageProcess = new PageProcess
            (getOutputTarget(), report.getPageDefinition(),
                    report.getReportConfiguration().isStrictErrorHandling());
    return pageProcess;
  }

  /**
   * Checks the state of the logical page, to see whether some content has been
   * printed on the page.
   *
   * @param state the state which should be checked
   * @return true, if the page is empty, false otherwise.
   */
  protected boolean isEmptyPageGenerated(final ReportState state)
  {
    final PageLayouter org = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
    return org.isGeneratedPageEmpty();
  }

  public synchronized boolean isPaginated ()
  {
    return stateList != null;
  }

  public synchronized void resetPagination ()
  {
    stateList = null;
  }

  protected synchronized ReportState getStateForPage (final int page)
    throws ReportProcessingException
  {
    if (isPaginated() == false)
    {
      // we perform no automatic pagination here, it is up
      // to the caller to ensure the correct state.
      throw new IllegalStateException("Report processor is not paginated.");
    }
    final int pageCount = getReport().getPageDefinition().getPageCount();
    return stateList.get(page / pageCount);
  }

  /**
   * Converts the meta page into operations for the output target.
   * This method will only return the operations for the given physical page.
   *
   * @param mp the metapage
   */
  protected void performGenerateAll(final MetaPage mp)
      throws ReportProcessingException
  {
    final PageDefinition physPage = getReport().getPageDefinition();
    for (int i = 0; i < physPage.getPageCount(); i++)
    {
      performGenerate(mp, i);
    }
  }

  /**
   * Converts the meta page into operations for the output target.
   * This method will only return the operations for the given physical page.
   *
   * @param mp the metapage
   */
  protected void performGenerate(final MetaPage mp, final int page)
      throws ReportProcessingException
  {

    final PageDefinition physPage = getReport().getPageDefinition();
    final OutputTarget output = getOutputTarget();
    try
    {
      output.printPage(mp, physPage, page);
    }
    catch (OutputTargetException e)
    {
      throw new ReportProcessingException("Failed to commit page " + page, e);
    }
  }

  public int getPageCount ()
  {
    if (isPaginated() == false)
    {
      return 0;
    }
    return getReport().getPageDefinition().getPageCount() *
            stateList.size();
  }
}
