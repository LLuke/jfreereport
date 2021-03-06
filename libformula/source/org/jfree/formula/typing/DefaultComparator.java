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
 * $Id: DefaultComparator.java,v 1.6 2007/04/10 14:10:41 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.formula.typing;

import java.math.BigDecimal;

import org.jfree.formula.FormulaContext;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 03.11.2006, 16:15:28
 * 
 * @author Thomas Morgner
 */
public class DefaultComparator implements ExtendedComparator
{
  private FormulaContext context;

  public static final Integer LESS = new Integer(-1);

  public static final Integer EQUAL = new Integer(0);

  private static final Integer MORE = new Integer(1);

  public DefaultComparator()
  {
  }

  public void inititalize(final FormulaContext context)
  {
    this.context = context;
  }

  public boolean isEqual(final Type type1, final Object value1,
      final Type type2, final Object value2)
  {
    // this is rather easy. If at least one of the types is a numeric,
    // try to compare them as numbers. (And here it gets messy.)

    final TypeRegistry typeRegistry = context.getTypeRegistry();
    try
    {
      final Number number1 = typeRegistry.convertToNumber(type1, value1);
      final Number number2 = typeRegistry.convertToNumber(type2, value2);
      final BigDecimal bd1 = new BigDecimal(number1.toString());
      final BigDecimal bd2 = new BigDecimal(number2.toString());
      if (bd1.signum() != bd2.signum())
      {
        return false;
      }

      final BigDecimal result = bd1.subtract(bd2);
      return (result.signum() == 0);
    }
    catch (TypeConversionException nfe)
    {
      // ignore ..
    }

    if (type1.isFlagSet(Type.TEXT_TYPE) || type2.isFlagSet(Type.TEXT_TYPE))
    {
      String text1 = null;
      String text2 = null;
      try
      {
        // Convert both values to text ..
        text1 = typeRegistry.convertToText(type1, value1);
        text2 = typeRegistry.convertToText(type2, value2);
      }
      catch (TypeConversionException nfe)
      {
        // ignore ..
      }

      if (text1 == null && text2 == null)
      {
        return true;
      }
      if (text1 == null || text2 == null)
      {
        return false;
      }
      return ObjectUtilities.equal(text1, text2);

    }

    // Fall back to Java's equals method and hope the best ..
    return (ObjectUtilities.equal(value1, value2));
  }

  /**
   * Returns null, if the types are not comparable and are not convertible at
   * all.
   * 
   * @param type1
   * @param value1
   * @param type2
   * @param value2
   * @return
   */
  public Integer compare(final Type type1, final Object value1,
      final Type type2, final Object value2)
  {
    // this is rather easy. If at least one of the types is a numeric,
    // try to compare them as numbers. (And here it gets messy.)
    if (value1 == null && value2 == null)
    {
      return DefaultComparator.EQUAL;
    }
    if (value1 == null)
    {
      return DefaultComparator.LESS;
    }
    if (value2 == null)
    {
      return DefaultComparator.MORE;
    }

    // First, we try to compare both types directly. This is the least-expensive
    // solution, as it does
    // not include any conversion operations ..
    if (type1.isFlagSet(Type.SCALAR_TYPE) && type2.isFlagSet(Type.SCALAR_TYPE))
    {
      // this is something else
      if (value1 instanceof Comparable && value2 instanceof Comparable)
      {
        final Comparable c1 = (Comparable) value1;
        try
        {
          final int result = c1.compareTo(value2);
          if (result == 0)
          {
            return DefaultComparator.EQUAL;
          }
          else if (result > 0)
          {
            return DefaultComparator.MORE;
          }
          else
          {
            return DefaultComparator.LESS;
          }
        }
        catch (Exception e)
        {
          // ignore any exception ..
        }
      }
    }

    // Next, we check the types on a numeric level.
    final TypeRegistry typeRegistry = context.getTypeRegistry();
    try
    {
      final Number number1 = typeRegistry.convertToNumber(type1, value1);
      final Number number2 = typeRegistry.convertToNumber(type2, value2);
      final BigDecimal bd1 = new BigDecimal(number1.toString());
      final BigDecimal bd2 = new BigDecimal(number2.toString());

      if (bd1.signum() != bd2.signum())
      {
        if (bd1.signum() < 0)
        {
          return DefaultComparator.LESS;
        }
        else if (bd1.signum() > 0)
        {
          return DefaultComparator.MORE;
        }
      }

      final BigDecimal result = bd1.subtract(bd2);
      if (result.signum() == 0)
      {
        return DefaultComparator.EQUAL;
      }
      if (result.signum() > 0)
      {
        return DefaultComparator.MORE;
      }
      return DefaultComparator.LESS;
    }
    catch (TypeConversionException nfe)
    {
      // Ignore ..
    }

    // And finally convert them to text and compare the text values ..
    // Convert both values to text ..
    String text1 = null;
    String text2 = null;
    try
    {
      text1 = typeRegistry.convertToText(type1, value1);
      text2 = typeRegistry.convertToText(type2, value2);
    }
    catch (TypeConversionException e)
    {
    }

    if (text1 == null && text2 == null)
    {
      return DefaultComparator.EQUAL;
    }
    if (text1 == null)
    {
      return DefaultComparator.LESS;
    }
    if (text2 == null)
    {
      return DefaultComparator.MORE;
    }

    final int result = text1.compareTo(text2);
    if (result == 0)
    {
      return DefaultComparator.EQUAL;
    }
    else if (result > 0)
    {
      return DefaultComparator.MORE;
    }
    else
    {
      return DefaultComparator.LESS;
    }
  }
}
