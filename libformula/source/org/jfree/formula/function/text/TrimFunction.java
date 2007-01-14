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
 * $Id: DateFunction.java,v 1.6 2006/12/30 14:54:38 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.text;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * This function returns the given text free of leading and trailing spaces.
 * Internal multiple spaces are replaced by one.
 * 
 * @author Cedric Pronzato
 *
 */
public class TrimFunction implements Function
{

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 1)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    final Type type1 = parameters.getType(0);
    final Object value1 = parameters.getValue(0);
    final String result = context.getTypeRegistry().convertToText(type1, value1);
    
    if(result == null)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }
    
    return new TypeValuePair(TextType.TYPE, result.trim().replaceAll("\\s+", " "));
  }

  public String getCanonicalName()
  {
    return "TRIM";
  }

}
