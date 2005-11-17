/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * ------------------------
 * CloseableTableModel.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: CloseableTableModel.java,v 1.4 2005/02/23 21:06:05 taqua Exp $
 *
 * Changes
 * -------
 * 09-Jun-2002 : Initial version to correct the fact, that resultsets are not closed when
 *               the tablemodel is no longer in use.
 * 10-Dec-2002 : Minor Javadoc updates (DG);
 *
 */

package org.jfree.report.util;

import javax.swing.table.TableModel;

/**
 * Extends the TableModel interface to be closeable. SQLResultSets need to be closed for
 * instance.
 *
 * @author Thomas Morgner
 */
public interface CloseableTableModel extends TableModel
{
  /**
   * If this model has a resultset assigned, close it, if this is a DefaultTableModel,
   * remove all data.
   */
  public void close ();

}
