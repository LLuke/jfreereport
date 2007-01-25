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
 * $Id: DateFunction.java,v 1.7 2007/01/18 22:34:32 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.coretypes.DateType;
import org.jfree.formula.typing.coretypes.ErrorType;
import org.jfree.formula.typing.TypeRegistry;

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
      final TypeRegistry typeRegistry = context.getTypeRegistry();
      n1 = typeRegistry.convertToNumber(parameters.getType(0), parameters.getValue(0));
      Object value = parameters.getValue(1);
      n2 = typeRegistry.convertToNumber(parameters.getType(1), value);
      n3 = typeRegistry.convertToNumber(parameters.getType(2), parameters.getValue(2));
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
    //System.out.println("DEGUG Y:"+n1+" M:"+n2+"["+value+"] D:"+n3);
    final LocalizationContext localizationContext = context.getLocalizationContext();
    final GregorianCalendar gc = new GregorianCalendar
        (localizationContext.getTimeZone(), localizationContext.getLocale());
    gc.set(GregorianCalendar.DAY_OF_MONTH, n3.intValue());
    gc.set(GregorianCalendar.MONTH, n2.intValue()-1);
    gc.set(GregorianCalendar.YEAR, n1.intValue());
    gc.set(GregorianCalendar.MILLISECOND, 0);
    gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
    gc.set(GregorianCalendar.MINUTE, 0);
    gc.set(GregorianCalendar.SECOND, 0);

    final Date date = gc.getTime();
    //System.out.println("Created date("+n2+"): "+date);
    return new TypeValuePair(DateType.TYPE, date);
  }
}
