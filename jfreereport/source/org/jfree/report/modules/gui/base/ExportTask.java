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
 * $Id: ExportTask.java,v 1.3 2003/08/31 21:05:54 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23-Aug-2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

/**
 * The export task provides a basic facility to execute an export task
 * in an asynchronous worker thread.
 *  
 * @author Thomas Morgner
 */
public abstract class ExportTask implements Runnable
{
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

  /**
   * DefaultConstructor.
   */
  public ExportTask()
  {
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
   * Defines the return value for this task. This should only be called
   * from ExportTask instances.
   *  
   * @param returnValue the new return value.
   */
  protected void setReturnValue(final int returnValue)
  {
    this.returnValue = returnValue;
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
   * Defines the exception that caused the task to fail, or set to null
   * if not applicable or known.
   * 
   * @param exception the exception or null, if there is no exception.
   */
  protected void setException(final Exception exception)
  {
    this.exception = exception;
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
   * Defines, whether the task is completed.
   * 
   * @param taskDone true, if the task is done, false otherwise.
   */
  protected synchronized void setTaskDone(final boolean taskDone)
  {
    this.taskDone = taskDone;
  }
}
