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
 * ----------------------
 * WeakReferenceList.java
 * ----------------------
 *
 * $Id: WeakReferenceList.java,v 1.9 2002/12/02 17:44:51 taqua Exp $
 *
 * Changes
 * -------
 * 31-May-2002 : Initial version
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
 *
 * @author TM
 */
public abstract class WeakReferenceList implements Serializable, Cloneable
{
  /** The master element. */
  private Object master;

  /** Storage for the references. */
  private Reference[] childs;

  /** The current number of elements. */
  private int size;

  /** The maximum number of elements. */
  private int maxChilds;

  /**
   * Creates a new weak reference list. The storage of the list is limited to getMaxChildCount()
   * elements.
   */
  public WeakReferenceList (int maxChildCount)
  {
    this.maxChilds = maxChildCount;
    this.childs = new Reference[maxChildCount - 1];
  }

  /**
   * Returns the maximum number of children in this list.
   *
   * @return 25
   */
  protected final int getMaxChildCount ()
  {
    return maxChilds;
  }

  /**
   * Returns the master element of this list. The master element is the element stored by
   * a strong reference and cannot be garbage collected.
   *
   * @return the master element
   */
  protected Object getMaster ()
  {
    return master;
  }

  /**
   * Attempts to restore the child stored on the given index.
   *
   * @param index  the index.
   *
   * @return null if the child could not be restored or the restored child.
   */
  protected abstract Object restoreChild (int index);

  /**
   * Returns the child stored at the given index. If the child has been garbage collected,
   * it gets restored using the restoreChild function.
   *
   * @param index  the index.
   *
   * @return the object.
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
      }
      return ob;
    }
  }

  /**
   * Replaces the child stored at the given index with the new child which can be null.
   *
   * @param report  the object.
   * @param index  the index.
   *
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
   * @param o  the object.
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
   * @param rs  the object.
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
   * Returns true, if the given index denotes a master index of this list.
   *
   * @param index  the index.
   *
   * @return true if the index is a master index.
   */
  protected boolean isMaster (int index)
  {
    return index % getMaxChildCount () == 0;
  }

  /**
   * Returns the internal storage position for the child.
   *
   * @param index  the index.
   *
   * @return the internal storage index.
   */
  protected int getChildPos (int index)
  {
    return index % getMaxChildCount () - 1;
  }

  /**
   * Returns the size of the list.
   *
   * @return the size.
   */
  public int getSize ()
  {
    return size;
  }

  /**
   * Serialisation support
   *
   * @param out  the output stream.
   *
   * @throws IOException if there is an I/O error.
   */
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

  /**
   * Serialisation support
   *
   * @param in  the input stream.
   *
   * @throws IOException if there is an I/O error.
   * @throws ClassNotFoundException ??
   */
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