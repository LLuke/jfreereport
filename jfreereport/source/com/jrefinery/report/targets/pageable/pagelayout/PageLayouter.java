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
 * $Id: PageLayouter.java,v 1.2 2002/12/02 20:43:17 taqua Exp $
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
  protected static abstract class LayoutManagerState implements Cloneable
  {
    public LayoutManagerState ()
    {
    }
  }

  private class CloneCarrier
  {
    // dont clone these
    public JFreeReport report;
    private LogicalPage logicalPage;
  }

  private LayoutManagerState layoutManagerState;
  // handles the layout
  private ReportEvent currentEvent;
  private boolean pageEnded;
  private CloneCarrier cloneCarrier;
  private int depLevel;

  private boolean finishingPage;
  private boolean restartingPage;

  public PageLayouter()
  {
    setDepencyLevel(-1);
    cloneCarrier = new CloneCarrier();
  }

  public void setLogicalPage(LogicalPage logicalPage)
  {
    if (logicalPage == null) throw new NullPointerException();
    this.cloneCarrier.logicalPage = logicalPage;
  }

  public LogicalPage getLogicalPage ()
  {
    return cloneCarrier.logicalPage;
  }

  public boolean isFinishingPage()
  {
    return finishingPage;
  }

  public void setFinishingPage(boolean finishingPage)
  {
    this.finishingPage = finishingPage;
  }

  public boolean isRestartingPage()
  {
    return restartingPage;
  }

  public void setRestartingPage(boolean restartingPage)
  {
    this.restartingPage = restartingPage;
  }

  public JFreeReport getReport()
  {
    return cloneCarrier.report;
  }

  protected void setReport(JFreeReport report)
  {
    if (report == null) throw new NullPointerException();
    this.cloneCarrier.report = report;
  }

  protected ReportEvent getCurrentEvent()
  {
    return currentEvent;
  }

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

  protected void endPage () throws ReportProcessingException
  {
    if (isRestartingPage()) throw new ReportProcessingException ("Report does not proceed");
    setFinishingPage(true);
    ReportState cEventState = getCurrentEvent().getState();
    cEventState.firePageFinishedEvent();
    pageEnded = true;
    cEventState.nextPage();
    setFinishingPage(false);
    // log // no cloning save the orignal state
    // Log.debug ("LayoutManagerState: " + layoutManagerState);
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
   * strucures contained in objects, you have to overwrite this function.
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
