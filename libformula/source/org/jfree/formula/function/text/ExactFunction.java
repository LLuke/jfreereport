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
 * $Id: ExactFunction.java,v 1.7 2007/04/10 14:10:41 taqua Exp $
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
import org.jfree.formula.typing.TypeConversionException;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * This function reports if two given text values are exactly equal using a case-sensitive comparison.
 *
 * @author Cedric Pronzato
 *
 */
public class ExactFunction implements Function
{
  private static final TypeValuePair RETURN_FALSE = new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
  private static final TypeValuePair RETURN_TRUE = new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);

  public ExactFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount != 2)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Type textType1 = parameters.getType(0);
    final Object textValue1 = parameters.getValue(0);
    final Type textType2 = parameters.getType(1);
    final Object textValue2 = parameters.getValue(1);


    // Numerical comparisons ignore "trivial" differences that
    // depend only on numeric precision of finite numbers.

    // This fixes the common rounding errors, that are encountered when computing "((1/3) * 3)", which results
    // in 0.99999 and not 1, as expected.
    try
    {
      final Number number1 = typeRegistry.convertToNumber(textType1, textValue1);
      final Number number2 = typeRegistry.convertToNumber(textType2, textValue2);

      final double delta =
          Math.abs(Math.abs(number1.doubleValue()) - Math.abs(number2.doubleValue()));
      if(delta < 0.00005)
      {
        return RETURN_TRUE;
      }
      return RETURN_FALSE;
    }
    catch(TypeConversionException tce)
    {
      // Ignore, try to compare them as strings ..
    }

    final String text1 = typeRegistry.convertToText(textType1, textValue1);
    final String text2 = typeRegistry.convertToText(textType2, textValue2);
    if(text1 == null || text2 == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    if (text1.equals(text2))
    {
      return RETURN_TRUE;
    }
    return RETURN_FALSE;
  }

  public String getCanonicalName()
  {
    return "EXACT";
  }

}