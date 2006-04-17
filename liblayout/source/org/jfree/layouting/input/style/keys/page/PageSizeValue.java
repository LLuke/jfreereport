/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
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
 * PageSizeValue.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.input.style.keys.page;

import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 30.11.2005, 17:13:35
 *
 * @author Thomas Morgner
 */
public class PageSizeValue implements CSSValue
{
  private CSSNumericValue width;
  private CSSNumericValue height;

  protected PageSizeValue(final PageSize pageSize)
  {
    this.width = new CSSNumericValue (CSSNumericType.PT, pageSize.getWidth());
    this.height = new CSSNumericValue (CSSNumericType.PT, pageSize.getHeight());
  }

  public PageSizeValue(final CSSNumericValue width,
                       final CSSNumericValue height)
  {
    this.width = width;
    this.height = height;
  }

  public CSSNumericValue getWidth()
  {
    return width;
  }

  public CSSNumericValue getHeight()
  {
    return height;
  }

  public String getCSSText()
  {
    return width + " " + height;
  }
}
