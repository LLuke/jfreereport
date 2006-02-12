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
 * ColorReadHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.parser.stylehandler.color;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.color.CSSSystemColors;
import org.jfree.layouting.input.style.keys.color.HtmlColors;
import org.jfree.layouting.input.style.keys.color.SVGColors;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.util.Log;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 26.11.2005, 20:22:46
 *
 * @author Thomas Morgner
 */
public class ColorReadHandler implements CSSValueReadHandler
{
  private static final float ONE_THIRD = 1f / 3f;

  public ColorReadHandler()
  {
  }

  public CSSValue createValue(StyleKey name, LexicalUnit value)
  {
    return createColorValue(value);
  }

  public static CSSValue createColorValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION ||
            value.getLexicalUnitType() == LexicalUnit.SAC_RGBCOLOR)
    {
      // a function with 3 parameters; hsl
      if (value.getFunctionName().equalsIgnoreCase("hsl"))
      {
        return parseHSLColor(value.getParameters());
      }
      else if (value.getFunctionName().equalsIgnoreCase("rgb") ||
              value.getFunctionName().equalsIgnoreCase("color"))
      {
        return parseRGBColor(value.getParameters());
      }
      else if (value.getFunctionName().equalsIgnoreCase("rgba"))
      {
        return parseRGBAColor(value.getParameters());
      }
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      // a constant color name
      return parseIdentColor(value.getStringValue());
    }
    return null;
  }


  private static CSSNumericValue[] getRGBParameterValues(LexicalUnit value,
                                                         int count)
  {
    final CSSNumericValue[] list = new CSSNumericValue[count];
    for (int index = 0; index < count; index++)
    {
      if (value == null)
      {
        return null;
      }
      int numericValue;
      if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
      {
        numericValue = value.getIntegerValue();
        if (numericValue > 255)
        {
          numericValue = 255;
        }
        if (numericValue < 0)
        {
          numericValue = 0;
        }
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
      {
        float percentage = value.getFloatValue();
        if (percentage >= 100)
        {
          numericValue = 255;
        }
        else if (percentage <= 0)
        {
          numericValue = 0;
        }
        else
        {
          numericValue = (int) (255f * percentage);
        }
      }
      else
      {
        return null;
      }

      list[index] = new CSSNumericValue(CSSNumericType.NUMBER, numericValue);
      value = CSSValueFactory.parseComma(value);
    }

    return list;
  }


  private static CSSNumericValue[] getHSLParameterValues(LexicalUnit value)
  {
    //  ((x mod 360) + 360) mod 360)
    if (value == null)
    {
      return null;
    }
    if (value.getLexicalUnitType() != LexicalUnit.SAC_INTEGER)
    {
      return null;
    }

    final int hue = normalizeHue(value.getIntegerValue());

    value = CSSValueFactory.parseComma(value);
    if (value == null)
    {
      return null;
    }
    if (value.getLexicalUnitType() != LexicalUnit.SAC_PERCENTAGE)
    {
      return null;
    }
    float saturation = value.getFloatValue();
    if (saturation > 100)
    {
      saturation = 100;
    }
    if (saturation < 0)
    {
      saturation = 0;
    }

    value = CSSValueFactory.parseComma(value);
    if (value == null)
    {
      return null;
    }
    if (value.getLexicalUnitType() != LexicalUnit.SAC_PERCENTAGE)
    {
      return null;
    }
    float lightness = value.getFloatValue();
    if (lightness > 100)
    {
      lightness = 100;
    }
    if (lightness < 0)
    {
      lightness = 0;
    }

    final CSSNumericValue[] list = new CSSNumericValue[3];
    list[0] = new CSSNumericValue(CSSNumericType.NUMBER, hue);
    list[1] = new CSSNumericValue(CSSNumericType.PERCENTAGE, saturation);
    list[2] = new CSSNumericValue(CSSNumericType.PERCENTAGE, lightness);
    return list;
  }

  private static int normalizeHue(final int integerValue)
  {
    return ((integerValue % 360) + 360) % 360;
  }
  /*
   * HOW TO RETURN hsl.to.rgb(h, s, l):
       SELECT:
	  l<=0.5: PUT l*(s+1) IN m2
	  ELSE: PUT l+s-l*s IN m2
       PUT l*2-m2 IN m1
       PUT hue.to.rgb(m1, m2, h+1/3) IN r
       PUT hue.to.rgb(m1, m2, h    ) IN g
       PUT hue.to.rgb(m1, m2, h-1/3) IN b
       RETURN (r, g, b)

    HOW TO RETURN hue.to.rgb(m1, m2, h):
       IF h<0: PUT h+1 IN h
       IF h>1: PUT h-1 IN h
       IF h*6<1: RETURN m1+(m2-m1)*h*6
       IF h*2<1: RETURN m2
       IF h*3<2: RETURN m1+(m2-m1)*(2/3-h)*6
       RETURN m1
   */

  private static float hueToRGB(float m1, float m2, float h)
  {
    if (h < 0)
    {
      h = h + 1;
    }
    if (h > 1)
    {
      h = h - 1;
    }
    if ((h * 6f) < 1)
    {
      return m1 + (m2 - m1) * h * 6;
    }
    if ((h * 2f) < 1)
    {
      return m2;
    }
    if ((h * 3f) < 2)
    {
      return m1 + (m2 - m1) * (2 * ONE_THIRD - h) * 6;
    }
    return m1;
  }

  private static CSSColorValue parseHSLColor(LexicalUnit lexUnit)
  {
    CSSNumericValue[] params = getHSLParameterValues(lexUnit);
    if (params == null)
    {
      return null;
    }

    float hue = (params[0].intValue() / 360f);
    float saturation = params[1].floatValue();
    float lightness = params[2].floatValue();

    float m2;
    if (lightness <= 0.5)
    {
      m2 = lightness * (saturation + 1);
    }
    else
    {
      m2 = lightness + saturation - lightness * saturation;
    }
    float m1 = lightness * 2 - m2;

    float r = hueToRGB(m1, m2, hue + ONE_THIRD);
    float g = hueToRGB(m1, m2, hue);
    float b = hueToRGB(m1, m2, hue - ONE_THIRD);
    return new CSSColorValue(r, g, b);
  }

  private static CSSColorValue parseRGBColor(LexicalUnit lexUnit)
  {
    CSSNumericValue[] params = getRGBParameterValues(lexUnit, 3);
    if (params == null)
    {
      return null;
    }

    return new CSSColorValue
            ((int) params[0].getValue(),
                    (int) params[1].getValue(),
                    (int) params[2].getValue());
  }

  private static CSSColorValue parseRGBAColor(LexicalUnit lexUnit)
  {
    CSSNumericValue[] params = getRGBParameterValues(lexUnit, 4);
    if (params == null)
    {
      return null;
    }

    return new CSSColorValue
            ((int) params[0].getValue(),
                    (int) params[1].getValue(),
                    (int) params[2].getValue(),
                    (int) params[3].getValue());
  }

  public static CSSValue parseIdentColor(String name)
  {
    if (CSSSystemColors.CURRENT_COLOR.getCSSText().equalsIgnoreCase(name))
    {
      return CSSSystemColors.CURRENT_COLOR;
    }

    CSSColorValue htmlColors = parseColorFromClass(name, HtmlColors.class);
    if (htmlColors != null)
    {
      return htmlColors;
    }
    CSSColorValue svgColors = parseColorFromClass(name, SVGColors.class);
    if (svgColors != null)
    {
      return svgColors;
    }
    CSSColorValue systemColors = parseColorFromClass(name,
            CSSSystemColors.class);
    if (systemColors != null)
    {
      return systemColors;
    }
    Log.debug("No such constant: " + name);
    return null;
  }

  private static CSSColorValue parseColorFromClass(String name, Class c)
  {
    // try to get a color by name using reflection
    final Field f[] = c.getFields();
    for (int i = 0; i < f.length; i++)
    {
      try
      {
        final Field field = f[i];
        if (field.getName().equalsIgnoreCase(name) == false)
        {
          continue;
        }
        if (CSSColorValue.class.isAssignableFrom(field.getType()) == false)
        {
          continue;
        }
        if (Modifier.isPublic(field.getModifiers()) &&
                Modifier.isStatic((field.getModifiers())))
        {
          return (CSSColorValue) field.get(null);
        }
      }
      catch (Exception ce)
      {
        // we ignore exceptions here.
      }
    }
    return null;
  }
}
