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
 * SpacingValue.java
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
package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;


/**
 * Creation-Date: 21.12.2005, 13:06:52
 *
 * @author Thomas Morgner
 */
public class SpacingValue implements CSSValue
{
  public static final SpacingValue DEFAULT =
          new SpacingValue(CSSNumericValue.ZERO_LENGTH,
                  CSSNumericValue.ZERO_LENGTH,
                  new CSSNumericValue(CSSNumericType.PT, Short.MAX_VALUE));

  private CSSValue optimumSpacing;
  private CSSValue minimumSpacing;
  private CSSValue maximumSpacing;

  public SpacingValue(final CSSValue optimumSpacing, final CSSValue minimumSpacing,
                      final CSSValue maximumSpacing)
  {
    this.optimumSpacing = optimumSpacing;
    this.minimumSpacing = minimumSpacing;
    if (maximumSpacing == null)
    {
      this.maximumSpacing = new CSSNumericValue(CSSNumericType.PT, Short.MAX_VALUE);
    }
    else
    {
      this.maximumSpacing = maximumSpacing;
    }
  }

  public CSSValue getOptimumSpacing()
  {
    return optimumSpacing;
  }

  public CSSValue getMinimumSpacing()
  {
    return minimumSpacing;
  }

  public CSSValue getMaximumSpacing()
  {
    return maximumSpacing;
  }

  public String getCSSText()
  {
    StringBuffer b = new StringBuffer();
    b.append(optimumSpacing);
    if (minimumSpacing != null)
    {
      b.append(" ");
      b.append(minimumSpacing);

      if (maximumSpacing != null)
      {
        b.append(" ");
        b.append(maximumSpacing);

      }
    }
    return b.toString();
  }

  public String toString ()
  {
    return getCSSText();
  }
  
}
