/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ------------
 * DataRow.java
 * ------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DataRow.java,v 1.5 2002/12/06 17:17:08 mungady Exp $
 *
 * Changes
 * -------
 * 27-Jul-2002 : Initial version
<<<<<<< DataRow.java
 * 05-Sep-2002 : Documentation
 * 13-Sep-2002 : Ran checkstyle and fixed reported issues
 * 07-Nov-2002 : Included an ID-tag into the header for better bug-tracking.
 * 06-Dec-2002 : Completed ChangeLog
=======
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
>>>>>>> 1.5
 */

package com.jrefinery.report;

/**
 * The datarow is used to access the current row in the <code>TableModel</code>,
 * <code>Expression</code>s and <code>Function</code>s using a generic interface.
 * The DataRow is able to detect deadlocks and to check function depencies.
 *
 * @see com.jrefinery.report.function.Expression
 * @see com.jrefinery.report.function.Function
 * @see javax.swing.table.TableModel
 * @author Thomas Morgner
 */
public interface DataRow
{
  /**
   * Returns the value of the function, expression or column in the tablemodel using the column
   * number.
   *
   * @param col  the item index.
   *
   * @return the value.
   */
  public Object get (int col);

  /**
   * Returns the value of the function, expression or column using its specific name.
   *
   * @param col  the item index.
   *
   * @return the value.
   * @throws IllegalStateException if the datarow detected a deadlock.
   */
  public Object get (String col) throws IllegalStateException;

  /**
   * Returns the name of the column, expression or function.
   *
   * @param col  the item index.
   *
   * @return the name.
   */
  public String getColumnName (int col);

  /**
   * Returns the column position of the column, expression or function with the given name or
   * -1 if the given name does not exist in this DataRow.
   *
   * @param name  the item name.
   *
   * @return the item index.
   */
  public int findColumn (String name);

  /**
   * Returns the number of columns, expressions and functions in the report.
   *
   * @return the item count.
   */
  public int getColumnCount ();

}