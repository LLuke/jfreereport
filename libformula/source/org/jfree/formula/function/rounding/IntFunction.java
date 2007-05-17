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
 * $Id: IntFunction.java,v 1.1 2007/05/12 23:53:15 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.rounding;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * This function returns a number down to the nearest integer.
 * 
 * @author Cedric Pronzato
 */
public class IntFunction implements Function
{
  final BigDecimal DELTA = new BigDecimal("0.000000000000000000000000000005");

  public IntFunction()
  {
  }

  public String getCanonicalName()
  {
    return "INT";
  }

  public TypeValuePair evaluate(final FormulaContext context,
      final ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() != 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    final Type type1 = parameters.getType(0);
    final Object value1 = parameters.getValue(0);
    final Number result = context.getTypeRegistry().convertToNumber(type1,
        value1);

    BigDecimal n = null;
    if (result instanceof BigDecimal)
    {
      n = (BigDecimal) result;
    }
    else
    {
      n = new BigDecimal(result.toString());
    }
    BigDecimal round = null;
    
    try
    {
      // no need to go further if the value is already an integer
      final Integer integer = new Integer(n.intValueExact());
      return new TypeValuePair(NumberType.GENERIC_NUMBER, integer);
    }
    catch(ArithmeticException e)
    {
      //ignore and continue
    }
    
    if(n.signum()<0)
    {
      n = n.subtract(DELTA);
      round = n.setScale(0, BigDecimal.ROUND_UP);
    }
    else
    {
      n = n.add(DELTA);
      round = n.setScale(1, BigDecimal.ROUND_DOWN);
    }
    Integer ret = new Integer(round.intValue());
    
    return new TypeValuePair(NumberType.GENERIC_NUMBER, ret);
  }
}
