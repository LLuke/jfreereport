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
 * -----------------------
 * ShapeFieldTemplate.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ShapeFieldTemplate.java,v 1.5 2004/05/07 08:24:41 mungady Exp $
 *
 * Changes
 * -------
 * 07-Mar-2003 : Version 1;
 *
 */

package org.jfree.report.filter.templates;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.ShapeFilter;

/**
 * A shape field template.
 *
 * @author Thomas Morgner.
 */
public class ShapeFieldTemplate extends AbstractTemplate
    implements ReportConnectable
{
  /** The data row reader. */
  private DataRowDataSource dataRowDataSource;

  /** A shape filter. */
  private ShapeFilter shapeFilter;

  /**
   * Creates a new shape field template.
   */
  public ShapeFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    shapeFilter = new ShapeFilter();
    shapeFilter.setDataSource(dataRowDataSource);
  }

  /**
   * Returns the field name.
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
   * Returns the current value for the data source.
   *
   * @return the value.
   */
  public Object getValue()
  {
    return shapeFilter.getValue();
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
    final ShapeFieldTemplate template = (ShapeFieldTemplate) super.clone();
    template.shapeFilter = (ShapeFilter) shapeFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.shapeFilter.getDataSource();
    return template;
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
