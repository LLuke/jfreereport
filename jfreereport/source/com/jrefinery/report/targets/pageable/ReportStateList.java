/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * $Id: ReportStateList.java,v 1.6 2002/12/12 12:26:57 mungady Exp $
 *
 * Changes
 * -------
 * 31-May-2002 : Initial version
 * 09-Jun-2002 : Documentation
 * 10-Jul-2002 : additional checks for method-input-parameters
 */
package com.jrefinery.report.targets.pageable;

import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.util.WeakReferenceList;

import java.util.ArrayList;

/**
 * The ReportState list stores a report states for the beginning of every page.
 * The list is filled on repagination and read when a report or a page of the report
 * is printed.
 * <p>
 * Important: This list stores page start report states, not arbitary report states.
 * These ReportStates are special: they can be reproduced by calling processPage on the report.
 * <p>
 * Internally this list is organized as a list of WeakReferenceLists, where every WeakReferenceList
 * stores a certain number of page states. The first 20 states are stored in an ordinary
 * list with strong-references, so these states never get GarbageCollected (and so
 * they must never be restored by reprocessing them). The next 100 states are stored
 * in 4-element ReferenceLists, so if a reference is lost, only 4 states have to be
 * reprocessed. All other states are stored in 10-element lists.
 *
 * @author Thomas Morgner
 */
public class ReportStateList
{
  /**
   * The position of the master element in the list. A greater value will reduce the
   * not-freeable memory used by the list, but restoring a single page will require more
   * time.
   */

  /** The maxmimum masterposition size. */
  private static final int MASTERPOSITIONS_MAX = 10;

  /** The medium masterposition size. */
  private static final int MASTERPOSITIONS_MED = 4;

  /** The max index that will be stored in the primary list. */
  private static final int PRIMARY_MAX = 20;

  /** The max index that will be stored in the master4 list. */
  private static final int MASTER4_MAX = 120;

  /**
   * Internal WeakReferenceList that is capable to restore its elements. The elements in
   * this list are page start report states.
   */
  private static class MasterList extends WeakReferenceList
  {
    /** The master list. */
    private ReportStateList master;

    /**
     * Creates a new master list.
     *
     * @param list  the list.
     * @param masterPositions  ??.
     */
    public MasterList (ReportStateList list, int masterPositions)
    {
      super (masterPositions);
      this.master = list;
    }

    /**
     * Function to restore the state of a child after the child was garbage collected.
     *
     * @param index  the index.
     *
     * @return the restored ReportState of the given index, or null, if the state
     * could not be restored.
     */
    protected Object restoreChild (int index)
    {
      ReportState master = (ReportState) getMaster ();
      if (master == null)
      {
        return null;
      }
      int max = getChildPos (index);
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
      for (int i = 0; i <= count; i++)
      {
        ReportState oldState = state;
        //Log.debug("o-State: " + state.getClass());
        state = master.proc.processPage (state, master.getDummyWriter());
        set (state, i + 1);
        //Log.debug("n-State: " + state.getClass());
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
   * contain their master state as first child. The weakReferenceLists have a maxSize of 10,
   * so every 10th state will protected from being garbageCollected.
   */
  private ArrayList masterStates10; // all states > 120
  /**
   * The list of master states. This is a list of WeakReferenceLists. These WeakReferenceLists
   * contain their master state as first child. The weakReferenceLists have a maxSize of 4,
   * so every 4th state will protected from being garbageCollected.
   */
  private ArrayList masterStates4; // all states from 20 - 120

  /**
   * The list of primary states. This is a list of ReportStates and is used to store the
   * first 20 elements of this state list.
   */
  private ArrayList primaryStates; // all states from 0 - 20

  /** The number of elements in this list. */
  private int size;

  /** The report processor that the state list relates to. */
  private PageableReportProcessor proc;

  /** The dummy output target. */
  private OutputTarget dummyWriter;

  /**
   * Returns the index of the WeakReferenceList in the master list.
   *
   * @param pos  the position.
   * @param maxListSize  the maximum list size.
   *
   * @return the position within the masterStateList.
   */
  private int getMasterPos (int pos, int maxListSize)
  {
    return (int) Math.floor (pos / maxListSize);
  }

  /**
   * Creates a new reportstatelist. The list will be filled using the specified report
   * and output target. Filling of the list is done elsewhere.
   *
   * @param proc the reportprocessor used to restore lost states (null not permitted).
   *
   * @throws OutputTargetException if there is a problem with the output target.
   * @throws NullPointerException if the report processor is <code>null</code>.
   */
  public ReportStateList (PageableReportProcessor proc) throws OutputTargetException
  {
    if (proc == null)
    {
      throw new NullPointerException ("ReportProcessor null");
    }
   // this.report = report;
    this.proc = proc;
    dummyWriter = proc.getOutputTarget().createDummyWriter();
    dummyWriter.open();

    primaryStates = new ArrayList ();
    masterStates4 = new ArrayList ();
    masterStates10 = new ArrayList ();

  }

  /**
   * Returns the dummy output target.
   *
   * @return the dummy output target.
   */
  public OutputTarget getDummyWriter()
  {
    return dummyWriter;
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
    {
      throw new IllegalArgumentException();
    }

    // the first 20 Elements are stored directly into an ArrayList
    if (size() < PRIMARY_MAX)
    {
      primaryStates.add(state);
      this.size++;
    }
    // the next 100 Elements are stored into a list of 4-element weakReference
    //list. So if an Element gets lost (GCd), only 4 states need to be replayed.
    else if (size() < MASTER4_MAX)
    {
      int secPos = size() - PRIMARY_MAX;
      MasterList master = null;
      int masterPos = getMasterPos (secPos, MASTERPOSITIONS_MED);
      if (masterPos >= masterStates4.size ())
      {
        master = new MasterList (this, MASTERPOSITIONS_MED);
        masterStates4.add (master);
      }
      else
      {
        master = (MasterList) masterStates4.get (masterPos);
      }
      master.add (state);
      this.size++;
    }
    // all other Elements are stored into a list of 10-element weakReference
    //list. So if an Element gets lost (GCd), 10 states need to be replayed.
    else
    {
      int thirdPos = size() - MASTER4_MAX;
      MasterList master = null;
      int masterPos = getMasterPos (thirdPos, MASTERPOSITIONS_MAX);
      if (masterPos >= masterStates10.size ())
      {
        master = new MasterList (this, MASTERPOSITIONS_MAX);
        masterStates10.add (master);
      }
      else
      {
        master = (MasterList) masterStates10.get (masterPos);
      }
      master.add (state);
      this.size++;
    }
  }

  /**
   * Removes all elements in the list
   */
  public void clear ()
  {
    masterStates10.clear ();
    masterStates4.clear ();
    primaryStates.clear ();
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
    if (index < PRIMARY_MAX)
    {
      return (ReportState) primaryStates.get (index);
    }
    else if (index < MASTER4_MAX)
    {
      index -= PRIMARY_MAX;
      MasterList master
          = (MasterList) masterStates4.get (getMasterPos (index, MASTERPOSITIONS_MED));
      return (ReportState) master.get (index);
    }
    else
    {
      index -= MASTER4_MAX;
      MasterList master
          = (MasterList) masterStates10.get (getMasterPos (index, MASTERPOSITIONS_MAX));
      return (ReportState) master.get (index);
    }
  }
}