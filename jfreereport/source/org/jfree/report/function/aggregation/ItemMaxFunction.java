/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * --------------------
 * ItemMaxFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemMaxFunction.java,v 1.9 2005/08/08 15:36:29 taqua Exp $
 *
 * Changes
 * -------
 * 26-Aug-2002 : Initial version
 * 31-Aug-2002 : Documentation update
 */

package org.jfree.report.function.aggregation;

import org.jfree.report.DataSourceException;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.function.Function;
import org.jfree.util.Log;

/**
 * A report function that calculates the maximum value of one field (column) from the
 * TableModel.
 * <p/>
 * The function can be used in two ways: <ul> <li>to calculate a maximum value for the
 * entire report;</li> <li>to calculate a maximum value within a particular group;</li>
 * </ul> This function expects its input values to be either java.lang.Number instances or
 * Strings that can be parsed to java.lang.Number instances using a
 * java.text.DecimalFormat.
 * <p/>
 * The function undestands two parameters, the <code>field</code> parameter is required
 * and denotes the name of an ItemBand-field which gets summed up.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is
 * started, the counter gets reseted to null.
 *
 * @author Thomas Morgner
 */
public class ItemMaxFunction extends AbstractFunction
{
  private String field;
  /**
   * The maximum value.
   */
  private transient Comparable max;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemMaxFunction ()
  {
    max = null;
  }

  /**
   * Constructs a named function. <P> The field must be defined before using the
   * function.
   *
   * @param name The function name.
   */
  public ItemMaxFunction (final String name)
  {
    this();
    setName(name);
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
   * The advance method is a signal for the function to update its internal
   * state.
   */
  public Function advance() throws DataSourceException
  {
    final Object fieldValue = getDataRow().get(getField());
    if (fieldValue instanceof Comparable == false)
    {
      return this;
    }

    try
    {
      final Comparable compare = (Comparable) fieldValue;

      if (max == null || max.compareTo(compare) < 0)
      {
        ItemMaxFunction function = (ItemMaxFunction) clone();
        function.max = compare;
        return function;
      }
      else
      {
        return this;
      }
    }
    catch (CloneNotSupportedException e)
    {
      throw new DataSourceException("Unable to clone the ItemMaxFunction");
    }
  }

  /**
   * Returns the function value, in this case the running total of a specific column in
   * the report's TableModel.
   *
   * @return The function value.
   */
  public Object getValue ()
  {
    return max;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final ItemMaxFunction function = (ItemMaxFunction) super.getInstance();
    function.max = null;
    return function;
  }

  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }

}
