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
import org.jfree.formula.typing.TypeRegistry;

/**
 * This function returns a selected number of text characters from the left.<br/>
 * This function depends on <code>MidFunction</code>.
 *
 * @see MidFunction
 *
 * @author Cedric Pronzato
 *
 */
public class LeftFunction implements Function
{
  public LeftFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1 || parameterCount > 2)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Type textType = parameters.getType(0);
    final Object textValue = parameters.getValue(0);

    final String text = typeRegistry.convertToText(textType, textValue);
    int l = -1;
    if(parameterCount == 2)
    {
      final Type lengthType = parameters.getType(1);
      final Object lengthValue = parameters.getValue(1);
      final Number length = typeRegistry.convertToNumber(lengthType, lengthValue);
      if(length != null)
      {
        l = length.intValue();
      }
    }
    else
    {
      l = 1;
    }

    if(text == null || l < 0)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    // Note that MID(T;1;Length) produces the same results as LEFT(T;Length).
    final MidFunction function = new MidFunction();
    return function.process(text, new Integer(1), new Integer(l));
  }

  public String getCanonicalName()
  {
    return "LEFT";
  }

}