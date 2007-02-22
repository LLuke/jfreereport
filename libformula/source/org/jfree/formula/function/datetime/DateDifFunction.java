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
 * $Id: DateFunction.java,v 1.9 2007/01/26 22:11:52 mimil Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.formula.function.datetime;

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

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This function returns the number of years, months, or days between two date numbers.<br/>
 * 
 * The Format is a code from the following table, entered as text, that specifies the format you want:
 * <TABLE>
 * <TR>
 *   <TH>format</TH>
 *   <TH>Returns the number of</TH>
 * </TR>
 * <TR>
 *   <TD>y</TD>
 *   <TD>Years</TD>
 * </TR>
 * <TR>
 *   <TD>m</TD>
 *   <TD>Months. If there is not a complete month between the dates, 0 will be
 * returned.</TD>
 * </TR>
 * <TR>
 *   <TD>d</TD>
 *   <TD>Days</TD>
 * </TR>
 * <TR>
 *   <TD>md</TD>
 *   <TD>Days, ignoring months and years</TD>
 * </TR>
 * <TR>
 *   <TD>ym</TD>
 *   <TD>Months, ignoring years</TD>
 * </TR>
 * <TR>
 *   <TD>yd</TD>
 *   <TD>Days, ignoring years</TD>
 * </TR>
 * <TR>
 *   <TD></TD>
 *   <TD></TD>
 * </TR>
 * </TABLE>
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
  
  public static final String[] CODES = {
    YEARS_CODE, MONTHS_CODE, DAYS_CODE, DAYS_IGNORING_YEARS,
    MONTHS_IGNORING_YEARS, DAYS_IGNORING_MONTHS_YEARS
  };
  
  public DateDifFunction()
  {
  }

  public String getCanonicalName()
  {
    return "DATEDIF";
  }

  public TypeValuePair evaluate(FormulaContext context,
                                ParameterCallback parameters) throws EvaluationException
  {
    if (parameters.getParameterCount() != 3)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_ARGUMENTS_VALUE);
    }
    
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    final Date date1 = typeRegistry.convertToDate(parameters.getType(0), parameters.getValue(0));
    final Date date2 = typeRegistry.convertToDate(parameters.getType(1), parameters.getValue(1));
    final String formatCode = typeRegistry.convertToText(parameters.getType(2), parameters.getValue(2));

    if (date1 == null || date2 == null || formatCode == null || "".equals(formatCode))
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }
    
    final LocalizationContext localizationContext = context.getLocalizationContext();
    final TimeZone timeZone = localizationContext.getTimeZone();
    final Locale locale = localizationContext.getLocale();
    final GregorianCalendar calandar1 = new GregorianCalendar
        (timeZone, locale);
    calandar1.setTime(date1);
    final GregorianCalendar calandar2 = new GregorianCalendar
    (timeZone, locale);
    calandar2.setTime(date2);
    
    int res = 0;
    
    if(YEARS_CODE.equals(formatCode))
    {
      res = calandar2.get(GregorianCalendar.YEAR) - calandar1.get(GregorianCalendar.YEAR);
    }
    else if(MONTHS_CODE.equals(formatCode))
    {
      final int month1 = calandar1.get(GregorianCalendar.MONTH);
      final int month2 = calandar2.get(GregorianCalendar.MONTH);
      final int year1 = calandar1.get(GregorianCalendar.YEAR);
      final int year2 = calandar2.get(GregorianCalendar.YEAR);
      
      res = (year2-year1)*12 + month2-month1;
    }
    else if(DAYS_IGNORING_MONTHS_YEARS.equals(formatCode))
    {
      res = calandar2.get(GregorianCalendar.DAY_OF_MONTH) - calandar2.get(GregorianCalendar.DAY_OF_MONTH);
    }
    else if(DAYS_CODE.equals(formatCode))
    {
      final int dayOfYear1 = calandar1.get(GregorianCalendar.DAY_OF_YEAR);
      final int dayOfYear2 = calandar2.get(GregorianCalendar.DAY_OF_YEAR);
      final int year1 = calandar1.get(GregorianCalendar.YEAR);
      final int year2 = calandar2.get(GregorianCalendar.YEAR);
      
      final GregorianCalendar workingCalandar = new GregorianCalendar
      (timeZone, locale);
      
      for(int i = year2; i>year1; i--)
      {
        workingCalandar.set(GregorianCalendar.YEAR, i);
        res += workingCalandar.getMaximum(GregorianCalendar.DAY_OF_YEAR);
      }
      
      res += dayOfYear2 - dayOfYear1;
    }
    else if(MONTHS_IGNORING_YEARS.equals(formatCode))
    {
      final int month1 = calandar1.get(GregorianCalendar.MONTH);
      final int month2 = calandar2.get(GregorianCalendar.MONTH);
      
      res = month2 - month1;
    }
    else if(DAYS_IGNORING_YEARS.equals(formatCode))
    {
      //Isn't that a stupid case? How could we count the days while ignoring
      //how much days there are in each months without using the year?
      throw new NotImplementedException();
    }
    else
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }
    
    if(res < 0)
    {
      throw new EvaluationException(LibFormulaErrorValue.ERROR_INVALID_ARGUMENT_VALUE);
    }
      
    return new TypeValuePair(NumberType.GENERIC_NUMBER, new Integer(res));
  }
}
