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
 * $Id: SumFunction.java,v 1.9 2007/04/10 14:10:41 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.math;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeConversionException;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * Creation-Date: 31.10.2006, 17:39:19
 *
 * @author Thomas Morgner
 */
public class SumFunction implements Function
{
  public static final BigDecimal ZERO = new BigDecimal(0);

  public SumFunction()
  {
  }

  public String getCanonicalName()
  {
    return "SUM";
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters)
      throws EvaluationException
  {
    BigDecimal computedResult = ZERO;
    final int parameterCount = parameters.getParameterCount();
    
    if(parameterCount == 0)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    for (int paramIdx = 0; paramIdx < parameterCount; paramIdx++)
    {
      final Object value = parameters.getValue(paramIdx);
      final Type type = parameters.getType(paramIdx);
      if (value == null)
      {
        continue;
      }

      try
      {
        final Number n = context.getTypeRegistry().convertToNumber(type, value);
        computedResult = compute(n, computedResult);
      }
      catch(TypeConversionException tce)
      {
        // The sum function ignores invalid values ..
      }
    }

      //    TODO: Array not yet supported
      /*final Type type = parameters.getType(paramIdx);
      if (type.isFlagSet(Type.ARRAY_TYPE) == false)
      {
        continue;
      }
      if (type.isFlagSet(Type.NUMERIC_TYPE) == false)
      {
        continue;
      }

      final Object[] values = (Object[]) value;
      for (int arrayIdx = 0; arrayIdx < values.length; arrayIdx++)
      {
        computedResult = compute(values[arrayIdx], computedResult);
      }
    }*/

    return new TypeValuePair(NumberType.GENERIC_NUMBER, computedResult);
  }

  private BigDecimal compute(final Object value,
                             final BigDecimal computedResult)
  {
    if (value == null)
    {
      // no-op ..
      return computedResult;
    }

    if (value instanceof Number == false)
    {
      // Paranoid checks can be disabled later ..
      throw new IllegalArgumentException(String.valueOf(value));
    }

    if (value instanceof BigDecimal)
    {
      return computedResult.add((BigDecimal) value);
    }
    else
    {
      final Number n = (Number) value;
      final BigDecimal nval = new BigDecimal(n.toString());
      return computedResult.add(nval);
    }
  }
}
