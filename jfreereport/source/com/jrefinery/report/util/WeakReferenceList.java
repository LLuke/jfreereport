/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * WeakReferenceList.java
 * ------------------------------
 * 31-May-2002 : initial version
 * 09-Jun-2002 : Documentation
 */
package com.jrefinery.report.util;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * The WeakReference list uses <code>java.lang.ref.WeakReference</code>s to store its
 * contents. In contrast to the WeakHashtable, this list knows how to restore missing content,
 * so that garbage collected elements can be restored when they are accessed.
 * <p>
 * By default this list can contain 25 elements, where the first element is stored using a
 * strong reference, which gets not garbage collected.
 * <p>
 * Restoring the elements is not implemented, concrete implementations will have to override
 * the <code>restoreChild(int)</code> method. The getMaxChildCount defines the maxmimum number
 * of children in the list. When more than maxChildCount elements are contained in this list,
 * add will always return false to indicate that adding the element failed.
 * <p>
 * To customize the list, override createReference to create a different kind of reference or
 * override getMaxChildCount() to define a different list size.
 * <p>
 * This list is able to add or replace elements, but inserting or removing of elements is not
 * possible.
 */
public abstract class WeakReferenceList implements Serializable, Cloneable
{
  private Object master;
  private Reference[] childs;
  private int size;

  /**
   * creates a new weak reference list. The storage of the list is limited to getMaxChildCount()
   * elements.
   */
  public WeakReferenceList ()
  {
    this.childs = new Reference[getMaxChildCount () - 1];
  }

  /**
   * returns the maximum number of children in this list.
   *
   * @returns 25
   */
  protected int getMaxChildCount ()
  {
    return 25;
  }

  /**
   * returns the master element of this list. The master element is the element stored by
   * a strong reference and cannot be garbage collected.
   *
   * @returns the master element
   */
  protected Object getMaster ()
  {
    return master;
  }

  /**
   * attempts to restore the child stored on the given index.
   * @returns null if the child could not be restored or the restored child.
   */
  protected abstract Object restoreChild (int index);

  /**
   * Returns the child stored at the given index. If the child has been garbage collected,
   * it gets restored using the restoreChild function.
   */
  public Object get (int index)
  {
    if (isMaster (index))
    {
      return master;
    }
    else
    {
      Reference ref = childs[getChildPos (index)];
      Object ob = ref.get ();
      if (ob == null)
      {
        ob = restoreChild (index);
        childs[getChildPos (index)] = createReference (ob);
        ;
      }
      return ob;
    }
  }

  /**
   * replaces the child stored at the given index with the new child which can be null.
   */
  public void set (Object report, int index)
  {
    if (isMaster (index))
    {
      master = report;
    }
    else
    {
      childs[getChildPos (index)] = createReference (report);
    }
  }

  /**
   * Creates a new reference for the given object.
   *
   * @return a WeakReference for the object o without any ReferenceQueue attached.
   */
  protected Reference createReference (Object o)
  {
    return new WeakReference (o);
  }

  /**
   * Adds the element to the list. If the maximum size of the list is exceeded, this function
   * returns false to indicate that adding failed.
   *
   * @return true, if the object was successfully added to the list, false otherwise
   */
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
      if (size < getMaxChildCount ())
      {
        childs[size - 1] = createReference (rs);
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

  /**
   * returns true, if the given index denotes a master index of this list.
   */
  protected boolean isMaster (int index)
  {
    return index % getMaxChildCount () == 0;
  }

  /**
   * returns the internal storage position for the child.
   */
  protected int getChildPos (int index)
  {
    return index % getMaxChildCount () - 1;
  }

  /**
   * returns the size of the list.
   */
  public int getSize ()
  {
    return size;
  }

  private void writeObject (java.io.ObjectOutputStream out)
          throws IOException
  {
    Reference[] orgChilds = childs;
    try
    {
      childs = null;
      out.defaultWriteObject ();
    }
    finally
    {
      childs = orgChilds;
    }
  }

  private void readObject (java.io.ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject ();
    childs = new Reference[getMaxChildCount () - 1];
    for (int i = 0; i < childs.length; i++)
    {
      childs[i] = createReference (null);
    }
  }
}