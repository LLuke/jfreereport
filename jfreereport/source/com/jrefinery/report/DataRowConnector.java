/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------
 * DataRowConnector.java
 * -----------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * ------------------------------
 * 27.07.2002 : Inital version
 */
package com.jrefinery.report;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DataRowConnectable;
import com.jrefinery.report.filter.DataTarget;

import java.util.List;

/**
 * This is the connection-proxy to the various data sources contained in the elements.
 * During report processing the report states get cloned while the elements remain uncloned.
 * The DataRowConnector connects the DataRowBackend (which contains the data) with the
 * stateless elements.
 */
public class DataRowConnector implements DataRow
{
  private DataRowBackend dataRow;

  public DataRowBackend getDataRowBackend ()
  {
    return dataRow;
  }

  public void setDataRowBackend (DataRowBackend dataRow)
  {
    this.dataRow = dataRow;
  }

  public Object get (int col)
  {
    return dataRow.get(col);
  }

  public Object get (String col)
  {
    return dataRow.get(col);
  }

  public String getColumnName (int col)
  {
    return dataRow.getColumnName(col);
  }

  public DataRowConnector ()
  {
  }

  /**
   * looks up the position of the column with the name <code>name</code>.
   * returns the position of the column or -1 if no columns could be retrieved.
   */
  public int findColumn (String name)
  {
    return getDataRowBackend().findColumn (name);
  }


  public int getColumnCount ()
  {
    return getDataRowBackend().getColumnCount ();
  }

  public void connectDataSources (JFreeReport report)
  {
    connectDataSources(report.getPageFooter());
    connectDataSources(report.getPageHeader());
    connectDataSources(report.getReportFooter());
    connectDataSources(report.getReportHeader());
    connectDataSources(report.getItemBand());

    int groupCount= report.getGroupCount();
    for (int i = 0; i < groupCount; i++)
    {
      Group g = report.getGroup(i);
      connectDataSources(g.getFooter());
      connectDataSources(g.getHeader());
    }
  }

  public void connectDataSources (Band band)
  {
    List l = band.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      Element e = (Element) l.get(i);
      DataSource ds = getLastDatasource(e);
      if (ds instanceof DataRowConnectable)
      {
        DataRowConnectable dc = (DataRowConnectable) ds;
        dc.connectDataRow(this);
      }
    }
  }

  public void disconnectDataSources (JFreeReport report)
  {
    disconnectDataSources(report.getPageFooter());
    disconnectDataSources(report.getPageHeader());
    disconnectDataSources(report.getReportFooter());
    disconnectDataSources(report.getReportHeader());
    disconnectDataSources(report.getItemBand());

    int groupCount= report.getGroupCount();
    for (int i = 0; i < groupCount; i++)
    {
      Group g = report.getGroup(i);
      disconnectDataSources(g.getFooter());
      disconnectDataSources(g.getHeader());
    }
  }

  public void disconnectDataSources (Band band)
  {
    List l = band.getElements();
    for (int i = 0; i < l.size(); i++)
    {
      Element e = (Element) l.get(i);
      DataSource ds = getLastDatasource(e);
      if (ds instanceof DataRowConnectable)
      {
        DataRowConnectable dc = (DataRowConnectable) ds;
        dc.disconnectDataRow(this);
      }
    }
  }

  /**
   * Queries the last datasource in the chain of targets and filters.
   * <p>
   * The last datasource is used to feed data into the data processing chain.
   * The result of this computation is retrieved by the element using the
   * registered datasource to query the queue.
   */
  public static DataSource getLastDatasource (DataTarget e)
  {
    if (e == null) throw new NullPointerException ();
    DataSource s = e.getDataSource ();
    if (s instanceof DataTarget)
    {
      DataTarget tgt = (DataTarget) s;
      return getLastDatasource (tgt);
    }
    return s;
  }
}