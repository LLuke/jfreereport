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
 * $Id: ExactFunction.java,v 1.2 2007/01/14 18:28:57 mimil Exp $
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
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.coretypes.TextType;

/**
 * This function returns extracted text, given an original text, starting position, and length.
 * 
 * @author Cedric Pronzato
 *
 */
public class MidFunction implements Function
{

  public TypeValuePair evaluate(FormulaContext context, ParameterCallback parameters) throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount != 3)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    
    final Type textType = parameters.getType(0);
    final Object textValue = parameters.getValue(0);
    final Type startType = parameters.getType(1);
    final Object startValue = parameters.getValue(1);
    final Type lengthType = parameters.getType(2);
    final Object lengthValue = parameters.getValue(2);
 
    final String text = typeRegistry.convertToText(textType, textValue);
    final Number start = typeRegistry.convertToNumber(startType, startValue);
    final Number length = typeRegistry.convertToNumber(lengthType, lengthValue);
    
    if(text == null || start == null || length == null ||
        length.intValue() < 0 || start.intValue() < 1)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }
    
    return process(text, start, length);
  }

  public TypeValuePair process(final String text, final Number start, final Number length)
  {
    int l = length.intValue();
    int s = start.intValue()-1;
    if(s >= text.length())
    {
      return new TypeValuePair(TextType.TYPE, "");
    }
    if((l+s) > text.length())
    {
      l = text.length()-s;
    }

    final String txt = text.substring(s, s+l);
    return new TypeValuePair(TextType.TYPE, txt);
  }

  public String getCanonicalName()
  {
    return "MID";
  }

}