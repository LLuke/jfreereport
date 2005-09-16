/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * AnchorFieldTemplate.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: AnchorFieldTemplate.java,v 1.3 2005/03/03 23:00:00 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.filter.templates;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.AnchorFilter;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ReportConnectable;

public class AnchorFieldTemplate extends AbstractTemplate
  implements ReportConnectable
{
  /**
   * The data-row data source.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A string filter.
   */
  private AnchorFilter anchorFilter;

  /**
   * Creates a new string field template.
   */
  public AnchorFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    anchorFilter = new AnchorFilter();
    anchorFilter.setDataSource(dataRowDataSource);
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
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue ()
  {
    return anchorFilter.getValue();
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
    final AnchorFieldTemplate template = (AnchorFieldTemplate) super.clone();
    template.anchorFilter = (AnchorFilter) anchorFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.anchorFilter.getDataSource();
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
}
