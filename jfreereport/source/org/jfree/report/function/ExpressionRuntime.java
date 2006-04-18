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
 * $Id: ExpressionRuntime.java,v 1.2 2006/02/08 18:03:02 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.01.2006 : Initial version
 */
package org.jfree.report.function;

import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.report.ReportData;
import org.jfree.report.DataRow;
import org.jfree.report.structure.Element;
import org.jfree.report.i18n.ResourceBundleFactory;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 24.01.2006, 16:58:34
 *
 * @author Thomas Morgner
 */
public interface ExpressionRuntime
{
  /**
   * Returns the datarow.
   * 
   * @return
   */
  public DataRow getDataRow();
  public Configuration getConfiguration();
  public ResourceBundleFactory getResourceBundleFactory();

  /**
   * Returns the report data used in this section. If subreports are used,
   * this does not reflect the complete report data.
   * <p>
   * All access to the report data must be properly synchronized. Failure to
   * do so may result in funny results. Do not assume that the report data
   * will be initialized on the current cursor positon.
   *
   * @return
   */
  public ReportData getData();
  public Element getDeclaringParent();

  public OutputProcessorMetaData getOutputMetaData();
}
