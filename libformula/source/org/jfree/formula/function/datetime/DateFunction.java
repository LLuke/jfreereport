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
 * $Id: DateFunction.java,v 1.12 2007/04/01 13:51:52 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.DateType;

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

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() != 3)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final Number n1 = typeRegistry.convertToNumber(parameters.getType(0), parameters.getValue(0));
    final Number n2 = typeRegistry.convertToNumber(parameters.getType(1), parameters.getValue(1));
    final Number n3 = typeRegistry.convertToNumber(parameters.getType(2), parameters.getValue(2));

    //System.out.println("DEGUG Y:"+n1+" M:"+n2+"["+value+"] D:"+n3);
    final LocalizationContext localizationContext = context.getLocalizationContext();
    final GregorianCalendar gc = new GregorianCalendar
        (localizationContext.getTimeZone(), localizationContext.getLocale());
    gc.set(Calendar.DAY_OF_MONTH, n3.intValue());
    gc.set(Calendar.MONTH, n2.intValue()-1);
    gc.set(Calendar.YEAR, n1.intValue());
    gc.set(Calendar.MILLISECOND, 0);
    gc.set(Calendar.HOUR_OF_DAY, 0);
    gc.set(Calendar.MINUTE, 0);
    gc.set(Calendar.SECOND, 0);

    final Date date = gc.getTime();
    return new TypeValuePair(DateType.TYPE, date);
  }
}
