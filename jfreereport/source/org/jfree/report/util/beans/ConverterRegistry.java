package org.jfree.report.util.beans;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public final class ConverterRegistry
{
  private static ConverterRegistry instance;
  private HashMap registeredClasses;

  public synchronized static ConverterRegistry getInstance()
  {
    if (instance == null)
    {
      instance = new ConverterRegistry();
    }
    return instance;
  }

  private ConverterRegistry()
  {
    registeredClasses = new HashMap();
    registeredClasses.put(BigDecimal.class, new BigDecimalValueConverter());
    registeredClasses.put(BigInteger.class, new BigIntegerValueConverter());
    registeredClasses.put(Boolean.class, new BooleanValueConverter());
    registeredClasses.put(Boolean.TYPE, new BooleanValueConverter());
    registeredClasses.put(Byte.class, new ByteValueConverter());
    registeredClasses.put(Byte.TYPE, new ByteValueConverter());
    registeredClasses.put(Character.class, new CharacterValueConverter());
    registeredClasses.put(Character.TYPE, new CharacterValueConverter());
    registeredClasses.put(Color.class, new ColorValueConverter());
    registeredClasses.put(Double.class, new DoubleValueConverter());
    registeredClasses.put(Double.TYPE, new DoubleValueConverter());
    registeredClasses.put(Float.class, new FloatValueConverter());
    registeredClasses.put(Float.TYPE, new FloatValueConverter());
    registeredClasses.put(Integer.class, new IntegerValueConverter());
    registeredClasses.put(Integer.TYPE, new IntegerValueConverter());
    registeredClasses.put(Long.class, new LongValueConverter());
    registeredClasses.put(Long.TYPE, new LongValueConverter());
    registeredClasses.put(Short.class, new ShortValueConverter());
    registeredClasses.put(Short.TYPE, new ShortValueConverter());
    registeredClasses.put(String.class, new StringValueConverter());
    registeredClasses.put(Number.class, new BigDecimalValueConverter());
    registeredClasses.put(Class.class, new ClassValueConverter());
  }

  public ValueConverter getValueConverter (final Class c)
  {
    final ValueConverter plain = (ValueConverter) registeredClasses.get(c);
    if (plain != null)
    {
      return plain;
    }
    if (c.isArray())
    {
      return (ValueConverter) registeredClasses.get(c.getComponentType());
    }
    return null;
  }
}
