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
 * ----------------------------
 * ImageURLElementTemplate.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ImageURLFieldTemplate.java,v 1.5 2005/01/24 23:59:48 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.filter.templates;

import java.net.URL;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DrawableLoadFilter;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.URLFilter;

/**
 * An image URL field template, which reads the image from an URL supplied from
 * a column in the DataRow.
 *
 * @author Thomas Morgner
 */
public class DrawableURLFieldTemplate extends AbstractTemplate
    implements ReportConnectable
{
  /** An image load filter. */
  private DrawableLoadFilter imageLoadFilter;

  /** A data row accessor. */
  private DataRowDataSource dataRowDataSource;

  /** A URL filter. */
  private URLFilter urlFilter;

  /**
   * Creates a new template.
   */
  public DrawableURLFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    urlFilter = new URLFilter();
    urlFilter.setDataSource(dataRowDataSource);
    imageLoadFilter = new DrawableLoadFilter();
    imageLoadFilter.setDataSource(urlFilter);
  }

  /**
   * Returns the name of the field from the data-row that the template gets images from.
   *
   * @return The field name.
   */
  public String getField()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field  the field name.
   */
  public void setField(final String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  /**
   * Returns the base URL.
   *
   * @return The base URL.
   */
  public URL getBaseURL()
  {
    return urlFilter.getBaseURL();
  }

  /**
   * Sets the base URL.
   *
   * @param baseURL  the base URL.
   */
  public void setBaseURL(final URL baseURL)
  {
    urlFilter.setBaseURL(baseURL);
  }

  /**
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return imageLoadFilter.getValue();
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final DrawableURLFieldTemplate template = (DrawableURLFieldTemplate) super.clone();
    template.imageLoadFilter = (DrawableLoadFilter) imageLoadFilter.clone();
    template.urlFilter = (URLFilter) template.imageLoadFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.urlFilter.getDataSource();
    return template;
  }

  public void registerReportDefinition(final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition(final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().unregisterReportDefinition(reportDefinition);
  }

  protected DataRowDataSource getDataRowDataSource()
  {
    return dataRowDataSource;
  }
}
