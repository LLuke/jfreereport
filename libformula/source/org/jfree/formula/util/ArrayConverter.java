/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.util;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Array;

/**
 * Creation-Date: 08.10.2006, 17:37:50
 *
 * @author Thomas Morgner
 */
public class ArrayConverter
{
  private ArrayConverter()
  {
  }

  public static Object[] getAsList(final Object maybeArray,
                                   final Class arrayType)
  {
    if (maybeArray == null)
    {
      return null;
    }

    if (maybeArray.getClass().isArray() == false)
    {
      return new Object[]{maybeArray};
    }

    ArrayList list = new ArrayList();
    ArrayConverter.addToList(list, maybeArray);
    final Object o = Array.newInstance(arrayType, list.size());
    return list.toArray((Object[]) o);
  }

  private static void addToList (List list, Object array)
  {
    final int length = Array.getLength(array);
    for (int i = 0; i < length; i++)
    {
      final Object value = Array.get(array, i);
      if (value == null)
      {
        list.add(null);
        continue;
      }

      if (value.getClass().isArray() == false)
      {
        list.add(value);
        continue;
      }

      ArrayConverter.addToList(list, value);
    }
  }


  /**
   * @param maybeArray
   * @param dimensions
   * @return
   */
  public static Object[] getArray(final Object maybeArray,
                                  final Class arrayType,
                                  final int dims)
  {
    if (maybeArray == null)
    {
      return null;
    }
    if (dims <= 0)
    {
      return null;
    }

    if (maybeArray.getClass().isArray() == false)
    {
      Object object = maybeArray;
      for (int i = 0; i < dims; i++)
      {
        final Object[] array = (Object[]) Array.newInstance(arrayType, 1);
        array[0] = object;
        object = array;
      }
      return (Object[]) object;
    }

    if (ArrayConverter.getDimensionCount(maybeArray.getClass()) < dims)
    {
      return null;
    }
    return (Object[]) maybeArray;
  }

  public static int getDimensionCount(Class arrayClass)
  {
    int count = 0;
    while (arrayClass != null && arrayClass.isArray())
    {
      count += 1;
      arrayClass = arrayClass.getComponentType();
    }
    return count;
  }

}
