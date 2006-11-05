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
 * Database.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.11.2006 : Initial version
 */
package org.jfree.formula.lvalues;

/**
 * A database is a two dimensional collection of data, arranged in a table.
 * Although we do not assume that the whole database is held in memory, we
 * allow random access of the data.
 *
 * Columns may have names, but there is no enforced requirement for that.
 *
 * As a database is not just a collection of raw data, this interface returns
 * LValues instead of plain objects. Columns may be computed values using
 * formulas (the exact semantics of adressing database cells in a formula is
 * beyond the scope of this specification and is implementation specific). 
 *
 * @author Thomas Morgner
 */
public interface DataTable extends LValue
{
  public int getRowCount();
  public int getColumnCount();

  public String getColumnName (int column);
  public LValue getValueAt (int row, int column);
}
