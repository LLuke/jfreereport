/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * DataSetUtility.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.util;

import org.jfree.report.DataSet;
import org.jfree.report.DataSourceException;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 17.04.2006, 11:43:04
 *
 * @author Thomas Morgner
 */
public class DataSetUtility
{
  private DataSetUtility()
  {
  }

  public static Object getByName (DataSet ds, String column)
          throws DataSourceException
  {
    return getByName(ds, column, null);
  }

  public static Object getByName (DataSet ds, String column, Object defaultVal)
          throws DataSourceException
  {
    final int size = ds.getColumnCount();
    for (int i = 0; i < size; i++)
    {
      final String columnName = ds.getColumnName(i);
      if (ObjectUtilities.equal(column, columnName))
      {
        return ds.get(i);
      }
    }
    return defaultVal;
  }


  public static boolean isColumnDefined (String name, DataSet params)
          throws DataSourceException
  {
    final int size = params.getColumnCount();
    for (int i = 0; i < size; i++)
    {
      final String columnName = params.getColumnName(i);
      if (ObjectUtilities.equal(name, columnName))
      {
        return true;
      }
    }
    return false;
  }

}