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
 * ------------------------
 * StringFieldTemplate.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: StringFieldTemplate.java,v 1.7 2005/03/01 10:09:19 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.filter.templates;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.RawDataSource;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.StringFilter;

/**
 * A string field template.
 *
 * @author Thomas Morgner
 */
public class StringFieldTemplate extends AbstractTemplate
        implements ReportConnectable, RawDataSource
{
  /**
   * The data-row data source.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * Creates a new string field template.
   */
  public StringFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(dataRowDataSource);
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field the field name.
   */
  public void setField (final String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  /**
   * Returns the value displayed by the field when the data source value is
   * <code>null</code>.
   *
   * @return A value to represent <code>null</code>.
   */
  public String getNullValue ()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the value displayed by the field when the data source value is
   * <code>null</code>.
   *
   * @param nullValue the value that represents <code>null</code>.
   */
  public void setNullValue (final String nullValue)
  {
    stringFilter.setNullValue(nullValue);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue ()
  {
    return stringFilter.getValue();
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final StringFieldTemplate template = (StringFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.stringFilter.getDataSource();
    return template;
  }

  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().unregisterReportDefinition(reportDefinition);
  }

  protected DataRowDataSource getDataRowDataSource ()
  {
    return dataRowDataSource;
  }

  public Object getRawValue ()
  {
    return stringFilter.getRawValue();
  }

}
