/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * ConditionalGroupTableModel.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConditionalGroupTableModel.java,v 1.2 2005/01/25 01:13:41 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 24.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.conditionalgroup;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ConditionalGroupTableModel extends AbstractTableModel
{
  private String[] COLUMN_NAMES = {
    "type", "level-one-account", "level-two-account", "balance"
  };

  private Class[] COLUMN_TYPE = {
    String.class, String.class, String.class, Number.class
  };

  private ArrayList entries;

  public ConditionalGroupTableModel ()
  {
    entries = new ArrayList();
  }

  public void addRecord (final String type, final String levelOne,
                         final String levelTwo, final Number balance)
  {
    entries.add(new Object[]{type, levelOne, levelTwo, balance});
  }

  /**
   * Returns the most specific superclass for all the cell values in the column.  This is
   * used by the <code>JTable</code> to set up a default renderer and editor for the
   * column.
   *
   * @param columnIndex the index of the column
   * @return the common ancestor class of the object values in the model.
   */
  public Class getColumnClass (final int columnIndex)
  {
    return COLUMN_TYPE[columnIndex];
  }

  /**
   * Returns the number of columns in the model. A <code>JTable</code> uses this method to
   * determine how many columns it should create and display by default.
   *
   * @return the number of columns in the model
   *
   * @see #getRowCount
   */
  public int getColumnCount ()
  {
    return COLUMN_TYPE.length;
  }

  /**
   * Returns the name of the column at <code>columnIndex</code>.  This is used to
   * initialize the table's column header name.  Note: this name does not need to be
   * unique; two columns in a table can have the same name.
   *
   * @return the name of the column
   *
   * @param	columnIndex	the index of the column
   */
  public String getColumnName (final int columnIndex)
  {
    return COLUMN_NAMES[columnIndex];
  }

  /**
   * Returns the number of rows in the model. A <code>JTable</code> uses this method to
   * determine how many rows it should display.  This method should be quick, as it is
   * called frequently during rendering.
   *
   * @return the number of rows in the model
   *
   * @see #getColumnCount
   */
  public int getRowCount ()
  {
    return entries.size();
  }

  /**
   * Returns the value for the cell at <code>columnIndex</code> and
   * <code>rowIndex</code>.
   *
   * @param	rowIndex	the row whose value is to be queried
   * @param	columnIndex the column whose value is to be queried
   * @return	the value Object at the specified cell
   */
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    final Object[] entry = (Object[]) entries.get(rowIndex);
    return entry[columnIndex];
  }
}
