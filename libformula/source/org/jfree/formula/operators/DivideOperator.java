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
 * $Id: DivideOperator.java,v 1.11 2007/05/21 21:39:41 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.operators;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.util.NumberUtil;
import org.jfree.util.Log;

/**
 * A division operation. This operation expects two valid numbers.
 *
 *
 * @author Thomas Morgner
 */
public class DivideOperator extends AbstractNumericOperator
{
  public DivideOperator()
  {
  }

  public Number evaluate(final Number number1, final Number number2) throws EvaluationException
  {
    final BigDecimal bd1 = new BigDecimal(number1.toString());
    final BigDecimal bd2 = new BigDecimal(number2.toString());
    if (bd2.signum() == 0)
    {
      // prevent a division by zero ..
      Log.debug ("Preventing a Division by Zero: " + number2);
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARITHMETIC_VALUE);
    }
    final BigDecimal divide = bd1.divide(bd2, 40, BigDecimal.ROUND_HALF_UP);
    return NumberUtil.removeTrailingZeros(divide);
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
