/**
 *
 *  Date: 31.05.2002
 *  WeakReferenceList.java
 *  ------------------------------
 *  31.05.2002 : ...
 */
package com.jrefinery.report.util;

import com.jrefinery.report.ReportState;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public abstract class WeakReferenceList
{
  private Object master;
  private Reference[] childs;
  private int size;

  public WeakReferenceList ()
  {
    this.childs = new Reference[getMaxChildCount() - 1];
  }

  protected int getMaxChildCount ()
  {
    return 25;
  }

  protected Object getMaster ()
  {
    return master;
  }

  protected abstract Object restoreChild (int index);

  public Object get (int index)
  {
    if (isMaster(index))
    {
      return master;
    }
    else
    {
      Reference ref = childs[getChildPos(index)];
      Object ob = ref.get();
      if (ob == null)
      {
        ob = restoreChild(index);
        childs[getChildPos(index)] = createReference(ob);;
      }
      return ob;
    }
  }

  public void set (Object report, int index)
  {
    if (isMaster(index))
    {
      master = report;
    }
    else
    {
      childs[getChildPos(index)] = createReference(report);
    }
  }

  protected Reference createReference (Object o)
  {
    return new WeakReference (o);
  }

  public boolean add (Object rs)
  {
    if (size == 0)
    {
      master = rs;
      size = 1;
      return true;
    }
    else
    {
      if (size < getMaxChildCount())
      {
        childs[size - 1] = createReference(rs);
        size++;
        return true;
      }
      else
      {
        // was not able to add this to this list, maximum number of entries reached.
        return false;
      }
    }
  }

  protected boolean isMaster (int index)
  {
    return index % getMaxChildCount() == 0;
  }

  protected int getChildPos (int index)
  {
    return index % getMaxChildCount() - 1;
  }

  public int getSize ()
  {
    return size;
  }

}