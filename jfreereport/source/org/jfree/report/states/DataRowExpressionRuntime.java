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
 * DataRowExpressionRuntime.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: DataRowExpressionRuntime.java,v 1.2 2006/02/08 18:03:56 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.states;

import javax.swing.table.TableModel;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 24.01.2006, 17:19:34
 *
 * @author Thomas Morgner
 */
public final class DataRowExpressionRuntime implements ExpressionRuntime
{
  private DataRowBackend backend;
  private Configuration configuration;
  private ResourceBundleFactory resourceBundleFactory;
  private String exportDescriptor;

  public DataRowExpressionRuntime(final DataRowBackend backend,
                                  final Configuration configuration,
                                  final ResourceBundleFactory resourceBundleFactory,
                                  final String exportDescriptor)
  {
    if (backend == null) throw new NullPointerException();
    if (configuration == null) throw new NullPointerException();
    if (exportDescriptor == null) throw new NullPointerException();
    if (resourceBundleFactory == null) throw new NullPointerException();

    this.exportDescriptor = exportDescriptor;
    this.backend = backend;
    this.configuration = configuration;
    this.resourceBundleFactory = resourceBundleFactory;
  }

  public DataRow getDataRow()
  {
    return backend.getDataRowConnector();
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
    return backend.getTablemodel();
  }

  /** Where are we in the current processing. */
  public int getCurrentRow()
  {
    return backend.getCurrentRow();
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
    return exportDescriptor;
  }
}
