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
 * $Id: ItemHideFunction.java,v 1.1 2002/08/27 13:14:26 taqua Exp $
 *
 * Changes
 * -------
 * 27-Aug-2002 : Initial version
 */

package com.jrefinery.report.function;

import com.jrefinery.report.Element;
import com.jrefinery.report.event.ReportEvent;

import java.math.BigDecimal;

/**
 * The ItemHideFunction hides equal values in an group. Only the first changed value is printed.
 * This function uses the property <code>element</code> to define the name of the element in the
 * ItemBand that should be made visible or invisible by this function. The property <code>field</code>
 * defines the field in the datasource or the expression which should be used to determine the visibility.
 */
public class ItemHideFunction extends AbstractFunction
{
  public static final String ELEMENT_PROPERTY = "element";
  public static final String FIELD_PROPERTY = "field";

  /** The sum. */
  private Object lastObject;
  private boolean visible;
  private boolean firstInGroup;

  /**
   * Constructs an unnamed function. Make sure to set a Name or function initialisation
   * will fail.
   */
  public ItemHideFunction()
  {
  }

  /**
   * Constructs a named function.
   * <P>
   * The field must be defined before using the function.
   *
   * @param name The function name.
   */
  public ItemHideFunction(String name)
  {
    this();
    setName(name);
  }

  /**
   * Returns the name of the element in the item band that should be set visible/invisible.
   *
   * @return The element name.
   */
  public String getElement()
  {
    return getProperty(ELEMENT_PROPERTY);
  }

  /**
   * Sets the name of the element in the item band that should be set visible/invisible.
   *
   * @param _element The element name (must not be null).
   */
  public void setElement(String _element)
  {
    setProperty(ELEMENT_PROPERTY, _element);
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel or to an expression.
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
   * @param The field name (null not permitted).
   */
  public void setField(String field)
  {
    if (field == null)
      throw new NullPointerException();

    setProperty(FIELD_PROPERTY, field);
  }

  /**
   * Receives notification that a row of data is being processed.  Reads the data from the field
   * defined for this function and hides the field if the value is equal to the last value and the
   * this is not the first row of the item group.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    Object fieldValue = event.getDataRow().get(getField());

    // is visible when last and current object are not equal
    // first element in group is always visible
    if (firstInGroup == true)
    {
      visible = true;
      firstInGroup = false;
    }
    else
    {
      visible = (secureEquals(lastObject, fieldValue) == false);
    }
    lastObject = fieldValue;
    Element e = event.getReport().getItemBand().getElement(getElement());
    if (e != null)
    {
      e.setVisible(visible);
    }
  }

  /**
   * Compares 2 values where both values can contain null values.
   */
  private boolean secureEquals(Object o1, Object o2)
  {
    if (o1 == null && o2 == null) return true;
    if (o1 == null) return false;
    if (o2 == null) return false;
    return o1.equals(o2);
  }

  /**
   * resets the state of the function when a new ItemGroup has started.
   */
  public void itemsStarted(ReportEvent event)
  {
    lastObject = null;
    firstInGroup = true;
  }

  /**
   * Returns the function value, in this case the visibility of the defined element.
   *
   * @return The function value.
   */
  public Object getValue()
  {
    return new Boolean(visible);
  }

  /**
   * Initializes the function and tests that all required properties are set. If the required
   * field property or the element property are not set, a FunctionInitializeException is thrown.
   *
   * @throws FunctionInitializeException when no field or element is set.
   */
  public void initialize()
      throws FunctionInitializeException
  {
    String fieldProp = getProperty(FIELD_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
    String elementProp = getProperty(ELEMENT_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : element");
    }
  }

}
