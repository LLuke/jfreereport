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
 * FormatFunction.java
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

import java.text.Format;
import java.util.Date;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.layouter.i18n.LocalizationContext;
import org.jfree.layouting.layouter.style.values.CSSFormatedValue;
import org.jfree.layouting.layouter.style.values.CSSRawValue;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.model.LayoutElement;

/**
 * Creation-Date: 16.04.2006, 14:14:42
 *
 * @author Thomas Morgner
 */
public class FormatFunction extends AbstractStyleFunction
{
  public FormatFunction()
  {
  }

  // takes two or three parameters.
  // param1: What shall we format
  // param2: What data do we expect
  // param3: What format string shall we use
  public CSSValue getValue(final LayoutProcess layoutProcess,
                           final LayoutElement element,
                           final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    final CSSValue[] params = function.getParameters();
    if (params.length < 2)
    {
      throw new FunctionEvaluationException(
              "Illegal parameter count");
    }
    final CSSValue rawValue = getParameter(layoutProcess, element, params[0]);
    final String typeValue = getStringParameter(layoutProcess, element, params[1]);
    final LocalizationContext localizationContext =
            DocumentContextUtility.getLocalizationContext
                    (layoutProcess.getDocumentContext());

    if ("date".equals(typeValue))
    {
      if (params.length < 3)
      {
        return getDateValue(rawValue,
                localizationContext.getDateFormat(element.getLocale()));
      }
      else
      {
        final String format = getStringParameter(layoutProcess, element, params[2]);
        return getDateValue(rawValue,
                localizationContext.getDateFormat(format, element.getLocale()));
      }
    }
    else if ("time".equals(typeValue))
    {
      if (params.length != 2)
      {
        throw new FunctionEvaluationException();
      }
      return getDateValue(rawValue,
              localizationContext.getTimeFormat(element.getLocale()));
    }
    else if ("number".equals(typeValue))
    {
      if (params.length < 3)
      {
        return getNumberValue(rawValue,
                localizationContext.getDateFormat(element.getLocale()));
      }
      else
      {
        final String format = getStringParameter(layoutProcess, element, params[2]);
        return getNumberValue(rawValue,
                localizationContext.getNumberFormat(format, element.getLocale()));
      }
    }
    else if ("integer".equals(typeValue))
    {
      if (params.length != 2)
      {
        throw new FunctionEvaluationException();
      }
      return getNumberValue(rawValue,
              localizationContext.getIntegerFormat(element.getLocale()));
    }
    throw new FunctionEvaluationException("FormatType not recognized");
  }


  private CSSValue getNumberValue
          (final CSSValue rawValue,
           final Format format)
          throws FunctionEvaluationException
  {
    final double number;
    if (rawValue instanceof CSSStringValue)
    {
      try
      {
        final CSSStringValue strVal = (CSSStringValue) rawValue;
        CSSNumericValue nval = convertToNumber(strVal.getValue());
        number = nval.getValue();
      }
      catch(FunctionEvaluationException fee)
      {
        return rawValue;
      }
    }
    else if (rawValue instanceof CSSNumericValue)
    {
      CSSNumericValue nval = (CSSNumericValue) rawValue;
      number = nval.getValue();
    }
    else
    {
      // Raw-Values should not have been created for number values
      throw new FunctionEvaluationException("Not a numeric value.");
    }

    final Double obj = new Double(number);
    return new CSSFormatedValue
            (format.format(obj), obj, format);
  }

  private CSSValue getDateValue
          (final CSSValue rawValue,
           final Format format)
          throws FunctionEvaluationException
  {
    Date date;
    if (rawValue instanceof CSSRawValue)
    {
      CSSRawValue cssRawValue = (CSSRawValue) rawValue;
      Object o = cssRawValue.getValue();
      if (o instanceof Date == false)
      {
        throw new FunctionEvaluationException("Not a date value.");
      }
      date = (Date) o;
    }
    else if (rawValue instanceof CSSFormatedValue)
    {
      CSSFormatedValue fval = (CSSFormatedValue) rawValue;
      Object o = fval.getRaw();
      if (o instanceof Date == false)
      {
        throw new FunctionEvaluationException("Not a date value.");
      }
      date = (Date) o;
    }
    else
    {
      throw new FunctionEvaluationException("Not a date value.");
    }

    return new CSSFormatedValue
            (format.format(date), date, format);
  }
}
