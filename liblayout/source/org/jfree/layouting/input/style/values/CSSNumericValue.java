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
 * PointValue.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: CSSNumericValue.java,v 1.1 2006/02/12 21:54:28 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 23.11.2005, 11:37:44
 *
 * @author Thomas Morgner
 */
public class CSSNumericValue extends Number implements CSSValue
{
  public static final CSSNumericValue ZERO_LENGTH =
          new CSSNumericValue(CSSNumericType.PT, 0);

  private double value;
  private CSSNumericType type;

  public CSSNumericValue(final CSSNumericType type, final double value)
  {
    this.type = type;
    this.value = value;
  }

  public double getValue()
  {
    return value;
  }

  public CSSNumericType getType()
  {
    return type;
  }

  public int intValue()
  {
    return (int) value;
  }

  public long longValue()
  {
    return (long) value;
  }

  public float floatValue()
  {
    return (float) value;
  }

  public double doubleValue()
  {
    return value;
  }

  public String getCSSText()
  {
    return value + " " + type.getType();
  }

  public String toString ()
  {
    return getCSSText();
  }
  
}
