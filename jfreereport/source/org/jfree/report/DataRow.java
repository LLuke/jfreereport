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
 * DataRow.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report;

/**
 * This is the base interface for all data access collectors. Expressions usually
 * work with the {@link MasterDataRow} interface instead. A data-row adds a
 * certain order to the elements in the dataset. It also allows statefull
 * comparisions and data attributes using DataFlags.
 *
 * The data-row is an internal concept of JFreeReport. The report engine will be
 * responsible for creating and maintaining these implementations. Authors of
 * functions and expressions usually dont have to care where a datarow comes from
 * or at which particular instance they are looking right now.
 *
 * Note: Do not attempt to cache the datarow outside the core engine. This can
 * have funny sideeffects and might trigger the end of the world.
 *
 * @author Thomas Morgner
 */
public interface DataRow extends DataSet
{
  /**
   * Returns the value of the expression or column in the tablemodel using the given
   * column number as index. For functions and expressions, the <code>getValue()</code>
   * method is called and for columns from the tablemodel the tablemodel method
   * <code>getValueAt(row, column)</code> gets called.
   *
   * @param col the item index.
   * @return the value.
   * @throws IllegalStateException if the datarow detected a deadlock.
   */
  Object get (int col) throws DataSourceException;

  /**
   * Returns the value of the function, expression or column using its specific name. The
   * given name is translated into a valid column number and the the column is queried.
   * For functions and expressions, the <code>getValue()</code> method is called and for
   * columns from the tablemodel the tablemodel method <code>getValueAt(row,
   * column)</code> gets called.
   *
   * @param col the item index.
   * @return the value.
   *
   * @throws IllegalStateException if the datarow detected a deadlock.
   */
  Object get (String col)
          throws DataSourceException;

  /**
   * Returns the name of the column, expression or function. For columns from the
   * tablemodel, the tablemodels <code>getColumnName</code> method is called. For
   * functions, expressions and report properties the assigned name is returned.
   *
   * @param col the item index.
   * @return the name.
   */
  String getColumnName (int col) throws DataSourceException;

  /**
   * Returns the number of columns, expressions and functions and marked ReportProperties
   * in the report.
   *
   * @return the item count.
   */
  int getColumnCount () throws DataSourceException;

  DataFlags getFlags (String col) throws DataSourceException;

  DataFlags getFlags (int col) throws DataSourceException;
}
