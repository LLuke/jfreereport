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
 * ---------------------
 * FlowPageLayouter.java
 * ---------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FlowPageLayouter.java,v 1.10 2003/06/27 14:25:24 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 *
 */
package com.jrefinery.report.targets.pageable.pagelayout;

import java.util.ArrayList;
import java.util.Stack;

import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.states.ReportState;

/**
 * A page layouter.  No real implementation ... next release ..
 *
 * @author Thomas Morgner.
 */
public class FlowPageLayouter extends PageLayouter
{
  /** Tasks. */
  private Stack tasks;

  /** ??. */
  private ArrayList taskWorker;

  /**
   * Creates a new page layouter.
   */
  public FlowPageLayouter()
  {
    tasks = new Stack();
    taskWorker = new ArrayList();
  }

  /**
   * Gets a task for an event.
   *
   * @param event  the event.
   *
   * @return the layout task.
   */
  public LayoutTask getTaskForEvent(final ReportEvent event)
  {
    final LayoutTask task = new LayoutTask();
    tasks.push(task);
    return task;
  }

  /**
   * Ends a task.
   *
   * @param task  the task.
   */
  public void endTask(final LayoutTask task)
  {
    tasks.pop();
  }

  /**
   * Performs layout.
   *
   * @param event  the event.
   */
  public void performLayout(final ReportEvent event)
  {
    final LayoutTask task = getTaskForEvent(event);

    boolean isProceeding = false;
    do
    {
      for (int i = 0; i < taskWorker.size(); i++)
      {
        final LayoutAgent agent = (LayoutAgent) taskWorker.get(i);
        final LayoutAgentProgress progress = agent.processTask(task);
        if (progress == LayoutAgentProgress.PROCESSING_COMPLETE)
        {
          isProceeding = true;
        }
      }
    } while (isProceeding);

    if (task.isDone())
    {
      endTask(task);
    }
  }

  /**
   * Saves the current state.
   *
   * @return <code>null</code>.
   */
  protected PageLayouter.LayoutManagerState saveCurrentState()
  {
    return null;
  }

  /**
   * Restores the layoutmanager state by using this state as new base for
   * processing. This state must be an clone of the last report state for
   * the previous page or the original last report state - or the processing
   * will print stupid things.
   *
   * @param state  the state.
   *
   * @throws ReportProcessingException never.
   */
  public void restoreSaveState(final ReportState state)
      throws ReportProcessingException
  {
  }

  /**
   * A detector whether the last pagebreak was a manual pagebreak or an automatic one.
   *
   * @return true or false.
   */
  public boolean isManualPageBreak()
  {
    return false;
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
  }

  /**
   * Returns <code>false</code>.
   *
   * @return <code>false</code>.
   */
  public boolean isNewPageStarted()
  {
    return false;
  }

}
