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
 * $Id: SubstituteFunction.java,v 1.1 2007/02/04 10:29:28 mimil Exp $
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
import org.jfree.formula.typing.coretypes.TextType;

/**
 * This function returns text where an old text is substituted with a new text.
 *
 * @author Cedric Pronzato
 */
public class SubstituteFunction implements Function
{
  public SubstituteFunction()
  {
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters)
      throws EvaluationException
  {
    final int parameterCount = parameters.getParameterCount();
    if (parameterCount < 3 || parameterCount > 4)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Type newTextType = parameters.getType(2);
    final Object newTextValue = parameters.getValue(2);
    final Type textType = parameters.getType(0);
    final Object textValue = parameters.getValue(0);
    final Type oldTextType = parameters.getType(1);
    final Object oldTextValue = parameters.getValue(1);

    final String newText = typeRegistry.convertToText(newTextType,
        newTextValue);
    final String text = typeRegistry.convertToText(textType, textValue);
    final String oldText = typeRegistry.convertToText(oldTextType,
        oldTextValue);

    if (newText == null || text == null || oldText == null)
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    if (parameterCount == 3)
    {
      int index = text.indexOf(oldText);
      if (index == -1)
      {
        return new TypeValuePair(TextType.TYPE, text);
      }

      final StringBuffer result = new StringBuffer(text);
      while (index >= 0)
      {
        result.replace(index, index + oldText.length(), newText);
        index = result.indexOf(oldText, index + newText.length());
      }
      return new TypeValuePair(TextType.TYPE, result.toString());
    }

    // Instead of replacing all occurences, the user only requested to replace
    // a specific one.
    final Type whichType = parameters.getType(3);
    final Object whichValue = parameters.getValue(3);
    final Number n = typeRegistry.convertToNumber(whichType, whichValue);

    if (n == null || n.intValue() < 1)
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final int nthOccurence = n.intValue();

    int index = text.indexOf(oldText);
    if (index == -1)
    {
      return new TypeValuePair(TextType.TYPE, text);
    }

    int counter = 1;
    final StringBuffer result = new StringBuffer(text);
    while (index >= 0)
    {
      if (counter == nthOccurence)
      {
        result.replace(index, index + oldText.length(), newText);
        index = result.indexOf(oldText, index + newText.length());
      }
      else
      {
        index = text.indexOf(oldText, index + 1);
      }
      counter += 1;
    }
    return new TypeValuePair(TextType.TYPE, result.toString());
  }

  public String getCanonicalName()
  {
    return "SUBSTITUTE";
  }

}