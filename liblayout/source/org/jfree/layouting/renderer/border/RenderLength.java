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
 * RenderLength.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: RenderLength.java,v 1.3 2006/07/20 17:50:52 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.border;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 09.07.2006, 21:03:12
 *
 * @author Thomas Morgner
 */
public class RenderLength
{
  public static RenderLength AUTO = new RenderLength(Long.MIN_VALUE, false);
  public static RenderLength EMPTY = new RenderLength(0, false);

  private long value;
  private boolean percentage;

  public RenderLength(final long value,
                      final boolean percentage)
  {
    this.value = value;
    this.percentage = percentage;
  }

  public long getValue()
  {
    return value;
  }

  public boolean isPercentage()
  {
    return percentage;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final RenderLength that = (RenderLength) o;

    if (percentage != that.percentage)
    {
      return false;
    }
    if (value != that.value)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = (int) (value ^ (value >>> 32));
    result = 29 * result + (percentage ? 1 : 0);
    return result;
  }

  public long resolve (final long parent)
  {
    if (isPercentage())
    {
      return (value * parent) / (100 * 1000);
    }
    else if (value == Long.MIN_VALUE)
    {
      return 0;
    }
    else
    {
      return value;
    }
  }


  public static RenderLength convertToInternal(final CSSValue value,
                                               final LayoutContext layoutContext,
                                               final OutputProcessorMetaData metaData)
  {
    if (value instanceof CSSNumericValue)
    {
      CSSNumericValue nval = (CSSNumericValue) value;
      if (nval.getType() == CSSNumericType.PERCENTAGE)
      {
        // Range is between 100.000 and 0
        return new RenderLength(StrictGeomUtility.toInternalValue
                (nval.getValue()), true);
      }
      if (nval.getType() == CSSNumericType.NUMBER)
      {
        // Range is between 100.000 and 0
        return new RenderLength(StrictGeomUtility.toInternalValue
                (nval.getValue()) * 100, true);
      }

      final CSSNumericValue cssNumericValue = CSSValueResolverUtility.convertLength
              ((CSSNumericValue) value, layoutContext, metaData);
      // the resulting nvalue is guaranteed to have the unit PT

      return new RenderLength(StrictGeomUtility.toInternalValue
                      (cssNumericValue.getValue()), false);
    }
    return RenderLength.EMPTY;
  }

}
