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
 * $Id: ColumnAggregationExpression.java,v 1.1 2006/01/27 20:15:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 04.01.2006 : Initial version
 */
package org.jfree.report.function;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.report.DataSourceException;
import org.jfree.report.DataRow;

/**
 * Creation-Date: 04.01.2006, 17:23:01
 *
 * @author Thomas Morgner
 */
public abstract class ColumnAggregationExpression extends AbstractExpression
{
  private ArrayList fields;
  private transient String[] fieldsCached;
  private transient ExpressionDependencyInfo dependencyInfo;

  protected ColumnAggregationExpression()
  {
    fields = new ArrayList();
  }

  /**
   * collects the values of all fields defined in the fieldList.
   *
   * @return an Objectarray containing all defined values from the datarow
   */
  protected Object[] getFieldValues () throws DataSourceException
  {
    final String[] fields = getField();
    final Object[] retval = new Object[fields.length];
    final DataRow dataRow = getDataRow();
    for (int i = 0; i < fields.length; i++)
    {
      final String field = fields[i];
      retval[i] = dataRow.get(field);
    }
    return retval;
  }

  public void setField (final int index, final String field)
  {
    if (fields.size() == index)
    {
      fields.add(field);
    }
    else
    {
      fields.set(index, field);
    }
    fieldsCached = null;
    dependencyInfo = null;
  }

  public String getField (final int index)
  {
    return (String) fields.get(index);
  }

  public int getFieldCount ()
  {
    return fields.size();
  }

  public String[] getField ()
  {
    if (fieldsCached == null)
    {
      fieldsCached = (String[]) fields.toArray(new String[fields.size()]);
    }
    return (String[]) fieldsCached.clone();
  }

  public void setField (final String[] fields)
  {
    this.fields.clear();
    this.fields.addAll(Arrays.asList(fields));
    this.fieldsCached = (String[]) fields.clone();
    this.dependencyInfo = null;
  }

  public Object clone() throws CloneNotSupportedException
  {
    ColumnAggregationExpression co = (ColumnAggregationExpression) super.clone();
    co.fields = (ArrayList) fields.clone();
    co.fieldsCached = fieldsCached;
    co.dependencyInfo = dependencyInfo;
    return co;
  }

  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(getField());
  }
}
