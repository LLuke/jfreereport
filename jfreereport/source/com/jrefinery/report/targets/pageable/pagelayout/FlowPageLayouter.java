/**
 * Date: Dec 1, 2002
 * Time: 5:20:48 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.pagelayout;

import com.jrefinery.report.targets.pageable.pagelayout.PageLayouter;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.FunctionInitializeException;

import java.util.Stack;
import java.util.ArrayList;

public class FlowPageLayouter extends PageLayouter
{
  private Stack tasks;
  private ArrayList taskWorker;

  public FlowPageLayouter()
  {
    tasks = new Stack();
    taskWorker = new ArrayList ();
  }

  public LayoutTask getTaskForEvent (ReportEvent event)
  {
    LayoutTask task = new LayoutTask();
//    task.setEvent (event);
    tasks.push(task);
    return task;
  }

  public void endTask (LayoutTask task)
  {
    tasks.pop();
  }

  public void performLayout (ReportEvent event)
  {
    LayoutTask task = getTaskForEvent(event);

    boolean isProceeding = false;
    do
    {
      for (int i = 0; i < taskWorker.size(); i++)
      {
        LayoutAgent agent = (LayoutAgent) taskWorker.get (i);
        LayoutAgentProgress progress = agent.processTask(task);
        if (progress == LayoutAgentProgress.PROCESSING_COMPLETE)
        {
          isProceeding = true;
        }
      }
    }
    while (isProceeding);

    if (task.isDone ())
    {
      endTask (task);
    }
  }

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
   * @param state
   */
  public void restoreSaveState(ReportState state)
      throws ReportProcessingException
  {
  }

  /**
   * A detector whether the last pagebreak was a manual pagebreak or an automatic one
   *
   * @return
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
   * @throws com.jrefinery.report.function.FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
  }


}
