/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * CSSValueList.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CSSValueList.java,v 1.1 2006/02/12 21:54:28 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.values;

import java.util.Collection;

/**
 * Creation-Date: 23.11.2005, 12:37:21
 *
 * @author Thomas Morgner
 */
public class CSSValueList implements CSSValue
{
  private CSSValue[] values;

  public CSSValueList(Collection collection)
  {
    CSSValue[] values = new CSSValue[collection.size()];
    this.values = (CSSValue[]) collection.toArray(values);
  }

  public CSSValueList(CSSValue[] values)
  {
    this.values = values;
  }

  public int getLength()
  {
    return values.length;
  }

  public CSSValue getItem(int index)
  {
    return values[index];
  }

  public String getCSSText()
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < values.length; i++)
    {
      CSSValue value = values[i];
      if (i > 0)
      {
        b.append(" ");
      }
      b.append(value);
    }
    return b.toString();
  }

  public String toString ()
  {
    return getCSSText();
  }

  public static CSSValueList createDuoList (CSSValue value)
  {
    return CSSValueList.createDuoList(value, value);
  }

  public static CSSValueList createDuoList (CSSValue first, CSSValue second)
  {
    final CSSValue[] values = new CSSValue[2];
    values[0] = first;
    values[1] = second;
    return new CSSValueList(values);
  }

  public static CSSValueList createQuadList (CSSValue value)
  {
    return CSSValueList.createQuadList(value, value);
  }

  public static CSSValueList createQuadList (CSSValue first, CSSValue second)
  {
    return CSSValueList.createQuadList(first, second, first, second);
  }

  public static CSSValueList createQuadList (CSSValue first, CSSValue second,
                                             CSSValue third, CSSValue fourth)
  {
    final CSSValue[] values = new CSSValue[4];
    values[0] = first;
    values[1] = second;
    values[2] = third;
    values[3] = fourth;
    return new CSSValueList(values);
  }

}
