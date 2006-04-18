/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * CountDistinctFunction.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: CountDistinctFunction.java,v 1.5 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------------------------
 * 16-05-2003 : Initial version
 *
 */

package org.jfree.report.function.aggregation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;

import org.jfree.report.DataSourceException;
import org.jfree.report.util.IntegerCache;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.function.Function;

/**
 * Counts the distinct occurences of an certain value of an column. This functionality is
 * similiar to the SQL distinct() function.
 *
 * @author Thomas Morgner
 */
public class CountDistinctFunction extends AbstractFunction implements Serializable
{
  /**
   * The collected values for the current group.
   */
  private transient HashSet values;
  private String field;

  /**
   * DefaultConstructor.
   */
  public CountDistinctFunction ()
  {
    values = new HashSet();
  }

  /**
   * Returns the field used by the function. <P> The field name corresponds to a column
   * name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return field;
  }

  /**
   * Sets the field name for the function. <P> The field name corresponds to a column name
   * in the report's TableModel.
   *
   * @param field the field name (null not permitted).
   */
  public void setField (final String field)
  {
    this.field = field;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public Function advance () throws DataSourceException
  {
    if (getField() == null)
    {
      return this;
    }

    try
    {
      final Object o = getDataRow().get(getField());
      if (values.contains(o) == false)
      {
        CountDistinctFunction function = (CountDistinctFunction) clone();
        function.values = (HashSet) values.clone();
        function.values.add(o);
        return function;
      }
    }
    catch (CloneNotSupportedException e)
    {
      throw new DataSourceException("Unable to clone the CountDistinctFunction");
    }
    return this;
  }

  /**
   * Return the number of distint values for the given column.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return IntegerCache.getInteger(values.size());
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException            when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object could
   *                                not be found.
   */
  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    values = new HashSet();
  }

  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }
}
