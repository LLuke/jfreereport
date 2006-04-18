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
 * OrExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: OrExpression.java,v 1.1 2006/01/27 20:15:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.01.2006 : Initial version
 */
package org.jfree.report.function.bool;

import org.jfree.report.function.ColumnAggregationExpression;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 27.01.2006, 21:09:20
 *
 * @author Thomas Morgner
 */
public class OrExpression extends ColumnAggregationExpression
{
  public OrExpression()
  {
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue() throws DataSourceException
  {
    Object[] values = getFieldValues();
    if (values.length == 0)
    {
      return Boolean.FALSE;
    }

    for (int i = 0; i < values.length; i++)
    {
      final Object value = values[i];
      if (value instanceof Boolean == false)
      {
        continue;
      }

      Boolean n = (Boolean) value;
      if (n.equals(Boolean.TRUE))
      {
        return Boolean.TRUE;
      }
    }

    return Boolean.FALSE;
  }
}
