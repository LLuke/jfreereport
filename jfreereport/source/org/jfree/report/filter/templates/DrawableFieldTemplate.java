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
 * ------------------------------
 * DrawableFieldTemplate.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DrawableFieldTemplate.java,v 1.2 2003/11/01 19:52:27 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.09.2003 : Initial version
 *  
 */

package org.jfree.report.filter.templates;

import org.jfree.report.DataRow;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DrawableFilter;
import org.jfree.report.filter.DataRowConnectable;

/**
 * An drawable field template. The drawable content will be read from
 * the datarow.
 *  
 * @author Thomas Morgner
 */
public class DrawableFieldTemplate extends AbstractTemplate
    implements DataRowConnectable
{
  /** The data row reader. */
  private DataRowDataSource dataRowDataSource;

  /** An image reference filter. */
  private DrawableFilter drawableFilter;

  /**
   * Creates a new image field template.
   */
  public DrawableFieldTemplate()
  {
    dataRowDataSource = new DataRowDataSource();
    drawableFilter = new DrawableFilter();
    drawableFilter.setDataSource(dataRowDataSource);
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
    return drawableFilter.getValue();
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
    final DrawableFieldTemplate template = (DrawableFieldTemplate) super.clone();
    template.drawableFilter = (DrawableFilter) drawableFilter.clone();
    template.dataRowDataSource = (DataRowDataSource) template.drawableFilter.getDataSource();
    return template;
  }

  /**
   * Connects a {@link org.jfree.report.DataRow} to the data source.
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

}
