/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * --------------------
 * ItemMaxFunction.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemMaxFunction.java,v 1.6 2003/01/14 21:06:39 taqua Exp $
 *
 * Changes
 * -------
 * 26-Aug-2002 : Initial version
 * 31-Aug-2002 : Documentation update
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.filter.DecimalFormatParser;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.util.Log;

import java.math.BigDecimal;

/**
 * A report function that calculates the maximum value of one field (column) from the TableModel.
 *
 * The function can be used in two ways:
 * <ul>
 * <li>to calculate a maximum value for the entire report;</li>
 * <li>to calculate a maximum value within a particular group;</li>
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
public class ItemMaxFunction extends AbstractFunction
{
  /** Literal text for the 'group' property. */
  public static final String GROUP_PROPERTY = "group";

  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** A useful constant for zero. */
  private static final BigDecimal ZERO = new BigDecimal(0.0);

  /** The maximum value. */
  private BigDecimal max;

  /** The parser for performing data conversion. */
  private NumberFormatParser parser;

  /** The datasource of the parser. */
  private StaticDataSource datasource;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemMaxFunction()
  {
    max = ZERO;
    datasource = new StaticDataSource();
    parser = new DecimalFormatParser();
    parser.setNullValue(ZERO);
    parser.setDataSource(datasource);
  }

  /**
   * Constructs a named function.
   * <P>
   * The field must be defined before using the function.
   *
   * @param name The function name.
   */
  public ItemMaxFunction(String name)
  {
    this();
    setName(name);
  }

  /**
   * Receives notification that a new report is about to start.
   * <P>
   * Does nothing.
   *
   * @param event Information about the event.
   *
   */
  public void reportStarted(ReportEvent event)
  {
    this.max = ZERO;
  }

  /**
   * Receives notification that a new group is about to start.  If this is the group defined for
   * the function, then the maximum value is reset to zero.
   *
   * @param event Information about the event.
   */
  public void groupStarted(ReportEvent event)
  {
    String mygroup = getGroup();
    if (mygroup == null)
    {
      return;
    }

    Group group = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    if (getGroup().equals(group.getName()))
    {
      this.max = ZERO;
    }
  }

  /**
   * Returns the group name.
   *
   * @return The group name.
   */
  public String getGroup()
  {
    return getProperty(GROUP_PROPERTY);
  }

  /**
   * Sets the group name.
   * <P>
   * If a group is defined, the maximum value is reset to zero at the start of every instance of
   * this group.
   *
   * @param name The group name (null permitted).
   */
  public void setGroup(String name)
  {
    setProperty(GROUP_PROPERTY, name);
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
  public void setField(String field)
  {
    if (field == null)
    {
      throw new NullPointerException();
    }
    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Receives notification that a row of data is being processed.  Reads the data from the field
   * defined for this function and performs the maximum value comparison with its old value.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    Object fieldValue = event.getDataRow().get(getField());
    datasource.setValue(fieldValue);
    Number n = (Number) parser.getValue();
    try
    {
      BigDecimal compare = new BigDecimal(n.doubleValue());
      if (max.compareTo(compare) < 0)
      {
        max = compare;
      }
    }
    catch (Exception e)
    {
      Log.error("ItemMaxFunction.advanceItems(): problem comparing number.");
    }
  }

  /**
   * Returns the function value, in this case the running total of a specific column in the
   * report's TableModel.
   *
   * @return The function value.
   */
  public Object getValue()
  {
    return max;
  }

  /**
   * Initializes the function and tests that all required properties are set. If the required
   * field property is not set, a FunctionInitializeException is thrown.
   *
   * @throws FunctionInitializeException when no field is set.
   */
  public void initialize()
      throws FunctionInitializeException
  {
    String fieldProp = getProperty(FIELD_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
    setField(fieldProp);
    setGroup(getProperty(GROUP_PROPERTY));
  }

  /**
   * Return a completly separated copy of this function. The copy does no
   * longer share any changeable objects with the original function.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    ItemMaxFunction function = (ItemMaxFunction) super.getInstance();
    function.max = ZERO;
    function.datasource = new StaticDataSource();
    function.parser = new DecimalFormatParser();
    function.parser.setNullValue(ZERO);
    function.parser.setDataSource(function.datasource);
    return function;
  }

}
