/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: ExactFunction.java,v 1.3 2007/01/22 15:54:02 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.text;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * This function reports if two given text values are exactly equal using a case-sensitive comparison.
 *
 * @author Cedric Pronzato
 *
 */
public class ExactFunction implements Function
{
  public ExactFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount != 2)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Type textType1 = parameters.getType(0);
    final Object textValue1 = parameters.getValue(0);
    final Type textType2 = parameters.getType(1);
    final Object textValue2 = parameters.getValue(1);

    final String text1 = typeRegistry.convertToText(textType1, textValue1);
    final String text2 = typeRegistry.convertToText(textType2, textValue2);

    if(text1 == null || text2 == null)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    // Numerical comparisons ignore "trivial" differences that
    // depend only on numeric precision of finite numbers.
    if((textType1.isFlagSet(Type.NUMERIC_TYPE) || textValue1 instanceof Number)
        && (textType2.isFlagSet(Type.NUMERIC_TYPE) || textValue2 instanceof Number))
    {
      final BigDecimal number1 = (BigDecimal)typeRegistry.convertToNumber(textType1, textValue1);
      final BigDecimal number2 = (BigDecimal)typeRegistry.convertToNumber(textType2, textValue2);

      final BigInteger n1 = number1.toBigInteger();
      final BigInteger n2 = number2.toBigInteger();
      if (n1.equals(n2))
      {
        return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
      }
      return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
    }

    if (text1.equals(text2))
    {
      return new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
    }
    return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
  }

  public String getCanonicalName()
  {
    return "EXACT";
  }

}