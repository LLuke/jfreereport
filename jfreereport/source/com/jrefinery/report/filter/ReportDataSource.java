/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ReportDataSource.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportDataSource.java,v 1.3 2002/06/06 16:00:59 mungady Exp $
 *
 * Changes
 * -------
 * 20-May-2002 : Initial version
 * 06-Jun-2002 : Updated Javadoc comments (DG);
 *
 */

package com.jrefinery.report.filter;

/**
 * A data source that returns the value of a field in the report's TableModel.
 * <P>
 * The field is identified by the column name.
 */
public class ReportDataSource implements DataSource
{

  /** The field name. */
  private String fieldName;

  /** The current value. */
  private Object value;

  /**
   * Default constructor.
   */
  public ReportDataSource ()
  {
  }

  /**
   * Constructs a new report data source.
   *
   * @param field The field name.
   */
  public ReportDataSource (String field)
  {
    setField(field);
  }

  /**
   * Sets the field name.
   * <P>
   * The field name should correspond to the name of one of the columns in the report's TableModel.
   *
   * @param field The field name.
   */
  public void setField (String field)
  {
    if (field == null) throw new NullPointerException();
    this.fieldName = field;
  }

  /**
   * Returns the field name.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return fieldName;
  }

  /**
   * Sets the current value of the data source.
   *
   * @param value The value.
   */
  public void setValue (Object value)
  {
    this.value = value;
  }

  /**
   * Returns the value of the data source.
   *
   * @return The value.
   */
  public Object getValue ()
  {
    return value;
  }

  public Object clone () throws CloneNotSupportedException
  {
    ReportDataSource rd = (ReportDataSource) super.clone ();
    return rd;
  }
}
