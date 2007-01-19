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
 * $Id: HasChangedFunction.java,v 1.2 2006/12/03 19:22:27 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.formula.function.information;

import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.EvaluationException;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.LogicalType;

/**
 * This function uses an index to return a value from a list of values. 
 * The first value index is 1, 2 for the second and so on.
 *
 * @author Cedric Pronzato
 */
public class ChooseFunction implements Function
{
  public ChooseFunction()
  {
  }

  public String getCanonicalName()
  {
    return "CHOOSE";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
      throws EvaluationException
  {

    if(parameters.getParameterCount() <= 2)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    final Type indexType = parameters.getType(0);
    final Object indexValue = parameters.getValue(0);
    
    if(indexType.isFlagSet(Type.NUMERIC_TYPE) || indexValue instanceof Number)
    {
      final int index= context.getTypeRegistry().convertToNumber(indexType, indexValue).intValue();
      if(index >= 1 && index < parameters.getParameterCount())
      {
        return new TypeValuePair(parameters.getType(index), parameters.getValue(index));
      }      
    }

    return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
  }
}