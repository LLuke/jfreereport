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
 * $Id: IsErrFunction.java,v 1.3 2007/04/01 13:51:52 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.information;

import org.jfree.formula.ErrorValue;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.LogicalType;
import org.jfree.util.Log;

/**
 * This function returns true if the parameter is of error and not of error type NA.
 *
 * @author Cedric Pronzato
 */
public class IsErrFunction implements Function
{

  private static final TypeValuePair RETURN_TRUE = new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
  private static final TypeValuePair RETURN_FALSE = new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);

  public IsErrFunction()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() != 1)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    try
    {
      final Type type = parameters.getType(0);
      final Object value = parameters.getValue(0);

      if (ErrorType.TYPE.equals(type) && value instanceof ErrorValue)
      {
        Log.warn ("Passing errors around is deprecated. Throw exceptions instead.");
        final ErrorValue na = (ErrorValue) value;
        if (na.getErrorCode() == LibFormulaErrorValue.ERROR_NA)
        {
          return RETURN_FALSE;
        }
        else
        {
          return RETURN_TRUE;
        }
      }
    }
    catch (EvaluationException e)
    {
      if (e.getErrorValue().getErrorCode() == LibFormulaErrorValue.ERROR_NA)
      {
        return RETURN_FALSE;
      }
      else
      {
        return RETURN_TRUE;
      }
    }

    return RETURN_FALSE;
  }

  public String getCanonicalName()
  {
    return "ISERR";
  }

}
