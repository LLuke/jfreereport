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
 * GetValueExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 03.03.2006 : Initial version
 */
package org.jfree.report.function.sys;

import org.jfree.report.DataFlags;
import org.jfree.report.DataSourceException;
import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.ExpressionDependencyInfo;

/**
 * Creation-Date: 03.03.2006, 17:43:38
 *
 * @author Thomas Morgner
 */
public class GetValueExpression extends AbstractExpression
{
  private String field;

  public GetValueExpression()
  {
  }

  public GetValueExpression(final String field)
  {
    this.field = field;
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
    if (field != null)
    {
      final DataFlags df = getDataRow().getFlags(getField());
      return df;
    }
    return null;
  }

  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    if (field != null)
    {
      info.setDependendFields(new String[]{field});
    }
    else
    {
      info.setDependendFields(new String[0]);
    }
  }
}
