/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * ----------------------
 * DataRowDataSource.java
 * ----------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DataRowDataSource.java,v 1.7 2005/01/25 21:40:12 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 28-Aug-2002 : Documentation
 * 06-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.filter;

import java.io.Serializable;

import org.jfree.report.DataRow;
import org.jfree.report.ReportDefinition;

/**
 * A DataSource that can access values from the 'data-row'. The data-row contains all
 * values from the current row of the report's <code>TableModel</code>, plus the current
 * values of the defined expressions and functions for the report.
 * <p/>
 *
 * @author Thomas Morgner
 * @see org.jfree.report.DataRow
 */
public class DataRowDataSource
        implements DataSource, Serializable, ReportConnectable
{
  /**
   * The name of the field/expression/function referenced by this data source.
   */
  private String dataSourceColumnName;

  private transient ReportDefinition reportDefinition;

  /**
   * Default constructor.
   * <p/>
   * The expression name is empty ("", not null), the value initially null.
   */
  public DataRowDataSource ()
  {
    setDataSourceColumnName("");
  }

  /**
   * Constructs a new data source.
   *
   * @param column the name of the field, function or expression in the data-row.
   */
  public DataRowDataSource (final String column)
  {
    setDataSourceColumnName(column);
  }

  /**
   * Returns the data source column name.
   *
   * @return the column name.
   */
  public String getDataSourceColumnName ()
  {
    return dataSourceColumnName;
  }

  /**
   * Defines the name of the column in the datarow to be queried.
   *
   * @param dataSourceColumnName the name of the column in the datarow to be queried.
   * @throws NullPointerException if the name is <code>null</code>.
   * @see org.jfree.report.DataRow#get
   */
  public void setDataSourceColumnName (final String dataSourceColumnName)
  {
    if (dataSourceColumnName == null)
    {
      throw new NullPointerException();
    }
    this.dataSourceColumnName = dataSourceColumnName;
  }

  /**
   * Returns the current value of the data source, obtained from a particular column in
   * the data-row.
   *
   * @return the value.
   *
   * @throws IllegalStateException if there is no data-row connected.
   */
  public Object getValue ()
  {
    if (getDataRow() == null)
    {
      throw new IllegalStateException("No DataRowBackend Connected");
    }
    return getDataRow().get(getDataSourceColumnName());
  }

  /**
   * Clones the data source. A previously registered report definition is not inherited to
   * the clone.
   *
   * @return a clone.
   *
   * @throws CloneNotSupportedException if the cloning is not supported.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final DataRowDataSource drs = (DataRowDataSource) super.clone();
    drs.reportDefinition = null;
    return drs;
  }

  /**
   * Returns the current data-row.
   *
   * @return the data-row.
   */
  public DataRow getDataRow ()
  {
    if (reportDefinition == null)
    {
      return null;
    }
    return reportDefinition.getDataRow();
  }


  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != null)
    {
      throw new IllegalStateException("Already connected.");
    }
    if (reportDefinition == null)
    {
      throw new NullPointerException("The given report definition is null");
    }
    this.reportDefinition = reportDefinition;
  }

  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != reportDefinition)
    {
      throw new IllegalStateException("This report definition is not registered.");
    }
    this.reportDefinition = null;
  }


}