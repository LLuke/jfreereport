/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * Changes
 * -------
 * 31-May-2002 : Initial version
 * 09-Jun-2002 : Documentation
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.WeakReferenceList;

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
    private int pos;
    private ReportStateList master;

    public MasterList (ReportStateList list)
    {
      this.master = list;
    }

    /**
     * function to restore the state of an child after the child was garbage collected.
     */
    protected Object restoreChild (int index)
    {
      ReportState master = (ReportState) getMaster ();
      if (master == null)
      {
        return null;
      }
      int max = getChildPos (index);
      Log.debug ("Position " + index + "(" + max + ") was lost, restoring it");
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
     * returns the number of children in the list. This value is a reference to the constant
     * MASTERPOSITIONS and is defaulted to 10.
     */
    protected int getMaxChildCount ()
    {
      return MASTERPOSITIONS;
    }

    /**
     * Internal handler function restore a state. Count denotes the number of pages required
     * to be processed to restore the page, when the reportstate master is used as source element.
     */
    protected ReportState restoreState (int count, ReportState rootstate)
            throws ReportProcessingException
    {
      if (rootstate == null) throw new NullPointerException ("Master is null");
      ReportState state = rootstate;

      for (int i = 0; i <= count; i++)
      {
        ReportState oldState = state;
        state = master.report.processPage (master.target, state, false);
        set(state, i + 1);
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
  }

  /**
   * The list of master states. This is a list of WeakReferenceLists. These WeakReferenceLists
   * contain their master state as first child
   */
  private List masterStates;

  /**
   * The number of elements in this list
   */
  private int _size;

  /**
   * the report used to create this list is also used to restore garbage collected states.
   */
  private JFreeReport report;

  /**
   * the output target that was used to create the list.
   */
  private OutputTarget target;

  /**
   * returns the index of the WeakReferenceList in the master list.
   */
  private int getMasterPos (int pos)
  {
    return (int) Math.floor (pos / MASTERPOSITIONS);
  }

  /**
   * creates a new reportstatelist. The list will be filled using the specified report
   * and output target. Filling of the list is done elsewhere.
   *
   * @throws NullPointerException if one or both of report or outputtarget are null
   */
  public ReportStateList (JFreeReport report, OutputTarget out)
  {
    if (report == null) throw new NullPointerException ("Report null");
    if (out == null) throw new NullPointerException ("outputtarget null");
    this.report = report;
    this.target = out;
    masterStates = new LinkedList ();
  }

  /**
   * returns the number of elements in this list
   */
  public int size ()
  {
    return _size;
  }

  /**
   * Adds this report state to the end of the list.
   */
  public void add (ReportState state)
  {
    if (state == null) throw new NullPointerException();

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
    _size++;
  }

  /**
   * removes all elements in the list
   */
  public void clear ()
  {
    masterStates.clear ();
    _size = 0;
  }

  /**
   * retrieves the element on position <code>index</code> in this list.
   */
  public ReportState get (int index)
  {
    if (index >= size () || index < 0)
      throw new IndexOutOfBoundsException ();
    MasterList master = (MasterList) masterStates.get (getMasterPos (index));
    return (ReportState) master.get (index);
  }
}