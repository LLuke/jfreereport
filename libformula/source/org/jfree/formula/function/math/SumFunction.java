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
 * SumFunction.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: SumFunction.java,v 1.1 2006/11/04 15:45:44 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.function.math;

import java.math.BigDecimal;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
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

  public TypeValuePair evaluate(FormulaContext context, TypeValuePair[] parameter)
  {
    BigDecimal computedResult = ZERO;
    for (int paramIdx = 0; paramIdx < parameter.length; paramIdx++)
    {
      final TypeValuePair param = parameter[paramIdx];
      Object value = param.getValue();
      if (value == null)
      {
        continue;
      }

      final Type type = param.getType();
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
    }

    return new TypeValuePair(NumberType.GENERIC_NUMBER, computedResult);
  }

  private BigDecimal compute(Object value,
                             final BigDecimal computedResult)
  {
    if (value instanceof Number == false)
    {
      throw new IllegalArgumentException(String.valueOf(value));
    }


    if (value instanceof BigDecimal)
    {
      return computedResult.add((BigDecimal) value);
    }
    else
    {
      Number n = (Number) value;
      BigDecimal nval = new BigDecimal(n.toString());
      return computedResult.add(nval);
    }
  }
}
