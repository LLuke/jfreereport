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
 * -----------------
 * PageLayouter.java
 * -----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageLayouter.java,v 1.16 2003/02/20 21:05:01 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.pagelayout;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.targets.pageable.LogicalPage;

/**
 * The baseclass for all PageLayouter. A page layouter is the layoutmanager of
 * an logical page. This layoutmanager is responsible for handling and detecting
 * page breaks, for placing the various Root-Bands (bands that are contained
 * directly in an report) on the page and for the global appeareance of an page
 * (columns, band placement policy etc.)
 * <p>
 * A PageLayouter for an Report is defined by the ReportProperty
 * <code>pageable.layoutManager</code> by setting the classname of the page layouter.
 * The specified class must contain an DefaultConstructor and must be an instance of
 * PageLayouter.
 * <p>
 * All PageLayouter may define a set of LayoutConstraints for use in the Root-Bands.
 * These constraints can be used to configure the layouting process. It is up to
 * the layoutmanager implementation, which constraints are recognized and how these
 * constraints are used.
 * <p>
 * All layoutmanagers should document their known constraints.
 *
 * @author Thomas Morgner
 */
public abstract class PageLayouter extends AbstractFunction
{
  /**
   * Represents the state of the page layouter. Subclasses must override this
   * class to create an internal state which can be saved and restored when
   * a page-break occurs.
   * <p>
   * The state is required to be cloneable and must support deep-cloning so that
   * the state can be restored multiple times whenever the page printing continues.
   */
  protected abstract static class LayoutManagerState implements Cloneable
  {
    /**
     * Creates a new state.
     */
    public LayoutManagerState ()
    {
    }
  }

  /**
   * A clone carrier. DefaultCloning does clone reference to an object, and
   * not the the object-content, so the content in that carrier is shared
   * among all cloned instances.
   */
  private class CloneCarrier
  {
    /** The report. */              // don't clone these
    public JFreeReport report;

    /** The logical page. */
    private LogicalPage logicalPage;
  }

  /**
   * The current layoutmanager state. This is used to reconstruct the state of
   * the last print operation whenever an pagebreak occured. The contents depend
   * on the specific implementation of the PageLayouter.
   */
  private LayoutManagerState layoutManagerState;

  /** The latest report event. */
  private ReportEvent currentEvent;

  /** A clone carrier. */
  private CloneCarrier cloneCarrier;

  /**
   * The depency level. Should be set to -1 or lower to ensure that this function
   * has the highest priority in the function collection.
   */
  private int depLevel;

  /**
   * The 'finishing page' flag. Indicates that the current page is shut down.
   * The page footer should be printed and the state stored. Trying to end the
   * page again while the finishing process is not complete will result in an
   * IllegalState.
   * <p>
   * If the page should be finished while the page is restarted, will throw an
   * ReportProcessing exception, as this would result in an infinite loop.
   */
  private boolean finishingPage;

  /**
   * The 'restarting page' flag. Is set to true, while the page is started.
   * The last saved state is restored and the page header gets prepared.
   * Trying the start the page again while the restarting process is not complete
   * or while the page is currently being finished, will result in an IllegalState.
   */
  private boolean restartingPage;

  /** A flag indicating whether some content was created */
  private boolean generatedPageEmpty;

  private boolean pageRestartDone;

  /**
   * Creates a new page layouter. The function depency level is set to -1 (highest
   * priority).
   */
  public PageLayouter()
  {
    setDependencyLevel(-1);
    cloneCarrier = new CloneCarrier();
  }

  public boolean isPageRestartDone()
  {
    return pageRestartDone;
  }

  public void setPageRestartDone(boolean pageRestartDone)
  {
    this.pageRestartDone = pageRestartDone;
  }

  public boolean isGeneratedPageEmpty()
  {
    return generatedPageEmpty;
  }

  public void setGeneratedPageEmpty(boolean generatedPageEmpty)
  {
    this.generatedPageEmpty = generatedPageEmpty;
  }

  /**
   * Sets the logical page for the layouter.
   *
   * @param logicalPage  the logical page (null not permitted).
   * @throws NullPointerException it the logical page is null
   */
  public void setLogicalPage(LogicalPage logicalPage)
  {
    if (logicalPage == null)
    {
      throw new NullPointerException("PageLayouter.setLogicalPage: logicalPage is null.");
    }
    this.cloneCarrier.logicalPage = logicalPage;
  }

  /**
   * Returns the logical page.
   *
   * @return the logical page.
   */
  public LogicalPage getLogicalPage ()
  {
    return cloneCarrier.logicalPage;
  }

  /**
   * Returns the 'finishing page' flag.
   * <p>
   * When set to true, indicates that the current page is shut down.
   * The page footer should be printed and the state stored. Trying to end the
   * page again while the finishing process is not complete will result in an
   * IllegalState.
   * <p>
   * If the page should be finished while the page is restarted, will throw an
   * ReportProcessing exception, as this would result in an infinite loop.
   *
   * @return true if the current page is currently shut down, false otherwise.
   */
  public boolean isFinishingPage()
  {
    return finishingPage;
  }

  /**
   * Sets the 'finishing page' flag.
   *
   * @see com.jrefinery.report.targets.pageable.pagelayout.PageLayouter#isFinishingPage
   * @param finishingPage  the new flag value.
   */
  public void setFinishingPage(boolean finishingPage)
  {
    this.finishingPage = finishingPage;
  }

  /**
   * Returns the 'restarting page' flag.
   * <p>
   * Is set to true, while the page is started.
   * The last saved state is restored and the page header gets prepared.
   * Trying the start the page again while the restarting process is not complete
   * or while the page is currently being finished, will result in an IllegalState.
   *
   * @return true if the current page is currently restarting, false otherwise
   */
  public boolean isRestartingPage()
  {
    return restartingPage;
  }

  /**
   * Sets the 'restarting page' flag.
   *
   * @see com.jrefinery.report.targets.pageable.pagelayout.PageLayouter#isRestartingPage
   * @param restartingPage  sets the restarting page flag.
   */
  public void setRestartingPage(boolean restartingPage)
  {
    this.restartingPage = restartingPage;
  }

  /**
   * Returns the report that should be printed. This is the report contained
   * in the last ReportEvent received, or null, if no event occurred here.
   *
   * @return the report.
   */
  public JFreeReport getReport()
  {
    return cloneCarrier.report;
  }

  /**
   * Sets the report.
   *
   * @param report  the report (null not permitted).
   * @throws NullPointerException if the given report is null.
   */
  private void setReport(JFreeReport report)
  {
    if (report == null)
    {
      throw new NullPointerException("PageLayouter.setReport: report is null.");
    }
    this.cloneCarrier.report = report;
  }

  /**
   * Returns the current report event.
   *
   * @return the event.
   */
  protected ReportEvent getCurrentEvent()
  {
    return currentEvent;
  }

  /**
   * Sets the current event (also updates the report reference).
   *
   * @param currentEvent event.
   */
  protected void setCurrentEvent(ReportEvent currentEvent)
  {
    this.currentEvent = currentEvent;
    if (currentEvent != null)
    {
      setReport(currentEvent.getReport());
    }
  }

  /**
   * Ends a page. During the process, an PageFinished event is generated and
   * the state advances to the next page. The Layoutmanager-State is saved and
   * the logical page is closed.
   * <p>
   * While this method is executed, the FinishingPage flag is set to true.
   * <p>
   * You are not able to print on the logical page after the page is finished.
   *
   * @throws IllegalStateException if the page end is requested a second time
   * @throws ReportProcessingException if the page end is requested while the page
   * is restarted.
   */
  protected void endPage () throws ReportProcessingException
  {
    // this can be dangerous if a band spans multiple pages.
    // rethink that.
    if (isRestartingPage())
    {
      throw new ReportProcessingException ("Report does not proceed (PageEnd during RestartPage)");
    }
    // cannot finish
    if (isFinishingPage())
    {
      throw new IllegalStateException("Page is currently finishing");
    }
    setFinishingPage(true);

    ReportState cEventState = getCurrentEvent().getState();
    cEventState.firePageFinishedEvent();
    cEventState.nextPage();
    setFinishingPage(false);

    // save the state after the PageFinished event is fired to
    // gather as many information as possible
    // log // no cloning save the orignal state
    layoutManagerState = saveCurrentState();

    setGeneratedPageEmpty(getLogicalPage().isEmpty());
    getLogicalPage().close();
  }

  /**
   * Restarts the current page. The logical page is opened again and the
   * PageStartedEvent is fired. While this method is executed, the RestartingPage
   * flag is set to true.
   */
  protected void startPage ()
  {
    if (isPageRestartDone() == true)
      throw new IllegalStateException("Page already started");

    ReportState state = getCurrentEvent().getState();
    if (state == null)
    {
      throw new NullPointerException("PageLayouter.startPage(...): state is null.");
    }

    setRestartingPage(true);
    state.firePageStartedEvent();
    setRestartingPage(false);
    setPageRestartDone(true);
  }

  /**
   * Checks whether this page has ended. If this method returns true, no
   * printing to the logical page is allowed.
   *
   * @return true, if the logical page is closed and no printing is allowed
   */
  public boolean isPageEnded()
  {
    return getLogicalPage().isOpen() == false;
  }

  public abstract boolean isNewPageStarted();

  /**
   * Save the current state into the LayoutManagerState object. The concrete
   * implementation is responsible to save all required information so that the
   * printing can be continued after the pagebreak is done.
   *
   * @return a valid saveState, never null
   */
  protected abstract LayoutManagerState saveCurrentState ();

  /**
   * Return the last stored LayoutManager state or null if there is no state
   * stored.
   * @return the last LayoutManagerState.
   */
  public LayoutManagerState getLayoutManagerState()
  {
    return layoutManagerState;
  }

  /**
   * Restores the layoutmanager state by using this state as new base for
   * processing. This state must be a clone of the last report state for
   * the previous page or the original last report state - or the processing
   * will print stupid things.
   *
   * @param state  the report state.
   *
   * @throws ReportProcessingException if there is a problem processing the report.
  public abstract void restoreSaveState (ReportState state)
      throws ReportProcessingException;
   */


  /**
   * Clear the saveState. This should be called after the saveState has been
   * restored.
   */
  protected void clearSaveState ()
  {
    layoutManagerState = null;
  }

  /**
   * Return a self-reference. This backdoor is used to extract the current
   * PageLayoutManager instance from the DataRow. Please don't use it in your functions,
   * it is required for the PageableReportProcessor.
   *
   * @return this PageLayouter.
   */
  public Object getValue ()
  {
    return this;
  }

  /**
   * The dependency level defines the level of execution for this function. Higher dependency functions
   * are executed before lower dependency functions. For ordinary functions and expressions,
   * the range for dependencies is defined to start from 0 (lowest dependency possible)
   * to 2^31 (upper limit of int).
   * <p>
   * PageLayouter functions override the default behaviour an place them self at depency level -1,
   * an so before any userdefined function.
   *
   * @return the level.
   */
  public int getDependencyLevel()
  {
    return depLevel;
  }

  /**
   * Overrides the depency level. Should be lower than any other function depency.
   * @param deplevel the new depency level.
   */
  public void setDependencyLevel(int deplevel)
  {
    this.depLevel = deplevel;
  }



  /**
   * Returns a clone of the PageLayouter.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * structures contained in objects, you have to overwrite this function.
   *
   * @return a clone of the function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Restores the state.
   *
   * @param anchestor  the ancestor state.
   *
   * @throws ReportProcessingException if the printing failed or a pagebreak is
   * requested while the page is restored.
   * @throws IllegalStateException if there is no SavedState but this is not the
   * first page.
   */
  public void restoreSaveState(ReportState anchestor)
      throws ReportProcessingException
  {
    Object state = getLayoutManagerState();
    Log.debug (state);
    // reset the report finished flag...
    //setStartNewPage(false);
    setGeneratedPageEmpty(true);
    setPageRestartDone(false);
    
    if (state == null)
    {
      if (anchestor.getCurrentPage() != 1)
      {
        throw new IllegalStateException("State is null, but this is not the first page." + anchestor.getCurrentPage());
      }
    }
    // open the logical page ...
    getLogicalPage().open();
  }

}
