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
 * -----------------------
 * DataRowConnectable.java
 * -----------------------
 * (C)opyright 2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataRowConnectable.java,v 1.1 2002/07/28 13:25:26 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
 *
 */
package com.jrefinery.report.filter;

import com.jrefinery.report.DataRow;

/**
 * Marks data sources or filters that can be connected to the data row. (FunctionDataSources,
 * ReportDataSources and ExpressionDataSources).
 *
 * @author TM
 */
public interface DataRowConnectable
{
  /**
   * Connects the DataRow with the named DataSource or DataFilter.
   * The filter is now able to query the other DataSources to compute the result.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void connectDataRow (DataRow row) throws IllegalStateException;

  /**
   * Releases the connection to the data row. If no data row is connected, an
   * IllegalStateException is thrown to indicate the programming error.
   *
   * @param row  the data row.
   *
   * @throws IllegalStateException if there is already a data row connected.
   */
  public void disconnectDataRow (DataRow row) throws IllegalStateException;

}