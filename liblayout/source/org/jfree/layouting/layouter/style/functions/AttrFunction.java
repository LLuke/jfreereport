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
 * AttrFunction.java
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
package org.jfree.layouting.layouter.style.functions;

import java.awt.Color;
import java.net.URL;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.values.CSSRawValue;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.util.ColorUtil;
import org.jfree.resourceloader.ResourceKey;

/**
 * Creation-Date: 15.04.2006, 18:33:56
 *
 * @author Thomas Morgner
 */
public class AttrFunction extends AbstractStyleFunction
{
  public AttrFunction()
  {
  }

  public CSSValue getValue(final LayoutProcess layoutProcess,
                           final LayoutElement element,
                           final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    final CSSValue[] params = function.getParameters();
    if (params.length < 2)
    {
      throw new FunctionEvaluationException();
    }
    final String namespace = getStringParameter
            (layoutProcess, element, params[0]);
    final String name = getStringParameter
            (layoutProcess, element, params[1]);

    String type = null;
    if (params.length >= 3)
    {
      type = getStringParameter(layoutProcess, element, params[2]);
    }

    if ("*".equals(namespace))
    {
      // this is a lot of work. Query all attributes in all namespaces...
      final Object value = element.getAttributes().getFirstAttribute(name);
      return convertValue(layoutProcess, value, type);
    }
    else
    {
      // thats easy.
      final Object value = element.getAttribute
              (namespace, name);
      return convertValue(layoutProcess, value, type);
    }
  }


  private CSSValue convertValue(final LayoutProcess layoutProcess,
                                final Object value,
                                final String type)
          throws FunctionEvaluationException
  {
    if (value instanceof CSSValue)
    {
      throw new FunctionEvaluationException();
    }

    if (value instanceof String)
    {
      final String strVal = (String) value;
      if ("length".equals(type))
      {
        return convertToNumber(strVal);
      }
      else if ("url".equals(type))
      {
        return convertToURI(layoutProcess, strVal);
      }
      else if ("color".equals(type))
      {
        CSSValue colorValue = ColorUtil.parseColor(strVal);
        if (colorValue == null)
        {
          throw new FunctionEvaluationException();
        }
        return colorValue;
      }
      else
      {
        // auto-mode. We check for URLs, as this is required for images
        try
        {
          return convertToNumber(strVal);
        }
        catch(Exception e)
        {
          // ok, we had to try it. Now proceed ..
        }
//        try
//        {
//          return convertToURI(layoutProcess, strVal);
//        }
//        catch(Exception e)
//        {
//          // ignore, it seems not to be an URI at all ..
//        }
        return new CSSStringValue(CSSStringType.STRING, String.valueOf(value));
      }
    }
    else if (value instanceof URL)
    {
      return convertToURI(layoutProcess, value);
    }
    else if (value instanceof ResourceKey)
    {
      return loadResource(layoutProcess, value);
    }
    else if (value instanceof Number)
    {
      final Number nval = (Number) value;
      if (type != null)
      {
        final CSSNumericType ntype = getUnitType(type);
        return new CSSNumericValue(ntype, nval.doubleValue());
      }
      return new CSSNumericValue(CSSNumericType.NUMBER, nval.doubleValue());

    }
    else if (value instanceof Color)
    {
      final Color color = (Color) value;
      return new CSSColorValue
              (color.getRed(), color.getGreen(),
                      color.getBlue(), color.getAlpha());
    }
    else
    {
      return new CSSRawValue(value);
    }
  }

}
