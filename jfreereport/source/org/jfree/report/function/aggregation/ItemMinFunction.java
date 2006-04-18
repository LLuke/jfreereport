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
 * ItemMinFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemMinFunction.java,v 1.9 2005/08/08 15:36:29 taqua Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface
 * 23-Jun-2002 : Documentation
 * 17-Jul-2002 : Handle empty data source without a crashing
 * 18-Jul-2002 : Handle out-of-bounds dataquery to the tablemodel
 * 21-Jul-2002 : Corrected the out-of-bounds constraint
 * 17-Feb-2005 : Use java.util.Comparable interface instead of number for the compared
 *               values.
 */

package org.jfree.report.function.aggregation;

import org.jfree.report.DataSourceException;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.function.Function;

/**
 * A report function that calculates the minimum value of one field (column) from the
 * TableModel. The function can be used in two ways: <ul> <li>to calculate a minimum value
 * for the entire report;</li> <li>to calculate a minimum value within a particular
 * group;</li> </ul> This function expects its input values to be either java.lang.Number
 * instances or Strings that can be parsed to java.lang.Number instances using a
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
public class ItemMinFunction extends AbstractFunction
{
  /**
   * The minimum value.
   */
  private transient Comparable min;
  private String field;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemMinFunction ()
  {
    min = null;
  }

  /**
   * Constructs a named function. <P> The field must be defined before using the
   * function.
   *
   * @param name The function name.
   */
  public ItemMinFunction (final String name)
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
      if (min == null || (min.compareTo(compare) > 0))
      {
        ItemMinFunction function = (ItemMinFunction) clone();
        function.min = compare;
        return function;
      }
      return this;
    }
    catch (Exception e)
    {
      throw new DataSourceException("Unable to clone the ItemMinFunction");
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
    return min;
  }


  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final ItemMinFunction function = (ItemMinFunction) super.getInstance();
    function.min = null;
    return function;
  }


  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }

}
