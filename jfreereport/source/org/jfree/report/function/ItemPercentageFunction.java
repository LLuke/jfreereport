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
 * $Id: ItemPercentageFunction.java,v 1.2 2003/08/24 15:13:22 taqua Exp $
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
import org.jfree.report.util.Log;

/**
 * Calculates the percentage value of a numeric field. The total sum is taken and divided by
 * the number of items counted.
 *
 * @author Thomas Morgner
 */
public class ItemPercentageFunction extends AbstractFunction implements Serializable
{
  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** A total group sum function. */
  private TotalGroupSumFunction totalSumFunction;

  /** The current value. */
  private BigDecimal currentValue;

  /** A useful constant representing zero. */
  private static final BigDecimal ZERO = new BigDecimal(0.0);

  /**
   * Creates a new ItemPercentageFunction.
   */
  public ItemPercentageFunction()
  {
    totalSumFunction = new TotalGroupSumFunction();
    totalSumFunction.setName("total");
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    totalSumFunction.initialize();
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
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
   * @param event  the event.
   */
  public void itemsAdvanced(final ReportEvent event)
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
   * @param event  the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    totalSumFunction.reportInitialized(event);
    currentValue = ZERO;
  }

  /**
   * Return the current function value.
   * <P>
   * Don not count on the correctness of this function until the preparerun has finished.
   *
   * @return The value of the function.
   */
  public Object getValue()
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
   * Returns the name of the group to be counted.  This is stored in the 'group' property.
   *
   * @return  the group name.
   */
  public String getGroup()
  {
    return getProperty(GROUP_PROPERTY);
  }

  /**
   * Defines the name of the group to be counted (if the name is null, all groups are counted).
   *
   * @param group  the name of the group (null permitted).
   */
  public void setGroup(final String group)
  {
    setProperty(GROUP_PROPERTY, group);
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField()
  {
    return getProperty(FIELD_PROPERTY);
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param field  the field name (null not permitted).
   */
  public void setField(final String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Sets a property for the function.
   *
   * @param name The property name.
   * @param value The property value.
   */
  public void setProperty(final String name, final String value)
  {
    super.setProperty(name, value);
    totalSumFunction.setProperty(name, value);
  }

  /**
   * Returns a clone of the function.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to overwrite this function.
   *
   * @return A clone of the function.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ItemPercentageFunction clone = (ItemPercentageFunction) super.clone();
    clone.totalSumFunction = (TotalGroupSumFunction) totalSumFunction.clone();
    return clone;
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final ItemPercentageFunction function = (ItemPercentageFunction) super.getInstance();
    function.totalSumFunction = (TotalGroupSumFunction) totalSumFunction.getInstance();
    function.currentValue = ZERO;
    return function;
  }
}