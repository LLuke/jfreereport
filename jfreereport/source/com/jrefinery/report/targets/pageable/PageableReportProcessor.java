/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: PageableReportProcessor.java,v 1.38 2003/05/11 13:39:18 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added Javadocs (DG);
 * 02-Feb-2003 : added Interrupt handling and removed log messages.
 * 04-Feb-2003 : Code-Optimizations
 * 12-Feb-2003 : BugFix: Page is ended when isPageEndedIsTrue(), dont wait for the next page.
 *               This caused lost rows.
 */

package com.jrefinery.report.targets.pageable;

import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportEventException;
import com.jrefinery.report.ReportInterruptedException;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.EmptyReportException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.FinishState;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.ReportStateProgress;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.targets.base.event.RepaginationListener;
import com.jrefinery.report.targets.base.event.RepaginationState;
import com.jrefinery.report.targets.pageable.pagelayout.PageLayouter;
import com.jrefinery.report.targets.pageable.pagelayout.SimplePageLayouter;
import com.jrefinery.report.util.Log;

/**
 * A report processor for Pageable OutputTargets. The processor coordinates
 * and initializes the output process for all page- and print-oriented output
 * formats.
 *
 * @author Thomas Morgner
 */
public class PageableReportProcessor
{
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

  /**
   * Creates a new ReportProcessor.
   *
   * @param report  the report.
   *
   * @throws ReportProcessingException if the report cannot be cloned.
   * @throws FunctionInitializeException if a function cannot be initialised.
   */
  public PageableReportProcessor(JFreeReport report)
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

    String layouter = (String) this.report.getProperty(LAYOUTMANAGER_NAME);
    PageLayouter lm = getLayoutManager(layouter);
    this.report.addFunction(lm);
  }

  /**
   * Adds a listener.
   * 
   * @param l  the listener.
   */
  public void addRepaginationListener (RepaginationListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      listeners = new ArrayList(5);
    }
    listeners.add (l);
  }

  /**
   * Removes a listener.
   * 
   * @param l  the listener.
   */
  public void removeRepaginationListener (RepaginationListener l)
  {
    if (l == null)
    {
      throw new NullPointerException("Listener == null");
    }
    if (listeners == null)
    {
      return;
    }
    listeners.remove(l);
  }

  /**
   * Sends a repagination update to all registered listeners.
   * 
   * @param state  the state.
   */
  protected void fireStateUpdate (RepaginationState state)
  {
    if (listeners == null)
    {
      return;
    }
    for (int i = 0; i < listeners.size(); i++)
    {
      RepaginationListener l = (RepaginationListener) listeners.get(i);
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
  public void setHandleInterruptedState(boolean handleInterruptedState)
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
  public void setOutputTarget(OutputTarget outputTarget)
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
   * @throws ReportProcessingException if there is a processing error.
   */
  private PageLayouter getLayoutManager (String key) throws ReportProcessingException
  {
    if (key == null)
    {
      key = SimplePageLayouter.class.getName();
    }
    try
    {
      Class c = getClass().getClassLoader().loadClass(key);
      PageLayouter lm = (PageLayouter) c.newInstance();
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
   * @throws ReportProcessingException if the report did not proceed and got stuck.
   * @throws EmptyReportException if the repagination returned no pages.
   */
  public void processReport() throws ReportProcessingException, EmptyReportException
  {
    // do a repagination
    ReportStateList list = repaginate();
    if (list.size() == 0)
    {
      throw new EmptyReportException("Repaginating returned no pages");
    }
    ReportState rs = list.get(0);

    boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();

    rs = processPage(rs, getOutputTarget(), failOnError);
    ReportStateProgress progress = null;
    while (!rs.isFinish())
    {
      progress = rs.createStateProgress(progress);
      rs = processPage(rs, getOutputTarget(), failOnError);
      if ((rs.isFinish() == false) && rs.isProceeding(progress) == false)
      {
        throw new ReportProcessingException("Report is not proceeding");
      }
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
      StartState startState = new StartState(getReport());
      ReportState state = startState;
      int maxRows = getReport().getData().getRowCount();
      ReportStateList pageStates = null;

      // the report processing can be splitted into 2 separate processes.
      // The first is the ReportPreparation; all function values are resolved and
      // a dummy run is done to calculate the final layout. This dummy run is
      // also necessary to resolve functions which use or depend on the PageCount.

      // the second process is the printing of the report, this is done in the
      // processReport() method.

      // during a prepare run the REPORT_PREPARERUN_PROPERTY is set to true.
      state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.TRUE);

      // the pageformat is added to the report properties, PageFormat is not serializable,
      // so a repaginated report is no longer serializable.
      //
      // The pageformat will cause trouble in later versions, when printing over
      // multiple pages gets implemented. This property will be replaced by a more
      // suitable alternative.
      PageFormat p = getOutputTarget().getLogicalPage().getPhysicalPageFormat();
      state.setProperty(JFreeReportConstants.REPORT_PAGEFORMAT_PROPERTY, p.clone());


      // now change the writer function to be a dummy writer. We don't want any
      // output in the prepare runs.
      OutputTarget dummyOutput = getOutputTarget().createDummyWriter();
      dummyOutput.configure(report.getReportConfiguration());
      dummyOutput.open();

      // now process all function levels.
      // there is at least one level defined, as we added the CSVWriter
      // to the report.
      // the levels are defined from +inf to 0
      // we don't draw and we do not collect states in a StateList yet
      Iterator it = startState.getLevels();
      if (it.hasNext() == false)
      {
        throw new IllegalStateException("No functions defined, invalid implementation.");
      }

      boolean hasNext;
      ReportStateProgress progress = null;
      int level = ((Integer) it.next()).intValue();
      // outer loop: process all function levels
      do
      {
        // if the current level is the output-level, then save the report state.
        // The state is used later to restart the report processing.
        if (level == -1)
        {
          pageStates = new ReportStateList(this);

          // inner loop: process the complete report, calculate the function values
          // for the current level. Higher level functions are not available in the
          // dataRow.
          boolean failOnError = getReport().getReportConfiguration().isStrictErrorHandling();

          while (!state.isFinish())
          {
            fireStateUpdate(new RepaginationState(level, state.getCurrentPage(),
                                                  state.getCurrentDataItem(), maxRows));

            ReportState oldstate = state;
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
              pageStates.add(oldstate);
            }
          }
        }
        else
        {
          // Function processing does not use the PageLayouter, so we don't need
          // the expensive cloning ...
          while (!state.isFinish())
          {
            progress = state.createStateProgress(progress);
            state = state.advance();
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
            if (state.getCurrentPage() != 1)
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

      dummyOutput.close();
      state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

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
   * @throws IllegalArgumentException if the given state is a start or a finish state.
   * @throws ReportProcessingException if there is a problem processing the report or the
   *                                   current thread has been interrupted.
   */
  public ReportState processPage(final ReportState currPage, OutputTarget out)
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
  public ReportState processPage(final ReportState currPage, OutputTarget out, boolean failOnError)
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
          PageLayouter org = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
          state = state.advance();
          if (failOnError)
          {
            if (state.isErrorOccured() == true)
            {
              throw new ReportEventException ("Failed to dispatch an event.", state.getErrors());
            }
          }
          else
          {
            if (state.isErrorOccured() == true)
            {
              Log.error("Failed to dispatch an event.",
                  new ReportEventException ("Failed to dispatch an event.", state.getErrors()));
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
  protected boolean isEmptyPageGenerated (ReportState state)
  {
    PageLayouter org = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
    return org.isGeneratedPageEmpty();
  }
}
