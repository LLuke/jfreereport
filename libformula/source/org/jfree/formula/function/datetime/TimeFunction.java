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
 * $Id: DateFunction.java,v 1.10 2007/02/22 21:34:46 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.sql.Time;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.TimeType;

/**
 * This fonction constructs a time from hours, minutes, and seconds.
 *
 * @author Cedric Pronzato
 */
public class TimeFunction implements Function
{
  public TimeFunction()
  {
  }

  public String getCanonicalName()
  {
    return "TIME";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() != 3)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    Number n1;
    Number n2;
    Number n3;
    try
    {
      final TypeRegistry typeRegistry = context.getTypeRegistry();
      n1 = typeRegistry.convertToNumber(parameters.getType(0), parameters.getValue(0));
      n2 = typeRegistry.convertToNumber(parameters.getType(1), parameters.getValue(1));
      n3 = typeRegistry.convertToNumber(parameters.getType(2), parameters.getValue(2));
    }
    catch (NumberFormatException e)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    if (n1 == null || n2 == null || n3 == null)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }
    final int hours = n1.intValue();
    final int minutes = n2.intValue();
    final int seconds = n3.intValue();
    
    final LocalizationContext localizationContext = context.getLocalizationContext();
    final GregorianCalendar gc = new GregorianCalendar
        (localizationContext.getTimeZone(), localizationContext.getLocale());
//    gc.set(GregorianCalendar.DAY_OF_MONTH, 0);
//    gc.set(GregorianCalendar.MONTH, 0);
//    gc.set(GregorianCalendar.YEAR, 0);
    gc.set(GregorianCalendar.MILLISECOND, 0);
    gc.set(GregorianCalendar.HOUR_OF_DAY, hours);
    gc.set(GregorianCalendar.MINUTE, minutes);
    gc.set(GregorianCalendar.SECOND, seconds);

    final Date date = gc.getTime();
    return new TypeValuePair(TimeType.TYPE, new Time(date.getTime()));
  }
}
