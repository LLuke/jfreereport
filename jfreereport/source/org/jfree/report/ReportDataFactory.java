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
 * ReportDataFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportDataFactory.java,v 1.2 2006/07/30 13:09:50 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report;

/**
 * The report data-factory is responsible for querying the data from arbitary
 * datasources. 
 *
 * @author Thomas Morgner
 */
public interface ReportDataFactory
{
  public void open();

  /**
   * Queries a datasource. The string 'query' defines the name of the query.
   * The Parameterset given here may contain more data than actually needed.
   *
   * The dataset may change between two calls, do not assume anything!
   *
   * @param query
   * @param parameters
   * @return
   */
  public ReportData queryData (final String query, final DataSet parameters)
          throws ReportDataFactoryException;

  /**
   * Closes the report data factory and all report data instances that have
   * been returned by this instance.
   */
  public void close();

  /**
   * Derives a freshly initialized report data factory, which is independend
   * of the original data factory. Opening or Closing one data factory must not
   * affect the other factories. 
   *
   * @return
   */
  public ReportDataFactory derive ();
}
