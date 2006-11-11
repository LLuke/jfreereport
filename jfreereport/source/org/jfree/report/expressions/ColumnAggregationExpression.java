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
 * ColumnAggreationExpression.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ColumnAggregationExpression.java,v 1.2 2006/04/18 11:28:39 taqua Exp $
 *
 * Changes
 * -------------------------
 * 04.01.2006 : Initial version
 */
package org.jfree.report.expressions;

import org.jfree.report.DataSourceException;

/**
 * Creation-Date: 04.01.2006, 17:23:01
 *
 * @author Thomas Morgner
 */
public abstract class ColumnAggregationExpression extends AbstractExpression
{
  protected ColumnAggregationExpression()
  {
  }

  protected abstract int getFieldListParameterPosition();

  protected Object[] getFieldValues() throws DataSourceException
  {
    return getFieldValues(Object.class);
  }

  protected Object[] getFieldValues(Class type) throws DataSourceException
  {
    return null; // todo
  }

}
