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
package org.jfree.formula.operators;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * This has to be implemented manually if we want to support arbitary precision.
 * Damn, do I have to implement the logarithm computation as well? For now:
 * Ignore that and use doubles!
 *
 * @author Thomas Morgner
 */
public class PowerOperator implements InfixOperator
{
  public PowerOperator()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final TypeValuePair value1,
                                final TypeValuePair value2)
      throws EvaluationException
  {
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Number number1 =
        typeRegistry.convertToNumber(value1.getType(), value1.getValue());
    final Number number2 =
        typeRegistry.convertToNumber(value2.getType(), value2.getValue());
    if (number1 == null || number2 == null)
    {
      throw new EvaluationException
          (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final double result = Math.pow(number1.doubleValue(), number2.doubleValue());
    final Double value = new Double(result);
    return new TypeValuePair(NumberType.GENERIC_NUMBER, value);
  }

  public int getLevel()
  {
    return 0;
  }

  public String toString()
  {
    return "POW";
  }

  public boolean isLeftOperation()
  {
    return false;
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
