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
 * IsNullExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: IsEmptyExpression.java,v 1.1 2006/01/27 20:15:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 27.01.2006 : Initial version
 */
package org.jfree.report.function.sys;

import org.jfree.report.DataSourceException;
import org.jfree.report.function.AbstractExpression;

/**
 * Creation-Date: 27.01.2006, 20:42:19
 *
 * @author Thomas Morgner
 */
public class IsEmptyExpression extends AbstractExpression
{
  private String field;

  public IsEmptyExpression()
  {
  }

  public String getField()
  {
    return field;
  }

  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue() throws DataSourceException
  {
    Object o = getDataRow().get(getField());
    if (o == null)
    {
      return Boolean.TRUE;
    }
    if (o instanceof String)
    {
      String s = (String) o;
      if (s.trim().length() == 0)
      {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    if (o instanceof Number)
    {
      Number n = (Number) o;
      if (n.intValue() == 0)
      {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    return Boolean.FALSE;
  }
}
