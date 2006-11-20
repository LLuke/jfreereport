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
 * ReportData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportData.java,v 1.1 2006/04/18 11:45:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report;

/**
 * A report data source is a ordered set of rows. For a report, we assume that
 * the report dataset does not change while the report is processed. Concurrent
 * updates will invalidate the whole precomputed layout.
 *
 * A report dataset will be accessed in a linear fashion. On certain points, the
 * cursor will be reset to the a previously read position, and processing the
 * data will restart from there. It is guaranteed, that the cursor will never
 * be set to a row that is beyond the last row that has been read with 'next()'.
 *
 * @author Thomas Morgner
 */
public interface ReportData extends DataSet
{
  public int getCursorPosition() throws DataSourceException;

  public void setCursorPosition(int cursor) throws DataSourceException;

  /**
   * This operation checks, whether a call to next will be likely to succeed.
   * If there is a next data row, this should return true.
   *
   * @return
   * @throws DataSourceException
   */
  public boolean isAdvanceable () throws DataSourceException;

  public boolean next() throws DataSourceException;

  /**
   * Closes the datasource. This should be called at the end of each report
   * processing run. Whether this closes the underlying data-source backend
   * depends on the ReportDataFactory. Calling 'close()' on the ReportDataFactory
   * *must* close all report data objects.
   *  
   * @throws DataSourceException
   */
  public void close() throws DataSourceException;
}
