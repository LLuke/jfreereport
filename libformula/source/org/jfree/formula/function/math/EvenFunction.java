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
 * $Id: OddFunction.java,v 1.1 2007/05/17 23:32:12 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.math;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * This function returns the rounding of a number up to the nearest even
 * integer.
 * 
 * @author Cedric Pronzato
 * 
 */
public class EvenFunction implements Function
{

  public EvenFunction()
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

    if (test % 2 == 0) // even
    {
      if (intValue == 0)
      {
        if (result.doubleValue() < 0)
        {
          ret = new Integer(-2);
        }
        else if (result.doubleValue() > 0)
        {
          ret = new Integer(2);
        }
        else
        {
          ret = new Integer(0);
        }
      }
      else
      {
        ret = new Integer(intValue);
      }
    }
    else
    // odd
    {
      if (result.doubleValue() < 0)
      {
        ret = new Integer(intValue - 1);
      }
      else
      {
        ret = new Integer(intValue + 1);
      }
    }

    return new TypeValuePair(NumberType.GENERIC_NUMBER, ret);
  }

  public String getCanonicalName()
  {
    return "EVEN";
  }

}
