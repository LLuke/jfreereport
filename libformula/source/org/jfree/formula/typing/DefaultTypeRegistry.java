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
 * $Id: DefaultTypeRegistry.java,v 1.12 2007/04/27 22:00:47 mimil Exp $
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
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.formula.typing.coretypes.DateTimeType;
import org.jfree.formula.typing.coretypes.DateType;
import org.jfree.formula.typing.coretypes.LogicalType;
import org.jfree.formula.typing.coretypes.NumberType;
import org.jfree.formula.typing.coretypes.TextType;
import org.jfree.formula.typing.coretypes.TimeType;
import org.jfree.formula.util.DateUtil;
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
   * @throws NumberFormatException
   *           if the type cannot be represented as number.
   */
  public Number convertToNumber(final Type type1, final Object value)
      throws TypeConversionException
  {
    if (type1.isFlagSet(Type.NUMERIC_TYPE))
    {
      if (type1.isFlagSet(Type.DATETIME_TYPE)
          || type1.isFlagSet(Type.TIME_TYPE) || type1.isFlagSet(Type.DATE_TYPE))
      {
        if (value instanceof Date)
        {
          final Number serial = DateUtil.toSerialDate((Date) value, context
              .getLocalizationContext());
          //System.out.println(serial);
          final Number ret = DateUtil.normalizeDate(serial, type1);
          //System.out.println(ret);
          return ret;
        }
      }

      if (value instanceof Number)
      {
        return (Number) value;
      }
    }

    if (type1.isFlagSet(Type.LOGICAL_TYPE))
    {
      if (value instanceof Boolean)
      {
        if (Boolean.TRUE.equals(value))
        {
          return new Integer(1);
        }
        else
        {
          return new Integer(0);
        }
      }
    }

    if (type1.isFlagSet(Type.TEXT_TYPE))
    {
      final String val = value.toString();

      // first, try to parse the value as a big-decimal.
      try
      {
        return new BigDecimal(val);
      }
      catch (NumberFormatException e)
      {
        // ignore ..
      }

      // then checking for dates
      for (int i = 0; i < dateFormats.length; i++)
      {
        try
        {
          final DateFormat dateFormat = dateFormats[i];
          final Date date = dateFormat.parse(val);
          final Number serial = DateUtil.toSerialDate(date, context
              .getLocalizationContext());
          // TODO : check which type of date it is (date, time, datetime)
          // DateUtil.normalizeDate(fromSerialDate, toType)
          return serial;
          // return convertDateToNumber(date);
        }
        catch (ParseException e)
        {
          // ignore as well ..
        }
      }

      // then checking for numbers
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
    }

    throw new TypeConversionException();
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
    final DecimalFormat defaultFormat = new DecimalFormat("#0.###",
        new DecimalFormatSymbols(Locale.US));
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
        final Method method = DecimalFormat.class.getMethod(
            "setParseBigDecimal", new Class[]
            { Boolean.TYPE });
        method.invoke(format, new Object[]
        { Boolean.TRUE });
      }
      catch (Exception e)
      {
        // ignore it, as it will always fail on JDK 1.4 or lower ..
      }
    }
  }

  public String convertToText(final Type type1, final Object value)
      throws TypeConversionException
  {
    if (value == null)
    {
      return "";
    }

    // already converted or compatible
    if (type1.isFlagSet(Type.TEXT_TYPE))
    {
      // no need to check whatever it is a String
      return value.toString();
    }

    if (type1.isFlagSet(Type.LOGICAL_TYPE))
    {
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
      else
      {
        throw new TypeConversionException();
      }
    }

    // 2 types of numeric : numbers and dates
    if (type1.isFlagSet(Type.NUMERIC_TYPE))
    {
      if (type1.isFlagSet(Type.DATETIME_TYPE)
          || type1.isFlagSet(Type.DATE_TYPE) || type1.isFlagSet(Type.TIME_TYPE))
      {
        final Date d = convertToDate(type1, value);
        final DateFormat format = getDefaultDateFormat(type1);
        return format.format(d);
      }
      else
      {
        try
        {
          final Number n = convertToNumber(type1, value);
          final NumberFormat format = getDefaultNumberFormat();
          return format.format(n);
        }
        catch (TypeConversionException nfe)
        {
          // ignore ..
        }
      }
    }

    // fallback
    return value.toString();
  }

  public Boolean convertToLogical(final Type type1, final Object value)
      throws TypeConversionException
  {
    if (value == null)
    {
      return Boolean.FALSE;
    }

    // already converted or compatible
    if (type1.isFlagSet(Type.LOGICAL_TYPE))
    {
      if (value instanceof Boolean)
      {
        return (Boolean) value;
      }

      // fallback
      return new Boolean(value.toString());
    }

    if (type1.isFlagSet(Type.NUMERIC_TYPE))
    {
      // no need to check between different types of numeric
      if (value instanceof Number)
      {
        final Number num = (Number) value;
        if (!ZERO.equals(num))
        {
          return Boolean.TRUE;
        }
      }

      // fallback
      return Boolean.FALSE;
    }

    if (type1.isFlagSet(Type.TEXT_TYPE))
    {
      // no need to convert it to String
      final String str = value.toString();
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

  public Date convertToDate(final Type type1, final Object value)
      throws TypeConversionException
  {
    if (type1.isFlagSet(Type.NUMERIC_TYPE))
    {
      if (type1.isFlagSet(Type.DATE_TYPE)
          || type1.isFlagSet(Type.DATETIME_TYPE)
          || type1.isFlagSet(Type.TIME_TYPE))
      {
        if (value instanceof Date)
        {
          return DateUtil.normalizeDate((Date) value, type1);
        }
      }
    }
    final Number serial = convertToNumber(type1, value);
    return DateUtil.toJavaDate(serial, context.getLocalizationContext());
  }

  protected NumberFormat getDefaultNumberFormat()
  {
    final Locale locale = context.getLocalizationContext().getLocale();
    return new DecimalFormat("#0.#########", new DecimalFormatSymbols(locale));
  }

  protected DateFormat getDefaultDateFormat(Type dateType)
  {
    final Locale locale = context.getLocalizationContext().getLocale();
    if (dateType.isFlagSet(Type.DATE_TYPE))
    {
      return SimpleDateFormat.getDateInstance(SimpleDateFormat.DEFAULT, locale);
    }
    else if (dateType.isFlagSet(Type.DATETIME_TYPE))
    {
      return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.DEFAULT,
          SimpleDateFormat.DEFAULT, locale);
    }
    else if (dateType.isFlagSet(Type.TIME_TYPE))
    {
      return SimpleDateFormat.getTimeInstance(SimpleDateFormat.DEFAULT, locale);
    }

    // fallback with a full detail formatting
    return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.FULL,
        SimpleDateFormat.FULL, locale);
  }

  /**
   * Checks, whether the target type would accept the specified value object and
   * value type.<br/> This method is called for auto conversion of fonction
   * parameters using the conversion type declared by the function metadata.
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
        final Object retval = convertPlainToPlain(targetType, valuePair
            .getType(), valuePair.getValue());
        return new TypeValuePair(targetType, new Object[]
        { retval });
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

  private Object convertPlainToPlain(final Type targetType, final Type type,
      final Object value) throws TypeConversionException
  {
    // lazy check
//    if (targetType.equals(type))
//    {
//      return value;
//    }

    if (targetType.isFlagSet(Type.TEXT_TYPE))
    {
      return convertToText(type, value);
    }

    if (targetType.isFlagSet(Type.NUMERIC_TYPE))
    {
      final Number serial = convertToNumber(type, value);
      if (targetType.isFlagSet(Type.DATE_TYPE)
          || targetType.isFlagSet(Type.DATETIME_TYPE)
          || targetType.isFlagSet(Type.TIME_TYPE))
      {
        final Number normalizedSerial = DateUtil.normalizeDate(serial,
            targetType);
        return DateUtil.toJavaDate(normalizedSerial, context
            .getLocalizationContext());
      }
      return serial;
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
  
  public Type guessTypeOfObject(Object o)
  {
    if(o instanceof Number)
    {
      return NumberType.GENERIC_NUMBER;
    }
    else if(o instanceof Date)
    {
      return DateTimeType.TYPE;
    }
    else if(o instanceof Time)
    {
      return TimeType.TYPE;
    }
    else if(o instanceof java.sql.Date)
    {
      return DateType.TYPE;
    }
    else if(o instanceof Boolean)
    {
      return LogicalType.TYPE;
    }
    else if(o instanceof String)
    {
      return TextType.TYPE;
    }
    
    return AnyType.TYPE;
  }
}
