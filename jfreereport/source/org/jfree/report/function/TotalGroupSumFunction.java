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
 * TotalGroupSumFunction.java
 * ---------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TotalGroupSumFunction.java,v 1.8 2005/02/04 19:22:54 taqua Exp $
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.util.Log;

/**
 * A report function that calculates the sum of one field (column) from the TableModel.
 * This function produces a global total. The total sum of the group is known when the
 * group processing starts and the report is not performing a prepare-run. The sum is
 * calculated in the prepare run and recalled in the printing run.
 * <p/>
 * The function can be used in two ways: <ul> <li>to calculate a sum for the entire
 * report;</li> <li>to calculate a sum within a particular group;</li> </ul> This function
 * expects its input values to be either java.lang.Number instances or Strings that can be
 * parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p/>
 * The function undestands two parameters, the <code>field</code> parameter is required
 * and denotes the name of an ItemBand-field which gets summed up.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is
 * started, the counter gets reseted to null. This parameter is optional.
 *
 * @author Thomas Morgner
 */
public class TotalGroupSumFunction extends AbstractFunction implements Serializable
{
  /**
   * Helperclass to make summing easier.
   */
  private static class GroupSum implements Serializable
  {
    /**
     * The result.
     */
    private BigDecimal result;

    /**
     * Default constructor.
     */
    public GroupSum ()
    {
      result = new BigDecimal(0);
    }

    /**
     * Adds a number to the result.
     *
     * @param n the number.
     */
    public void add (final Number n)
    {
      if (n == null)
      {
        throw new NullPointerException("Number is null");
      }

      result = result.add(new BigDecimal(n.toString()));
    }

    /**
     * Returns the sum.
     *
     * @return the sum.
     */
    public BigDecimal getResult ()
    {
      return result;
    }
  }

  /**
   * The group sum.
   */
  private transient GroupSum groupResult;

  /**
   * A list of results.
   */
  private transient ArrayList results;

  /**
   * The current index.
   */
  private transient int currentIndex;

  private String field;
  private String group;

  /**
   * Constructs a new function. <P> Initially the function has no name...be sure to assign
   * one before using the function.
   */
  public TotalGroupSumFunction ()
  {
    groupResult = new GroupSum();
    results = new ArrayList();
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    currentIndex = -1;
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      results.clear();
      if (getGroup() == null)
      {
        groupResult = new GroupSum();
        results.add(groupResult);
      }
      else
      {
        groupResult = new GroupSum();
      }
    }
    else
    {
      if (getGroup() == null)
      {
        groupResult = (GroupSum) results.get(0);
      }
    }
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      // wrong group ...
      return;
    }

    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
    {
      groupResult = new GroupSum();
      results.add(groupResult);
      currentIndex += 1;
    }
    else
    {
      // Activate the current group, which was filled in the prepare run.
      currentIndex += 1;
      groupResult = (GroupSum) results.get(currentIndex);
    }
  }


  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedPrepareRunLevel(this, event) == false)
    {
      return;
    }

    final Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue instanceof Number == false)
    {
      // No add, field is null
      return;
    }
    try
    {
      final Number n = (Number) fieldValue;
      groupResult.add(n);
    }
    catch (Exception e)
    {
      Log.error("TotalItemSumFunction.advanceItems(): problem adding number." + fieldValue, e);
    }
  }

  /**
   * Returns the name of the group to be totalled.
   *
   * @return the group name.
   */
  public String getGroup ()
  {
    return group;
  }

  /**
   * Defines the name of the group to be totalled. If the name is null, all groups are
   * totalled.
   *
   * @param group the group name.
   */
  public void setGroup (final String group)
  {
    this.group = group;
  }

  /**
   * Return the current function value. <P> The value depends (obviously) on the function
   * implementation.   For example, a page counting function will return the current page
   * number.
   *
   * @return The value of the function.
   */
  public Object getValue ()
  {
    return groupResult.getResult();
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
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final TotalGroupSumFunction function = (TotalGroupSumFunction) super.getInstance();
    function.groupResult = new GroupSum();
    function.results = new ArrayList();
    return function;
  }

  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    this.results = new ArrayList();
  }
}