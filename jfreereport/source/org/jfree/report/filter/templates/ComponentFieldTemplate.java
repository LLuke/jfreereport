/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * DrawableFieldTemplate.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ComponentFieldTemplate.java,v 1.1 2005/10/11 14:53:21 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.09.2003 : Initial version
 *  
 */

package org.jfree.report.filter.templates;

import org.jfree.report.ReportDefinition;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.filter.ComponentDrawableFilter;

/**
 * An drawable field template. The drawable content will be read from the datarow.
 *
 * @author Thomas Morgner
 */
public class ComponentFieldTemplate extends AbstractTemplate
        implements ReportConnectable
{
  /**
   * The data row reader.
   */
  private DataRowDataSource dataRowDataSource;
  private ComponentDrawableFilter drawableFilter;

  /**
   * Creates a new image field template.
   */
  public ComponentFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    drawableFilter = new ComponentDrawableFilter();
    drawableFilter.setDataSource(dataRowDataSource);
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
    return drawableFilter.getValue();
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
    final ComponentFieldTemplate template = (ComponentFieldTemplate) super.clone();
    template.drawableFilter = (ComponentDrawableFilter) template.drawableFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.drawableFilter.getDataSource();
    return template;
  }

  /**
   * Connects the connectable to the given report definition.
   *
   * @param reportDefinition the reportDefinition for this report connectable.
   */
  public void registerReportDefinition (final ReportDefinition reportDefinition)
  {
    getDataRowDataSource().registerReportDefinition(reportDefinition);
    getDrawableFilter().registerReportDefinition(reportDefinition);
  }

  /**
   * Disconnects this ReportConnectable from the report definition.
   *
   * @param reportDefinition the ReportDefinition.
   */
  public void unregisterReportDefinition (final ReportDefinition reportDefinition)
  {
    getDrawableFilter().unregisterReportDefinition(reportDefinition);
    getDataRowDataSource().unregisterReportDefinition(reportDefinition);
  }

  /**
   * Returns the datarow data source used in this template.
   *
   * @return the datarow data source.
   */
  protected DataRowDataSource getDataRowDataSource ()
  {
    return dataRowDataSource;
  }

  protected ComponentDrawableFilter getDrawableFilter()
  {
    return drawableFilter;
  }
}
