/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://reporting.pentaho.org/libformula/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.formula.function.text;

import java.io.UnsupportedEncodingException;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.TextType;
import org.jfree.formula.util.URLEncoder;

/**
 * This function encodes a given text using the URL-Encoding schema. An optional
 * second parameter can be given to specify the character encoding that should
 * be used when converting text to bytes.
 *
 * @author Cedric Pronzato
 */
public class URLEncodeFunction implements Function
{
  public URLEncodeFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final Type textType = parameters.getType(0);
    final Object textValue = parameters.getValue(0);
    final String textResult =
        context.getTypeRegistry().convertToText(textType, textValue);

    if(textResult == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    String encodingResult = null;
    if(parameterCount == 2)
    {
      final Type encodingType = parameters.getType(1);
      final Object encodingValue = parameters.getValue(1);
      encodingResult = context.getTypeRegistry().convertToText(encodingType, encodingValue);
      if(encodingResult == null)
      {
        throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
      }
    }
    try
    {
      if (encodingResult == null)
      {
        return new TypeValuePair
            (TextType.TYPE, URLEncoder.encode(textResult, "ISO-8859-1"));
      }
      return new TypeValuePair
          (TextType.TYPE, URLEncoder.encode(textResult, encodingResult));

    }
    catch(UnsupportedEncodingException use)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }
  }

  public String getCanonicalName()
  {
    return "URLENCODE";
  }

}
