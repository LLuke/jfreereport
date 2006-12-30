/**
 * =========================================
 * LibFormula : a free Java formula library
 * =========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libformula
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * $Id: IfFunction.java,v 1.4 2006/12/03 19:22:27 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.logical;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.LogicalType;
import org.jfree.formula.typing.Type;

/**
 * Creation-Date: 04.11.2006, 18:28:15
 *
 * @author Thomas Morgner
 */
public class IfFunction implements Function
{
  public IfFunction()
  {
  }

  public String getCanonicalName()
  {
    return "IF";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
      throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 2)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }

    final Type conditionType = parameters.getType(0);
    final Object conditionValue = parameters.getValue(0);
    final Boolean condition = context.getTypeRegistry().convertToLogical(conditionType, conditionValue);
    if(condition == null)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }
    if (Boolean.TRUE.equals(condition))
    {
      final Object value = parameters.getValue(1);
      final Type type = parameters.getType(1);
      return new TypeValuePair(type, value);
    }
    // if condition is false and no third parameter, return false
    if(parameterCount == 2 || parameters.getValue(2) == null)
    {
      return new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);
    }
    // else return third parameter
    final Object value = parameters.getValue(2);
    final Type type = parameters.getType(2);
    return new TypeValuePair(type, value);
  }
}
