/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * IsNegativeExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.01.2006 : Initial version
 */
package org.jfree.report.function;

/**
 * Creation-Date: 27.01.2006, 20:38:41
 *
 * @author Thomas Morgner
 */
public abstract class AbstractCompareExpression extends AbstractExpression
{
  public static final String EQUAL = "equal";
  public static final String NOT_EQUAL = "not-equal";
  public static final String LOWER = "lower";
  public static final String GREATER = "greater";
  public static final String LOWER_EQUAL = "lower-equal";
  public static final String GREATER_EQUAL = "greater-equal";

  private String compareMethod;
  private String field;

  public AbstractCompareExpression()
  {
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    Object o = getDataRow().get(getField());

    if (o instanceof Comparable == false)
    {
      return Boolean.FALSE;
    }

    try
    {
      final Comparable c = (Comparable) o;
      final Comparable comparable = getComparable();
      if (comparable == null)
      {
        return Boolean.FALSE;
      }
      
      int result = c.compareTo(comparable);

      final String method = getCompareMethod();
      if (EQUAL.equals(method))
      {
        if (result == 0)
        {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
      if (NOT_EQUAL.equals(method))
      {
        if (result != 0)
        {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
      if (LOWER.equals(method))
      {
        if (result < 0)
        {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
      if (LOWER_EQUAL.equals(method))
      {
        if (result <= 0)
        {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
      if (GREATER.equals(method))
      {
        if (result > 0)
        {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
      if (GREATER_EQUAL.equals(method))
      {
        if (result >= 0)
        {
          return Boolean.TRUE;
        }
        return Boolean.FALSE;
      }
    }
    catch(Exception e)
    {
      // ignore ...
    }
    return Boolean.FALSE;
  }

  public String getField()
  {
    return field;
  }

  public void setField(final String field)
  {
    this.field = field;
  }

  public String getCompareMethod()
  {
    return compareMethod;
  }

  public void setCompareMethod(final String compareMethod)
  {
    this.compareMethod = compareMethod;
  }

  protected abstract Comparable getComparable();

}
