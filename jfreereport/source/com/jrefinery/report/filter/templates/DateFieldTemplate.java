/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * AbstractTemplate.java
 * ---------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DateFieldTemplate.java,v 1.6 2003/06/27 14:25:18 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package com.jrefinery.report.filter.templates;

import java.text.SimpleDateFormat;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.StringFilter;

/**
 * A date field template.
 *
 * @author Thomas Morgner
 */
public class DateFieldTemplate extends AbstractTemplate implements DataRowConnectable
{
  /** The date format filter. */
  private SimpleDateFormatFilter dateFilter;

  /** The data-row datasource. */
  private DataRowDataSource dataRowDataSource;

  /** A string filter. */
  private StringFilter stringFilter;

  /**
   * Creates a new date field template.
   */
  public DateFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    dateFilter = new SimpleDateFormatFilter();
    dateFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(dateFilter);
  }

  /**
   * Returns the date format string.
   *
   * @return The date format string.
   */
  public String getFormat()
  {
    return getDateFilter().getFormatString();
  }

  /**
   * Sets the date format string.
   *
   * @param format  the format string.
   */
  public void setFormat(final String format)
  {
    getDateFilter().setFormatString(format);
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField()
  {
    return getDataRowDataSource().getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field  the field name.
   */
  public void setField(final String field)
  {
    getDataRowDataSource().setDataSourceColumnName(field);
  }

  /**
   * Returns the string that represents <code>null</code> values.
   *
   * @return A string.
   */
  public String getNullValue()
  {
    return getStringFilter().getNullValue();
  }

  /**
   * Sets the string that represents <code>null</code> values.
   *
   * @param nullValue  the string that represents <code>null</code> values.
   */
  public void setNullValue(final String nullValue)
  {
    getStringFilter().setNullValue(nullValue);
  }

  /**
   * Returns the date formatter.
   *
   * @return The date formatter.
   */
  public SimpleDateFormat getDateFormat()
  {
    return (SimpleDateFormat) getDateFilter().getFormatter();
  }

  /**
   * Sets the date formatter.
   *
   * @param dateFormat  the date formatter.
   */
  public void setDateFormat(final SimpleDateFormat dateFormat)
  {
    getDateFilter().setFormatter(dateFormat);
  }

  /**
   * Connects a {@link DataRow} to the data source.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow(final DataRow row) throws IllegalStateException
  {
    getDataRowDataSource().connectDataRow(row);
  }

  /**
   * Releases the connection to the data row.
   * <p>
   * If no data row is connected, an <code>IllegalStateException</code> is thrown to indicate the
   * programming error.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void disconnectDataRow(final DataRow row) throws IllegalStateException
  {
    getDataRowDataSource().disconnectDataRow(row);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return getStringFilter().getValue();
  }

  /**
   * Clones this template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final DateFieldTemplate template = (DateFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.dateFilter = (SimpleDateFormatFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.dateFilter.getDataSource();
    return template;
  }

  /**
   * Returns the date filter.
   *
   * @return The date filter.
   */
  protected SimpleDateFormatFilter getDateFilter()
  {
    return dateFilter;
  }

  /**
   * Returns the data-row datasource.
   *
   * @return The data-row datasource.
   */
  protected DataRowDataSource getDataRowDataSource()
  {
    return dataRowDataSource;
  }

  /**
   * Returns the string filter.
   *
   * @return The string filter.
   */
  protected StringFilter getStringFilter()
  {
    return stringFilter;
  }

}
