/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * ConverterRegistry.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util.beans;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public final class ConverterRegistry
{
  private static ConverterRegistry instance;
  private HashMap registeredClasses;

  public synchronized static ConverterRegistry getInstance ()
  {
    if (instance == null)
    {
      instance = new ConverterRegistry();
    }
    return instance;
  }

  private ConverterRegistry ()
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

  /**
   * Converts an object to an attribute value.
   *
   * @param o the object.
   * @return the attribute value.
   */
  public static String toAttributeValue (final Object o)
  {
    if (o == null)
    {
      return null;
    }
    final ValueConverter vc =
            ConverterRegistry.getInstance().getValueConverter(o.getClass());
    if (vc == null)
    {
      return null;
    }
    return vc.toAttributeValue(o);
  }

  /**
   * Converts a string to a property value.
   *
   * @param s the string.
   * @return a property value.
   */
  public static Object toPropertyValue (final String s, final Class c)
  {
    if (s == null)
    {
      return null;
    }
    final ValueConverter vc =
            ConverterRegistry.getInstance().getValueConverter(c);
    if (vc == null)
    {
      return null;
    }
    return vc.toPropertyValue(s);
  }

}
