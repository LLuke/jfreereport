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
 * $Id: AbsFunction.java,v 1.3 2007/04/10 14:10:41 taqua Exp $
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
import org.jfree.formula.typing.coretypes.NumberType;
import org.jfree.formula.util.NumberUtil;

/**
 * This function returns the rounding a number up to the nearest odd integer,
 * where "up" means "away from 0".
 * 
 * @author Cedric Pronzato
 * 
 */
public class OddFunction implements Function
{
  private static final TypeValuePair RETURN_ONE = new TypeValuePair(
      NumberType.GENERIC_NUMBER, new Integer(1));

  private static final TypeValuePair RETURN_MINUSONE = new TypeValuePair(
      NumberType.GENERIC_NUMBER, new Integer(-1));

  public OddFunction()
  {
  }

  public TypeValuePair evaluate(FormulaContext context,
      ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final Type type1 = parameters.getType(0);
    final Object value1 = parameters.getValue(0);
    final Number result = context.getTypeRegistry().convertToNumber(type1,
        value1);

    final int intValue = result.intValue();
    Integer ret = null;

    int test = intValue;
    if (intValue < 0)
    {
      test = intValue * -1;
    }

    if (test % 2 == 1)
    {
      ret = new Integer(intValue);
    }
    else
    {
      if (intValue < 0)
      {
        System.err.println(intValue);
        ret = new Integer(intValue - 1);
      }
      else
      {
        // we cannot yet know if 0 or -0
        if (intValue == 0)
        {
          final BigDecimal bd = NumberUtil.getAsBigDecimal(result);
          if (bd.signum() < 0)
          {
            return RETURN_MINUSONE;
          }
          else
          {
            return RETURN_ONE;
          }
        }
        // else
        ret = new Integer(intValue + 1);
      }
    }

    return new TypeValuePair(NumberType.GENERIC_NUMBER, ret);
  }

  public String getCanonicalName()
  {
    return "ODD";
  }

}
