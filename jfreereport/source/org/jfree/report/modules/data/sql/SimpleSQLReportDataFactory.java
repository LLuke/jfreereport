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
 * SimpleSQLReportDataFactory.java
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
package org.jfree.report.modules.data.sql;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.DataSet;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportData;
import org.jfree.report.ReportDataFactory;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.TableReportData;
import org.jfree.report.util.DataSetUtility;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 19.02.2006, 17:37:33
 *
 * @author Thomas Morgner
 */
public class SimpleSQLReportDataFactory implements ReportDataFactory
{
  private static final Object NULL_TOKEN = new Object();

  private static class PreparedStatementCarrier
  {
    private PreparedStatement preparedStatement;
    private String[] parameters;

    public PreparedStatementCarrier(final PreparedStatement preparedStatement,
                                    final String[] parameters)
    {
      this.preparedStatement = preparedStatement;
      this.parameters = parameters;
    }

    public PreparedStatement getPreparedStatement()
    {
      return preparedStatement;
    }

    public String[] getParameters()
    {
      return parameters;
    }
  }

  private HashMap preparedStatements;
  private Connection connection;
  private ConnectionProvider connectionProvider;

  private boolean labelMapping;
  private static final String COLUMN_NAME_MAPPING_KEY =
          "org.jfree.report.modules.data.sql.ColumnNameMapping";

  public SimpleSQLReportDataFactory(final Connection connection)
  {
    this (new StaticConnectionProvider(connection));
  }

  public SimpleSQLReportDataFactory(final ConnectionProvider connectionProvider)
  {
    if (connectionProvider == null)
    {
      throw new NullPointerException();
    }
    this.connectionProvider = connectionProvider;
    this.preparedStatements = new HashMap();
    final Configuration globalConfig =
            JFreeReportBoot.getInstance().getGlobalConfig();
    this.labelMapping = globalConfig.getConfigProperty
            (SimpleSQLReportDataFactory.COLUMN_NAME_MAPPING_KEY, "Label").equals("Label");
  }

  public boolean isLabelMapping()
  {
    return labelMapping;
  }

  public void setLabelMapping(final boolean labelMapping)
  {
    this.labelMapping = labelMapping;
  }

  private Connection getConnection() throws SQLException
  {
    if (connection == null)
    {
      connection = connectionProvider.getConnection();
    }
    return connection;
  }

  private int getBestResultSetType() throws SQLException
  {
    final Connection connection = getConnection();
    boolean supportsScrollInsensitive =
            connection.getMetaData().supportsResultSetType
                    (ResultSet.TYPE_SCROLL_INSENSITIVE);
    boolean supportsScrollSensitive =
            connection.getMetaData().supportsResultSetType
                    (ResultSet.TYPE_SCROLL_SENSITIVE);

    if (supportsScrollInsensitive)
    {
      return ResultSet.TYPE_SCROLL_INSENSITIVE;
    }
    if (supportsScrollSensitive)
    {
      return ResultSet.TYPE_SCROLL_SENSITIVE;
    }
    return ResultSet.TYPE_FORWARD_ONLY;
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
      PreparedStatementCarrier pstmtCarrier = (PreparedStatementCarrier)
              preparedStatements.get(query);
      if (pstmtCarrier == null)
      {
        SQLParameterLookupParser parser = new SQLParameterLookupParser();
        final String translatedQuery = parser.translateAndLookup(query);
        PreparedStatement pstmt = getConnection().prepareStatement
                (translatedQuery, getBestResultSetType());
        pstmtCarrier = new PreparedStatementCarrier(pstmt, parser.getFields());
        preparedStatements.put(query, pstmtCarrier);
      }

      final PreparedStatement pstmt = pstmtCarrier.getPreparedStatement();
      pstmt.clearParameters();

      final String[] params = pstmtCarrier.getParameters();
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
          pstmt.setNull(i+1, getParameterType(i+1, pstmt));
        }
        else
        {
          pstmt.setObject(i+1, pvalue, getParameterType(i+1, pstmt));
        }
      }
      ResultSet res = pstmt.executeQuery();
      final int resultSetType = res.getType();

      if (resultSetType == ResultSet.TYPE_FORWARD_ONLY)
      {
        TableModel model = generateDefaultTableModel(res, labelMapping);
        res.close();
        return new TableReportData(model);
      }
      else
      {
        return new SQLReportData(res, labelMapping);
      }
    }
    catch(ReportDataFactoryException rdfe)
    {
      throw rdfe;
    }
    catch (Exception e)
    {
      throw new ReportDataFactoryException("Failed at query: " + query, e);
    }
  }

  private int getParameterType (int paramCol, PreparedStatement pstmt)
          throws SQLException
  {
    ParameterMetaData pmdta = pstmt.getParameterMetaData();
    return pmdta.getParameterType(paramCol);
  }

  public void close() throws SQLException
  {
    getConnection().close();
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
  public TableModel generateDefaultTableModel
          (final ResultSet rs, final boolean labelMapping)
          throws SQLException
  {
    final ResultSetMetaData rsmd = rs.getMetaData();
    final int colcount = rsmd.getColumnCount();
    final ArrayList header = new ArrayList(colcount);
    for (int i = 0; i < colcount; i++)
    {
      if (labelMapping)
      {
        final String name = rsmd.getColumnLabel(i + 1);
        header.add(name);
      }
      else
      {
        final String name = rsmd.getColumnName(i + 1);
        header.add(name);
      }
    }
    final ArrayList rows = new ArrayList();
    while (rs.next())
    {
      final Object[] column = new Object[colcount];
      for (int i = 0; i < colcount; i++)
      {
        column[i] = rs.getObject(i + 1);
        if (rs.wasNull())
        {
          column[i] = null;
        }
      }
      rows.add(column);
    }

    final Object[] tempRows = rows.toArray();
    final Object[][] rowMap = new Object[tempRows.length][];
    for (int i = 0; i < tempRows.length; i++)
    {
      rowMap[i] = (Object[]) tempRows[i];
    }
    return new DefaultTableModel(rowMap, header.toArray());
  }

}
