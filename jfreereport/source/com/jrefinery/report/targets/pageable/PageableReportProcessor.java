/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ----------------------------
 * PageableReportProcessor.java
 * ----------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageableReportProcessor.java,v 1.5 2002/12/04 16:27:30 mungady Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.JFreeReportConstants;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.FinishState;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.states.StartState;
import com.jrefinery.report.targets.pageable.pagelayout.PageLayouter;
import com.jrefinery.report.targets.pageable.pagelayout.SimplePageLayouter;
import com.jrefinery.report.util.Log;

import java.awt.print.PageFormat;
import java.util.Iterator;

/**
 * A report processor.
 *
 * @author Thomas Morgner
 */
public class PageableReportProcessor
{
  /** The layout manager name. */
  public static final String LAYOUTMANAGER_NAME = "pageable.layoutManager";

  /** The report being processed. */
  private JFreeReport report;
  
  /** The output target. */
  private OutputTarget outputTarget;

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
      Class c = Class.forName(key);
      PageLayouter lm = (PageLayouter) c.newInstance();
      lm.setName(LAYOUTMANAGER_NAME);
      return lm;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new ReportProcessingException();
    }
  }

  /**
   * Processes the report in two passes, the first pass calculates page boundaries and function 
   * values, the second pass sends each page to the output target.
   * <p>
   * It is possible for the report processing to fail.  A common cause is that the report is
   * designed for a certain page size, but ends up being sent to an output target with a much 
   * smaller page size.  If the headers and footers don't leave enough room on the page for at 
   * least one row of data to be printed, then no progress is made.  An exception will be thrown
   * if this happens.
   *
   * @throws ReportProcessingException if the report did not proceed and got stuck.
   */
  public void processReport() throws ReportProcessingException
  {
    // do a repagination
    ReportStateList list = repaginate();
    if (list.size() == 0)
    {
      throw new ReportProcessingException("Repaginating returned no pages");
    }
    ReportState rs = list.get(0);
    rs = processPage(rs, outputTarget);
    while (!rs.isFinish())
    {
      ReportState nrs = processPage(rs, outputTarget);
      if ((nrs.isFinish() == false) && nrs.isProceeding(rs) == false)
      {
        throw new ReportProcessingException("Report is not proceeding");
      }
      rs = nrs;
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
      StartState startState = new StartState(getReport());
      ReportState state = startState;

      // PrepareRuns, part 1: resolve the function dependencies by running the report
      // until all function levels are completed.
      JFreeReport report = state.getReport();

      // all prepare runs have this property set, test details with getLevel()
      state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.TRUE);
      PageFormat p = outputTarget.getLogicalPage().getPhysicalPageFormat();
      state.setProperty(JFreeReportConstants.REPORT_PAGEFORMAT_PROPERTY, p);

      // the levels are defined from +inf to 0
      // we don't draw and we do not collect states in a StateList yet
      ReportStateList pageStates = null;
      OutputTarget dummyOutput = outputTarget.createDummyWriter();
      dummyOutput.configure(report.getReportConfiguration());
      dummyOutput.open();

      Iterator it = startState.getLevels();
      while (it.hasNext())
      {
        Log.debug("Will Process: " + it.next());
      }

      it = startState.getLevels();
      boolean hasNext = true;
      int level = 0;
      if (it.hasNext())
      {
        level = ((Integer) it.next()).intValue();
        try
        {
          Thread.sleep(100);
        }
        catch (Exception e)
        {
          // suppress
        }
      }

      do
      {
        Log.debug ("Processing Level " + level);
        if (level == -1)
        {
          pageStates = new ReportStateList(this);
        }
        while (!state.isFinish())
        {
          if (level == -1)
          {
            pageStates.add(state);
          }
          ReportState oldstate = state;
          state = processPage(state, dummyOutput);
          if (!state.isFinish())
          {
            if (!state.isProceeding(oldstate))
            {
              throw new ReportProcessingException("State did not proceed, bailing out!");
            }
          }
        }

        hasNext = it.hasNext();
        if (hasNext)
        {
          level = ((Integer) it.next()).intValue();
          state = new StartState((FinishState) state, level);
        }
      }
      while (hasNext == true);

      Log.debug("DummyMode done " 
                + "Free: " + Runtime.getRuntime().freeMemory() + "; " 
                + "Total: " + Runtime.getRuntime().totalMemory());

      dummyOutput.close();
      Log.debug("DummyWriting Done " 
                + "Free: " + Runtime.getRuntime().freeMemory() + "; " 
                + "Total: " + Runtime.getRuntime().totalMemory());
      // root of evilness here ... pagecount should not be handled specially ...

      state.setProperty(JFreeReportConstants.REPORT_PAGECOUNT_PROPERTY, 
                        new Integer(state.getCurrentPage() - 1));
      state.setProperty(JFreeReportConstants.REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);

      // part 3: (done by processing the ReportStateList:) Print the report
      return pageStates;
    }
    catch (ReportInitialisationException re)
    {
      throw new ReportProcessingException("Unable to initialise StartState: " + re.getMessage());
    }
    catch (OutputTargetException ote)
    {
      Log.error("Unable to repaginate Report:", ote);
      throw new ReportProcessingException(ote.getMessage());
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
   * @throws ReportProcessingException if there is a problem processing the report.
   */
  public ReportState processPage(final ReportState currPage, OutputTarget out) 
      throws ReportProcessingException
  {
    if (out == null) 
    {
      throw new NullPointerException("OutPutTarget != null");
    }
    if (out.isOpen() == false) 
    {
      throw new IllegalStateException("OutputTarget is not open!");
    }
    if (currPage == null) 
    {
      throw new NullPointerException("State != null");
    }

    // just crash to make sure that FinishStates are caught outside, we cannot handle them here
    if (currPage.isFinish())
    {
      throw new IllegalArgumentException("No finish state for processpage allowed: ");
    }
    
    ReportState state = (ReportState) currPage.clone();
    PageLayouter lm = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
    lm.setLogicalPage(out.getLogicalPage());
//    Log.info ("Restoring SaveState... " + lm.hashCode() + " -> " + state.getAnchestorHashcode());
    lm.restoreSaveState(state);

    if (lm.isPageEnded())
    {
      Log.info("PageEnded after Restore");
    }
    // Do some real work.  The report header and footer, and the page headers and footers are
    // just decorations, as far as the report state is concerned.  The state only changes in
    // the following code...
    while ((lm.isPageEnded() == false) && (state.isFinish() == false))
    {
      PageLayouter org = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
      state = state.advance();
      lm = (PageLayouter) state.getDataRow().get(LAYOUTMANAGER_NAME);
      if (org != lm) 
      {
        throw new IllegalStateException();
      }
    }

//    Log.info ("End ProcessPage: " + state.getCurrentPage() + " -> " + state.getCurrentDataItem());
//    Log.info ("End ProcessPage: " + state.getClass());
    return state;
  }
  
}
