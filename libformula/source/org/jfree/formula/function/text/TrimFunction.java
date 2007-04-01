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

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * This function returns the given text free of leading spaces.
 * Removes all leading and trailing spaces and all extra spaces inside the text.
 *
 * @author Cedric Pronzato
 * @see http://mercury.ccil.org/%7Ecowan/OF/textfuncs.html
 */
public class TrimFunction implements Function
{
  public TrimFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final Type type1 = parameters.getType(0);
    final Object value1 = parameters.getValue(0);
    final String result = context.getTypeRegistry().convertToText(type1, value1);

    if(result == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    // remove all unnecessary spaces ..
    // we dont use regexps, because they are JDK 1.4, but this library is aimed
    // for JDK 1.2.2

    final char[] chars = result.toCharArray();
    final StringBuffer b = new StringBuffer(chars.length);
    boolean removeNextWs = true;

    for (int i = 0; i < chars.length; i++)
    {
      final char c = chars[i];
      if (Character.isWhitespace(c))
      {
        if (removeNextWs)
        {
          continue;
        }
        b.append(c);
        removeNextWs = true;
        continue;
      }

      b.append(c);
      removeNextWs = false;
    }

    // now check whether the last char is a whitespace and remove that one
    // if neccessary
    final String trimmedResult;
    if (removeNextWs)
    {
      trimmedResult = b.substring(0, b.length() - 1);
    }
    else
    {
      trimmedResult = b.toString();
    }

    return new TypeValuePair(TextType.TYPE, trimmedResult);
  }

  public String getCanonicalName()
  {
    return "TRIM";
  }

}
