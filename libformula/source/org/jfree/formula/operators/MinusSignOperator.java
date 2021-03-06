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
 * $Id: MinusSignOperator.java,v 1.8 2007/04/10 14:10:41 taqua Exp $
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
public class MinusSignOperator implements PrefixOperator
{
  private static final BigDecimal ZERO = new BigDecimal(0);


  public MinusSignOperator()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final TypeValuePair value1)
      throws EvaluationException
  {
    final Type type = value1.getType();
    final Object val = value1.getValue();
    if (val == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_NA_VALUE);
    }
    
    if (type.isFlagSet(Type.NUMERIC_TYPE))
    {
      final TypeRegistry typeRegistry = context.getTypeRegistry();
      // return the same as zero minus value.
      final Number number = typeRegistry.convertToNumber(type, val);
      final BigDecimal value = getAsBigDecimal(number);
      return new TypeValuePair(type, ZERO.subtract(value));
    }

    if(val instanceof Number)
    {
      final BigDecimal value = getAsBigDecimal((Number)val);
      return new TypeValuePair(type, ZERO.subtract(value));
    }

    throw new EvaluationException
        (LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
  }

  private BigDecimal getAsBigDecimal(final Number number)
  {
    if (number instanceof BigDecimal)
    {
      return (BigDecimal) number;
    }
    return new BigDecimal(number.toString());
  }

  public String toString()
  {
    return "-";
  }
}
