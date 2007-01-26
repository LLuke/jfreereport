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
 * $Id: DivideOperator.java,v 1.5 2007/01/18 21:52:06 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.operators;

import java.math.BigDecimal;

import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * A division operation. This operation expects two valid numbers.
 *
 *
 * @author Thomas Morgner
 */
public class DivideOperator implements InfixOperator
{
  public DivideOperator()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                TypeValuePair value1, TypeValuePair value2)
      throws EvaluationException
  {
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Object raw1 = value1.getValue();
    final Object raw2 = value2.getValue();

    if (raw1 == null && raw2 == null)
    {
      return null;
    }
  
    final Number number1 =
        typeRegistry.convertToNumber(value1.getType(), raw1);
    final Number number2 =
        typeRegistry.convertToNumber(value2.getType(), raw2);
    if (number1 == null && number2 == null)
    {
      throw new EvaluationException
          (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final Type resultType = NumberType.GENERIC_NUMBER;
    if (number1 == null)
    {
      return new TypeValuePair(resultType, new BigDecimal(0));
    }
    if (number2 == null)
    {
      throw new EvaluationException
          (LibFormulaErrorValue.ERROR_ARITHMETIC_VALUE);
    }

    final BigDecimal bd1 = new BigDecimal(number1.toString());
    final BigDecimal bd2 = new BigDecimal(number2.toString());
    if (bd2.signum() == 0)
    {
      // prevent a division by zero ..
      throw new EvaluationException
          (LibFormulaErrorValue.ERROR_ARITHMETIC_VALUE);
    }
    return new TypeValuePair(resultType, bd1.divide(bd2, BigDecimal.ROUND_HALF_UP));
  }

  public int getLevel()
  {
    return 100;
  }


  public String toString()
  {
    return "/";
  }

  public boolean isLeftOperation()
  {
    return true;
  }

  /**
   * Defines, whether the operation is associative. For associative operations,
   * the evaluation order does not matter, if the operation appears more than
   * once in an expression, and therefore we can optimize them a lot better than
   * non-associative operations (ie. merge constant parts and precompute them
   * once).
   *
   * @return true, if the operation is associative, false otherwise
   */
  public boolean isAssociative()
  {
    return false;
  }

}
