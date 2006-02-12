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
 * CSSValueSupport.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CSSValueSupport.java,v 1.1 2006/02/12 21:54:28 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 14.12.2005, 12:53:16
 *
 * @author Thomas Morgner
 */
public class CSSValueSupport
{
  public static boolean isNumericType (CSSNumericType type, CSSValue value)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return false;
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    return nval.getType().equals(type);
  }

  public static boolean isLength (CSSValue value)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return false;
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    final CSSNumericType type = nval.getType();
    if (type.equals(CSSNumericType.CM)) return true;
    if (type.equals(CSSNumericType.EM)) return true;
    if (type.equals(CSSNumericType.EX)) return true;
    if (type.equals(CSSNumericType.INCH)) return true;
    if (type.equals(CSSNumericType.MM)) return true;
    if (type.equals(CSSNumericType.PC)) return true;
    if (type.equals(CSSNumericType.PT)) return true;
    return type.equals(CSSNumericType.PX);
  }

  private CSSValueSupport()
  {
  }

  public static boolean isURI(CSSValue value)
  {
    if (value instanceof CSSStringValue == false)
    {
      return false;
    }
    CSSStringValue sval = (CSSStringValue) value;
    return sval.getType().equals(CSSStringType.URI);
  }
}
