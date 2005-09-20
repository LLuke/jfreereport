/*
 * Copyright (c) 1998, 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package org.jfree.report.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * An enumeration that iterates over an array.
 *
 * @author Thomas Morgner
 */
public class ArrayEnumeration implements Enumeration
{
  /** The base datasource. */
  private Object[] objectarray = null;
  /** The counter holds the current position inside the array. */
  private int counter = 0;

  /**
   * Creates a new enumeration for the given array.
   *
   * @param objectarray the array over which to iterate
   * @throws NullPointerException if the array is null.
   */
  public ArrayEnumeration(Object[] objectarray)
  {
    if (objectarray == null)
    {
      throw new NullPointerException("The array must not be null.");
    }

    this.objectarray = objectarray;
  }

  /**
   * Returns true if this enumeration has at least one more Element.
   *
   * @return true, if there are more elements, false otherwise.
   */
  public boolean hasMoreElements()
  {
    return (counter < objectarray.length);
  }

  /**
   * Returns the next element in the Array.
   *
   * @return the next element in the array.
   * @throws  NoSuchElementException  if no more elements exist.
   */
  public Object nextElement()
  {
    if (counter >= objectarray.length)
    {
      throw new NoSuchElementException();
    }

    Object retval = objectarray[counter];
    counter += 1;
    return retval;
  }
}