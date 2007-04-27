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
 * $Id: DefaultTypeRegistry.java,v 1.11 2007/04/10 14:10:41 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.typing;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.jfree.formula.FormulaContext;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.util.org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;


/**
 * Creation-Date: 02.11.2006, 12:46:08
 *
 * @author Thomas Morgner
 */
public class DefaultTypeRegistry implements TypeRegistry
{
  private FormulaContext context;
  private static final long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
  private static final BigDecimal ZERO = new BigDecimal(0);
  private static final BigDecimal MILLISECS = new BigDecimal(MILLISECS_PER_DAY);
  private NumberFormat[] numberFormats;
  private DateFormat[] dateFormats;

  public DefaultTypeRegistry()
  {
  }

  /**
   * Returns an comparator for the given types.
   *
   * @param type1
   * @param type2
   * @return
   */
  public ExtendedComparator getComparator(final Type type1, final Type type2)
  {
    final DefaultComparator comparator = new DefaultComparator();
    comparator.inititalize(context);
    return comparator;
  }

  /**
   * converts the object of the given type into a number. If the object is not
   * convertible, a NumberFormatException is thrown. If the given value is null
   * or not parsable as number, return null.
   *
   * @param type1
   * @param value
   * @return
   * @throws NumberFormatException if the type cannot be represented as number.
   */
  public Number convertToNumber(final Type type1, final Object value)
      throws TypeConversionException
  {
    Number n = null;
    
    if (value instanceof Number)
    {
      n = (Number) value;
    }
    else if (value instanceof Date)
    {
      n = convertDateToNumber((Date) value);
    }
    else if (value instanceof Boolean)
    {
      if (Boolean.TRUE.equals(value))
      {
        n = new Integer(1);
      }
      else
      {
        n = new Integer(0);
      }
    }
    else if (value instanceof String)
    {
      final String val = (String) value;
      try
      {
        // first, try to parse the value as a big-decimal.
        n = new BigDecimal(val);
      }
      catch (NumberFormatException e)
      {
        // ignore ..
      }
      for (int i = 0; i < dateFormats.length; i++)
      {
        try
        {
          final DateFormat dateFormat = dateFormats[i];
          final Date date = dateFormat.parse(val);
          n = convertDateToNumber(date);
          break;
        }
        catch (ParseException e)
        {
          // ignore as well ..
        }
      }

      
      // iterate through the various number and date formats.
      if(n == null)
      {
      for (int i = 0; i < numberFormats.length; i++)
      {
        try
        {
          final NumberFormat format = numberFormats[i];
          n = format.parse(val);
          break;
        }
        catch (ParseException e)
        {
          // ignore ..
        }
      }
      }
    }
    //System.out.println("["+value+"]Date converted to number "+n);
    if(n != null)
    {
      if(value instanceof Time || (type1 != null && type1.isFlagSet(Type.TIME_TYPE)))
      {
        // only return the decimal part
        final Double d = new Double(n.doubleValue() - n.intValue());
        return d;
      }
      else if(value instanceof java.sql.Date || (type1 != null && type1.isFlagSet(Type.DATE_TYPE)))
      {
        // only return the integer part
        return new Integer(n.intValue());
      }
      else
      {
        return n;
      }
    }
    
    throw new TypeConversionException();
  }

  private Date convertNumberToDate(final Number number) throws TypeConversionException
  {
    final Date javaDate = HSSFDateUtil.getJavaDate(number.doubleValue());
    if(javaDate == null)
    {
      throw new TypeConversionException();
    }
    /*final BigDecimal bd;
    if (number instanceof BigDecimal)
    {
      bd = (BigDecimal) number;
    }
    else
    {
      bd = new BigDecimal(number.toString());
    }

    final BigDecimal bigDecimal = bd.multiply(MILLISECS);
    //just a test to remove the millisecond part
    // final long longValue = (bigDecimal.longValue()/1000)*1000;
    return new Date(bigDecimal.longValue());*/
    return javaDate;
  }

  private Number convertDateToNumber(final Date date) throws TypeConversionException
  {
    /*final GregorianCalendar gc = new GregorianCalendar
        (context.getLocalizationContext().getTimeZone(), context.getLocalizationContext().getLocale());
    gc.setTime(date);
    final long timeInMillis = gc.getTime().getTime();
    final long days = timeInMillis / MILLISECS_PER_DAY;
    final long secs = timeInMillis - (days * MILLISECS_PER_DAY);

    final BigDecimal daysBd = new BigDecimal(days);
    final BigDecimal secsBd = new BigDecimal(secs);

    //fractional part must be between 0.0 to 0.99999
    //reprenting from 00:00:00 to 23:59:59
    final BigDecimal daySecs = secsBd.divide(MILLISECS, 25,BigDecimal.ROUND_UP);
    return daysBd.add(daySecs);*/
    final double excelDate = HSSFDateUtil.getExcelDate(date);
    if(!HSSFDateUtil.isValidExcelDate(excelDate)) {
      throw new TypeConversionException();
    }
    return new BigDecimal(excelDate);
  }

  public void initialize(final Configuration configuration,
                         final FormulaContext formulaContext)
  {
    this.context = formulaContext;
    this.numberFormats = loadNumberFormats();
    this.dateFormats = loadDateFormats();
  }

  protected DateFormat[] loadDateFormats()
  {
    final ArrayList formats = new ArrayList();
    formats.add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    formats.add(new SimpleDateFormat("yyyy-MM-dd"));
    formats.add(new SimpleDateFormat("hh:mm:ss"));
    // todo: This should also be configurable ..

    return (DateFormat[]) formats.toArray(new DateFormat[formats.size()]);
  }

  protected NumberFormat[] loadNumberFormats()
  {
    final ArrayList formats = new ArrayList();
    final DecimalFormat defaultFormat = new DecimalFormat("#0.###", new DecimalFormatSymbols(Locale.US));
    activateBigDecimalMode(defaultFormat);
    formats.add(defaultFormat);

    return (NumberFormat[]) formats.toArray(new NumberFormat[formats.size()]);
  }

  private void activateBigDecimalMode(final DecimalFormat format)
  {
    if (ObjectUtilities.isJDK14())
    {
      try
      {
        final Method method = DecimalFormat.class.getMethod
            ("setParseBigDecimal", new Class[]{Boolean.TYPE});
        method.invoke(format, new Object[]{Boolean.TRUE});
      }
      catch (Exception e)
      {
        // ignore it, as it will always fail on JDK 1.4 or lower ..
      }
    }
  }

  public String convertToText(final Type type1, final Object value)
  {
    if (value instanceof String)
    {
      return (String) value;
    }

    if (value == null)
    {
      return "";
    }

    if (value instanceof Boolean)
    {
      final Boolean b = (Boolean) value;
      if (Boolean.TRUE.equals(b))
      {
        return "TRUE";
      }
      else
      {
        return "FALSE";
      }
    }

    try
    {
      final Number n = convertToNumber(type1, value);
      final NumberFormat format = getDefaultNumberFormat();
      return format.format(n);
    }
    catch(TypeConversionException nfe)
    {
      // ignore ..
    }

    return value.toString();
  }

  public Boolean convertToLogical(final Type type1, final Object value) throws TypeConversionException
  {
    if (value instanceof Boolean)
    {
      return (Boolean) value;
    }

    if (value instanceof Number)
    {
      final Number num = (Number) value;
      if (!ZERO.equals(num))
      {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }

    if (value instanceof String)
    {
      final String str = (String) value;
      if ("TRUE".equalsIgnoreCase(str))
      {
        return Boolean.TRUE;
      }
      else if ("FALSE".equalsIgnoreCase(str))
      {
        return Boolean.FALSE;
      }
    }

    throw new TypeConversionException();
  }

  public Date convertToDate(final Type type1, final Object value) throws TypeConversionException
  {
    Date date = null;
    
    if(value instanceof Time || value instanceof java.sql.Date || value instanceof String)
    {
      final Number conv = convertToNumber(type1, value);
      if (conv == null)
      {
        throw new TypeConversionException();
      }
      date = convertNumberToDate(conv);
    }
    else if (value instanceof Date)
    {
      date = (Date)value;
    }
    else if (value instanceof Number)
    {
      date = convertNumberToDate((Number) value);
    }
    else
    {
      throw new TypeConversionException();
    }
    
    if(type1 != null && type1.isFlagSet(Type.TIME_TYPE))
    {
      return new Time(date.getTime());
    }
    else if(type1 != null && type1.isFlagSet(Type.DATE_TYPE))
    {
      return new java.sql.Date(date.getTime());
    }
    else
    {
    	if(date != null)
    	{
      	return date;
      }  
    }
    throw new TypeConversionException();
  }

  protected NumberFormat getDefaultNumberFormat()
  {
    final Locale locale = context.getLocalizationContext().getLocale();
    return new DecimalFormat("#0.#########", new DecimalFormatSymbols(locale));
  }

  /**
   * Checks, whether the target type would accept the specified value object and
   * value type.
   *
   * @param targetType
   * @param valueType
   * @param value
   */
  public TypeValuePair convertTo(final Type targetType,
                                 final TypeValuePair valuePair) throws TypeConversionException
  {
    if (targetType.isFlagSet(Type.ARRAY_TYPE))
    {
      // Array conversion requested.
      if (valuePair.getType().isFlagSet(Type.ARRAY_TYPE))
      {
        return convertArrayToArray(targetType, valuePair);
      }
      else
      {
        final Object retval = convertPlainToPlain
            (targetType, valuePair.getType(), valuePair.getValue());
        return new TypeValuePair(targetType, new Object[]{retval});
      }
    }

    final Object value = valuePair.getValue();
    final Object o = convertPlainToPlain(targetType, valuePair.getType(), value);
    if (value == o)
    {
      return valuePair;
    }
    return new TypeValuePair(targetType, o);
  }

  private Object convertPlainToPlain(final Type targetType,
                                     final Type type,
                                     final Object value) throws TypeConversionException
  {
    if (targetType.isFlagSet(Type.TEXT_TYPE))
    {
      if (type.isFlagSet(Type.TEXT_TYPE))
      {
        return value;
      }

      return convertToText(type, value);
    }

    if (targetType.isFlagSet(Type.DATE_TYPE))
    {
      if (type.isFlagSet(Type.DATE_TYPE))
      {
        return value;
      }

      try
      {
        final Number number = convertToNumber(type, value);
        return convertNumberToDate(number);
      }
      catch (TypeConversionException e)
      {
        throw e;
      }
    }

    if (targetType.isFlagSet(Type.NUMERIC_TYPE))
    {
      if (type.isFlagSet(Type.NUMERIC_TYPE))
      {
        return value;
      }

      return convertToNumber(type, value);
    }

    if (targetType.isFlagSet(Type.LOGICAL_TYPE))
    {
      if (type.isFlagSet(Type.LOGICAL_TYPE))
      {
        return value;
      }

      return convertToLogical(type, value);
    }

    // Unknown type - ignore it, crash later :)
    return value;
  }

  private TypeValuePair convertArrayToArray(final Type targetType,
                                            final TypeValuePair pair) throws TypeConversionException
  {
    final Type type = pair.getType();
    // the pair value is also an array ...
    final Object[] array = (Object[]) pair.getValue();
    final Object[] target = new Object[array.length];
    for (int i = 0; i < array.length; i++)
    {
      final Object converted = convertPlainToPlain(targetType, type, array[i]);
      if (converted == null)
      {
        return null;
      }
      target[i] = converted;
    }
    return new TypeValuePair(targetType, target);
  }
}
