/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DateFieldTemplate.java,v 1.8 2005/06/25 17:51:59 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.filter.templates;

import java.text.SimpleDateFormat;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.RawDataSource;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.SimpleDateFormatFilter;
import org.jfree.report.filter.StringFilter;

/**
 * A date field template.
 *
 * @author Thomas Morgner
 */
public class DateFieldTemplate extends AbstractTemplate
        implements ReportConnectable, RawDataSource
{
  /**
   * The date format filter.
   */
  private SimpleDateFormatFilter dateFilter;

  /**
   * The data-row datasource.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * Creates a new date field template.
   */
  public DateFieldTemplate ()
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
  public String getFormat ()
  {
    return getDateFilter().getFormatString();
  }

  /**
   * Sets the date format string.
   *
   * @param format the format string.
   */
  public void setFormat (final String format)
  {
    getDateFilter().setFormatString(format);
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return getDataRowDataSource().getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field the field name.
   */
  public void setField (final String field)
  {
    getDataRowDataSource().setDataSourceColumnName(field);
  }

  /**
   * Returns the string that represents <code>null</code> values.
   *
   * @return A string.
   */
  public String getNullValue ()
  {
    return getStringFilter().getNullValue();
  }

  /**
   * Sets the string that represents <code>null</code> values.
   *
   * @param nullValue the string that represents <code>null</code> values.
   */
  public void setNullValue (final String nullValue)
  {
    getStringFilter().setNullValue(nullValue);
  }

  /**
   * Returns the date formatter.
   *
   * @return The date formatter.
   */
  public SimpleDateFormat getDateFormat ()
  {
    return (SimpleDateFormat) getDateFilter().getFormatter();
  }

  /**
   * Sets the date formatter.
   *
   * @param dateFormat the date formatter.
   */
  public void setDateFormat (final SimpleDateFormat dateFormat)
  {
    getDateFilter().setFormatter(dateFormat);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue ()
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
  public Object clone ()
          throws CloneNotSupportedException
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
  protected SimpleDateFormatFilter getDateFilter ()
  {
    return dateFilter;
  }

  /**
   * Returns the data-row datasource.
   *
   * @return The data-row datasource.
   */
  protected DataRowDataSource getDataRowDataSource ()
  {
    return dataRowDataSource;
  }

  /**
   * Returns the string filter.
   *
   * @return The string filter.
   */
  protected StringFilter getStringFilter ()
  {
    return stringFilter;
  }

  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().unregisterReportDefinition(reportDefinition);
  }

  public Object getRawValue ()
  {
    return stringFilter.getRawValue();
  }
}
