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
 * ------------------------------
 * ExportTask.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExportTask.java,v 1.6 2003/11/01 19:52:27 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

import java.util.ArrayList;

import org.jfree.report.util.Log;

/**
 * The export task provides a basic facility to execute an export task
 * in an asynchronous worker thread.
 * <p>
 * These task objects are one-time objects and should (and hopefully cannot)
 * not be reused.
 *  
 * @author Thomas Morgner
 */
public abstract class ExportTask implements Runnable
{
  /** A public constant that indicates that the export is not yet complete. */
  public static final int RETURN_PENDING = -1;
  /** A public constant that indicates a successfull export. */
  public static final int RETURN_SUCCESS = 0;
  /** A public constant that indicates a failed export. */ 
  public static final int RETURN_FAILED = 1;
  /** A public constant that indicates an aborted export. */ 
  public static final int RETURN_ABORT = 2;

  /** The return value for this task. */
  private int returnValue;
  /** a flag that indicates, whether this task is done. */
  private boolean taskDone;
  /** stores a possible exception that caused the task to fail. */
  private Exception exception;
  /** A list of task listeners. */
  private final ArrayList listeners;

  /**
   * DefaultConstructor.
   */
  public ExportTask()
  {
    returnValue = RETURN_PENDING;
    listeners = new ArrayList();
  }

  /**
   * Returns the return value of the task. The caller should test,
   * whether the task is done, before querying the result.
   *  
   * @return the execution result of the task.
   */
  public int getReturnValue()
  {
    return returnValue;
  }

  /**
   * Returns the exception that caused the task to fail, or null
   * if not set or known.
   * 
   * @return the exception or null, if there is no exception.
   */
  public Exception getException()
  {
    return exception;
  }

  /**
   * Checks, whether the task is completed.
   * 
   * @return true, if the task is done, false otherwise.
   */
  public synchronized boolean isTaskDone()
  {
    return taskDone;
  }

  /**
   * Call this method to signal, that the task is completed and the
   * result is computed and all state information is stored.
   * <p>
   * This will set the return value to Sucess and fires the taskDoneEvent.
   */
  protected synchronized void setTaskDone()
  {
    this.taskDone = true;
    this.returnValue = RETURN_SUCCESS;
    fireExportDone();
  }

  /**
   * Call this method to signal, that the task was aborted by the user or
   * the system.
   * <p>
   * This will set the return value to Abort and fires the taskAborted Event.
   */
  protected synchronized void setTaskAborted()
  {
    this.taskDone = true;
    this.returnValue = RETURN_ABORT;
    fireExportAborted();
  }

  /**
   * Call this method to signal, that an error occured while trying to complete
   * the task.
   * <p>
   * This will set the return value to Sucess and fires the taskFailed Event.
   *
   * @param ex the optional exception that caused the trouble.
   */
  protected synchronized void setTaskFailed(final Exception ex)
  {
    this.taskDone = true;
    this.returnValue = RETURN_FAILED;
    this.exception = ex;
    fireExportFailed();
  }

  /**
   * Adds an export task listener to this task. The listener will be informed
   * when the task is either aborted or finished.
   * 
   * @param listener the task listener to be added.
   */
  public void addExportTaskListener (final ExportTaskListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException();
    }
    listeners.add (listener);
  }

  /**
   * Removes an export task listener to this task. 
   * 
   * @param listener the task listener to be removed.
   */
  public void removeExportTaskListener (final ExportTaskListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException();
    }
    listeners.remove (listener);
  }

  /**
   * Informs all registered listeners, that the report was finished successfully. 
   */
  protected void fireExportDone ()
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      final ExportTaskListener tl = (ExportTaskListener) listeners.get(i);
      tl.taskDone(this);
    }
  }

  /**
   * Informs all registered listeners, that the report was aborted due to 
   * unexpected errors. 
   */
  protected void fireExportFailed ()
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      final ExportTaskListener tl = (ExportTaskListener) listeners.get(i);
      tl.taskFailed(this);
    }
  }

  /**
   * Informs all registered listeners, that the report processing was aborted
   * by the user. 
   */
  protected void fireExportAborted ()
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      final ExportTaskListener tl = (ExportTaskListener) listeners.get(i);
      tl.taskAborted(this);
    }
  }

  /**
   * Remove all listeners and prepare the finalization.
   */
  protected void dispose ()
  {
    listeners.clear();
    exception = null;
  }

  /**
   * Starts the export by calling performExport. If perfomExport causes
   * Exceptions, the task will be marked as failed. 
   *  
   * @see java.lang.Runnable#run()
   */
  public final void run ()
  {
    try
    {
      performExport();
    }
    catch (Exception e)
    {
      Log.debug ("ExportTask.run(): Caught exception while exporting...", e);
      setTaskFailed(e);
    }
    dispose();
  }

  /**
   * Performs the export.
   */
  protected abstract void performExport ();
}
