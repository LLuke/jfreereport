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
 * ---------------------
 * DataRowConnector.java
 * ---------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DataRowConnector.java,v 1.3 2003/08/18 18:27:57 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 * 02-Dec-2002 : Updated connection/disconnection to support band composites.
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 06-Dec-2002 : Added more exceptions when there is no backend connected.
 * 18-Dec-2003 : Added toString() method
 */

package org.jfree.report.states;

import java.util.List;

import org.jfree.report.filter.DataRowConnectable;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DataTarget;
import org.jfree.report.states.DataRowBackend;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDefinition;
import org.jfree.report.Group;
import org.jfree.report.Band;
import org.jfree.report.Element;

/**
 * This is the connection-proxy to the various data sources contained in the elements.
 * During report processing the report states get cloned while the elements remain uncloned.
 * The DataRowConnector connects the DataRowBackend (which contains the data) with the
 * stateless elements.
 *
 * @see DataRowBackend
 *
 * @author Thomas Morgner
 */
public class DataRowConnector implements DataRow
{
  /** The data row backend. */
  private DataRowBackend dataRow;

  /**
   * Default constructor.
   */
  public DataRowConnector()
  {
  }

  /**
   * Returns the assigned data row backend.
   *
   * @return the currently assigned DataRowBackend for this DataRowConnector.
   */
  public DataRowBackend getDataRowBackend()
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
  public void setDataRowBackend(final DataRowBackend dataRow)
  {
    this.dataRow = dataRow;
  }

  /**
   * Return the value of the function, expression or column in the tablemodel using the column
   * number.
   *
   * @param col  the column, function or expression index.
   *
   * @return the column, function or expression value.
   *
   * @throws java.lang.IllegalStateException if there is no backend connected.
   */
  public Object get(final int col)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return dataRow.get(col);
  }

  /**
   * Returns the value of the column, function or expression using its name.
   *
   * @param col  the column, function or expression index.
   *
   * @return The column, function or expression value.
   * @throws java.lang.IllegalStateException if there is no backend connected
   */
  public Object get(final String col)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return dataRow.get(col);
  }

  /**
   * Returns the name of the column, function or expression.
   *
   * @param col  the column, function or expression index.
   *
   * @return the column, function or expression name.
   *
   * @throws java.lang.IllegalStateException if there is no backend connected.
   */
  public String getColumnName(final int col)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return dataRow.getColumnName(col);
  }

  /**
   * Looks up the position of the column with the name <code>name</code>.
   * returns the position of the column or -1 if no columns could be retrieved.
   *
   * @param name  the column, function or expression name.
   *
   * @return the column position of the column, expression or function with the given name or
   * -1 if the given name does not exist in this DataRow.
   *
   * @throws java.lang.IllegalStateException if there is no backend connected.
   */
  public int findColumn(final String name)
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return getDataRowBackend().findColumn(name);
  }

  /**
   * Returns the count of columns in this datarow. The columncount is the sum of all
   * DataSource columns, all functions and all expressions.
   *
   * @return the number of accessible columns in this datarow.
   *
   * @throws java.lang.IllegalStateException if there is no backend connected.
   */
  public int getColumnCount()
  {
    if (dataRow == null)
    {
      throw new IllegalStateException("Not connected");
    }
    return getDataRowBackend().getColumnCount();
  }

  /**
   * Connects the Report and all contained bands and all Elements within the bands to this DataRow.
   *
   * @param report the report which will be connected
   * @param con  the data row connector.
   */
  public static void connectDataSources(final ReportDefinition report, final DataRowConnector con)
  {
    DataRowConnector.connectDataSources(report.getPageFooter(), con);
    DataRowConnector.connectDataSources(report.getPageHeader(), con);
    DataRowConnector.connectDataSources(report.getReportFooter(), con);
    DataRowConnector.connectDataSources(report.getReportHeader(), con);
    DataRowConnector.connectDataSources(report.getItemBand(), con);

    final int groupCount = report.getGroupCount();
    for (int i = 0; i < groupCount; i++)
    {
      final Group g = report.getGroup(i);
      DataRowConnector.connectDataSources(g.getFooter(), con);
      DataRowConnector.connectDataSources(g.getHeader(), con);
    }
  }

  /**
   * Connects the Band and all Elements within the band to this DataRow.
   *
   * @param band the band which will be connected.
   * @param con  the connector.
   */
  public static void connectDataSources(final Band band, final DataRowConnector con)
  {
    final Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      final Element e = elements[i];
      if (e instanceof Band)
      {
        connectDataSources((Band) e, con);
      }
      else
      {
        final DataSource ds = getLastDatasource(e);
        if (ds instanceof DataRowConnectable)
        {
          final DataRowConnectable dc = (DataRowConnectable) ds;
          dc.connectDataRow(con);
        }
      }
    }
  }

  /**
   * Removes the reference to this DataRow from the Report and all contained Bands and all
   * Elements contained in the Bands.
   *
   * @param report  the report which will be disconnected from this DataRow.
   * @param con  the connector.
   */
  public static void disconnectDataSources(final ReportDefinition report, final DataRowConnector con)
  {
    disconnectDataSources(report.getPageFooter(), con);
    disconnectDataSources(report.getPageHeader(), con);
    disconnectDataSources(report.getReportFooter(), con);
    disconnectDataSources(report.getReportHeader(), con);
    disconnectDataSources(report.getItemBand(), con);

    final int groupCount = report.getGroupCount();
    for (int i = 0; i < groupCount; i++)
    {
      final Group g = report.getGroup(i);
      disconnectDataSources(g.getFooter(), con);
      disconnectDataSources(g.getHeader(), con);
    }
  }

  /**
   * Removes the reference to this DataRow from the Band and all Elements contained in the Band.
   *
   * @param band  the band which will be disconnected from this DataRow.
   * @param con  the connector.
   */
  public static void disconnectDataSources(final Band band, final DataRowConnector con)
  {
    final List l = band.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      final Element e = (Element) l.get(i);
      if (e instanceof Band)
      {
        disconnectDataSources((Band) e, con);
      }
      else
      {
        final DataSource ds = getLastDatasource(e);
        if (ds instanceof DataRowConnectable)
        {
          final DataRowConnectable dc = (DataRowConnectable) ds;
          dc.disconnectDataRow(con);
        }
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
  public static DataSource getLastDatasource(final DataTarget e)
  {
    if (e == null)
    {
      throw new NullPointerException();
    }
    final DataSource s = e.getDataSource();
    if (s instanceof DataTarget)
    {
      final DataTarget tgt = (DataTarget) s;
      return getLastDatasource(tgt);
    }
    return s;
  }

  /**
   * Returns a string describing the object.
   *
   * @return The string.
   */
  public String toString()
  {
    if (dataRow == null)
    {
      return "org.jfree.report.states.DataRowConnector=Not Connected";
    }
    return "org.jfree.report.states.DataRowConnector=Connected:" + dataRow.getCurrentRow();

  }
}
