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
 * $Id: ExportTask.java,v 1.2 2003/08/25 14:29:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

public abstract class ExportTask implements Runnable
{
  public static final int RETURN_SUCCESS = 0;
  public static final int RETURN_FAILED = 1;
  public static final int RETURN_ABORT = 2;

  private int returnValue;
  private boolean taskDone;
  private Exception exception;

  public ExportTask()
  {
  }

  public int getReturnValue()
  {
    return returnValue;
  }

  public void setReturnValue(final int returnValue)
  {
    this.returnValue = returnValue;
  }

  public Exception getException()
  {
    return exception;
  }

  public void setException(final Exception exception)
  {
    this.exception = exception;
  }

  public synchronized boolean isTaskDone()
  {
    return taskDone;
  }

  protected synchronized void setTaskDone(final boolean taskDone)
  {
    this.taskDone = taskDone;
  }
}
