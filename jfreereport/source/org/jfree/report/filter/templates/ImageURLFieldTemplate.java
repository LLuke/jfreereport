/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ----------------------------
 * ImageURLElementTemplate.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageURLFieldTemplate.java,v 1.2 2003/08/24 15:13:22 taqua Exp $
 *
 * Changes (from 18-Feb-2003)
 * -------------------------
 * 18-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.filter.templates;

import java.net.URL;

import org.jfree.report.DataRow;
import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowConnectable;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ImageLoadFilter;
import org.jfree.report.filter.URLFilter;
import org.jfree.report.filter.ReportConnectable;

/**
 * An image URL field template, which reads the image from an URL supplied from
 * a column in the DataRow.
 *
 * @author Thomas Morgner
 */
public class ImageURLFieldTemplate extends AbstractTemplate
    implements DataRowConnectable, ReportConnectable
{
  /** An image load filter. */
  private ImageLoadFilter imageLoadFilter;

  /** A data row accessor. */
  private DataRowDataSource dataRowDataSource;

  /** A URL filter. */
  private URLFilter urlFilter;

  /**
   * Creates a new template.
   */
  public ImageURLFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    urlFilter = new URLFilter();
    urlFilter.setDataSource(dataRowDataSource);
    imageLoadFilter = new ImageLoadFilter();
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
    final ImageURLFieldTemplate template = (ImageURLFieldTemplate) super.clone();
    template.imageLoadFilter = (ImageLoadFilter) imageLoadFilter.clone();
    template.urlFilter = (URLFilter) template.imageLoadFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.urlFilter.getDataSource();
    return template;
  }

  /**
   * Connects a {@link DataRow} to the template.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow(final DataRow row) throws IllegalStateException
  {
    dataRowDataSource.connectDataRow(row);
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
    dataRowDataSource.disconnectDataRow(row);
  }


  public void registerReportDefinition(ReportDefinition reportDefinition)
  {
    getDataRowDataSource().registerReportDefinition(reportDefinition);
  }

  public void unregisterReportDefinition(ReportDefinition reportDefinition)
  {
    getDataRowDataSource().unregisterReportDefinition(reportDefinition);
  }

  protected DataRowDataSource getDataRowDataSource()
  {
    return dataRowDataSource;
  }
}
