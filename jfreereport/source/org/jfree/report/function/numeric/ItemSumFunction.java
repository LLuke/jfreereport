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
 * ItemSumFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemSumFunction.java,v 1.1 2006/04/18 11:45:15 taqua Exp $
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
 * 28-Jul-2002 : Function now uses the datarow
 * 08-Aug-2002 : Imports cleaned
 * 20-Aug-2002 : Moved function configuration into function properties, removed local fields
 *               "group" and "field"
 */

package org.jfree.report.function.numeric;

import java.math.BigDecimal;

import org.jfree.report.DataSourceException;
import org.jfree.report.DataRow;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionDependencyInfo;
import org.jfree.report.function.Function;
import org.jfree.util.Log;

/**
 * A report function that calculates the sum of one field (column) from the TableModel.
 * This function produces a running total, no global total. For a global sum, use the
 * TotalGroupSumFunction function. The function can be used in two ways: <ul> <li>to
 * calculate a sum for the entire report;</li> <li>to calculate a sum within a particular
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
public class ItemSumFunction extends AbstractFunction
{
  /**
   * A useful constant representing zero.
   */
  protected static final BigDecimal ZERO = new BigDecimal(0.0);

  /**
   * The item sum.
   */
  private transient BigDecimal sum;
  private String field;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemSumFunction ()
  {
    sum = ZERO;
  }

  /**
   * Constructs a named function. <P> The field must be defined before using the
   * function.
   *
   * @param name The function name.
   */
  public ItemSumFunction (final String name)
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
   * When the advance method is called, the function is asked to perform the
   * next step of its computation.
   * <p/>
   * The original function must not be altered during that step (or more
   * correctly, calling advance on the original expression again must not return
   * a different result).
   *
   * @return a copy of the function containing the new state.
   */
  public Function advance() throws DataSourceException
  {
    final DataRow dataRow = getDataRow();
    final Object fieldValue = dataRow.get(getField());
    if (fieldValue == null)
    {
      return this;
    }
    if (fieldValue instanceof Number == false)
    {
      Log.error("ItemSumFunction.advanceItems(): problem adding number.");
      return this;
    }

    final Number n = (Number) fieldValue;
    try
    {
      final ItemSumFunction function = (ItemSumFunction) clone();
      function.sum = sum.add(new BigDecimal(n.doubleValue()));
      return function;
    }
    catch (CloneNotSupportedException e)
    {
      throw new DataSourceException("Unable to create a new copy of the ItemSumFunction");
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
    return sum;
  }

  protected BigDecimal getSum()
  {
    return sum;
  }

  protected void setSum(final BigDecimal sum)
  {
    if (sum == null)
    {
      throw new NullPointerException("Sum must not be null");
    }
    this.sum = sum;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final ItemSumFunction function = (ItemSumFunction) super.getInstance();
    function.sum = ZERO;
    return function;
  }

  public void queryDependencyInfo(final ExpressionDependencyInfo info)
  {
    super.queryDependencyInfo(info);
    info.setDependendFields(new String[]{getField()});
  }
}
