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
 * -------------------------------
 * ResultSetTableModelFactory.java
 * -------------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ResultSetTableModelFactory.java,v 1.1 2003/01/27 03:20:01 taqua Exp $
 *
 * Changes
 * -------
 * 25-Apr-2002 : Initial version
 * 09-Jun-2002 : Documentation and changed the return value to be a CloseableTableModel
 * 02-Oct-2002 : <Robert Fuller> Bugs fixed for DefaultTableModel creation.
 * 12-Nov-2002 : Fixed errors reported by Checkstyle 2.4 (DG)
 * 10-Dec-2002 : Fixed more issues reported by Checkstyle (DG);
 */
package com.jrefinery.report.tablemodel;

import com.jrefinery.report.util.CloseableTableModel;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Creates a <code>TableModel</code> which is backed up by a <code>ResultSet</code>.
 * If the <code>ResultSet</code> is scrollable, a {@link com.jrefinery.report.tablemodel.ScrollableResultSetTableModel} is
 * created, otherwise all data is copied from the <code>ResultSet</code> into a
 * <code>DefaultTableModel</code>.
 * <p>
 * The creation of a <code>DefaultTableModel</code> can be forced if the system property
 * <code>"com.jrefinery.report.TableFactoryMode"</code> is set to <code>"simple"</code>.
 *
 * @author Thomas Morgner
 */
public class ResultSetTableModelFactory
{
  /** Singleton instance of the factory */
  private static ResultSetTableModelFactory defaultInstance;

  /**
   * Default constructor. This is a Singleton, use getInstance().
   */
  protected ResultSetTableModelFactory()
  {
  }

  /**
   * Creates a table model by using the given <code>ResultSet</code> as the backend.
   * If the <code>ResultSet</code> is scrollable (the type is not <code>TYPE_FORWARD_ONLY</code>),
   * an instance of {@link com.jrefinery.report.tablemodel.ScrollableResultSetTableModel} is returned. This model uses the
   * extended capabilities of scrollable resultsets to directly read data from the database
   * without caching or the need of copying the complete <code>ResultSet</code> into the programs
   * memory.
   * <p>
   * If the <code>ResultSet</code> lacks the scollable features, the data will be copied into a
   * <code>DefaultTableModel</code> and the <code>ResultSet</code> gets closed.
   *
   * @param rs  the result set.
   *
   * @return a closeable table model.
   *
   * @throws java.sql.SQLException if there is a problem with the result set.
   */
  public CloseableTableModel createTableModel(ResultSet rs)
      throws SQLException
  {
    // Allow for override, some jdbc drivers are buggy :(
    String prop = ReportConfiguration
        .getGlobalConfig().getConfigProperty(ReportConfiguration.RESULTSET_FACTORY_MODE, "");

    if (prop.equalsIgnoreCase("simple"))
    {
      return generateDefaultTableModel(rs);
    }
    if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY)
    {
      return generateDefaultTableModel(rs);
    }
    else
    {
      return new ScrollableResultSetTableModel(rs);
    }
  }

  /**
   * A DefaultTableModel that implements the CloseableTableModel interface.
   */
  private class CloseableDefaultTableModel extends DefaultTableModel implements CloseableTableModel
  {
    /** The results set. */
    private ResultSet res;

    /**
     * Creates a new closeable table model.
     *
     * @param objects  the table data.
     * @param objects1  the column names.
     * @param res  the result set.
     */
    public CloseableDefaultTableModel(Object[][] objects, Object[] objects1, ResultSet res)
    {
      super(objects, objects1);
      this.res = res;
    }

    /**
     * If this model has a resultset assigned, close it, if this is a DefaultTableModel,
     * remove all data.
     */
    public void close()
    {
      setDataVector(new Object[0][0], new Object[0]);
      try
      {
        res.close();
      }
      catch (Exception e)
      {
        Log.debug ("Close failed for default table model", e);
      }
    }
  }

  /**
   * Generates a <code>TableModel</code> that gets its contents filled from a
   * <code>ResultSet</code>. The column names of the <code>ResultSet</code> will form the column
   * names of the table model.
   * <p>
   * Hint: To customize the names of the columns, use the SQL column aliasing (done with
   * <code>SELECT nativecolumnname AS "JavaColumnName" FROM ....</code>
   *
   * @param rs  the result set.
   *
   * @return a closeable table model.
   *
   * @throws java.sql.SQLException if there is a problem with the result set.
   */
  public CloseableTableModel generateDefaultTableModel(ResultSet rs)
      throws SQLException
  {
    ResultSetMetaData rsmd = rs.getMetaData();
    int colcount = rsmd.getColumnCount();
    Vector header = new Vector(colcount);
    for (int i = 0; i < colcount; i++)
    {
      String name = rsmd.getColumnName(i + 1);
      header.add(name);
    }
    ArrayList rows = new ArrayList();
    while (rs.next())
    {
      Vector column = new Vector(colcount);
      for (int i = 0; i < colcount; i++)
      {
        Object val = rs.getObject(i + 1);
        column.add(val);
      }
      rows.add(column.toArray());
    }

    Object[] tempRows = rows.toArray();
    Object[][] rowMap = new Object[tempRows.length][];
    for (int i = 0; i < tempRows.length; i++)
    {
      rowMap[i] = (Object[]) tempRows[i];
    }
    CloseableDefaultTableModel model = new CloseableDefaultTableModel(rowMap, header.toArray(), rs);
    return model;
  }

  /**
   * Returns the singleton instance of the factory.
   *
   * @return an instance of this factory.
   */
  public static ResultSetTableModelFactory getInstance()
  {
    if (defaultInstance == null)
    {
      defaultInstance = new ResultSetTableModelFactory();
    }
    return defaultInstance;
  }

}
