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
 * $Id: PageLayouter.java,v 1.4 2002/12/05 16:49:44 mungady Exp $
 *
 * Changes
 * -------
 * 04-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.pagelayout;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.pageable.physicals.LayoutPrepareFunction;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.util.Log;

/**
 * An abstract base class that is used to lay out a report.
 * 
 * @author Thomas Morgner
 */
public abstract class PageLayouter extends AbstractFunction
{
  /**
   * Represents the state of the page layouter. 
   */
  protected static abstract class LayoutManagerState implements Cloneable
  {
    /**
     * Creates a new state.
     */
    public LayoutManagerState ()
    {
    }
  }

  /**
   * A clone carrier.
   */
  private class CloneCarrier
  {
    // dont clone these
    public JFreeReport report;
    
    /** The logical page. */
    private LogicalPage logicalPage;
  }

  /** The current state. */
  private LayoutManagerState layoutManagerState;
  
  /** The latest report event. */
  private ReportEvent currentEvent;
  
  /** The 'page-ended' flag. */
  private boolean pageEnded;
  
  /** A clone carrier. */
  private CloneCarrier cloneCarrier;
  
  /** The dep level. */
  private int depLevel;

  /** The 'finishing page' flag. */
  private boolean finishingPage;
  
  /** The 'restarting page' flag. */
  private boolean restartingPage;

  /**
   * Creates a new page layouter.
   */
  public PageLayouter()
  {
    setDepencyLevel(-1);
    cloneCarrier = new CloneCarrier();
  }

  /**
   * Sets the logical page for the layouter.
   *
   * @param logicalPage  the logical page (null not permitted).
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
   *
   * @return true or false.
   */
  public boolean isFinishingPage()
  {
    return finishingPage;
  }

  /**
   * Sets the 'finishing page' flag.
   *
   * @param finishingPage  the new flag value.
   */
  public void setFinishingPage(boolean finishingPage)
  {
    this.finishingPage = finishingPage;
  }

  /**
   * Returns the 'restarting page' flag.
   *
   * @return true or false.
   */
  public boolean isRestartingPage()
  {
    return restartingPage;
  }

  /**
   * Sets the 'restarting page' flag.
   *
   * @param restartingPage  sets the restarting page flag.
   */
  public void setRestartingPage(boolean restartingPage)
  {
    this.restartingPage = restartingPage;
  }

  /**
   * Returns the report.
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
   */
  protected void setReport(JFreeReport report)
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
    else
    {
      setReport(null);
    }
  }

  /**
   * Ends a page.
   */
  protected void endPage () throws ReportProcessingException
  {
    if (isRestartingPage()) 
    {
      throw new ReportProcessingException ("Report does not proceed");
    }
    setFinishingPage(true);
    ReportState cEventState = getCurrentEvent().getState();
    cEventState.firePageFinishedEvent();
    pageEnded = true;
    cEventState.nextPage();
    setFinishingPage(false);
    // log // no cloning save the orignal state
    layoutManagerState = saveCurrentState();

    getLogicalPage().close();
  }

  protected void startPage (ReportState state)
  {
    if (state == null) throw new NullPointerException();

    getLogicalPage().open();
    setRestartingPage(true);
    pageEnded = false;
    state.firePageStartedEvent();

    setRestartingPage(false);
  }

  public boolean isPageEnded()
  {
    return pageEnded;
  }

  protected abstract LayoutManagerState saveCurrentState ();

  public LayoutManagerState getLayoutManagerState()
  {
    return layoutManagerState;
  }

  /**
   * Restores the layoutmanager state by using this state as new base for
   * processing. This state must be an clone of the last report state for
   * the previous page or the original last report state - or the processing
   * will print stupid things.
   *
   * @param state
   */
  public abstract void restoreSaveState (ReportState state)
      throws ReportProcessingException;


  protected void clearSaveState ()
  {
    layoutManagerState = null;
  }

  public Object getValue ()
  {
    return this;
  }

  /**
   * The depency level defines the level of execution for this function. Higher depency functions are
   * executed before lower depency functions. The range for depencies is defined to start from 0 (lowest
   * depency possible) to 2^31 (upper limit of int).
   */
  public int getDepencyLevel()
  {
    return depLevel;
  }

  public void setDepencyLevel(int deplevel)
  {
    this.depLevel = deplevel;
  }

  /**
   * Returns the 'layout prepare' function.
   * <p>
   * This method returns <code>null</code>.
   *
   * @return null.
   */
  public LayoutPrepareFunction getPrepareLayoutFunction ()
  {
    return null;
  }

  /**
   * Returns a clone of the PageLayouter.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * structures contained in objects, you have to overwrite this function.
   *
   * @return a clone of the function.
   *
   * @throws java.lang.CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * A detector whether the last pagebreak was a manual pagebreak or an automatic one.
   *
   * @return true or false.
   */
  public abstract boolean isManualPageBreak ();
}
