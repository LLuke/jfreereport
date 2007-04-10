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
 * $Id: ReptFunction.java,v 1.4 2007/04/01 13:51:53 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.text;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * This function returns text repeated Count times.
 *
 * @author Cedric Pronzato
 *
 */
public class ReptFunction implements Function
{
  public ReptFunction()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount != 2)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Type textType1 = parameters.getType(0);
    final Object textValue1 = parameters.getValue(0);
    final Type countType = parameters.getType(1);
    final Object countValue = parameters.getValue(1);

    final String text1 = typeRegistry.convertToText(textType1, textValue1);
    final int count = typeRegistry.convertToNumber(countType, countValue).intValue();
    if(text1 == null && count >= 0)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final StringBuffer buffer = new StringBuffer();
    for(int i=0; i<count; i++)
    {
      buffer.append(text1);
    }
    return new TypeValuePair(TextType.TYPE, buffer.toString());
  }

  public String getCanonicalName()
  {
    return "REPT";
  }

}