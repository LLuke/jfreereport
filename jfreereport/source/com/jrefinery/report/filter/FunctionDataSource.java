/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * -----------------------
 * FunctionDataSource.java
 * -----------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: FunctionDataSource.java,v 1.3 2002/06/06 16:00:59 mungady Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

import com.jrefinery.report.function.Function;
import com.jrefinery.report.ReportState;

/**
 * The base class for a function data source. A function datasource does not query a
 * function directly. The functions value is filled into this datasource by the current
 * band to be printend when the bands populateElements() function is called.
 * <p>
 * The value for this element is retrieved from a function registered in the report's
 * function collection by the name defined in this elements <code>function</code>
 * property.
 * <p>
 * If the function name is invalid (no function registered by that name), null is
 * returned.
 *
 * @see com.jrefinery.report.Band#populateElements
 */
public class FunctionDataSource implements DataSource
{

  /**
   * The name of the function as defined in the function collection for the report
   *
   * @see com.jrefinery.report.function.Function#setName(String)
   * @see com.jrefinery.report.function.Function#getName
   */
  private String function;

  /** The current value of the function. */
  private Object value;

  /**
   * Default constructor. The function name is empty ("", not null), the value initially
   * null.
   */
  public FunctionDataSource ()
  {
    setFunction("");
  }

  /**
   * Constructs a new function data source.
   *
   * @param function The function.
   */
  public FunctionDataSource (String function)
  {
    setFunction(function);
  }

  /**
   * Sets the function.
   *
   * @param field the name of the function as defined in the function collection.
   */
  public void setFunction (String field)
  {
    if (field == null) throw new NullPointerException();
    this.function = field;
  }

  /**
   * Returns the name of the function bound to this datasource.
   *
   * @return the registered function name
   */
  public String getFunction ()
  {
    return function;
  }

  /**
   * Sets the value retrieved from the function by the current band.
   *
   * @param value The value.
   */
  public void setValue (Object value)
  {
    this.value = value;
  }

  /**
   * Returns the value of the function.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    return value;
  }

}
