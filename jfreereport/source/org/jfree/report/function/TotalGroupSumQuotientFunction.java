/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: TotalGroupSumQuotientFunction.java,v 1.13 2003/06/29 16:59:25 taqua Exp $
 *
 * Changes
 * -------
 * 16-Dec-2002 : Initial version, based on TotalGroupSumFunction.java
 *
 */

package org.jfree.report.function;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.jfree.report.event.ReportEvent;
import org.jfree.report.filter.DecimalFormatParser;
import org.jfree.report.filter.NumberFormatParser;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.util.Log;

/**
 * A report function that calculates the quotient of two summed fields (columns) from the
 * TableModel.
 * This function produces a global total. The total sum of the group is known when the group
 * processing starts and the report is not performing a prepare-run. The sum is calculated in
 * the prepare run and recalled in the printing run.
 * <p>
 * The function can be used in two ways:
 * <ul>
 * <li>to calculate a quotient for the entire report;</li>
 * <li>to calculate a quotient within a particular group;</li>
 * </ul>
 * This function expects its input values to be either java.lang.Number instances or Strings
 * that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p>
 * The function undestands tree parameters.
 * The <code>dividend</code> parameter is required and denotes the name of an ItemBand-field
 * which gets summed up as dividend. The <code>divisor</code> parameter is required and denotes
 * the name of an ItemBand-field which gets summed up as divisor.
 * <p>
 * The parameter <code>group</code> denotes the name of a group. When this group is started,
 * the counter gets reseted to null. This parameter is optional.
 *
 * @author Thomas Morgner
 */
public class TotalGroupSumQuotientFunction extends AbstractFunction implements Serializable
{
  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** Literal text for the 'dividend' property. */
  public static final String DIVIDEND_PROPERTY = "dividend";

  /** Literal text for the 'divisor' property. */
  public static final String DIVISOR_PROPERTY = "divisor";

  /**
   * Helperclass to make summing easier.
   */
  private static class GroupSum implements Serializable
  {
    /** The result. */
    private BigDecimal result;

    /**
     * Default constructor.
     */
    public GroupSum()
    {
      result = new BigDecimal(0);
    }

    /**
     * Adds a number to the result.
     *
     * @param n  the number.
     */
    public void add(final Number n)
    {
      result = result.add(new BigDecimal(n.toString()));
    }

    /**
     * Returns the sum.
     *
     * @return the sum.
     */
    public BigDecimal getResult()
    {
      return result;
    }
  }

  /** A useful constant representing zero. */
  private static final BigDecimal ZERO = new BigDecimal(0.0);

  /** The parser for performing data conversion. */
  private NumberFormatParser parser;

  /** The datasource of the parser. */
  private StaticDataSource datasource;

  /** The group sums for dividend and divisor. */
  private GroupSum groupDividend;

  /** The group divisor. */
  private GroupSum groupDivisor;

  /** A list of results. */
  private ArrayList dividendResults;

  /** A list of divisor results. */
  private ArrayList divisorResults;

  /** The current index. */
  private int currentIndex;

  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  public TotalGroupSumQuotientFunction()
  {
    groupDividend = new GroupSum();
    groupDivisor = new GroupSum();
    datasource = new StaticDataSource();
    parser = new DecimalFormatParser();
    parser.setNullValue(ZERO);
    parser.setDataSource(datasource);
    dividendResults = new ArrayList();
    divisorResults = new ArrayList();
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportInitialized(final ReportEvent event)
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
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
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
   * @param event  the event.
   */
  public void itemsAdvanced(final ReportEvent event)
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
        datasource.setValue(fieldValue);
        final Number n = (Number) parser.getValue();
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
        datasource.setValue(fieldValue);
        final Number n = (Number) parser.getValue();
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
  public String getGroup()
  {
    return getProperty(GROUP_PROPERTY);
  }

  /**
   * Defines the name of the group to be totalled.
   * If the name is null, all groups are totalled.
   *
   * @param group  the group name.
   */
  public void setGroup(final String group)
  {
    setProperty(GROUP_PROPERTY, group);
  }

  /**
   * Return the current function value.
   * <P>
   * The value depends (obviously) on the function implementation.   For example, a page counting
   * function will return the current page number.
   *
   * @return The value of the function.
   */
  public Object getValue()
  {
    final BigDecimal dividend = groupDividend.getResult();
    final BigDecimal divisor = groupDivisor.getResult();
    if (divisor.intValue() == 0)
    {
      return "n/a";
    }
    return new Double(dividend.doubleValue() / divisor.doubleValue());
  }

  /**
   * Returns the field used as dividend by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDividend()
  {
    return getProperty(DIVIDEND_PROPERTY);
  }

  /**
   * Returns the field used as divisor by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getDivisor()
  {
    return getProperty(DIVISOR_PROPERTY);
  }

  /**
   * Sets the field name to be used as dividend for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param dividend the field name (null not permitted).
   */
  public void setDividend(final String dividend)
  {
    if (dividend == null)
    {
      throw new NullPointerException();
    }
    setProperty(DIVIDEND_PROPERTY, dividend);
  }

  /**
   * Sets the field name to be used as divisor for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param divisor the field name (null not permitted).
   */
  public void setDivisor(final String divisor)
  {
    if (divisor == null)
    {
      throw new NullPointerException();
    }
    setProperty(DIVISOR_PROPERTY, divisor);
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   * <P>
   * The implementation checks that the Dividend and Divisor have been set.
   *
   * @throws FunctionInitializeException if a required property was not set.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    if (getProperty(DIVIDEND_PROPERTY) == null)
    {
      throw new FunctionInitializeException("Dividend is required");
    }
    if (getProperty(DIVISOR_PROPERTY) == null)
    {
      throw new FunctionInitializeException("Divisor is required");
    }
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final TotalGroupSumQuotientFunction function = (TotalGroupSumQuotientFunction) super.getInstance();
    function.groupDividend = new GroupSum();
    function.groupDivisor = new GroupSum();
    function.datasource = new StaticDataSource();
    function.parser = new DecimalFormatParser();
    function.parser.setNullValue(ZERO);
    function.parser.setDataSource(function.datasource);
    function.dividendResults = new ArrayList();
    function.divisorResults = new ArrayList();
    return function;
  }

}