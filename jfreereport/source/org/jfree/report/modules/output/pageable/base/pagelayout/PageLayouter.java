/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------
 * PageLayouter.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PageLayouter.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.base.pagelayout;

import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.pageable.base.LogicalPage;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.states.ReportState;

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
    implements PageEventListener
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
    public LayoutManagerState()
    {
    }
  }

  /**
   * The current layoutmanager state. This is used to reconstruct the state of
   * the last print operation whenever an pagebreak occured. The contents depend
   * on the specific implementation of the PageLayouter.
   */
  private LayoutManagerState layoutManagerState;

  /** The logical page used to output the generated content. */
  private LogicalPage logicalPage;

  /** The latest report event. */
  private ReportEvent currentEvent;

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

  /** A flag indicating whether some content was created. */
  private boolean generatedPageEmpty;

  /** A flag indicating whether the process of restarting the page is completed. */
  private boolean pageRestartDone;

  /**
   * Creates a new page layouter. The function depency level is set to -1 (highest
   * priority).
   */
  public PageLayouter()
  {
    setDependencyLevel(-1);
  }

  /**
   * Returns whether the restarting of the page is completed. Restarting the
   * page is separated into two processes. The first step restores the save state,
   * and the second step starts to print the page header and opens the logical page.
   * The second step is not executed, if no more content is printed.
   *
   * @return true, if the restart process for this page is completed, false otherwise.
   */
  public boolean isPageRestartDone()
  {
    return pageRestartDone;
  }

  /**
   * Defines whether the restarting of the page is completed. Restarting the
   * page is separated into two processes. The first step restores the save state,
   * and the second step starts to print the page header and opens the logical page.
   * The second step is not executed, if no more content is printed.
   *
   * @param pageRestartDone set to true, if the restart process for this page is completed,
   *                        false otherwise.
   */
  public void setPageRestartDone(final boolean pageRestartDone)
  {
    this.pageRestartDone = pageRestartDone;
  }

  /**
   * Maintains a flag, whether the generated page was completly empty.
   *
   * @return true, if the page was empty when the logical page was closed,
   * false otherwise.
   */
  public boolean isGeneratedPageEmpty()
  {
    return generatedPageEmpty;
  }

  /**
   * Defines a flag, whether the generated page was completly empty.
   *
   * @param generatedPageEmpty true, if the page was empty when the logical page was closed,
   * false otherwise.
   */
  protected void setGeneratedPageEmpty(final boolean generatedPageEmpty)
  {
    this.generatedPageEmpty = generatedPageEmpty;
  }

  /**
   * Sets the logical page for the layouter.
   *
   * @param logicalPage  the logical page (null not permitted).
   * @throws NullPointerException it the logical page is null
   */
  public void setLogicalPage(final LogicalPage logicalPage)
  {
    if (logicalPage == null)
    {
      throw new NullPointerException("PageLayouter.setLogicalPage: logicalPage is null.");
    }
    this.logicalPage = logicalPage;
  }

  /**
   * Clears the logical page reference. This method must be called after
   * the page has been processed.
   */
  public void clearLogicalPage()
  {
    this.logicalPage = null;
  }

  /**
   * Returns the logical page.
   *
   * @return the logical page.
   */
  public LogicalPage getLogicalPage()
  {
    return logicalPage;
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
   * @see org.jfree.report.modules.output.pageable.base.pagelayout.PageLayouter#isFinishingPage
   * @param finishingPage  the new flag value.
   */
  public void setFinishingPage(final boolean finishingPage)
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
   * @see org.jfree.report.modules.output.pageable.base.pagelayout.PageLayouter#isRestartingPage
   * @param restartingPage  sets the restarting page flag.
   */
  public void setRestartingPage(final boolean restartingPage)
  {
    this.restartingPage = restartingPage;
  }

  /**
   * Returns the report that should be printed. This is the report contained
   * in the last ReportEvent received, or null, if no event occurred here.
   *
   * @return the report.
   */
  public ReportDefinition getReport()
  {
    if (getCurrentEvent() == null)
    {
      throw new IllegalStateException("No Current Event available.");
    }
    return getCurrentEvent().getReport();
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
  protected void setCurrentEvent(final ReportEvent currentEvent)
  {
    if (currentEvent == null)
    {
      throw new NullPointerException();
    }
    this.currentEvent = currentEvent;
  }

  /**
   * Clears the current event.
   */
  protected void clearCurrentEvent()
  {
    if (currentEvent == null)
    {
      throw new IllegalStateException("Failed!");
    }
    this.currentEvent = null;
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
  protected void endPage() throws ReportProcessingException
  {
    // this can be dangerous if a band spans multiple pages.
    // rethink that.
    if (isRestartingPage())
    {
      throw new ReportProcessingException("Report does not proceed (PageEnd during RestartPage)");
    }
    // cannot finish
    if (isFinishingPage())
    {
      throw new IllegalStateException("Page is currently finishing");
    }
    setFinishingPage(true);

    final ReportEvent cEvent = getCurrentEvent();
    clearCurrentEvent();

    cEvent.getState().firePageFinishedEvent();
    cEvent.getState().nextPage();

    setCurrentEvent(cEvent);
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
  protected void startPage()
  {
    if (isPageRestartDone() == true)
    {
      throw new IllegalStateException("Page already started");
    }
    final ReportEvent event = getCurrentEvent();
    if (event == null)
    {
      throw new NullPointerException("PageLayouter.startPage(...): state is null.");
    }
    // remove the old event binding ....
    clearCurrentEvent();

    setRestartingPage(true);
    event.getState().firePageStartedEvent(event.getType());

    // restore the saved original event ...
    setCurrentEvent(event);
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

  /**
   * Returns true, if the PageLayouter has successfully started a new page. The
   * start of the new page is delayed, until the first content is printed.
   *
   * @return true, if a new page has already been started, false otherwise.
   */
  public abstract boolean isNewPageStarted();

  /**
   * Save the current state into the LayoutManagerState object. The concrete
   * implementation is responsible to save all required information so that the
   * printing can be continued after the pagebreak is done.
   *
   * @return a valid saveState, never null
   */
  protected abstract LayoutManagerState saveCurrentState();

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
  protected void clearSaveState()
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
  public Object getValue()
  {
    return this;
  }

  /**
   * The dependency level defines the level of execution for this function. Higher dependency
   * functions are executed before lower dependency functions. For ordinary functions and
   * expressions, the range for dependencies is defined to start from 0 (lowest dependency
   * possible) to 2^31 (upper limit of int).
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
  public void setDependencyLevel(final int deplevel)
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
    final PageLayouter l = (PageLayouter) super.clone();
    l.currentEvent = null;
    return l;
  }

  /**
   * Restores the state.
   *
   * @param ancestor  the ancestor state.
   *
   * @throws ReportProcessingException if the printing failed or a pagebreak is
   * requested while the page is restored.
   * @throws IllegalStateException if there is no SavedState but this is not the
   * first page.
   */
  public void restoreSaveState(final ReportState ancestor)
      throws ReportProcessingException
  {
    final Object state = getLayoutManagerState();
    // reset the report finished flag...
    //setStartNewPage(false);
    setGeneratedPageEmpty(true);
    setPageRestartDone(false);

    if (state == null)
    {
      if (ancestor.getCurrentPage() != ReportState.BEFORE_FIRST_PAGE)
      {
        throw new IllegalStateException("State is null, but this is not the first page."
            + ancestor.getCurrentPage());
      }
    }
    // open the logical page ...
    getLogicalPage().open();
  }

  /**
   * Receives notification that a page was canceled by the ReportProcessor.
   * This method is called, when a page was removed from the report after
   * it was generated.
   *
   * @param event The event.
   */
  public void pageCanceled(ReportEvent event)
  {
    // does nothing, we dont care about canceled pages by default..
  }
}
