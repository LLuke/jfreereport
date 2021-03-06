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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.logical;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * Creation-Date: 04.11.2006, 18:28:15
 *
 * @author Thomas Morgner
 */
public class AndFunction implements Function
{
  private static final TypeValuePair RETURN_TRUE = new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
  private static final TypeValuePair RETURN_FALSE = new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);

  public AndFunction()
  {
  }

  public String getCanonicalName()
  {
    return "AND";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
      throws EvaluationException
  {
    final int length = parameters.getParameterCount();
    if(length < 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    for (int i = 0; i < length; i++)
    {
      final Object value = parameters.getValue(i);
      final Type type1 = parameters.getType(i);
      final Boolean condition = context.getTypeRegistry().convertToLogical(type1, value);
      if(condition == null)
      {
        throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
      }
      if (Boolean.FALSE.equals(condition))
      {
        return RETURN_FALSE;
      }
    }
    return RETURN_TRUE;
  }
}
