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
 * AbstractStyleFunction.java
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

import java.awt.Image;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.values.CSSRawValue;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;
import org.jfree.layouting.model.DocumentContext;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.ui.Drawable;

/**
 * Creation-Date: 16.04.2006, 14:36:25
 *
 * @author Thomas Morgner
 */
public abstract class AbstractStyleFunction implements StyleFunction
{
  private static final Class[] KNOWN_FACTORY_TYPES =
          {Drawable.class, Image.class};

  protected AbstractStyleFunction()
  {
  }

  protected String getStringParameter(LayoutProcess layoutProcess,
                                      LayoutElement layoutElement,
                                      CSSValue value) throws
          FunctionEvaluationException
  {
    final CSSValue notAFunctionAnymore =
            getParameter(layoutProcess, layoutElement, value);
    if (notAFunctionAnymore instanceof CSSStringValue)
    {
      CSSStringValue strVal = (CSSStringValue) notAFunctionAnymore;
      return strVal.getValue();
    }
    String retval = notAFunctionAnymore.getCSSText();
    if (retval == null)
    {
      throw new FunctionEvaluationException
              ("Value " + notAFunctionAnymore + " is invalid");
    }
    return retval;
  }

  protected CSSValue getParameter(LayoutProcess layoutProcess,
                                  LayoutElement layoutElement,
                                  CSSValue value) throws
          FunctionEvaluationException
  {
    if (value instanceof CSSFunctionValue)
    {
      final CSSFunctionValue functionValue = (CSSFunctionValue) value;
      final StyleFunction function =
              FunctionFactory.getInstance().getFunction(
                      functionValue.getFunctionName());
      final Object retval = function.getValue
              (layoutProcess, layoutElement, functionValue);
      // now, map the retval into something useable ...
      if (retval instanceof CSSValue)
      {
        return (CSSValue) retval;
      }
      else
      {
        return new CSSRawValue(retval);
      }
    }
    else
    {
      return value;
    }
  }

  protected ResourceKey createURI(String uri, LayoutProcess layoutProcess)
  {
    try
    {
      final DocumentContext context = layoutProcess.getDocumentContext();
      final ResourceKey base = DocumentContextUtility.getBaseResource(context);
      final ResourceManager resourceManager = layoutProcess
              .getResourceManager();
      if (base == null)
      {
        return resourceManager.createKey(uri);
      }
      else
      {
        return resourceManager.deriveKey(base, uri);
      }
    }
    catch (Exception e)
    {
      return null;
    }
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

  protected CSSNumericType getUnitType(String typeText)
  {
    if (typeText == null)
    {
      throw new NullPointerException();
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

  protected CSSNumericValue convertToNumber(String stringValue)
          throws FunctionEvaluationException
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
    try
    {
      final String number = txt.substring
              (0, txt.length() - type.getType().length()).trim();
      final double nVal = Double.parseDouble(number);
      return new CSSNumericValue(type, nVal);
    }
    catch (Exception e)
    {
      throw new FunctionEvaluationException("Failed to convert value to number",
              e);
    }
  }


  protected CSSResourceValue convertToURI(final LayoutProcess process,
                                          final Object value)
          throws FunctionEvaluationException
  {
    return convertToURI(process, value, KNOWN_FACTORY_TYPES);
  }

  protected CSSResourceValue convertToURI(final LayoutProcess process,
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
      if (baseKey == null)
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
      throw new FunctionEvaluationException("Failed to create URI: Resource loading failed ", e);
    }
  }


  protected CSSValue loadResource(final LayoutProcess layoutProcess,
                                  final Object value) throws
          FunctionEvaluationException
  {
    try
    {
      ResourceManager manager = layoutProcess.getResourceManager();
      final ResourceKey baseKey = DocumentContextUtility.getBaseResource
              (layoutProcess.getDocumentContext());

      final Resource resource = manager.create
              ((ResourceKey) value, baseKey, KNOWN_FACTORY_TYPES);
      return new CSSResourceValue(resource);
    }
    catch (ResourceCreationException e)
    {
      throw new FunctionEvaluationException("Failed to load the resource", e);
    }
    catch (ResourceLoadingException e)
    {
      throw new FunctionEvaluationException("Failed to load the resource", e);
    }
  }

}
