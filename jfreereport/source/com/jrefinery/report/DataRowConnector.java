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
 * DataRowConnector.java
 * ---------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 */
package com.jrefinery.report;

import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DataTarget;

import java.util.List;

/**
 * This is the connection-proxy to the various data sources contained in the elements.
 * During report processing the report states get cloned while the elements remain uncloned.
 * The DataRowConnector connects the DataRowBackend (which contains the data) with the
 * stateless elements.
 *
 * @author TM
 */
public class DataRowConnector implements DataRow
{
  /** The data row backend. */
  private DataRowBackend dataRow;

  /**
   * Default constructor.
   */
  public DataRowConnector ()
  {
  }

  /**
   * Returns the assigned data row backend.
   *
   * @return the currently assigned DataRowBackend for this DataRowConnector.
   */
  public DataRowBackend getDataRowBackend ()
  {
    return dataRow;
  }

  /**
   * Sets the data row backend for this DataRowConnector. The backend actually contains the data
   * which will be queried, while this DataRowConnector is simply a proxy forwarding all requests
   * to the backend.
   *
   * @param dataRow the data row backend
   */
  public void setDataRowBackend (DataRowBackend dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * Return the value of the function, expression or column in the tablemodel using the column
   * number.
   *
   * @param col  the column, function or expression index.
   *
   * @return The column, function or expression value.
   */
  public Object get (int col)
  {
    return dataRow.get (col);
  }

  /**
   * Returns the value of the column, function or expression using its name.
   *
   * @param col  the column, function or expression index.
   *
   * @return The column, function or expression value.
   */
  public Object get (String col)
  {
    return dataRow.get (col);
  }

  /**
   * Returns the name of the column, function or expression.
   *
   * @param col  the column, function or expression index.
   *
   * @return The column, function or expression name.
   */
  public String getColumnName (int col)
  {
    return dataRow.getColumnName (col);
  }

  /**
   * Looks up the position of the column with the name <code>name</code>.
   * returns the position of the column or -1 if no columns could be retrieved.
   *
   * @param name  the column, function or expression name.
   *
   * @return the column position of the column, expression or function with the given name or
   * -1 if the given name does not exist in this DataRow.
   */
  public int findColumn (String name)
  {
    return getDataRowBackend ().findColumn (name);
  }

  /**
   * Returns the count of columns in this datarow. The columncount is the sum of all
   * DataSource columns, all functions and all expressions.
   *
   * @return the number of accessible columns in this datarow
   */
  public int getColumnCount ()
  {
    return getDataRowBackend ().getColumnCount ();
  }

  /**
   * Connects the Report and all contained bands and all Elements within the bands to this DataRow.
   *
   * @param report the report which will be connected
   */
  public void connectDataSources (JFreeReport report)
  {
    connectDataSources (report.getPageFooter ());
    connectDataSources (report.getPageHeader ());
    connectDataSources (report.getReportFooter ());
    connectDataSources (report.getReportHeader ());
    connectDataSources (report.getItemBand ());

    int groupCount = report.getGroupCount ();
    for (int i = 0; i < groupCount; i++)
    {
      Group g = report.getGroup (i);
      connectDataSources (g.getFooter ());
      connectDataSources (g.getHeader ());
    }
  }

  /**
   * Connects the Band and all Elements within the band to this DataRow.
   *
   * @param band the band which will be connected.
   */
  public void connectDataSources (Band band)
  {
    List l = band.getElements ();
    for (int i = 0; i < l.size (); i++)
    {
      Element e = (Element) l.get (i);
      DataSource ds = getLastDatasource (e);
      if (ds instanceof DataRowConnectable)
      {
        DataRowConnectable dc = (DataRowConnectable) ds;
        dc.connectDataRow (this);
      }
    }
  }

  /**
   * Removes the reference to this DataRow from the Report and all contained Bands and all
   * Elements contained in the Bands.
   *
   * @param report the report which will be disconnected from this DataRow.
   */
  public void disconnectDataSources (JFreeReport report)
  {
    disconnectDataSources (report.getPageFooter ());
    disconnectDataSources (report.getPageHeader ());
    disconnectDataSources (report.getReportFooter ());
    disconnectDataSources (report.getReportHeader ());
    disconnectDataSources (report.getItemBand ());

    int groupCount = report.getGroupCount ();
    for (int i = 0; i < groupCount; i++)
    {
      Group g = report.getGroup (i);
      disconnectDataSources (g.getFooter ());
      disconnectDataSources (g.getHeader ());
    }
  }

  /**
   * Removes the reference to this DataRow from the Band and all Elements contained in the Band.
   *
   * @param band the band which will be disconnected from this DataRow.
   */
  public void disconnectDataSources (Band band)
  {
    List l = band.getElements ();
    for (int i = 0; i < l.size (); i++)
    {
      Element e = (Element) l.get (i);
      DataSource ds = getLastDatasource (e);
      if (ds instanceof DataRowConnectable)
      {
        DataRowConnectable dc = (DataRowConnectable) ds;
        dc.disconnectDataRow (this);
      }
    }
  }

  /**
   * Queries the last datasource in the chain of targets and filters.
   * <p>
   * The last datasource is used to feed data into the data processing chain.
   * The result of this computation is retrieved by the element using the
   * registered datasource to query the queue.
   *
   * @param e  the data target.
   *
   * @return The last DataSource in the chain.
   */
  public static DataSource getLastDatasource (DataTarget e)
  {
    if (e == null)
    {
      throw new NullPointerException ();
    }
    DataSource s = e.getDataSource ();
    if (s instanceof DataTarget)
    {
      DataTarget tgt = (DataTarget) s;
      return getLastDatasource (tgt);
    }
    return s;
  }

}