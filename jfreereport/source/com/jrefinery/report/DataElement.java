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
 * ----------------
 * DataElement.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: DataElement.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Changed constructors from public --> protected (DG);
 * 10-May-2002 : Removed all complex constructors and declared abstract
 */

package com.jrefinery.report;

/**
 * The base class for all report elements that display data (that is, information from the report's
 * data source) rather than just static information.
 */
public abstract class DataElement extends TextElement
{

  /** The name of the field in the data source that this element obtains its data from. */
  private String field;

  /** The current value from the data source. */
  private Object value;

  /**
   * Constructs a data element using float coordinates.
   */
  protected DataElement()
  {
  }

  /**
   * Sets the fieldname for this element.
   *
   * @throws NullPointerException if the field is null.
   */
  public void setField(String fieldname)
  {
    if (fieldname == null)
      throw new NullPointerException("Fieldname must not be null for field " + getName());

    this.field = fieldname;
  }

  /**
   * Returns the name of the field in the data source that this element obtains its data from.
   * @return The field name.
   */
  public String getField()
  {
    return this.field;
  }

  /**
   * Sets the current value of the element.
   * @param value The new value for the element;
   */
  public void setValue(Object value)
  {
    this.value = value;
  }

  /**
   * Queries the current value of the element.
   * @returns the value for the element;
   */
  public Object getValue()
  {
    return value;
  }

  /**
   * Returns a string representing the formatted data.
   * @return A formatted version of the data value or "null" if the element
   * is null
   */
  public String getFormattedText()
  {
    return value == null ? "" : value.toString();
  }
}