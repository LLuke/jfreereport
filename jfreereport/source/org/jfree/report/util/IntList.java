/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * IntList.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: IntList.java,v 1.4 2005/06/25 17:52:03 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util;

/**
 * A Array-List for integer objects. Ints can be added to the list and will be
 * stored in an int-array.
 * <p>
 * Using this list for storing ints is much faster than creating java.lang.Integer
 * objects and storing them in an ArrayList.
 *
 * @author Thomas Morgner
 */
public class IntList
{
  private static final int[] EMPTY_ARRAY = new int[0];
  private int[] data;
  private int size;
  private int increment;

  public IntList (final int capacity)
  {
    data = new int[capacity];
    increment = capacity;
  }

  private void ensureCapacity (final int c)
  {
    if (data.length <= c)
    {
      final int[] newData = new int[Math.max(data.length + increment, c + 1)];
      System.arraycopy(data, 0, newData, 0, size);
      data = newData;
    }
  }

  public void add (final int value)
  {
    ensureCapacity(size);
    data[size] = value;
    size += 1;
  }

  public int get (final int index)
  {
    if (index >= size)
    {
      throw new IndexOutOfBoundsException();
    }
    return data[index];
  }

  public void clear ()
  {
    size = 0;
  }

  public int size ()
  {
    return size;
  }

  public int[] toArray ()
  {
    if (size == 0)
    {
      return EMPTY_ARRAY;
    }

    final int[] retval = new int[size];
    System.arraycopy(data, 0, retval, 0, size);
    return retval;
  }
}
