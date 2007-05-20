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
 * $Id: DateFunction.java,v 1.14 2007/05/07 22:57:01 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.math.BigDecimal;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.NumberType;
import org.jfree.formula.util.NumberUtil;

/**
 * This function extracts the hour (0 through 23) from a time.
 * 
 * @author Cedric Pronzato
 */
public class HourFunction implements Function
{
  private static final BigDecimal HOUR_24 = new BigDecimal(24);

  public HourFunction()
  {
  }

  public String getCanonicalName()
  {
    return "HOUR";
  }

  public TypeValuePair evaluate(final FormulaContext context,
      final ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() != 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final Number n = typeRegistry.convertToNumber(parameters.getType(0),
        parameters.getValue(0));

    if (n == null)
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final BigDecimal bd = NumberUtil.getAsBigDecimal(n);
    final BigDecimal round1 = new BigDecimal(NumberUtil
            .performIntRounding(bd).intValue());
    final BigDecimal dayFraction = bd.subtract(round1);
    final Integer hour = NumberUtil.performIntRounding(dayFraction
        .multiply(HOUR_24));

    return new TypeValuePair(NumberType.GENERIC_NUMBER, hour);
  }
}
