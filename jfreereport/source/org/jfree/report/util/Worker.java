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
 * -----------
 * Worker.java
 * -----------
 * (C)opyright 2002, 2003 by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Worker.java,v 1.4 2003/08/25 14:29:34 taqua Exp $
 *
 *
 * Changes
 * -------
 * 05-Feb-2002 : Initial version
 */

package org.jfree.report.util;

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
  private final int sleeptime;

  /**
   * Creates a new worker.
   *
   * @param sleeptime  the time this worker sleeps until he checks for new work.
   */
  public Worker(final int sleeptime)
  {
    this.sleeptime = sleeptime;
    this.setDaemon(true);
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
    Log.debug("Workload set...");
    synchronized (this)
    {
      workload = r;
      Log.debug("Workload assigned: Notified " + getName());
      this.notifyAll();
    }
  }

  /**
   * Kills the worker after he completed his work. Awakens the worker if he's
   * sleeping, so that the worker dies without delay.
   */
  public void finish()
  {
    finish = true;
    // we are evil ..
    try
    {
      this.interrupt();
    }
    catch (SecurityException se)
    {
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
//      else
//      {
//        Log.debug ("Nothing to do ...");
//      }
//
//      Log.debug ("Sleeping ..." + this.getName());
      synchronized (this)
      {
        try
        {
          // remove lock
          this.wait(sleeptime);
        }
        catch (InterruptedException ie)
        {
          // ignored
//          Log.debug ("Interrupted ..." + getName());
        }
      }
//      Log.debug ("Wakeup ..." + getName());
    }
  }


//  private static class HeavyWorkLoad implements Runnable
//  {
//    static int task;
//    char test;
//
//    public HeavyWorkLoad(char t)
//    {
//      test = t;
//    }
//
//    public void run()
//    {
//      synchronized (HeavyWorkLoad.class)
//      {
//        try
//        {
//          System.out.println ("HeavyWorkLoad Start...  " + test);
//          for (int i = 0; i < 100; i++ )
//          {
//            System.out.print (test);
//            System.out.flush();
//            Thread.sleep(100);
//            task = i;
//          }
//          System.out.println ("HeavyWorkLoad Finish...");
//        }
//        catch (Exception e)
//        {
//          System.out.println ("HeavyWorkLoad Failed...");
//        }
//      }
//    }
//  }
//
//  public static void main(String[] args)
//  {
//    HeavyWorkLoad wl1 = new HeavyWorkLoad('.');
//    HeavyWorkLoad wl2 = new HeavyWorkLoad('+');
//    Worker worker = new Worker(10000);
//    worker.setWorkload(wl1);
//    wl2.run();
//    while (worker.isAvailable() == false)
//    {
//      try
//      {
//        System.out.println ("1Waiting ...");
//        synchronized (worker)
//        {
//          worker.wait(1000);
//        }
//      }
//      catch (InterruptedException ie)
//      {
//        System.out.println ("1Int ...");
//      }
//    }
//    System.out.println ("Finishing ...");
//    worker.finish();
//    System.out.flush();
//  }

}