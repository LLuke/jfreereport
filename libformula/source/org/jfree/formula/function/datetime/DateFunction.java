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
 * $Id: DateFunction.java,v 1.5 2006/12/30 13:50:19 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.util.GregorianCalendar;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.DateType;
import org.jfree.formula.typing.coretypes.ErrorType;

/**
 * Creation-Date: 04.11.2006, 18:59:11
 *
 * @author Thomas Morgner
 */
public class DateFunction implements Function
{
  public DateFunction()
  {
  }

  public String getCanonicalName()
  {
    return "DATE";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters)
  {
    if (parameters.getParameterCount() != 3)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_ARGUMENTS));
    }
    Number n1;
    Number n2;
    Number n3;
    try
    {
      n1 = context.getTypeRegistry().convertToNumber(parameters.getType(0), parameters.getValue(0));
      n2 = context.getTypeRegistry().convertToNumber(parameters.getType(1), parameters.getValue(1));
      n3 = context.getTypeRegistry().convertToNumber(parameters.getType(2), parameters.getValue(2));
    }
    catch (NumberFormatException e)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }
    catch (EvaluationException e)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    if (n1 == null || n2 == null || n3 == null)
    {
      return new TypeValuePair(ErrorType.TYPE, new LibFormulaErrorValue(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT));
    }

    final GregorianCalendar gc = new GregorianCalendar
        (context.getLocalizationContext().getTimeZone(), context.getLocalizationContext().getLocale());
    gc.set(GregorianCalendar.YEAR, n1.intValue());
    gc.set(GregorianCalendar.MONTH, n2.intValue());
    gc.set(GregorianCalendar.DAY_OF_MONTH, n3.intValue());

    return new TypeValuePair(DateType.TYPE, gc.getTime());
  }
}
