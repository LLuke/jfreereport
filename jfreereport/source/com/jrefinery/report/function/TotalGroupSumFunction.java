/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: TotalGroupSumFunction.java,v 1.14 2002/12/02 17:29:23 taqua Exp $
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

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.filter.DecimalFormatParser;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.states.ReportState;
import com.jrefinery.report.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * A report function that calculates the sum of one field (column) from the TableModel.
 * This function produces a global total. The total sum of the group is known when the group
 * processing starts and the report is not performing a prepare-run. The sum is calculated in
 * the prepare run and recalled in the printing run.
 * <p>
 * The function can be used in two ways:
 * <ul>
 * <li>to calculate a sum for the entire report;</li>
 * <li>to calculate a sum within a particular group;</li>
 * </ul>
 * This function expects its input values to be either java.lang.Number instances or Strings
 * that can be parsed to java.lang.Number instances using a java.text.DecimalFormat.
 * <p>
 * The function undestands two parameters, the <code>field</code> parameter is required and
 * denotes the name of an ItemBand-field which gets summed up.
 * <p>
 * The parameter <code>group</code> denotes the name of a group. When this group is started,
 * the counter gets reseted to null.
 *
 * @author Thomas Morgner
 */
public class TotalGroupSumFunction extends AbstractFunction
{
  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /**
   * Helperclass to make summing easier.
   */
  private static class GroupSum
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
    public void add(Number n)
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

  /** The parser for performing data conversion */
  private NumberFormatParser parser;

  /** The datasource of the parser */
  private StaticDataSource datasource;

  /** The group sum. */
  private GroupSum groupResult;

  /** A list of results. */
  private ArrayList results;

  /** The current index. */
  private int currentIndex;

  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  public TotalGroupSumFunction()
  {
    groupResult = new GroupSum();
    datasource = new StaticDataSource();
    parser = new DecimalFormatParser();
    parser.setNullValue(ZERO);
    parser.setDataSource(datasource);
    results = new ArrayList();
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event  the event.
   */
  public void reportStarted(ReportEvent event)
  {
    currentIndex = -1;
    if (event.getState().isPrepareRun() == false)
    {
      return;
    }
    else
    {
      results.clear();
      // just make sure that we dont get any nullpointerexceptions ...
      groupResult = new GroupSum();
    }
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event  the event.
   */
  public void groupStarted(ReportEvent event)
  {

    if (getGroup() != null)
    {
      JFreeReport report = event.getReport();
      ReportState state = event.getState();
      Group group = report.getGroup(state.getCurrentGroupIndex());
      if (getGroup().equals(group.getName()))
      {
        if (event.getState().isPrepareRun() == false)
        {
          // Activate the current group, which was filled in the prepare run.
          currentIndex += 1;
          groupResult = (GroupSum) results.get(currentIndex);
        }
        else
        {
          groupResult = new GroupSum();
          results.add(groupResult);
        }
      }
    }
  }


  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    if (event.getState().isPrepareRun() == false)
    {
      return;
    }

    Object fieldValue = event.getDataRow().get(getField());
    if (fieldValue == null)
    {
      // No add, field is null
      return;
    }
    try
    {
      datasource.setValue(fieldValue);
      Number n = (Number) parser.getValue();
      groupResult.add(n);
    }
    catch (Exception e)
    {
      Log.error("ItemSumFunction.advanceItems(): problem adding number.");
    }
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
    return super.clone();
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
  public void setGroup(String group)
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
    return groupResult.getResult();
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
   * @param field the field name (null not permitted).
   */
  public void setField(String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   * <P>
   * The default implementation checks that the function name is not null, and calls the
   * isInitialized() method (now deprecated).
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    if (getProperty(FIELD_PROPERTY) == null)
    {
      throw new FunctionInitializeException("Field is required");
    }
  }

}