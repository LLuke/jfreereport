/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * ------------
 * PercentageOperator.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PercentageOperator.java,v 1.1 2006/11/04 15:44:33 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.operators;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.lvalues.TypeValuePair;

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


  public TypeValuePair evaluate(FormulaContext context, TypeValuePair value1)
      throws EvaluationException
  {
    final Type type = value1.getType();
    if (type.isFlagSet(Type.NUMERIC_TYPE) == false)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue
          (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    // return the same as zero minus value.
    final Number number =
        context.getTypeRegistry().convertToNumber(type, value1.getValue());
    if (number == null)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue
          (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    final BigDecimal value = OperatorUtility.getAsBigDecimal(number);
    return new TypeValuePair(type, value.divide(HUNDRED, BigDecimal.ROUND_HALF_UP));
  }

  public String toString()
  {
    return "%";
  }

}

