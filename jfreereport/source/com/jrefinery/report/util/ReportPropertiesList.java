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
 * -------------------------
 * ReportPropertiesList.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * $Id: ReportPropertiesList.java,v 1.2 2002/11/07 21:45:29 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.util;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * A collection of report properties arranged into columns to provide access for the
 * DataRowBackend class.
 *
 * @author TM
 */
public class ReportPropertiesList
{
  /** The base report properties. */
  private ReportProperties base;

  /** The columns. */
  private ArrayList columns;

  /**
   * Creates a list of report properties.
   *
   * @param base  the underlying properties.
   */
  public ReportPropertiesList(ReportProperties base)
  {
    if (base == null)
    {
      throw new NullPointerException();
    }
    this.base = base;
    this.columns = new ArrayList();

    Enumeration enum = base.keys();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      if (base.isMarked(key))
      {
        columns.add (key);
      }
    }
  }

  /**
   * Returns the number of columns.
   *
   * @return the column count.
   */
  public int getColumnCount ()
  {
    return columns.size();
  }

  /**
   * Returns the name of the specified column.
   *
   * @param column  the column index (zero-based).
   *
   * @return the column name.
   */
  public String getColumnName (int column)
  {
    return (String) columns.get (column);
  }

  /**
   * Returns the value in the specified column.
   *
   * @param column  the column index (zero-based).
   *
   * @return the value.
   */
  public Object get(int column)
  {
    return (base.get(getColumnName(column)));
  }

}
