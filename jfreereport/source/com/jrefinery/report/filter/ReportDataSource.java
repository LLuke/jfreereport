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
 * ---------------------
 * ReportDataSource.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDataSource.java,v 1.8 2002/08/31 14:00:22 taqua Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

import com.jrefinery.report.DataRow;

/**
 * A data source that returns the value of a field in the report's TableModel.
 * <P>
 * The field is identified by the column name.
 *
 * @author TM
 *
 * @deprecated use DataRowDataSource as unified access class instead
 */
public class ReportDataSource implements DataSource, DataRowConnectable
{

  /** The field name. */
  private String fieldName;

  /** The data row. */
  private DataRow dataRow;

  /**
   * Default constructor.
   */
  public ReportDataSource ()
  {
  }

  /**
   * Constructs a new report data source.
   *
   * @param field The field name.
   */
  public ReportDataSource (String field)
  {
    setField (field);
  }

  /**
   * Sets the field name.
   * <P>
   * The field name should correspond to the name of one of the columns in the report's TableModel.
   *
   * @param field The field name.
   */
  public void setField (String field)
  {
    if (field == null)
    {
      throw new NullPointerException ();
    }
    this.fieldName = field;
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return fieldName;
  }

  /**
   * Returns the value of the data source.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    if (getDataRow () == null)
    {
      throw new IllegalStateException ("No DataRowBackend Connected");
    }
    return getDataRow ().get (getField ());
  }

  /**
   * Clones the data source.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone () throws CloneNotSupportedException
  {
    ReportDataSource rd = (ReportDataSource) super.clone ();
    return rd;
  }


  /**
   * Connects the DataRowBackend with the named DataSource or DataFilter.
   * The filter is now able to query the other DataSources to compute the result.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null)
    {
      throw new NullPointerException ("Null-DataRowBackend cannot be set.");
    }
    if (dataRow != null)
    {
      throw new IllegalStateException ("There is a datarow already connected: " + getField());
    }
    dataRow = row;
  }

  /**
   * Releases the connection to the datarow.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if no datarow is connected.
   */
  public void disconnectDataRow (DataRow row) throws IllegalStateException
  {
    if (row == null)
    {
      throw new NullPointerException ("Null-DataRowBackend cannot be disconnected.");
    }
    if (dataRow == null)
    {
      throw new IllegalStateException ("There is no datarow connected");
    }
    dataRow = null;
  }

  /**
   * Returns the data row.
   *
   * @return the data row.
   */
  protected DataRow getDataRow ()
  {
    return dataRow;
  }

}
