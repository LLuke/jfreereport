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
 * $Id: FunctionDataSource.java,v 1.9 2002/08/22 21:08:24 taqua Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 * 28-Jul-2002 : Added support for the DataRow
 * 08-Aug-2002 : removed unused Import statements
 * 14-Aug-2002 : Logging added, Modified Exception messages to help debugging
 * 22-Aug-2002 : Removed System.out statements
 * 28-Aug-2002 : Removed Logging, Documentation updated
 *
 */

package com.jrefinery.report.filter;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.util.Log;

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
public class FunctionDataSource implements DataSource, DataRowConnectable
{

  /**
   * The name of the function as defined in the function collection for the report
   *
   * @see com.jrefinery.report.function.Function#setName(String)
   * @see com.jrefinery.report.function.Function#getName
   */
  private String function;

  private DataRow dataRow;

  /**
   * Default constructor. The function name is empty ("", not null), the value initially
   * null.
   */
  public FunctionDataSource ()
  {
    setFunction ("");
  }

  /**
   * Constructs a new function data source.
   *
   * @param function The function.
   */
  public FunctionDataSource (String function)
  {
    setFunction (function);
  }

  /**
   * Sets the function.
   *
   * @param field the name of the function as defined in the function collection.
   */
  public void setFunction (String field)
  {
    if (field == null) throw new NullPointerException ();
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
   * Returns the value of the function.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    if (getDataRow () == null) throw new IllegalStateException ("No Datarow connected");
    return getDataRow ().get (getFunction ());
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }

  /**
   * Connects the DataRowBackend with the named DataSource or DataFilter.
   * The filter is now able to query the other DataSources to compute the result.
   *
   * If there is already a datarow connected, an IllegalStateException is thrown.
   */
  public void connectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null) throw new NullPointerException ("Null-DataRowBackend cannot be set.");
    if (dataRow != null) throw new IllegalStateException ("There is a datarow already connected for Function-Datasource " + getFunction());
    dataRow = row;
  }

  /**
   * Releases the connection to the datarow. If no datarow is connected, an
   * IllegalStateException is thrown to indicate the programming error.
   */
  public void disconnectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null) throw new NullPointerException ("Null-DataRowBackend cannot be disconnected.");
    if (dataRow == null) throw new IllegalStateException ("There is no datarow connected");
    dataRow = null;
  }

  /**
   * returns the datarow assigned with this DataSource.
   */
  protected DataRow getDataRow ()
  {
    return dataRow;
  }

}
