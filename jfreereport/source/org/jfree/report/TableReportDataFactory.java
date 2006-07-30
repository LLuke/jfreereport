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
 * TableReportDataFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TableReportDataFactory.java,v 1.1 2006/04/18 11:45:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report;

import java.util.HashMap;
import javax.swing.table.TableModel;

/**
 * Creation-Date: 21.02.2006, 17:59:32
 *
 * @author Thomas Morgner
 */
public class TableReportDataFactory implements ReportDataFactory
{
  private HashMap tables;

  public TableReportDataFactory()
  {
    this.tables = new HashMap();
  }

  public TableReportDataFactory(String name, TableModel tableModel)
  {
    this();
    addTable(name, tableModel);
  }

  public void addTable(String name, TableModel tableModel)
  {
    tables.put(name, tableModel);
  }

  public void removeTable(String name)
  {
    tables.remove(name);
  }

  /**
   * Queries a datasource. The string 'query' defines the name of the query. The
   * Parameterset given here may contain more data than actually needed.
   * <p/>
   * The dataset may change between two calls, do not assume anything!
   *
   * @param query the name of the table.
   * @param parameters are ignored for this factory.
   * @return the report data or null.
   */
  public ReportData queryData(final String query, final DataSet parameters)
          throws ReportDataFactoryException
  {
    TableModel model = (TableModel) tables.get(query);
    if (model == null)
    {
      return null;
    }

    return new TableReportData(model);
  }

  public void open()
  {

  }

  public void close()
  {

  }
}
