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
 * $Id: ItemSumFunction.java,v 1.17 2002/08/20 20:58:20 taqua Exp $
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
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Group;
import com.jrefinery.report.Element;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.filter.DecimalFormatParser;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.util.Log;

import java.math.BigDecimal;

/**
 * A report function that calculates the sum of one field (column) from the TableModel.
 * This function produces a running total, no global total. For a global sum, use the
 * TotalGroupSumFunction function.
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
 */
public class ItemHideFunction extends AbstractFunction
{
  public static final String ELEMENT_PROPERTY = "element";
  public static final String FIELD_PROPERTY = "field";

  /** Zero. */
  private static final BigDecimal ZERO = new BigDecimal (0.0);

  /** The sum. */
  private Object lastObject;
  private boolean visible;
  private boolean firstInGroup;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemHideFunction ()
  {
  }

  /**
   * Constructs a named function.
   * <P>
   * The field must be defined before using the function.
   *
   * @param name The function name.
   */
  public ItemHideFunction (String name)
  {
    this ();
    setName (name);
  }

  /**
   * Returns the group name.
   *
   * @return The group name.
   */
  public String getElement ()
  {
    return getProperty(ELEMENT_PROPERTY);
  }

  /**
   * Sets the group name.
   * <P>
   * If a group is defined, the running total is reset to zero at the start of every instance of
   * this group.
   *
   * @param _group The group name (null permitted).
   */
  public void setElement (String _element)
  {
    setProperty (ELEMENT_PROPERTY, _element);
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
    return getProperty(FIELD_PROPERTY);
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

    setProperty (FIELD_PROPERTY, field);
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
    Object fieldValue = event.getDataRow ().get (getField ());

    Log.debug ("ItemAdvanced:  ItemHide");
    // is visible when last and current object are not equal
    // first element in group is always visible
    if (firstInGroup == true)
    {
      visible = true;
      firstInGroup = false;
    }
    else
    {
      visible = (secureEquals (lastObject, fieldValue) == false);
    }
    lastObject = fieldValue;
    Element e = event.getReport().getItemBand().getElement(getElement());
    if (e != null)
    {
      Log.debug ("Element found and visible is set to " + visible);
      e.setVisible(visible);
    }
  }

  private boolean secureEquals (Object o1, Object o2)
  {
    Log.debug ("O1 = " + o1 + " - O2 = " + o2);
    if (o1 == null && o2 == null) return true;
    if (o1 == null) return false;
    if (o2 == null) return false;
    return o1.equals(o2);
  }

  public void itemsStarted(ReportEvent event)
  {
    lastObject = null;
    firstInGroup = true;
  }

  /**
   * Returns the function value, in this case the running total of a specific column in the
   * report's TableModel.
   *
   * @return The function value.
   */
  public Object getValue ()
  {
    return new Boolean(visible);
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
    String fieldProp = getProperty (FIELD_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException ("No Such Property : field");
    }
    String elementProp = getProperty (ELEMENT_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException ("No Such Property : element");
    }
  }

}
