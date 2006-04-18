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
 * IsEndOfDataExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 19.02.2006 : Initial version
 */
package org.jfree.report.function.sys;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.ReportData;
import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 19.02.2006, 19:15:45
 *
 * @author Thomas Morgner
 */
public class IsNotEndOfDataExpression extends AbstractExpression
{
  public IsNotEndOfDataExpression()
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
    ReportData data = getRuntime().getData();
    synchronized(data)
    {
      if (data.getCurrentRow() >= (data.getRowCount() - 1))
      {
        return Boolean.FALSE;
      }
      return Boolean.TRUE;
    }
  }
}
