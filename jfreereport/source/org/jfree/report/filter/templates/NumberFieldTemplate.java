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
 * ------------------
 * LabelTemplate.java
 * ------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: NumberFieldTemplate.java,v 1.6 2005/02/23 21:04:46 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.filter.templates;

import java.text.DecimalFormat;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DecimalFormatFilter;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.filter.RawDataSource;

/**
 * A number field template.
 *
 * @author Thomas Morgner
 */
public class NumberFieldTemplate extends AbstractTemplate
        implements ReportConnectable, RawDataSource
{
  /**
   * A decimal format filter.
   */
  private DecimalFormatFilter decimalFormatFilter;

  /**
   * A data-row accessor.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * Creates a new number field template.
   */
  public NumberFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    decimalFormatFilter = new DecimalFormatFilter();
    decimalFormatFilter.setDataSource(dataRowDataSource);
    stringFilter = new StringFilter();
    stringFilter.setDataSource(decimalFormatFilter);
  }

  /**
   * Returns the number formatter.
   *
   * @return The number formatter.
   */
  public DecimalFormat getDecimalFormat ()
  {
    return (DecimalFormat) decimalFormatFilter.getFormatter();
  }

  /**
   * Sets the number formatter.
   *
   * @param decimalFormat the number formatter.
   */
  public void setDecimalFormat (final DecimalFormat decimalFormat)
  {
    decimalFormatFilter.setFormatter(decimalFormat);
  }

  /**
   * Returns the format string.
   *
   * @return The format string.
   */
  public String getFormat ()
  {
    return decimalFormatFilter.getFormatString();
  }

  /**
   * Sets the format string.
   *
   * @param format the format string.
   */
  public void setFormat (final String format)
  {
    decimalFormatFilter.setFormatString(format);
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
   * Returns the string that represents a <code>null</code> value.
   *
   * @return A string.
   */
  public String getNullValue ()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the string that represents a <code>null</code> value.
   *
   * @param nullValue the string that represents a <code>null</code> value.
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
    final NumberFieldTemplate template = (NumberFieldTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.decimalFormatFilter = (DecimalFormatFilter) template.stringFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.decimalFormatFilter.getDataSource();
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
