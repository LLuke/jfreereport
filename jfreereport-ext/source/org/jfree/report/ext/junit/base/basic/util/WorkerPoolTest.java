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
 * ------------------------------
 * WorkerPoolTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 18.10.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.util;

import junit.framework.TestCase;
import org.jfree.report.util.WorkerHandle;
import org.jfree.report.util.WorkerPool;
import org.jfree.report.util.Log;
import org.jfree.report.Boot;

public class WorkerPoolTest extends TestCase
{
  private class TestRunner implements Runnable
  {
    private Object[] content;

    public TestRunner(Object[] content)
    {
      if (content.length != 1)
      {
        throw new IllegalArgumentException();
      }
      this.content = content;
    }

    public void run()
    {
      while (content[0] == null)
      {
        try
        {
          Thread.sleep(200);
        }
        catch (InterruptedException ie)
        {
        }
      }
      Log.debug ("Finished: " + content[0]);
    }
  }

  private class GetWorker implements Runnable
  {
    private WorkerPool pool;

    public GetWorker(WorkerPool pool)
    {
      this.pool = pool;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     Thread#run()
     */
    public void run()
    {
      Object[] content = new Object[1];
      Log.debug ("Try to get next worker");
      assertNotNull(pool.getWorkerForWorkload(new TestRunner(content)));
      content[0] = Boolean.FALSE;
      Log.debug ("Got next worker");
    }
  }

  public WorkerPoolTest()
  {
  }

  public WorkerPoolTest(String s)
  {
    super(s);
  }

  public void testBasicPool ()
  {
    WorkerPool pool = new WorkerPool(1);
    assertTrue(pool.isWorkerAvailable());
    Object[] content = new Object[1];

    WorkerHandle handle = pool.getWorkerForWorkload(new TestRunner(content));
    assertNotNull(handle);
    assertFalse(pool.isWorkerAvailable());
    try
    {
      Thread.sleep(500);
    }
    catch (InterruptedException ie)
    {
    }
    content[0] = new Boolean(true);
    try
    {
      Thread.sleep(500);
    }
    catch (InterruptedException ie)
    {
    }
    assertTrue(pool.isWorkerAvailable());
  }

  public void testBasicPool2 ()
  {
    Boot.start();
    WorkerPool pool = new WorkerPool(1);
    assertTrue(pool.isWorkerAvailable());
    Object[] content = new Object[1];

    WorkerHandle handle = pool.getWorkerForWorkload(new TestRunner(content));
    assertNotNull(handle);
    assertFalse(pool.isWorkerAvailable());
    Thread t = new Thread(new GetWorker(pool));
    t.start();

    try
    {
      Thread.sleep(5000);
    }
    catch (InterruptedException ie)
    {
    }
    content[0] = new Boolean(true);
    try
    {
      Thread.sleep(500);
    }
    catch (InterruptedException ie)
    {
    }
    assertTrue(pool.isWorkerAvailable());
  }
}
