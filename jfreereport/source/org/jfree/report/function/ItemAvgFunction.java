/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ItemAvgFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemAvgFunction.java,v 1.5 2005/01/25 00:00:10 taqua Exp $
 *
 * Changes
 * -------
 * 26-Aug-2002 : Initial version
 * 31-Aug-2002 : Documentation update
 */

package org.jfree.report.function;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.util.Log;

/**
 * A report function that calculates the average of one field (column) from the
 * TableModel. This function produces a running total, no global total. The function can
 * be used in two ways: <ul> <li>to calculate an average value for the entire report;</li>
 * <li>to calculate an average value within a particular group;</li> </ul> This function
 * expects its input values to be either java.lang.Number instances or Strings that can be
 * parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p/>
 * The function undestands two parameters, the <code>field</code> parameter is required
 * and denotes the name of an ItemBand-field which gets summed up.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is
 * started, the counter gets reseted to null.
 *
 * @author Thomas Morgner
 */
public class ItemAvgFunction extends AbstractFunction implements Serializable
{
  /**
   * Useful constant for zero.
   */
  private static final BigDecimal ZERO = new BigDecimal(0.0);

  /**
   * Useful constant for one.
   */
  private static final BigDecimal ONE = new BigDecimal(1.0);

  /**
   * The item sum.
   */
  private transient BigDecimal sum;

  /**
   * The item count.
   */
  private transient BigDecimal itemCount;

  private String group;
  private String field;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemAvgFunction ()
  {
    sum = ZERO;
    itemCount = ZERO;
  }

  /**
   * Constructs a named function. <P> The field must be defined before using the
   * function.
   *
   * @param name The function name.
   */
  public ItemAvgFunction (final String name)
  {
    this();
    setName(name);
  }

  /**
   * Receives notification that a new report is about to start. <P> Does nothing.
   *
   * @param event Information about the event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    this.sum = ZERO;
    itemCount = ZERO;
  }

  /**
   * Receives notification that a new group is about to start.  If this is the group
   * defined for the function, then the running total is reset to zero.
   *
   * @param event Information about the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    final String mygroup = getGroup();
    if (mygroup == null)
    {
      return;
    }

    final Group group = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    if (getGroup().equals(group.getName()))
    {
      this.sum = ZERO;
      itemCount = ZERO;
    }
  }

  /**
   * Returns the group name.
   *
   * @return The group name.
   */
  public String getGroup ()
  {
    return group;
  }

  /**
   * Sets the group name. <P> If a group is defined, the functions value is reset to zero
   * at the start of every instance of this group.
   *
   * @param name The group name (null permitted).
   */
  public void setGroup (final String name)
  {
    this.group = name;
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
    if (field == null)
    {
      throw new NullPointerException();
    }

    this.field = field;
  }

  /**
   * Receives notification that a row of data is being processed.  Reads the data from the
   * field defined for this function and calculates the average value of all values read
   * so far. <P> This function assumes that it will find an instance of the Number class
   * in the column of the TableModel specified by the field name.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    final Object fieldValue = event.getDataRow().get(getField());

    final Number n = (Number) fieldValue;
    try
    {
      sum = sum.add(new BigDecimal(n.doubleValue()));
      itemCount = itemCount.add(ONE);
    }
    catch (Exception e)
    {
      Log.error("ItemAvgFunction.advanceItems(): problem adding number.");
    }
  }

  /**
   * Returns the function value, in this case the average of all values of a specific
   * column in the report's TableModel.
   *
   * @return The function value.
   */
  public Object getValue ()
  {
    if (itemCount.longValue() == 0)
    {
      return null;
    }
    else
    {
      return sum.divide(itemCount, BigDecimal.ROUND_HALF_DOWN);
    }
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final ItemAvgFunction function = (ItemAvgFunction) super.getInstance();
    function.sum = ZERO;
    function.itemCount = ZERO;
    return function;
  }
}
