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
 * -----------
 * Worker.java
 * -----------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Worker.java,v 1.7 2003/06/27 14:25:26 taqua Exp $
 *
 *
 * Changes
 * -------
 * 05-Feb-2002 : Initial version
 */

package com.jrefinery.report.util;

/**
 * A simple worker implementation.
 * The worker executes a assigned workload and then sleeps until
 * another workload is set or the worker is killed.
 *
 * @author Thomas Morgner
 */
public class Worker extends Thread
{
  /** the worker's task. */
  private Runnable workload = null;

  /** a flag whether the worker should exit after the processing. */
  private boolean finish = false;

  /** the time in milliseconds beween 2 checks for exit or work requests. */
  private int sleeptime;

  /**
   * Creates a new worker.
   *
   * @param sleeptime  the time this worker sleeps until he checks for new work.
   */
  public Worker(final int sleeptime)
  {
    this.sleeptime = sleeptime;
    start();
  }

  /**
   * Creates a new worker with an default idle timeout of 2 minutes.
   */
  public Worker()
  {
    this(120000);
  }

  /**
   * Set the next workload for this worker.
   *
   * @param r  the next workload for the worker.
   *
   * @throws IllegalStateException if the worker is not idle.
   */
  public void setWorkload(final Runnable r)
  {
    if (workload != null)
    {
      throw new IllegalStateException("This worker is not idle.");
    }
    workload = r;
    synchronized (this)
    {
      this.notify();
    }
  }

  /**
   * Kills the worker after he completed his work. Awakens the worker if he's
   * sleeping, so that the worker dies without delay.
   */
  public void finish()
  {
    finish = true;
    synchronized (this)
    {
      this.notify();
    }
  }

  /**
   * Checks, whether this worker has some work to do.
   *
   * @return true, if this worker has no more work and is currently sleeping.
   */
  public boolean isAvailable()
  {
    return (workload == null);
  }

  /**
   * If a workload is set, process it. After the workload is processed,
   * this worker starts to sleep until a new workload is set for the worker
   * or the worker got the finish() request.
   */
  public synchronized void run()
  {
    while (!finish)
    {
      if (workload != null)
      {
        try
        {
          workload.run();
        }
        catch (Exception e)
        {
          Log.error("Worker caught exception on run: ", e);
        }
        workload = null;

      }

      try
      {
        this.wait(sleeptime);
      }
      catch (InterruptedException ie)
      {
        // ignored
      }
    }
  }
}