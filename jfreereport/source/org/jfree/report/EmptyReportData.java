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
 * EmptyReportData.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: EmptyReportData.java,v 1.1 2006/04/30 09:49:10 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report;

/**
 * Creation-Date: 22.04.2006, 14:25:04
 *
 * @author Thomas Morgner
 */
public class EmptyReportData implements ReportData
{
  public EmptyReportData()
  {
  }

  public int getCursorPosition() throws DataSourceException
  {
    return 0;
  }

  public void setCursorPosition(int cursor) throws DataSourceException
  {
    throw new DataSourceException("This beast is empty!");
  }

  /**
   * This operation checks, whether a call to next will be likely to succeed. If
   * there is a next data row, this should return true.
   *
   * @return
   * @throws org.jfree.report.DataSourceException
   *
   */
  public boolean isAdvanceable() throws DataSourceException
  {
    return false;
  }

  public boolean next() throws DataSourceException
  {
    return false;
  }

  public void close() throws DataSourceException
  {

  }

  public int getColumnCount() throws DataSourceException
  {
    return 0;
  }

  public String getColumnName(int column) throws DataSourceException
  {
    return null;
  }

  public Object get(int column) throws DataSourceException
  {
    return null;
  }
}
