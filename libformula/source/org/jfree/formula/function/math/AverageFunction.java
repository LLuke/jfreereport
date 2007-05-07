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
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * This function returns the average of the number sequence.
 *
 * @author Cedric Pronzato
 */
public class AverageFunction implements Function
{

  public AverageFunction()
  {
  }

  public String getCanonicalName()
  {
    return "AVERAGE";
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters)
      throws EvaluationException
  {
    final SumFunction sumFunction = new SumFunction();
    final TypeValuePair sum = sumFunction.evaluate(context, parameters);
    
    final Number n = context.getTypeRegistry().convertToNumber(sum.getType(),
        sum.getValue());
    final BigDecimal avg = new BigDecimal(n.toString()).divide(new BigDecimal(
        parameters.getParameterCount()));
    
    return new TypeValuePair(NumberType.GENERIC_NUMBER, avg);
  }
}
