/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * --------------------
 * ReportStateList.java
 * --------------------
 *
 * $Id: ReportStateList.java,v 1.10 2002/11/07 21:45:20 taqua Exp $
 *
 * Changes
 * -------
 * 31-May-2002 : Initial version
 * 09-Jun-2002 : Documentation
 * 10-Jul-2002 : additional checks for method-input-parameters
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.WeakReferenceList;
import com.jrefinery.report.states.ReportState;

import java.util.LinkedList;
import java.util.List;

/**
 * The ReportState list stores a report states for the beginning of every page.
 * The list is filled on repagination and read when a report or a page of the report
 * is printed.
 * <p>
 * Important: This list stores page start report states, not arbitary report states.
 * These ReportStates are special: they can be reproduced by calling processPage on the report.
 * <p>
 * Internally this list is organized as a list of WeakReferenceLists, where every WeakReferenceList
 * stores a certain number of page states.
 *
 * @author TM
 */
public class ReportStateList
{
  /**
   * The position of the master element in the list. A greater value will reduce the
   * not-freeable memory used by the list, but restoring a single page will require more
   * time.
   */
  private static final int MASTERPOSITIONS = 10;

  /**
   * Internal WeakReferenceList that is capable to restore its elements. The elements in
   * this list are page start report states.
   */
  private static class MasterList extends WeakReferenceList
  {
    /** The position. */
    private int pos;

    /** The master list. */
    private ReportStateList master;

    /**
     * Creates a new master list.
     *
     * @param list  the list.
     */
    public MasterList (ReportStateList list)
    {
      this.master = list;
    }

    /**
     * Function to restore the state of a child after the child was garbage collected.
     *
     * @param index  the index.
     *
     * @return ??
     */
    protected Object restoreChild (int index)
    {
      ReportState master = (ReportState) getMaster ();
      if (master == null)
      {
        return null;
      }
      int max = getChildPos (index);
      Log.debug ("Position " + index + "(" + max + ") was lost, restoring it.");
      try
      {
        return this.restoreState (max, master);
      }
      catch (ReportProcessingException rpe)
      {
        return null;
      }
    }

    /**
     * Returns the number of children in the list. This value is a reference to the constant
     * MASTERPOSITIONS and is defaulted to 10.
     *
     * @return the maximum number of children in the list.
     */
    protected int getMaxChildCount ()
    {
      return MASTERPOSITIONS;
    }

    /**
     * Internal handler function restore a state. Count denotes the number of pages required
     * to be processed to restore the page, when the reportstate master is used as source element.
     *
     * @param count  the count.
     * @param rootstate  the root state.
     *
     * @return the report state.
     *
     * @throws ReportProcessingException if there was a problem processing the report.
     */
    protected ReportState restoreState (int count, ReportState rootstate)
            throws ReportProcessingException
    {
      if (rootstate == null)
      {
        throw new NullPointerException ("Master is null");
      }
      ReportState state = rootstate;

      OutputTarget ot = master.target.createDummyWriter();
      try
      {
        ot.open();

        for (int i = 0; i <= count; i++)
        {
          ReportState oldState = state;
          Log.debug("o-State: " + state.getClass());
          state = master.report.processPage (ot, state, true);
          set (state, i + 1);
          Log.debug("n-State: " + state.getClass());
          if (state.isFinish ())
          {
            return state;
          }
          if (state.isProceeding (oldState) == false)
          {
            return null;
          }
        }
        return state;
      }
      catch (OutputTargetException ote)
      {
        Log.debug ("OTE :" + ote);
        throw new ReportProcessingException(ote.getMessage());
      }
    }
  }

  /**
   * The list of master states. This is a list of WeakReferenceLists. These WeakReferenceLists
   * contain their master state as first child
   */
  private List masterStates;

  /**
   * The number of elements in this list
   */
  private int size;

  /**
   * the report used to create this list is also used to restore garbage collected states.
   */
  private JFreeReport report;

  /**
   * the output target that was used to create the list.
   */
  private OutputTarget target;

  /**
   * Returns the index of the WeakReferenceList in the master list.
   *
   * @param pos  the position.
   *
   * @return ??
   */
  private int getMasterPos (int pos)
  {
    return (int) Math.floor (pos / MASTERPOSITIONS);
  }

  /**
   * Creates a new reportstatelist. The list will be filled using the specified report
   * and output target. Filling of the list is done elsewhere.
   *
   * @param report  the report
   * @param out  the output target.
   *
   * @throws NullPointerException if one or both of report or outputtarget are null
   */
  public ReportStateList (JFreeReport report, OutputTarget out)
  {
    if (report == null)
    {
      throw new NullPointerException ("Report null");
    }
    if (out == null)
    {
      throw new NullPointerException ("outputtarget null");
    }
    this.report = report;
    this.target = out;
    masterStates = new LinkedList ();
  }

  /**
   * Returns the number of elements in this list.
   *
   * @return the number of elements in the list.
   */
  public int size ()
  {
    return this.size;
  }

  /**
   * Adds this report state to the end of the list.
   *
   * @param state  the report state.
   */
  public void add (ReportState state)
  {
    if (state == null)
    {
      throw new NullPointerException ();
    }

    if (state.isFinish())
      throw new IllegalArgumentException();

    MasterList master = null;
    if (getMasterPos (size ()) >= masterStates.size ())
    {
      master = new MasterList (this);
      master.pos = size ();
      masterStates.add (master);
    }
    else
    {
      master = (MasterList) masterStates.get (getMasterPos (size ()));
    }
    master.add (state);
    this.size++;
  }

  /**
   * Removes all elements in the list
   */
  public void clear ()
  {
    masterStates.clear ();
    this.size = 0;
  }

  /**
   * Retrieves the element on position <code>index</code> in this list.
   *
   * @param index  the index.
   *
   * @return the report state.
   */
  public ReportState get (int index)
  {
    if (index >= size () || index < 0)
    {
      throw new IndexOutOfBoundsException ();
    }
    MasterList master = (MasterList) masterStates.get (getMasterPos (index));
    return (ReportState) master.get (index);
  }
}