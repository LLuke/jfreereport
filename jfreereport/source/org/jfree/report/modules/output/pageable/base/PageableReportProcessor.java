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
 * $Id: PageableReportProcessor.java,v 1.3 2003/08/18 18:28:00 taqua Exp $
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

import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.EmptyReportException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportEventException;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.event.RepaginationListener;
import org.jfree.report.event.RepaginationState;
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
 * @author Thomas Morgner
 */
public class PageableReportProcessor
{
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
  private Object[] listenersCache;

  /**
   * Creates a new ReportProcessor.
   *
   * @param report  the report.
   *
   * @throws ReportProcessingException if the report cannot be cloned.
   * @throws FunctionInitializeException if a function cannot be initialised.
   */
  public PageableReportProcessor(final JFreeReport report)
      throws ReportProcessingException, FunctionInitializeException
  {
    try
    {
      // first cloning ... protect the page layouter function ...
      // and any changes we may do to the report instance.

      // a second cloning is done in the start state, to protect the
      // processed data.
      this.report = (JFreeReport) report.clone();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new ReportProcessingException("Initial Clone of Report failed");
    }

    final String layouter = (String) this.report.getProperty(LAYOUTMANAGER_NAME);
    final PageLayouter lm = getLayoutManager(layouter);
    this.report.addFunction(lm);
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
   * Returns the report.
   *
   * @return the report.
   */
  public JFreeReport getReport()
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
  private PageLayouter getLayoutManager(String key) throws ReportProcessingException
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
    // do a repagination
    final ReportStateList list = repaginate();
    if (list.size() == 0)
    {
      throw new EmptyReportException("Repaginating returned no pages");
    }

    final boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();
    RepaginationState repaginationState = new RepaginationState(this, 0, 0, 0, 0, false);

    for (int i = 0; i < list.size(); i++)
    {
      ReportState rs = list.get(i);
      // fire an event for every generated page. It does not really matter
      // if that policy is not very informative, it is sufficient ...
      repaginationState.reuse(PRINT_FUNCTION_LEVEL,
          rs.getCurrentPage(), rs.getCurrentDataItem(), rs.getNumberOfRows(), false);
      fireStateUpdate(repaginationState);

      processPage(rs, getOutputTarget(), failOnError);
    }
  }

  /**
   * Processes the entire report and records the state at the end of every page.
   *
   * @return a list of report states (one for the beginning of each page in the report).
   *
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  public ReportStateList repaginate() throws ReportProcessingException
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

      // the pageformat is added to the report properties, PageFormat is not serializable,
      // so a repaginated report is no longer serializable.
      //
      // The pageformat will cause trouble in later versions, when printing over
      // multiple pages gets implemented. This property will be replaced by a more
      // suitable alternative.
      final PageFormat p = getOutputTarget().getLogicalPage().getPhysicalPageFormat();
      state.setProperty(JFreeReport.REPORT_PAGEFORMAT_PROPERTY, p.clone());


      // now change the writer function to be a dummy writer. We don't want any
      // output in the prepare runs.
      final OutputTarget dummyOutput = getOutputTarget().createDummyWriter();
      dummyOutput.configure(report.getReportConfiguration());
      dummyOutput.open();

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
          pageStates = new ReportStateList(this);
          state = processPrintedPages(state, pageStates, dummyOutput, maxRows);
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
      } while (hasNext == true);

      dummyOutput.close();
      state.setProperty(JFreeReport.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

      // finally return the saved page states.
      return pageStates;
    }
    catch (OutputTargetException ote)
    {
      throw new ReportProcessingException("Unable to repaginate Report", ote);
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
   * @param dummyOutput a dummy output target which performs the layout calculations.
   * @param maxRows the number of rows in the report (used to estaminate the current progress).
   * @return the finish state for the report.
   * @throws ReportProcessingException if there was a problem processing the report.
   */
  private ReportState processPrintedPages (ReportState state, final ReportStateList pageStates,
                                           final OutputTarget dummyOutput, final int maxRows)
      throws ReportProcessingException
  {
    final boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();
    ReportStateProgress progress = null;
    RepaginationState repaginationState = new RepaginationState(this, 0, 0, 0, 0, true);

    // inner loop: process the complete report, calculate the function values
    // for the current level. Higher level functions are not available in the
    // dataRow.

    while (!state.isFinish())
    {
      // fire an event for every generated page. It does not really matter
      // if that policy is not very informative, it is sufficient ...
      repaginationState.reuse(PRINT_FUNCTION_LEVEL,
          state.getCurrentPage(), state.getCurrentDataItem(), maxRows, true);
      fireStateUpdate(repaginationState);

      final ReportState oldstate = state;
      progress = state.createStateProgress(progress);
      state = processPage(state, dummyOutput, failOnError);
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
   * @return the finish state for the current level.
   * @throws org.jfree.report.ReportProcessingException if processing failed or if there are
   * exceptions during the function execution.
   */
  private ReportState processPrepareLevels (ReportState state, final int level, final int maxRows)
    throws ReportProcessingException
  {
    final boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();
    ReportStateProgress progress = null;

    int lastRow = -1;
    int eventCount = 0;
    int eventTrigger = maxRows / MAX_EVENTS_PER_RUN;
    RepaginationState repaginationState = new RepaginationState(this, 0, 0, 0, 0, true);
    // Function processing does not use the PageLayouter, so we don't need
    // the expensive cloning ...
    while (!state.isFinish())
    {
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
   * Draws a single page of the report to the specified graphics device, and returns state
   * information.  The caller should check the returned state to ensure that some progress has
   * been made, because on some small paper sizes the report may get stuck (particularly if the
   * header and footer are large).
   * <p>
   * To check the progress, use ReportState.isProceeding(oldstate).
   *
   * @param out The output target.
   * @param currPage The report state at the beginning of the current page.
   *
   * @return The report state suitable for the next page or ReportState.FinishState.
   *
   * @throws java.lang.IllegalArgumentException if the given state is a start or a finish state.
   * @throws ReportProcessingException if there is a problem processing the report or the
   *                                   current thread has been interrupted.
   */
  public ReportState processPage(final ReportState currPage, final OutputTarget out)
      throws ReportProcessingException
  {
    return processPage(currPage, out, getReport().getReportConfiguration().isStrictErrorHandling());
  }

  /**
   * Draws a single page of the report to the specified graphics device, and returns state
   * information.  The caller should check the returned state to ensure that some progress has
   * been made, because on some small paper sizes the report may get stuck (particularly if the
   * header and footer are large).
   * <p>
   * To check the progress, use ReportState.isProceeding(oldstate).
   *
   * @param out The output target.
   * @param currPage The report state at the beginning of the current page.
   * @param failOnError if set to true, then errors in the report event handling will cause the
   *                    reporting to fail.
   *
   * @return The report state suitable for the next page or ReportState.FinishState.
   *
   * @throws IllegalArgumentException if the given state is a start or a finish state.
   * @throws ReportProcessingException if there is a problem processing the report or the
   *                                   current thread has been interrupted.
   */
  public ReportState processPage(final ReportState currPage, final OutputTarget out, 
                                 final boolean failOnError)
      throws ReportProcessingException
  {
    ReportState state = null;
    PageLayouter lm = null;
    try
    {
      if (isHandleInterruptedState() && Thread.interrupted())
      {
        throw new ReportInterruptedException("Current thread is interrupted. Returning.");
      }

      if (out == null)
      {
        throw new NullPointerException("OutputTarget != null");
      }
      if (out.isOpen() == false)
      {
        throw new IllegalStateException("OutputTarget is not open!");
      }
      if (currPage == null)
      {
        throw new NullPointerException("State != null");
      }

      // if a finish state is set to be processed, crash to make sure that FinishStates
      // are caught outside, we won't handle them here
      if (currPage.isFinish())
      {
        throw new IllegalArgumentException("No finish state for processpage allowed: ");
      }
      try
      {
        state = (ReportState) currPage.clone();
      }
      catch (CloneNotSupportedException cne)
      {
        throw new ReportProcessingException("Clone not supported by ReportState?!");
      }
      lm = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
      lm.setLogicalPage(out.getLogicalPage());
      lm.restoreSaveState(state);

      // docmark: page spanning bands will affect this badly designed code.
      // this code will definitly be affected by the Band-intenal-pagebreak code
      // to this is non-fatal. the next redesign is planed here :)

      // The state restoration must not finish the current page, except the whole
      // report processing will be finished.
      if (lm.isPageEnded())
      {
        state = state.advance();
        if (state.isFinish() == false)
        {
          throw new ReportProcessingException("State finished page during restore");
        }
      }
      else
      {
        // Do some real work.  The report header and footer, and the page headers and footers are
        // just decorations, as far as the report state is concerned.  The state only changes in
        // the following code...

        // this loop advances the report state until the next page gets started or
        // the end of the reporting is reached.
        // note: Dont test the end of the page, this gives no hint whether there will
        // be a next page ...
        while ((lm.isPageEnded() == false) && (state.isFinish() == false))
        {
          final PageLayouter org = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
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
          lm = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
          if (org != lm)
          {
            // assertation check, the pagelayouter must not change during the processing.
            throw new IllegalStateException("Lost the layout manager");
          }
        }
      }
    }
    finally
    {
      // clear the logical page reference, so that no memleak is created...
      if (lm != null)
      {
        lm.clearLogicalPage();
      }
    }
    return state;
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
}
