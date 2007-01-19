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
 * $Id: IsNaFunction.java,v 1.2 2007/01/14 18:28:57 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
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

/**
 * This function returns true if the parameter is of error and not of error type NA.
 * 
 * @author Cedric Pronzato
 *
 */
public class IsErrFunction implements Function
{

  private static final TypeValuePair RETURN_TRUE = new TypeValuePair(LogicalType.TYPE, Boolean.TRUE);
  private static final TypeValuePair RETURN_FALSE = new TypeValuePair(LogicalType.TYPE, Boolean.FALSE);

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    if(parameters.getParameterCount() != 1) {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    
    try
    {
      final Type type = parameters.getType(0);
      Object value = parameters.getValue(0);
      
      if(ErrorType.TYPE.equals(type) && value instanceof ErrorValue)
      {
        final ErrorValue na = (ErrorValue)value;
        if(na.getErrorCode() == LibFormulaErrorValue.ERROR_NA)
        {
          return RETURN_FALSE;
        }
        else
        {
          return RETURN_TRUE;
        }
      }
    } catch (EvaluationException e)
    {
      if(e.getErrorValue().getErrorCode() == LibFormulaErrorValue.ERROR_NA)
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