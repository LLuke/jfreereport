/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * TestingExpressionRuntime.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: TestingExpressionRuntime.java,v 1.1 2006/02/01 09:48:23 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.ext.junit.base.basic.function;

import javax.swing.table.TableModel;

import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.DefaultResourceBundleFactory;
import org.jfree.report.states.DataRowConnector;
import org.jfree.util.Configuration;
import org.jfree.util.DefaultConfiguration;

/**
 * Creation-Date: 24.01.2006, 17:11:46
 *
 * @author Thomas Morgner
 */
public class TestingExpressionRuntime implements ExpressionRuntime
{
  private DataRow dataRow;
  private Configuration configuration;
  private ResourceBundleFactory resourceBundleFactory;
  private TableModel data;
  private int currentRow;
  private String exportType;

  public TestingExpressionRuntime(final TableModel data,
                                  final int currentRow,
                                  final String exportType)
  {
    this.exportType = exportType;
    this.data = data;
    this.currentRow = currentRow;
    dataRow = new DataRowConnector();
    configuration = new DefaultConfiguration();
    resourceBundleFactory = new DefaultResourceBundleFactory();
  }

  public DataRow getDataRow()
  {
    return dataRow;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  /** Access to the tablemodel was granted using report properties, now direct. */
  public TableModel getData()
  {
    return data;
  }

  /** Where are we in the current processing. */
  public int getCurrentRow()
  {
    return currentRow;
  }

  /**
   * The output descriptor is a simple string collections consisting of the
   * following components: exportclass/type/subtype
   * <p/>
   * For example, the PDF export would be: pageable/pdf The StreamHTML export
   * would return table/html/stream
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor()
  {
    return exportType;
  }
}
