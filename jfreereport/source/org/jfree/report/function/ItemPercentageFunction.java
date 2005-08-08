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
 * ---------------------------
 * ItemPercentageFunction.java
 * ---------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemPercentageFunction.java,v 1.7 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------
 * 23-Jun-2002 : Initial version
 * 17-Jul-2002 : Handle empty data source without a crashing
 * 18-Jul-2002 : Handle out-of-bounds dataquery to the tablemodel
 * 21-Jul-2002 : Corrected the out-of-bounds constraint
 * 12-Dec-2002 : Fixed issues reported by Checkstyle (DG);
 *
 */

package org.jfree.report.function;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jfree.report.event.ReportEvent;
import org.jfree.util.Log;

/**
 * Calculates the percentage value of a numeric field. The total sum is taken and divided
 * by the number of items counted.
 *
 * @author Thomas Morgner
 */
public class ItemPercentageFunction extends AbstractFunction implements Serializable
{
  /**
   * A total group sum function.
   */
  private TotalGroupSumFunction totalSumFunction;

  /**
   * The current value.
   */
  private transient BigDecimal currentValue;

  /**
   * A useful constant representing zero.
   */
  private static final BigDecimal ZERO = new BigDecimal(0.0);

  private String group;
  private String field;

  /**
   * Creates a new ItemPercentageFunction.
   */
  public ItemPercentageFunction ()
  {
    totalSumFunction = new TotalGroupSumFunction();
    totalSumFunction.setName("total");
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    totalSumFunction.groupStarted(event);

    final Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue == null)
    {
      // No add, field is null
      return;
    }
    try
    {
      final Number n = (Number) fieldValue;
      currentValue = new BigDecimal(n.toString());
    }
    catch (Exception e)
    {
      Log.error("ItemSumFunction.advanceItems(): problem adding number.");
    }
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    totalSumFunction.itemsAdvanced(event);

    final Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue == null)
    {
      // No add, field is null
      return;
    }
    try
    {
      final Number n = (Number) fieldValue;
      currentValue = new BigDecimal(n.toString());
    }
    catch (Exception e)
    {
      Log.error("ItemSumFunction.advanceItems(): problem adding number.");
    }

  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    totalSumFunction.reportInitialized(event);
    currentValue = ZERO;
  }

  /**
   * Return the current function value. <P> Don not count on the correctness of this
   * function until the preparerun has finished.
   *
   * @return The value of the function.
   */
  public Object getValue ()
  {
    final BigDecimal total = (BigDecimal) totalSumFunction.getValue();

    if (total.longValue() == 0)
    {
      return null;
    }
    final BigDecimal retval =
            currentValue.multiply(new BigDecimal(100)).divide(total, 4, BigDecimal.ROUND_HALF_UP);
    return retval;
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
   * Sets the group name. <P> If a group is defined, the minimum value is reset to zero at
   * the start of every instance of this group.
   *
   * @param name the group name (null permitted).
   */
  public void setGroup (final String name)
  {
    this.group = name;
    this.totalSumFunction.setGroup(group);
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
    this.totalSumFunction.setField(field);
  }

  /**
   * Returns a clone of the function. <P> Be aware, this does not create a deep copy. If
   * you have complex strucures contained in objects, you have to overwrite this
   * function.
   *
   * @return A clone of the function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final ItemPercentageFunction clone = (ItemPercentageFunction) super.clone();
    clone.totalSumFunction = (TotalGroupSumFunction) totalSumFunction.clone();
    return clone;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final ItemPercentageFunction function = (ItemPercentageFunction) super.getInstance();
    function.totalSumFunction = (TotalGroupSumFunction) totalSumFunction.getInstance();
    function.currentValue = ZERO;
    return function;
  }
}