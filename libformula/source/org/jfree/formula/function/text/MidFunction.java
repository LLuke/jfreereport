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
 * $Id: MidFunction.java,v 1.4 2007/04/01 13:51:53 taqua Exp $
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
import org.jfree.formula.typing.coretypes.TextType;

/**
 * This function returns extracted text, given an original text, starting position, and length.
 *
 * @author Cedric Pronzato
 *
 */
public class MidFunction implements Function
{
  public MidFunction()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount != 3)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Type textType = parameters.getType(0);
    final Object textValue = parameters.getValue(0);
    final Type startType = parameters.getType(1);
    final Object startValue = parameters.getValue(1);
    final Type lengthType = parameters.getType(2);
    final Object lengthValue = parameters.getValue(2);

    final String text = typeRegistry.convertToText(textType, textValue);
    final Number start = typeRegistry.convertToNumber(startType, startValue);
    final Number length = typeRegistry.convertToNumber(lengthType, lengthValue);

    if(length.doubleValue() < 0 || start.doubleValue() < 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    return new TypeValuePair(TextType.TYPE, process(text, start, length));
  }

  public String process(final String text, final Number start, final Number length)
  {
    int lengthValue = length.intValue();
    final int startValue = start.intValue()-1;
    if(startValue >= text.length())
    {
      return "";
    }

    if((lengthValue +startValue) > text.length())
    {
      lengthValue = text.length()-startValue;
    }

    return text.substring(startValue, startValue +lengthValue);
  }

  public String getCanonicalName()
  {
    return "MID";
  }

}