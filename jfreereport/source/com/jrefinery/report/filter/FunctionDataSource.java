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
 * $Id$
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
 * The base class for a function data source.
 */
public class FunctionDataSource implements DataSource
{

  /** ??? */
  private String function;

  /** The current value of the function. */
  private Object value;

  /**
   * Default constructor.
   */
  public FunctionDataSource ()
  {
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
   * @param field ???
   */
  public void setFunction (String field)
  {
    if (field == null) throw new NullPointerException();
    this.function = field;
  }

  /**
   * Returns the function.
   *
   * @return ???
   */
  public String getFunction ()
  {
    return function;
  }

  /**
   * Sets the value of the function.
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
