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
 * ---------------
 * DataRowConnectable.java
 * ---------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataSource.java,v 1.4 2002/07/03 18:49:48 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 *
 */
package com.jrefinery.report.filter;

import com.jrefinery.report.DataRow;

/**
 * Marks data sources or filters that can be connected to the datarow. (FunctionDataSources,
 * ReportDataSources and ExpressionDataSources).
 */
public interface DataRowConnectable
{
  /**
   * Connects the DataRow with the named DataSource or DataFilter.
   * The filter is now able to query the other DataSources to compute the result.
   *
   * If there is already a datarow connected, an IllegalStateException is thrown.
   */
  public void connectDataRow (DataRow row) throws IllegalStateException;

  /**
   * Releases the connection to the datarow. If no datarow is connected, an
   * IllegalStateException is thrown to indicate the programming error.
   */
  public void disconnectDataRow (DataRow row) throws IllegalStateException;

}