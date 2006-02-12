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
 * BorderRadiusValue.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: BorderRadiusValue.java,v 1.1 2006/02/12 21:54:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
 */
package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 14.12.2005, 23:47:56
 *
 * @author Thomas Morgner
 */
public class BorderRadiusValue implements CSSValue
{
  private CSSNumericValue first;
  private CSSNumericValue second;

  public BorderRadiusValue(final CSSNumericValue first,
                           final CSSNumericValue second)
  {
    if (first == null)
    {
      throw new NullPointerException();
    }

    this.first = first;
    if (second == null)
    {
      this.second = first;
    }
    else
    {
      this.second = second;
    }
  }

  public CSSNumericValue getFirst()
  {
    return first;
  }

  public CSSNumericValue getSecond()
  {
    return second;
  }

  public String getCSSText()
  {
    return first + " " + second;
  }

  public String toString()
  {
    return getCSSText();
  }
}
