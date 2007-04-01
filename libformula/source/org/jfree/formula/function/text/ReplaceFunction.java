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
import org.jfree.formula.Formula;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.parser.ParseException;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.TextType;
import org.jfree.util.Log;

/**
 * This function returns text where an old text is substituted with a new text.
 *
 * @author Cedric Pronzato
 *
 */
public class ReplaceFunction implements Function
{

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount != 4)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Type newTextType = parameters.getType(3);
    final Object newTextValue = parameters.getValue(3);
    final Type textType = parameters.getType(0);
    final Object textValue = parameters.getValue(0);
    final Type startType = parameters.getType(1);
    final Object startValue = parameters.getValue(1);
    final Type lengthType = parameters.getType(2);
    final Object lengthValue = parameters.getValue(2);

    final String newText = typeRegistry.convertToText(newTextType, newTextValue);
    final String text = typeRegistry.convertToText(textType, textValue);
    final Number start = typeRegistry.convertToNumber(startType, startValue);
    final Number length = typeRegistry.convertToNumber(lengthType, lengthValue);

    if(newText == null || text == null || start == null || length == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    // = LEFT(T;Start-1) & New & MID(T; Start+Len; LEN(T)))
    final StringBuffer buffer = new StringBuffer();
    buffer.append("LEFT(\"").append(text).append("\";").append(start.doubleValue()).append("-1) & \"");
    buffer.append(newText).append("\" & ");
    buffer.append("MID(\"").append(text).append("\";").append(start.doubleValue()).append("+");
    buffer.append(length.doubleValue()).append(";").append(text.length()).append("))");

    Formula formula = null;
    try
    {
      formula = new Formula(buffer.toString());
    } catch (ParseException e)
    {
      Log.debug("Nested formula parsing error", e);
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }
    formula.initialize(context);
    final Object result = formula.evaluate();

    return new TypeValuePair(TextType.TYPE, result.toString());
  }

  public String getCanonicalName()
  {
    return "REPLACE";
  }

}