/*
 * Copyright (c) 1998, 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package com.jrefinery.report.util;

/**
 * Ein einfacher Arbeiter, der die über setWorkload gelieferte Arbeit
 * ausführt und danach wieder schläft.
 *
 */
public class Worker extends Thread
{
  private Runnable workload = null;
  private boolean finish = false;
  private int sleeptime = 0;

  /**
   * creates a new worker.
   *
   * @param sleeptime the time this worker sleeps until he checks for new work.
   */
  public Worker(int sleeptime)
  {
    this.sleeptime = sleeptime;
    start();
  }

  /**
   * creates a new worker with an idle timeout of 2 minutes.
   */
  public Worker()
  {
    this(120000);
  }

  /**
   * set the next workload for this worker.
   */
  public void setWorkload(Runnable r)
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
   * Kills the worker after he completed his work.
   */
  public void finish()
  {
    finish = true;
  }

  /**
   * checks, whether this worker has some work to do.
   */
  public boolean isAvailable()
  {
    return (workload == null);
  }

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
      }
    }
  }
}