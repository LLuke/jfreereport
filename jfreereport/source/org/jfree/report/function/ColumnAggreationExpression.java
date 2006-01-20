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
 * $Id: ColumnAggreationExpression.java,v 1.1 2006/01/06 09:05:36 taqua Exp $
 *
 * Changes
 * -------------------------
 * 04.01.2006 : Initial version
 */
package org.jfree.report.function;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creation-Date: 04.01.2006, 17:23:01
 *
 * @author Thomas Morgner
 */
public abstract class ColumnAggreationExpression extends AbstractExpression
{
  private ArrayList fields;

  protected ColumnAggreationExpression()
  {
    fields = new ArrayList();
  }


  /**
   * collects the values of all fields defined in the fieldList.
   *
   * @return an Objectarray containing all defined values from the datarow
   */
  protected Object[] getFieldValues ()
  {
    final Object[] retval = new Object[fields.size()];
    for (int i = 0; i < fields.size(); i++)
    {
      final String field = (String) fields.get(i);
      retval[i] = getDataRow().get(field);
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
    return (String[]) fields.toArray(new String[fields.size()]);
  }

  public void setField (final String[] fields)
  {
    this.fields.clear();
    this.fields.addAll(Arrays.asList(fields));
  }

  public Object clone() throws CloneNotSupportedException
  {
    ColumnAggreationExpression co = (ColumnAggreationExpression) super.clone();
    co.fields = (ArrayList) fields.clone();
    return co;
  }
}
