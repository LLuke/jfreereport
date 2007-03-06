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
 * $Id: DateDifFunction.java,v 1.2 2007/03/01 16:55:30 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.jfree.formula.EvaluationException;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LibFormulaErrorValue;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.Function;
import org.jfree.formula.function.ParameterCallback;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.NumberType;

/**
 * This function returns the number of years, months, or days between two date
 * numbers.<br/>
 * <p/>
 * The Format is a code from the following table, entered as text, that
 * specifies the format you want: <TABLE> <TR> <TH>format</TH> <TH>Returns the
 * number of</TH> </TR> <TR> <TD>y</TD> <TD>Years</TD> </TR> <TR> <TD>m</TD>
 * <TD>Months. If there is not a complete month between the dates, 0 will be
 * returned.</TD> </TR> <TR> <TD>d</TD> <TD>Days</TD> </TR> <TR> <TD>md</TD>
 * <TD>Days, ignoring months and years</TD> </TR> <TR> <TD>ym</TD> <TD>Months,
 * ignoring years</TD> </TR> <TR> <TD>yd</TD> <TD>Days, ignoring years</TD>
 * </TR> <TR> <TD></TD> <TD></TD> </TR> </TABLE>
 *
 * @author Cedric Pronzato
 */
public class DateDifFunction implements Function
{
  public static final String YEARS_CODE = "y";
  public static final String MONTHS_CODE = "m";
  public static final String DAYS_CODE = "d";
  public static final String DAYS_IGNORING_YEARS = "yd";
  public static final String MONTHS_IGNORING_YEARS = "ym";
  public static final String DAYS_IGNORING_MONTHS_YEARS = "md";

//  private static final String[] CODES = {
//    YEARS_CODE, MONTHS_CODE, DAYS_CODE, DAYS_IGNORING_YEARS,
//    MONTHS_IGNORING_YEARS, DAYS_IGNORING_MONTHS_YEARS
//  };
//

  public DateDifFunction()
  {
  }

  public String getCanonicalName()
  {
    return "DATEDIF";
  }

  public TypeValuePair evaluate(final FormulaContext context,
                                final ParameterCallback parameters)
      throws EvaluationException
  {
    if (parameters.getParameterCount() != 3)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }

    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final Date date1 = typeRegistry.convertToDate
        (parameters.getType(0), parameters.getValue(0));
    final Date date2 = typeRegistry.convertToDate
        (parameters.getType(1), parameters.getValue(1));
    final String formatCode = typeRegistry.convertToText
        (parameters.getType(2), parameters.getValue(2));

    if (date1 == null || date2 == null || formatCode == null || "".equals(
        formatCode))
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    final LocalizationContext localizationContext = context.getLocalizationContext();
    final TimeZone timeZone = localizationContext.getTimeZone();
    final Locale locale = localizationContext.getLocale();
    final GregorianCalendar calandar1 =
        new GregorianCalendar(timeZone, locale);
    calandar1.setTime(date1);

    final GregorianCalendar calandar2 =
        new GregorianCalendar(timeZone, locale);
    calandar2.setTime(date2);

    int res;

    if (DateDifFunction.YEARS_CODE.equals(formatCode))
    {
      res = calandar2.get(Calendar.YEAR) - calandar1.get(Calendar.YEAR);
    }
    else if (DateDifFunction.MONTHS_CODE.equals(formatCode))
    {
      final int month1 = calandar1.get(Calendar.MONTH);
      final int month2 = calandar2.get(Calendar.MONTH);
      final int year1 = calandar1.get(Calendar.YEAR);
      final int year2 = calandar2.get(Calendar.YEAR);

      res = (year2 - year1) * 12 +
            month2 - month1;
    }
    else if (DateDifFunction.DAYS_IGNORING_MONTHS_YEARS.equals(formatCode))
    {
      // The number of days between Date1 and Date2, as if Date1 and
      // Date2 were in the same month and the same year.

      // Not sure what happens to leap years, so this solution may be invalid.
      calandar1.set(Calendar.YEAR, calandar2.get(Calendar.YEAR));
      calandar1.set(Calendar.MONTH, calandar2.get(Calendar.MONTH));

      res = calandar2.get(Calendar.DAY_OF_MONTH) -
            calandar1.get(Calendar.DAY_OF_MONTH);
    }
    else if (DateDifFunction.DAYS_CODE.equals(formatCode))
    {
      final int dayOfYear1 = calandar1.get(Calendar.DAY_OF_YEAR);
      final int dayOfYear2 = calandar2.get(Calendar.DAY_OF_YEAR);
      final int year1 = calandar1.get(Calendar.YEAR);
      final int year2 = calandar2.get(Calendar.YEAR);

      final GregorianCalendar workingCalandar =
          new GregorianCalendar(timeZone, locale);

      res = dayOfYear2 - dayOfYear1;

      // run through the inner years, without counting the border years
      // Always run from the lower to the higher, so that we prevent infinite
      // loops ..
      final int targetYear = Math.max(year1, year2);
      for (int i = Math.min(year1, year2); i < targetYear; i++)
      {
        workingCalandar.set(Calendar.YEAR, i);
        res += workingCalandar.getActualMaximum(Calendar.DAY_OF_YEAR);
      }
    }
    else if (DateDifFunction.MONTHS_IGNORING_YEARS.equals(formatCode))
    {
      final int month1 = calandar1.get(Calendar.MONTH);
      final int month2 = calandar2.get(Calendar.MONTH);

      res = month2 - month1;
    }
    else if (DateDifFunction.DAYS_IGNORING_YEARS.equals(formatCode))
    {
      //Isn't that a stupid case? How could we count the days while ignoring
      //how much days there are in each months without using the year?

      // The number of days between Date1 and Date2, as if Date1 and Date2
      // were in the same year.

      // Not sure what happens to leap years, so this solution may be invalid.
      calandar1.set(Calendar.YEAR, calandar2.get(Calendar.YEAR));
      final int dayOne = calandar1.get(Calendar.DAY_OF_YEAR);
      final int dayTwo = calandar2.get(Calendar.DAY_OF_YEAR);
      res = Math.abs(dayOne - dayTwo);
    }
    else
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    if (res < 0)
    {
      throw new EvaluationException(
          LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }

    return new TypeValuePair(NumberType.GENERIC_NUMBER, new Integer(res));
  }
}
