/**
 *
 *  Date: 31.05.2002
 *  ReportStateList.java
 *  ------------------------------
 *  31.05.2002 : ...
 */
package com.jrefinery.report;

import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.WeakReferenceList;

import java.util.LinkedList;
import java.util.List;

/**
 * Stores pages, not arbitary report states. ReportStates can be reproduced by calling
 * processPage on the report.
 */
public class ReportStateList
{
  private static final int MASTERPOSITIONS = 10;

  private static class MasterList extends WeakReferenceList
  {
    private int pos;
    private ReportStateList master;

    public MasterList (ReportStateList list)
    {
      this.master = list;
    }

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
        return this.master.restoreState (max, master);
      }
      catch (ReportProcessingException rpe)
      {
        return null;
      }
    }

    protected int getMaxChildCount ()
    {
      return MASTERPOSITIONS;
    }
  }

  protected ReportState restoreState (int count, ReportState master)
          throws ReportProcessingException
  {
    if (master == null) throw new NullPointerException ("Master is null");
    ReportState state = master;

    for (int i = 0; i <= count; i++)
    {
      ReportState oldState = state;
      state = report.processPage (target, state, false);
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

  private List masterStates;
  private int _size;
  private JFreeReport report;
  private OutputTarget target;

  private int getMasterPos (int pos)
  {
    return (int) Math.floor (pos / MASTERPOSITIONS);
  }

  public ReportStateList (JFreeReport report, OutputTarget out)
  {
    if (report == null) throw new NullPointerException ("Report null");
    if (out == null) throw new NullPointerException ("outputtarget null");
    this.report = report;
    this.target = out;
    masterStates = new LinkedList ();
  }

  public int size ()
  {
    return _size;
  }

  public void add (ReportState state)
  {
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

  public void clear ()
  {
    masterStates.clear ();
    _size = 0;
  }

  public ReportState get (int index)
  {
    if (index >= size ())
      throw new IndexOutOfBoundsException ();
    MasterList master = (MasterList) masterStates.get (getMasterPos (index));
    return (ReportState) master.get (index);
  }
}