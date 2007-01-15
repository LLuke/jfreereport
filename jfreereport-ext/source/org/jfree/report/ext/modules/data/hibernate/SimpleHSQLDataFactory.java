/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * SimpleHSQLDataFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.ext.modules.data.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jfree.report.DataSet;
import org.jfree.report.ReportData;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.TableReportData;
import org.jfree.report.util.DataSetUtility;

/**
 * Creation-Date: Jan 12, 2007, 5:41:18 PM
 *
 * @author Thomas Morgner
 */
public class SimpleHSQLDataFactory implements ReportDataFactory
{
  private static final Object NULL_TOKEN = new Object();

  private transient Session session;
  private SessionProvider sessionProvider;

  public SimpleHSQLDataFactory(final Session connection)
  {
    this (new StaticSessionProvider(connection));
  }

  public SimpleHSQLDataFactory(final SessionProvider connectionProvider)
  {
    if (connectionProvider == null)
    {
      throw new NullPointerException();
    }
    this.sessionProvider = connectionProvider;
  }

  private synchronized Session getSession() throws HibernateException
  {
    if (session == null)
    {
      session = sessionProvider.getSession();
    }
    return session;
  }


  /**
   * Queries a datasource. The string 'query' defines the name of the query. The
   * Parameterset given here may contain more data than actually needed.
   * <p/>
   * The dataset may change between two calls, do not assume anything!
   *
   * @param query
   * @param parameters
   * @return
   */
  public synchronized ReportData queryData(final String query, final DataSet parameters)
          throws ReportDataFactoryException
  {
    try
    {
      Query pstmt = getSession().createQuery(query);
      final String[] params = pstmt.getNamedParameters();
      for (int i = 0; i < params.length; i++)
      {
        final String param = params[i];
        final Object pvalue = DataSetUtility.getByName(parameters, param, NULL_TOKEN);
        if (pvalue == NULL_TOKEN)
        {
          // this either means, that the parameter is explicitly set to null
          // or that there is no such column.
          throw new ReportDataFactoryException ("Setting parameter '" +
                    param + "' failed: No such column.");
        }
        else if (pvalue == null)
        {
          // this should work, but some driver are known to die here.
          // they should be fed with setNull(..) instead; something
          // we cant do as JDK1.2's JDBC does not define it.
          pstmt.setParameter(param, null);
        }
        else
        {
          pstmt.setParameter(param, pvalue);
        }
      }
      final ScrollableResults res = pstmt.scroll(ScrollMode.FORWARD_ONLY);

      return new TableReportData
          (generateDefaultTableModel(res, pstmt.getReturnAliases()));
    }
    catch (Exception e)
    {
      throw new ReportDataFactoryException("Failed at query: " + query, e);
    }
  }

  /**
   * Generates a <code>TableModel</code> that gets its contents filled from a
   * <code>ResultSet</code>. The column names of the <code>ResultSet</code> will form the
   * column names of the table model.
   * <p/>
   * Hint: To customize the names of the columns, use the SQL column aliasing (done with
   * <code>SELECT nativecolumnname AS "JavaColumnName" FROM ....</code>
   *
   * @param rs           the result set.
   * @param labelMapping defines, whether to use column names or column labels to compute
   *                     the column index.
   * @return a closeable table model.
   *
   * @throws SQLException if there is a problem with the result set.
   */
  private TableModel generateDefaultTableModel
          (final ScrollableResults rs, final String[] labelMapping)
  {
    final int colcount = labelMapping.length;

    final ArrayList rows = new ArrayList();
    while (rs.next())
    {
      final Object[] column = new Object[colcount];
      for (int i = 0; i < colcount; i++)
      {
        column[i] = rs.get(i);
      }
      rows.add(column);
    }

    final Object[] tempRows = rows.toArray();
    final Object[][] rowMap = new Object[tempRows.length][];
    for (int i = 0; i < tempRows.length; i++)
    {
      rowMap[i] = (Object[]) tempRows[i];
    }
    return new DefaultTableModel(rowMap, labelMapping);
  }

  public void open()
  {

  }

  public synchronized void close()
  {
    if (session == null)
    {
      return;
    }

    try
    {
      session.close();
    }
    catch (HibernateException e)
    {
      // we tried our very best ..
    }
    session = null;
  }

  /**
   * Derives a freshly initialized report data factory, which is independend of
   * the original data factory. Opening or Closing one data factory must not
   * affect the other factories.
   *
   * @return
   */
  public ReportDataFactory derive()
  {
    try
    {
      return (ReportDataFactory) clone();
    }
    catch (CloneNotSupportedException e)
    {
      // this should not happen ..
      throw new IllegalStateException("Clone failed?");
    }
  }

  public Object clone () throws CloneNotSupportedException
  {
    SimpleHSQLDataFactory dataFactory = (SimpleHSQLDataFactory) super.clone();
    dataFactory.session = null;
    return dataFactory;
  }

}
