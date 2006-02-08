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
 * ExpressionRuntime.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: ExpressionRuntime.java,v 1.1 2006/01/24 19:01:08 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.function;

import javax.swing.table.TableModel;

import org.jfree.report.DataRow;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 24.01.2006, 16:58:34
 *
 * @author Thomas Morgner
 */
public interface ExpressionRuntime
{
  public DataRow getDataRow();
  public Configuration getConfiguration();
  public ResourceBundleFactory getResourceBundleFactory();

  /** Access to the tablemodel was granted using report properties, now direct.*/
  public TableModel getData();
  /** Where are we in the current processing. */
  public int getCurrentRow();

  /**
   * The output descriptor is a simple string collections consisting of
   * the following components: exportclass/type/subtype
   *
   * For example, the PDF export would be: pageable/pdf
   * The StreamHTML export would return table/html/stream
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor();

  // JFreeReport 0.9.x will introduce libLayout's OutputProcessorMetaData here.
}
