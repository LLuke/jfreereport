/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * $Id: ItemSumFunction.java,v 1.7 2002/06/05 23:21:47 mungady Exp $
 *
 * Changes
 * -------
 * 18-Feb-2002 : Version 1, contributed by Thomas Morgner (DG);
 * 24-Apr-2002 : Changed the implementation to reflect the changes in Function and
 *               AbstractFunction
 * 10-May-2002 : Applied the ReportEvent interface
 *
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.filter.DataFilter;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.DecimalFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.event.ReportEvent;

import javax.swing.table.TableModel;
import java.math.BigDecimal;

/**
 * A report function that calculates the sum of one field (column) from the TableModel.  The
 * function can be used in two ways:
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
 */
public class ItemSumFunction extends AbstractFunction
{
  /** Zero. */
  private static final BigDecimal ZERO = new BigDecimal (0.0);

  /** The group name. */
  private String group;

  /** The field name. */
  private String field;

  /** The sum. */
  private BigDecimal sum;

  /** The parser for performing data conversion */
  private NumberFormatParser parser;

  /** The datasource of the parser */
  private StaticDataSource datasource;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemSumFunction ()
  {
    sum = ZERO;
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
  public ItemSumFunction (String name)
  {
    setName (name);
    this.sum = ZERO;
  }

  /**
   * Receives notification that a new report is about to start.
   * <P>
   * Does nothing.
   *
   * @param event Information about the event.
   *
   */
  public void reportStarted (ReportEvent event)
  {
    this.sum = ZERO;
  }

  /**
   * Receives notification that a new group is about to start.  If this is the group defined for
   * the function, then the running total is reset to zero.
   *
   * @param event Information about the event.
   */
  public void groupStarted (ReportEvent event)
  {
    String mygroup = getGroup();
    if (mygroup == null)
    {
      return;
    }

    Group group = event.getReport().getGroup(event.getState().getCurrentGroupIndex());
    if (  this.group.equals (group.getName ()))
    {
      this.sum = ZERO;
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
   * Sets the group name.
   * <P>
   * If a group is defined, the running total is reset to zero at the start of every instance of
   * this group.
   *
   * @param _group The group name (null permitted).
   */
  public void setGroup (String _group)
  {
    this.group = _group;
    setProperty ("group", _group);
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return field;
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param The field name (null not permitted).
   */
  public void setField (String field)
  {
    if (field == null)
      throw new NullPointerException ();

    this.field = field;
    setProperty ("field", field);
  }

  /**
   * Receives notification that a row of data is being processed.  Reads the data from the field
   * defined for this function and adds it to the running total.
   * <P>
   * This function assumes that it will find an instance of the Number class in the column of the
   * TableModel specified by the field name.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    TableModel data = event.getReport().getData ();
    int row = event.getState().getCurrentDataItem();

    Object fieldValue = null;
    for (int c = 0; c < data.getColumnCount (); c++)
    {
      if (this.field.equals (data.getColumnName (c)))
      {
        fieldValue = data.getValueAt (row, c);
      }
    }

    if (fieldValue == null)
    {
      return;
    }
    datasource.setValue(fieldValue);
    Number n = (Number) parser.getValue();
    try
    {
      sum = sum.add (new BigDecimal (n.doubleValue()));
    }
    catch (Exception e)
    {
      Log.error ("ItemSumFunction.advanceItems(): problem adding number.");
    }
  }

  /**
   * Returns the function value, in this case the running total of a specific column in the
   * report's TableModel.
   *
   * @return The function value.
   */
  public Object getValue ()
  {
    return sum;
  }

  /**
   * Initializes the function and tests that all required properties are set. If the required
   * field property is not set, a FunctionInitializeException is thrown.
   *
   * @throws FunctionInitializeException when no field is set.
   */
  public void initialize ()
    throws FunctionInitializeException
  {
    String fieldProp = getProperty ("field");
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
    setField (fieldProp);
    setGroup (getProperty ("group"));
  }

}
