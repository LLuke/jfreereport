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
 * ---------------------
 * ItemHideFunction.java
 * ---------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ItemHideFunction.java,v 1.4 2003/10/18 19:32:12 taqua Exp $
 *
 * Changes
 * -------
 * 27-Aug-2002 : Initial version
 */

package org.jfree.report.function;

import java.io.Serializable;

import org.jfree.report.Element;
import org.jfree.report.event.ReportEvent;
import org.jfree.util.ObjectUtils;

/**
 * The ItemHideFunction hides equal values in a group. Only the first changed value is printed.
 * This function uses the property <code>element</code> to define the name of the element in the
 * ItemBand that should be made visible or invisible by this function.
 * The property <code>field</code> defines the field in the datasource or the expression which
 * should be used to determine the visibility.
 *
 * @author Thomas Morgner
 */
public class ItemHideFunction extends AbstractFunction implements Serializable
{
  /** Literal text for the 'element' property. */
  public static final String ELEMENT_PROPERTY = "element";

  /** Literal text for the 'field' property. */
  public static final String FIELD_PROPERTY = "field";

  /** The last object. */
  private transient Object lastObject;

  /** The 'visible' flag. */
  private boolean visible;

  /** The 'first-in-group' flag. */
  private boolean firstInGroup;

  /**
   * Constructs an unnamed function.
   * <P>
   * Make sure to set the function name before it is used, or function initialisation will fail.
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
  public ItemHideFunction(final String name)
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
   * @param name  the element name (must not be null).
   */
  public void setElement(final String name)
  {
    setProperty(ELEMENT_PROPERTY, name);
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
   * Receives notification that a row of data is being processed.  Reads the data from the field
   * defined for this function and hides the field if the value is equal to the last value and the
   * this is not the first row of the item group.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    final Object fieldValue = event.getDataRow().get(getField());

    // is visible when last and current object are not equal
    // first element in group is always visible
    if (firstInGroup == true)
    {
      visible = true;
      firstInGroup = false;
    }
    else
    {
      visible = (ObjectUtils.equal(lastObject, fieldValue) == false);
    }
    lastObject = fieldValue;
    final Element e = event.getReport().getItemBand().getElement(getElement());
    if (e != null)
    {
      e.setVisible(visible);
    }
  }

  /**
   * Resets the state of the function when a new ItemGroup has started.
   *
   * @param event  the report event.
   */
  public void itemsStarted(final ReportEvent event)
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
    if (visible)
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
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
    final String fieldProp = getProperty(FIELD_PROPERTY);
    if (fieldProp == null)
    {
      throw new FunctionInitializeException("No Such Property : field");
    }
    final String elementProp = getProperty(ELEMENT_PROPERTY);
    if (elementProp == null)
    {
      throw new FunctionInitializeException("No Such Property : element");
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
    final ItemHideFunction ih = (ItemHideFunction) super.getInstance();
    ih.lastObject = null;
    return ih;
  }
}
