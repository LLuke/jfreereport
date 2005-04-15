/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ---------------------------------
 * TotalGroupSumQuotienFunction.java
 * ---------------------------------
 * (C)opyright 2002, 2003, by Heiko Evermann and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG);
 * Contributor(s):   Thomas Morgner, David Gilbert (for Simba Management Limited)
 *                   for programming TotalGroupSumFunction
 *
 * $Id: TotalGroupSumQuotientFunction.java,v 1.9 2005/02/23 21:04:47 taqua Exp $
 *
 * Changes
 * -------
 * 16-Dec-2002 : Initial version, based on TotalGroupSumFunction.java
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
 * A report function that calculates the quotient of two summed fields (columns) from the
 * TableModel. This function produces a global total. The total sum of the group is known
 * when the group processing starts and the report is not performing a prepare-run. The
 * sum is calculated in the prepare run and recalled in the printing run.
 * <p/>
 * The function can be used in two ways: <ul> <li>to calculate a quotient for the entire
 * report;</li> <li>to calculate a quotient within a particular group;</li> </ul> This
 * function expects its input values to be either java.lang.Number instances or Strings
 * that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p/>
 * The function undestands tree parameters. The <code>dividend</code> parameter is
 * required and denotes the name of an ItemBand-field which gets summed up as dividend.
 * The <code>divisor</code> parameter is required and denotes the name of an
 * ItemBand-field which gets summed up as divisor.
 * <p/>
 * The parameter <code>group</code> denotes the name of a group. When this group is
 * started, the counter gets reseted to null. This parameter is optional.
 *
 * @author Thomas Morgner
 */
public class TotalGroupSumQuotientFunction extends AbstractFunction
        implements Serializable
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
   * The group sums for dividend and divisor.
   */
  private transient GroupSum groupDividend;

  /**
   * The group divisor.
   */
  private transient GroupSum groupDivisor;

  /**
   * A list of results.
   */
  private transient ArrayList dividendResults;

  /**
   * A list of divisor results.
   */
  private transient ArrayList divisorResults;

  /**
   * The current index.
   */
  private transient int currentIndex;

  private String group;
  private String dividend;
  private String divisor;

  /**
   * Constructs a new function. <P> Initially the function has no name...be sure to assign
   * one before using the function.
   */
  public TotalGroupSumQuotientFunction ()
  {
    groupDividend = new GroupSum();
    groupDivisor = new GroupSum();
    dividendResults = new ArrayList();
    divisorResults = new ArrayList();
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
      dividendResults.clear();
      divisorResults.clear();
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
      groupDividend = new GroupSum();
      dividendResults.add(groupDividend);

      groupDivisor = new GroupSum();
      divisorResults.add(groupDivisor);
    }
    else
    {
      if (event.getState().isPrepareRun() == false)
      {
        // Activate the current group, which was filled in the prepare run.
        currentIndex += 1;
        groupDividend = (GroupSum) dividendResults.get(currentIndex);
        groupDivisor = (GroupSum) divisorResults.get(currentIndex);
      }
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

    // sum up dividend column
    Object fieldValue = event.getDataRow().get(getDividend());
    // do not add when field is null
    if (fieldValue != null)
    {
      try
      {
        final Number n = (Number) fieldValue;
        groupDividend.add(n);
      }
      catch (Exception e)
      {
        Log.error("ItemSumFunction.advanceItems(): problem adding dividend.");
      }
    }
    // sum up divisor column
    fieldValue = event.getDataRow().get(getDivisor());
    // do not add when field is null
    if (fieldValue != null)
    {
      try
      {
        final Number n = (Number) fieldValue;
        groupDivisor.add(n);
      }
      catch (Exception e)
      {
        Log.error("ItemSumFunction.advanceItems(): problem adding divisor.");
      }
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
    final BigDecimal dividend = groupDividend.getResult();
    final BigDecimal divisor = groupDivisor.getResult();
    if (divisor.intValue() == 0)
    {
      return new Double(Double.NaN);
    }
    return new Double(dividend.doubleValue() / divisor.doubleValue());
  }

  /**
   * Returns the field used as dividend by the function. <P> The field name corresponds to
   * a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDividend ()
  {
    return dividend;
  }

  /**
   * Returns the field used as divisor by the function. <P> The field name corresponds to
   * a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDivisor ()
  {
    return this.divisor;
  }

  /**
   * Sets the field name to be used as dividend for the function. <P> The field name
   * corresponds to a column name in the report's TableModel.
   *
   * @param dividend the field name (null not permitted).
   */
  public void setDividend (final String dividend)
  {
    this.dividend = dividend;
  }

  /**
   * Sets the field name to be used as divisor for the function. <P> The field name
   * corresponds to a column name in the report's TableModel.
   *
   * @param divisor the field name (null not permitted).
   */
  public void setDivisor (final String divisor)
  {
    this.divisor = divisor;
  }

  /**
   * Return a completly separated copy of this function. The copy does no longer share any
   * changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance ()
  {
    final TotalGroupSumQuotientFunction function =
            (TotalGroupSumQuotientFunction) super.getInstance();
    function.groupDividend = new GroupSum();
    function.groupDivisor = new GroupSum();
    function.dividendResults = new ArrayList();
    function.divisorResults = new ArrayList();
    return function;
  }

  private void readObject (final ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    groupDividend = new GroupSum();
    groupDivisor = new GroupSum();
    dividendResults = new ArrayList();
    divisorResults = new ArrayList();
  }


}