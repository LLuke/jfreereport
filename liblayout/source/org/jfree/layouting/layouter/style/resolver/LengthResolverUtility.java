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
 * LengthResolverUtility.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: LengthResolverUtility.java,v 1.2 2006/04/17 20:51:16 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueSupport;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.output.OutputProcessorFeature;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * Creation-Date: 15.12.2005, 11:29:22
 *
 * @author Thomas Morgner
 */
public class LengthResolverUtility
{
  public static final double DEFAULT_X_HEIGHT_FACTOR = 0.58;

  public static boolean isAbsoluteValue(final CSSNumericValue value)
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

  public static boolean isLengthValue(final CSSNumericValue value)
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

  public static double convertLengthToDouble(final CSSValue rawValue,
                                             final LayoutContext context,
                                             final OutputProcessorMetaData metaData)
  {
    if (rawValue instanceof CSSNumericValue == false)
    {
      return 0;
    }

    CSSNumericValue value = (CSSNumericValue) rawValue;
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return value.getValue();
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return (value.getValue() / 12d);
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return (value.getValue() / 72d);
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return (value.getValue() * (72d / 254d) * 100d);
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return (value.getValue() * (72d / 254d) * 10d);
    }
    if (CSSNumericType.PX.equals(value.getType()))
    {
      final int pixelPerInch = (int) metaData.getNumericFeatureValue
              (OutputProcessorFeature.DEVICE_RESOLUTION);
      if (pixelPerInch <= 0)
      {
        // we assume 72 pixel per inch ...
        return value.getValue();
      }
      return value.getValue() * 72d / pixelPerInch;
    }
    if (CSSNumericType.EM.equals(value.getType()))
    {
      final FontSpecification fspec =
              context.getFontSpecification();
      final double fontSize = fspec.getFontSize();
      return (fontSize * value.getValue());
    }
    if (CSSNumericType.EX.equals(value.getType()))
    {
      final FontSpecification fspec =
              context.getFontSpecification();
      final FontMetrics fontMetrics = metaData.getFontMetrics(fspec);
      if (fontMetrics == null)
      {
        final double fontSize = fspec.getFontSize() * DEFAULT_X_HEIGHT_FACTOR;
        return (value.getValue() * fontSize);
      }
      else
      {
        return value.getValue() * fontMetrics.getXHeight();
      }
    }

    return 0;
  }


  public static CSSNumericValue convertLength(final CSSValue rawValue,
                                              final LayoutContext context,
                                              final OutputProcessorMetaData metaData)
  {
    return new CSSNumericValue(CSSNumericType.PT,
            convertLengthToDouble(rawValue, context, metaData));
  }

  public static CSSNumericValue getLength(CSSValue value)
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

  public static CSSNumericValue getLength(final CSSValue value,
                                          final CSSNumericValue percentageBase)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return null;
    }

    CSSNumericValue nval = (CSSNumericValue) value;
    if (CSSValueSupport.isNumericType(CSSNumericType.PERCENTAGE, nval))
    {
      if (percentageBase == null)
      {
        return null;
      }

      final double percentage = nval.getValue();
      return new CSSNumericValue(percentageBase.getType(),
              percentageBase.getValue() * percentage / 100d);
    }

    return nval;
  }


  public static CSSNumericValue getParentFontSize(final LayoutElement node)
  {
    final LayoutElement parent = node.getParent();
    if (parent == null)
    {
      return null;
    }
    final double fs = parent.getLayoutContext().getFontSpecification().getFontSize();
    return new CSSNumericValue(CSSNumericType.PT, fs);
  }

  private LengthResolverUtility()
  {
  }
}
