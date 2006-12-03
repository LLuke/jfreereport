/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
 *
 * (C) Copyright 2000-2005, by Pentaho Corporation and Contributors.
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
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
public class CSSValueResolverUtility
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

  public static double convertLengthToDouble(final CSSValue rawValue)
  {
    return convertLengthToDouble(rawValue, null, null);
  }

  /**
   * Returns the length in point as a double primitive value.
   *
   * @param rawValue
   * @param context
   * @param metaData
   * @return
   */
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

    if (metaData == null)
    {
      return 0;
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

    if (context != null)
    {
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
    if (isNumericType(CSSNumericType.PERCENTAGE, nval))
    {
      return null;
    }

    return nval;
  }

  public static boolean isNumericType(CSSNumericType type, CSSValue value)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return false;
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    return nval.getType().equals(type);
  }


  public static CSSNumericValue getLength(final CSSValue value,
                                          final CSSNumericValue percentageBase)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return null;
    }

    CSSNumericValue nval = (CSSNumericValue) value;
    if (isNumericType(CSSNumericType.PERCENTAGE, nval))
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


  public static boolean isURI(CSSValue value)
  {
    if (value instanceof CSSStringValue == false)
    {
      return false;
    }
    CSSStringValue sval = (CSSStringValue) value;
    return sval.getType().equals(CSSStringType.URI);
  }

  public static double getNumericValue(CSSValue value, double defaultValue)
  {
    if (value instanceof CSSNumericValue)
    {
      CSSNumericValue nval = (CSSNumericValue) value;
      if (CSSNumericType.NUMBER.equals(nval.getType()))
      {
        return nval.getValue();
      }
    }
    return defaultValue;
  }

  private CSSValueResolverUtility()
  {
  }
}
