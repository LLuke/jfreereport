/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * ------------
 * DefaultTypeRegistry.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultTypeRegistry.java,v 1.1 2006/11/04 15:43:46 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.typing;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.jfree.formula.LocalizationContext;
import org.jfree.formula.lvalues.TypeValuePair;
import org.jfree.formula.operators.InfixOperator;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 02.11.2006, 12:46:08
 *
 * @author Thomas Morgner
 */
public class DefaultTypeRegistry implements TypeRegistry
{
  private LocalizationContext localizationContext;
  private static final long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;
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
  public ExtendedComparator getComparator(Type type1, Type type2)
  {
    return null;
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
  public Number convertToNumber(Type type1, Object value)
      throws NumberFormatException
  {
    if (value instanceof Number)
    {
      return (Number) value;
    }

    if (value instanceof Date)
    {
      return convertDateToNumber((Date) value);
    }

    if (value instanceof Boolean)
    {
      if (Boolean.TRUE.equals(value))
      {
        return new Integer(1);
      }
      return new Integer(0);
    }

    if (value instanceof String)
    {
      final String val = (String) value;
      try
      {
        // first, try to parse the value as a big-decimal.
        return new BigDecimal(val);
      }
      catch (NumberFormatException e)
      {
        // ignore ..
      }
      // iterate through the various number and date formats.
      for (int i = 0; i < numberFormats.length; i++)
      {
        try
        {
          final NumberFormat format = numberFormats[i];
          return format.parse(val);
        }
        catch (ParseException e)
        {
          // ignore ..
        }
      }
      for (int i = 0; i < dateFormats.length; i++)
      {
        try
        {
          final DateFormat dateFormat = dateFormats[i];
          final Date date = dateFormat.parse(val);
          return convertDateToNumber(date);
        }
        catch (ParseException e)
        {
          // ignore as well ..
        }
      }
    }
    return new Integer(0);
  }

  private Date convertNumberToDate(final Number number)
  {
    final BigDecimal bd;
    if (number instanceof BigDecimal)
    {
      bd = (BigDecimal) number;
    }
    else
    {
      bd = new BigDecimal(number.toString());
    }
    final BigDecimal bigDecimal = bd.multiply(MILLISECS);
    return new Date(bigDecimal.longValue());
  }

  private Number convertDateToNumber(final Date date)
  {
    final GregorianCalendar gc = new GregorianCalendar
        (localizationContext.getTimeZone(), localizationContext.getLocale());
    gc.setTime(date);
    final long timeInMillis = gc.getTime().getTime();
    final long days = timeInMillis / MILLISECS_PER_DAY;
    final long secs = timeInMillis - (days * MILLISECS_PER_DAY);

    final BigDecimal daysBd = new BigDecimal(days);
    final BigDecimal secsBd = new BigDecimal(secs);
    final BigDecimal daySecs = secsBd.divide
        (MILLISECS, BigDecimal.ROUND_HALF_UP);
    return daysBd.add(daySecs);
  }

  /**
   * Computes the combined type that would result in the combination of the
   * given types.
   *
   * @param type1
   * @param type2
   * @return
   */
  public Type combine(Type type1, InfixOperator operator, Type type2)
  {
    final boolean anyType1 = type1.isFlagSet(Type.ANY_TYPE);
    final boolean anyType2 = type2.isFlagSet(Type.ANY_TYPE);
    if (anyType1 && anyType2)
    {
      return type1;
    }
    else if (anyType1)
    {
      return type2;
    }
    else if (anyType2)
    {
      return type1;
    }

    return null;
  }

  public void initialize(final Configuration configuration,
                         final LocalizationContext localizationContext)
  {
    this.localizationContext = localizationContext;
    this.numberFormats = loadNumberFormats();
    this.dateFormats = loadDateFormats();
  }

  protected DateFormat[] loadDateFormats()
  {
    ArrayList formats = new ArrayList();
    final SimpleDateFormat defaultDateFormat =
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

    formats.add(defaultDateFormat);
    return (DateFormat[]) formats.toArray(new DateFormat[formats.size()]);
  }

  protected NumberFormat[] loadNumberFormats()
  {
    ArrayList formats = new ArrayList();
    final DecimalFormat defaultFormat =
        new DecimalFormat("#0.###", new DecimalFormatSymbols(Locale.US));
    activateBigDecimalMode(defaultFormat);
    formats.add(defaultFormat);

    return (NumberFormat[]) formats.toArray(new NumberFormat[formats.size()]);
  }

  private void activateBigDecimalMode(DecimalFormat format)
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

  public String convertToText(Type type1, Object value)
  {
    if (value instanceof String)
    {
      return (String) value;
    }

    if (value == null)
    {
      return "";
    }

    Number n = convertToNumber(type1, value);
    if (n != null)
    {
      NumberFormat format = getDefaultNumberFormat();
      return format.format(n);
    }

    return value.toString();
  }

  protected NumberFormat getDefaultNumberFormat()
  {
    Locale locale = localizationContext.getLocale();
    return new DecimalFormat("#0.###", new DecimalFormatSymbols(locale));
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
                                 final TypeValuePair valuePair)
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
                                     final Object value)
  {
    if (targetType.isFlagSet(Type.TEXT_TYPE))
    {
      if (type.isFlagSet(Type.TEXT_TYPE))
      {
        return value;
      }

      final String text = convertToText(type, value);
      if (text == null)
      {
        return null;
      }
      return text;
    }

    if (targetType.isFlagSet(Type.DATE_TYPE))
    {
      if (type.isFlagSet(Type.DATE_TYPE))
      {
        return value;
      }

      final Number number = convertToNumber(type, value);
      // OK, lets reverse it ..
      if (number == null)
      {
        return null;
      }

      final Date date = convertNumberToDate(number);
      if (date == null)
      {
        return null;
      }
      return date;
    }

    if (targetType.isFlagSet(Type.NUMERIC_TYPE))
    {
      if (type.isFlagSet(Type.NUMERIC_TYPE))
      {
        return value;
      }

      final Number number = convertToNumber(type, value);
      if (number == null)
      {
        return null;
      }
      return number;
    }

    // Unknown type - ignore it, crash later :)
    return value;
  }

  private TypeValuePair convertArrayToArray(final Type targetType,
                                            final TypeValuePair pair)
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
