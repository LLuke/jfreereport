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
 * $Id: DateFunction.java,v 1.14 2007/05/07 22:57:01 mimil Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.util.Calendar;
import java.util.Date;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.NumberType;
import org.jfree.formula.util.DateUtil;

/**
 * This function extracts the day of week from a date. <p/> The returned value
 * depends of the Type passed as second argument using the following table:<br/>
 * <TABLE>
 * <TR>
 * <TH>Day of Week</TH>
 * <TH>Type=1 Result</TH>
 * <TH>Type=2 Result</TH>
 * <TH>Type=3 Result</TH>
 * </TR>
 * <TR>
 * <TD>Sunday</TD>
 * <TD> 1</TD>
 * <TD> 7</TD>
 * <TD> 6</TD>
 * </TR>
 * <TR>
 * <TD>Monday</TD>
 * <TD> 2</TD>
 * <TD> 1</TD>
 * <TD> 0</TD>
 * </TR>
 * <TR>
 * <TD>Tuesday</TD>
 * <TD> 3</TD>
 * <TD> 2</TD>
 * <TD> 1</TD>
 * </TR>
 * <TR>
 * <TD>Wednesday</TD>
 * <TD> 4</TD>
 * <TD> 3</TD>
 * <TD> 2</TD>
 * </TR>
 * <TR>
 * <TD>Thursday</TD>
 * <TD> 5</TD>
 * <TD> 4</TD>
 * <TD> 3</TD>
 * </TR>
 * <TR>
 * <TD>Friday</TD>
 * <TD> 6</TD>
 * <TD> 5</TD>
 * <TD> 4</TD>
 * </TR>
 * <TR>
 * <TD>Saturday</TD>
 * <TD> 7</TD>
 * <TD> 6</TD>
 * <TD> 5</TD>
 * </TR>
 * </TABLE>
 * 
 * @author Cedric Pronzato
 */
public class WeekDayFunction implements Function
{
  public WeekDayFunction()
  {
  }

  public String getCanonicalName()
  {
    return "WEEKDAY";
  }

  public TypeValuePair evaluate(final FormulaContext context,
      final ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() > 2)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    final TypeRegistry typeRegistry = context.getTypeRegistry();

    final Date d = typeRegistry.convertToDate(parameters.getType(0), parameters
        .getValue(0));
    int type = 1; // default is Type 1
    if (parameters.getParameterCount() == 2)
    {
      final Number n = typeRegistry.convertToNumber(parameters.getType(1),
          parameters.getValue(1));
      if (n == null)
      {
        throw new EvaluationException(
            LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
      }
      type = n.intValue();
      if (type < 1 || type > 3)
      {
        throw new EvaluationException(
            LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
      }
    }

    if (d == null)
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final Calendar gc = DateUtil.createCalendar(d, context
        .getLocalizationContext());

    int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
    // in java Sunday = 1 (= Type 1 of openformula)
    return new TypeValuePair(NumberType.GENERIC_NUMBER, new Integer(
        convertType(dayOfWeek, type)));
  }

  public int convertType(int currentDayOfWeek, int type)
  {
    if (type == 1)
    {
      return currentDayOfWeek;
    }
    else if (type == 2)
    {
      final int i = ((currentDayOfWeek + 6) % 8);
      if(i == 7)
      {
        return i;
      }
      else
      {
        return i + 1;
      }
    }
    else
    {
      return (currentDayOfWeek + 5) % 7;
    }
  }
}
