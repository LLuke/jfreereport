/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * $Id: ReportPropertiesList.java,v 1.7 2003/02/05 17:56:03 taqua Exp $
 *
 * Changes
 * -------
 * 23-Oct-2002 : Initial version
 * 07-Nov-2002 : Documentation
 * 29-Nov-2002 : More Documentation and CheckStyle-Fixes
 * 09-Dec-2002 : Documentation
 * 12-Dec-2002 : Documentation
 * 16-Jan-2003 : BugFix: Properties could not be marked when no property value was set 
 */
package com.jrefinery.report.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A collection of report properties arranged into columns to provide access for the
 * DataRowBackend class. All marked properties are added as column to the ReportPropertyList.
 *
 * @see ReportProperties#setMarked
 * @see ReportProperties#isMarked
 *
 * @author Thomas Morgner
 */

public class ReportPropertiesList
{
  /** The base report properties. */
  private ReportProperties base;

  /** The columns. */
  private ArrayList columns;

  /**
   * Creates a list of report properties. Searches all marked properties
   * and adds them to the ReportPropertyList. The property-values remain
   * in the original ReportProperties-collection, all queries to this
   * list are forwarded to that base-object.
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

    Iterator enum = base.keys();
    while (enum.hasNext())
    {
      String key = (String) enum.next();
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

  /**
   * Returns a string describing the object.
   * 
   * @return The string.
   */
  public String toString ()
  {
    StringBuffer b = new StringBuffer();

    b.append("ReportPropertiesList: ");
    b.append(columns);
    return b.toString();
  }
}
