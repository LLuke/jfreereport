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
 * LengthResolverUtility.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 15.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.percentages;

import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValueSupport;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.OutputProcessorFeature;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.fonts.registry.FontMetrics;

/**
 * Creation-Date: 15.12.2005, 11:29:22
 *
 * @author Thomas Morgner
 */
public class LengthResolverUtility
{
  public static final double DEFAULT_X_HEIGHT_FACTOR = 0.58;

  public static boolean isAbsoluteValue ( final CSSNumericValue value)
  {
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return true;
    }
    // PX is relative to the device, so during a layouting process, it can
    // be considered absolute 
    return CSSNumericType.PX.equals(value.getType());
  }

  public static boolean isLengthValue ( final CSSNumericValue value)
  {
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.EM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.EX.equals(value.getType()))
    {
      return true;
    }
    // PX is relative to the device, so during a layouting process, it can
    // be considered absolute
    return CSSNumericType.PX.equals(value.getType());
  }

  public static long convertLengthToInternal(final CSSNumericValue value,
                                             final LayoutNode node,
                                             final OutputProcessorMetaData metaData)
  {
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return StrictGeomUtility.toInternalValue (value.getValue());
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return StrictGeomUtility.toInternalValue (value.getValue() / 12d);
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return StrictGeomUtility.toInternalValue (value.getValue() / 72d);
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return StrictGeomUtility.toInternalValue
              (value.getValue() * (72d / 254d) * 100d);
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return StrictGeomUtility.toInternalValue
              (value.getValue() * (72d / 254d) * 10d);
    }


    if (CSSNumericType.PX.equals(value.getType()))
    {
      final int pixelPerInch = (int) metaData.getNumericFeatureValue
                      (OutputProcessorFeature.DEVICE_RESOLUTION);
      if (pixelPerInch <= 0)
      {
        return StrictGeomUtility.toInternalValue(value.getValue());
      }
      return StrictGeomUtility.toInternalValue
              (value.getValue() * 72d / pixelPerInch);
    }
    if (CSSNumericType.EM.equals(value.getType()))
    {
      final FontSpecification fspec =
              node.getLayoutContext().getFontSpecification();
      final double fontSize = fspec.getFontSize();
      return StrictGeomUtility.toInternalValue
              (fontSize * value.getValue());
    }
    if (CSSNumericType.EX.equals(value.getType()))
    {
      final FontSpecification fspec =
              node.getLayoutContext().getFontSpecification();
      final FontMetrics fontMetrics = fspec.getFontMetrics();
      if (fontMetrics == null)
      {
        final double fontSize = fspec.getFontSize() * DEFAULT_X_HEIGHT_FACTOR;
        return StrictGeomUtility.toInternalValue (value.getValue() * fontSize);
      }
      else
      {
        return StrictGeomUtility.toInternalValue
                (value.getValue() * fontMetrics.getXHeight());
      }
    }

    return 0;
  }

  public static CSSNumericValue getLength (CSSValue value)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return null;
    }

    CSSNumericValue nval = (CSSNumericValue) value;
    if (CSSValueSupport.isNumericType(CSSNumericType.PERCENTAGE, nval))
    {
      return null;
    }
    
    return nval;
  }

  private LengthResolverUtility()
  {
  }
}
