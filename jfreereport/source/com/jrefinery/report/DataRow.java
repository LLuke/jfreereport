/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------
 * DataRow.java
 * -----------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Changes
 * ------------------------------
 * 27-Jul-2002 : Inital version
 */
package com.jrefinery.report;

/**
 * The datarow is used to access the current row in the tablemodel as well as expressions
 * and functions using a generic interface.
 */
public interface DataRow
{
  /**
   * return the value of the function,expression or column in the tablemodel using the column
   * number.
   */
  public Object get (int col);

  /**
   * returns the value of the function, expression or column using its specific name.
   */
  public Object get (String col);

  /**
   * returns the name of the column, expression or function
   */
  public String getColumnName (int col);

  /**
   * returns the column position of the column, expression or function with the given name or
   * -1 if the given name does not exist in this DataRow.
   */
  public int findColumn (String name);

  /**
   * returns the number of columns, expressions and functions in the report
   */
  public int getColumnCount ();

}