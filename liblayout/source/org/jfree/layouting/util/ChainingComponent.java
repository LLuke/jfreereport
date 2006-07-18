/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ChainingComponent.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.util;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.util.Log;

/**
 * A chaining component accepts calls from outside, forwards them to its
 * wrapped object, and records all calls to the next chain element. Only
 * after the initial call has been fully completed (and thus the wrapped
 * object is back in a consistent state) all generated sub-calls will be
 * forwarded to the next chain element.
 *
 * Of course, the whole architecture assumes, that the execution flow is a
 * one-way street and that the execution and computation of the n-th step does
 * not rely on results and/or the current state of the n+1-th step.
 *
 * It is guaranteed, that all calls are executed in the same order they have
 * been recorded.
 *
 * @author Thomas Morgner
 */
public abstract class ChainingComponent
{
  public static final int STATE_FRESH = 0;
  public static final int STATE_ERROR = 1;
  public static final int STATE_DONE = 2;

  public static class RecordedCall
  {
    private int methodId;
    private Object parameters;
    private int state;

    public RecordedCall(final int method, final Object parameters)
    {
      this.methodId = method;
      this.parameters = parameters;
    }

    public int getState()
    {
      return state;
    }

    public void setState(final int state)
    {
      this.state = state;
    }

    public int getMethod()
    {
      return methodId;
    }

    public Object getParameters()
    {
      return parameters;
    }
  }

  private ArrayList calls;

  public ChainingComponent()
  {
    calls = new ArrayList();
  }

  public void addCall (RecordedCall c)
  {
    calls.add(c);
  }

  public void clear ()
  {
    calls.clear();
  }

  protected RecordedCall[] getRecordedCalls()
  {
    return  (RecordedCall[]) calls.toArray(new RecordedCall[calls.size()]);
  }

  public void setRecordedCalls (RecordedCall[] recordedCalls)
  {
    calls.addAll(Arrays.asList(recordedCalls));
  }

  public synchronized void replay ()
  {
    replay(this);
  }

  public synchronized void replay (Object target)
  {
    final RecordedCall[] recordedCalls = getRecordedCalls();

//    Log.debug ("REPLAY:"  + this + " : " + recordedCalls.length);
    for (int i = 0; i < recordedCalls.length; i++)
    {
      final RecordedCall call = recordedCalls[i];
      if (call.getState() == STATE_DONE)
      {
        calls.remove(0);
      }

      try
      {
//        Log.debug ("Invoking: " + target.toString() + " " + call.getMethod() + " " + call.getParameters());
        invoke(target, call.getMethod(), call.getParameters());
        call.setState(STATE_DONE);
        calls.remove(0);
      }
      catch(Exception e)
      {
        Log.debug ("Error: ", e);
        call.setState(STATE_ERROR);
        break;
      }
    }
  }

  protected abstract void invoke (Object target, int methodId, Object parameters)
          throws Exception;
}