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
 * FunctionUtilities.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FunctionUtilities.java,v 1.3 2006/07/29 18:57:12 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.functions;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.values.StyleValueFunction;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceManager;

/**
 * Creation-Date: 04.07.2006, 14:30:10
 *
 * @author Thomas Morgner
 */
public class FunctionUtilities
{
  private FunctionUtilities()
  {
  }


  public static CSSResourceValue loadResource(final LayoutProcess process,
                                              final Object value)
          throws FunctionEvaluationException
  {
    final Class[] supportedResourceTypes =
            process.getOutputMetaData().getSupportedResourceTypes();
    if (supportedResourceTypes.length == 0)
    {
      throw new FunctionEvaluationException
              ("Failed to create URI: Resource loading failed as the output " +
                      "target does not support any resource types.");
    }
    return loadResource(process, value, supportedResourceTypes);
  }

  public static CSSResourceValue loadResource(final LayoutProcess process,
                                              final Object value,
                                              final Class[] type)
          throws FunctionEvaluationException
  {
    // ok, this is going to be expensive. Kids, you dont wanna try this at home ...
    final ResourceManager manager = process.getResourceManager();
    final ResourceKey baseKey = DocumentContextUtility.getBaseResource
            (process.getDocumentContext());
    try
    {
      final ResourceKey key;
      if (value instanceof ResourceKey)
      {
        key = (ResourceKey) value;
      }
      else if (baseKey == null)
      {
        key = manager.createKey(value);
      }
      else
      {
        key = manager.deriveKey(baseKey, value);
      }

      final Resource res = manager.create(key, baseKey, type);
      return new CSSResourceValue(res);
    }
    catch (Exception e)
    {
      throw new FunctionEvaluationException
              ("Failed to create URI: Resource loading failed: " + e.getMessage(), e);
    }
  }


  public static CSSValue parseValue (final LayoutProcess process,
                                     final String text)
  {
    CSSNumericValue val = convertToNumber(text);
    if (val != null)
    {
      return val;
    }

    // next step: That may be expensive, but we search for URLs ..
    try
    {
      return loadResource(process, text);
    }
    catch (FunctionEvaluationException e)
    {
      // ignore, it was just an attempt ...
    }

    return new CSSStringValue(CSSStringType.STRING, text);
  }

  public static CSSNumericValue parseNumberValue (String text, String type)
          throws FunctionEvaluationException
  {
    CSSNumericValue val = convertToNumber(text, getUnitType(type));
    if (val != null)
    {
      return val;
    }
    throw new FunctionEvaluationException("Unable to convert to number.");
  }

  public static CSSNumericValue parseNumberValue (String text)
          throws FunctionEvaluationException
  {
    CSSNumericValue val = convertToNumber(text);
    if (val != null)
    {
      return val;
    }
    throw new FunctionEvaluationException("Unable to convert to number.");
  }

  private static CSSNumericType[] KNOWN_TYPES = {
          CSSNumericType.PERCENTAGE,
          CSSNumericType.EM,
          CSSNumericType.EX,
          CSSNumericType.CM,
          CSSNumericType.MM,
          CSSNumericType.INCH,
          CSSNumericType.PT,
          CSSNumericType.PC,
          CSSNumericType.DEG,
          CSSNumericType.PX
  };

  private static CSSNumericValue convertToNumber(String stringValue)
  {
    final String txt = stringValue.trim();
    CSSNumericType type = null;
    for (int i = 0; i < KNOWN_TYPES.length; i++)
    {
      final CSSNumericType numericType = KNOWN_TYPES[i];
      if (txt.endsWith(numericType.getType()))
      {
        type = numericType;
      }
    }
    if (type == null)
    {
      type = CSSNumericType.NUMBER;
    }
    final String number = txt.substring
            (0, txt.length() - type.getType().length()).trim();
    return convertToNumber(number, type);
  }

  private static CSSNumericValue convertToNumber(String stringValue,
                                                 CSSNumericType type)
  {
    if (type == null)
    {
      type = CSSNumericType.NUMBER;
    }
    try
    {
      final String number = stringValue.trim();
      final double nVal = Double.parseDouble(number);
      return new CSSNumericValue(type, nVal);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public static CSSNumericType getUnitType(String typeText)
  {
    if (typeText == null)
    {
      return CSSNumericType.NUMBER;
    }
    final String txt = typeText.trim();
    for (int i = 0; i < KNOWN_TYPES.length; i++)
    {
      final CSSNumericType numericType = KNOWN_TYPES[i];
      if (txt.equalsIgnoreCase(numericType.getType()))
      {
        return numericType;
      }
    }
    return CSSNumericType.NUMBER;
  }


  public static String resolveString(LayoutProcess layoutProcess,
                                 LayoutElement layoutElement,
                                 CSSValue value)
          throws FunctionEvaluationException
  {
    final CSSValue notAFunctionAnymore =
            resolveParameter(layoutProcess, layoutElement, value);
    if (notAFunctionAnymore instanceof CSSStringValue)
    {
      CSSStringValue strVal = (CSSStringValue) notAFunctionAnymore;
      return strVal.getValue();
    }

    // Falling back to the Value itself ..

    String retval = notAFunctionAnymore.getCSSText();
    if (retval == null)
    {
      throw new FunctionEvaluationException
              ("Value " + notAFunctionAnymore + " is invalid");
    }
    return retval;
  }

  public static CSSValue resolveParameter(LayoutProcess layoutProcess,
                                      LayoutElement layoutElement,
                                      CSSValue value)
          throws FunctionEvaluationException
  {
    if (value instanceof CSSFunctionValue == false)
    {
      return value;
    }

    final CSSFunctionValue functionValue = (CSSFunctionValue) value;

    final StyleValueFunction function =
            FunctionFactory.getInstance().getStyleFunction
                    (functionValue.getFunctionName());
    if (function == null)
    {
      throw new FunctionEvaluationException
              ("Unsupported Function: " + functionValue);
    }
    return function.evaluate
            (layoutProcess, layoutElement, functionValue);
  }

  public static ResourceKey createURI(String uri, LayoutProcess layoutProcess)
  {
    try
    {
      final DocumentContext context = layoutProcess.getDocumentContext();
      final ResourceKey base = DocumentContextUtility.getBaseResource(context);
      final ResourceManager resourceManager =
              layoutProcess.getResourceManager();

      if (base != null)
      {
        try
        {
          return resourceManager.deriveKey(base, uri);
        }
        catch(ResourceKeyCreationException ex)
        {
          // ignore ..
        }
      }
      return resourceManager.createKey(uri);
    }
    catch (Exception e)
    {
      return null;
    }
  }
}
