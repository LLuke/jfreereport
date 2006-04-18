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
 * GroupByExpression.java
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

import org.jfree.report.function.ColumnAggregationExpression;
import org.jfree.report.DataFlags;
import org.jfree.report.DataSourceException;
import org.jfree.report.DataRow;
import org.jfree.util.Log;

/**
 * Creation-Date: 19.02.2006, 16:08:59
 *
 * @author Thomas Morgner
 */
public class GroupByExpression extends ColumnAggregationExpression
{
  public GroupByExpression()
  {
  }

  /**
   * Returns true, if the group continues, false otherwise. 
   *
   * @return the value of the function.
   */
  public Object getValue() throws DataSourceException
  {
    final DataRow dr = getDataRow();
    final String[] columns = getField();
    for (int i = 0; i < columns.length; i++)
    {
      String column = columns[i];
      DataFlags df = dr.getFlags(column);
      if (df == null)
      {
        // invalid column or invalid implementation ...
        continue;
      }
      if (df.isChanged())
      {
        Log.debug ("Group-by: " + df.getName());
        return Boolean.FALSE;
      }
    }
    return Boolean.TRUE;
  }
}
