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
 * ColumnSumExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ColumnSumExpression.java,v 1.1 2006/01/06 09:05:37 taqua Exp $
 *
 * Changes
 * -------------------------
 * 04.01.2006 : Initial version
 */
package org.jfree.report.function;

/**
 * Creation-Date: 04.01.2006, 17:25:37
 *
 * @author Thomas Morgner
 */
public class ColumnSumExpression extends ColumnAggregationExpression
{
  public ColumnSumExpression()
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
    Object[] values = getFieldValues();
    double computedResult = 0;
    for (int i = 0; i < values.length; i++)
    {
      final Object value = values[i];
      if (value instanceof Number == false)
      {
        continue;
      }

      Number n = (Number) value;
      computedResult += n.doubleValue();
    }

    return new Double(computedResult);
  }
}
