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
 * $Id: PercentageOperator.java,v 1.7 2007/04/10 14:10:41 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.operators;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;

/**
 * Creation-Date: 02.11.2006, 10:27:03
 *
 * @author Thomas Morgner
 */
public class PercentageOperator implements PostfixOperator
{
  private static final BigDecimal HUNDRED = new BigDecimal(100);


  public PercentageOperator()
  {
  }


  public TypeValuePair evaluate(final FormulaContext context, final TypeValuePair value1)
      throws EvaluationException
  {
    final Type type = value1.getType();
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    if (type.isFlagSet(Type.NUMERIC_TYPE) == false &&
        type.isFlagSet(Type.ANY_TYPE) == false)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    // return the same as zero minus value.
    final Number number = typeRegistry.convertToNumber(type, value1.getValue());
    final BigDecimal value = OperatorUtility.getAsBigDecimal(number);
    final BigDecimal divide = value.divide(HUNDRED, 2,BigDecimal.ROUND_HALF_UP);
    return new TypeValuePair(type, divide);
  }

  public String toString()
  {
    return "%";
  }

}

