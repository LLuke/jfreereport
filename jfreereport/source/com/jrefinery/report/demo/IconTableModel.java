/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
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
 * -------------------
 * IconTableModel.java
 * -------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: IconTableModel.java,v 1.2 2002/07/16 13:29:11 mungady Exp $
 *
 * Changes
 * -------
 * 15-Jul-2002 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo;

import javax.swing.table.AbstractTableModel;
import java.awt.Image;
import java.util.List;

/**
 * A simple TableModel implementation used for demonstration purposes.
 */
public class IconTableModel extends AbstractTableModel
{

  /** The table data. */
  protected List data;

  /**
   * Constructs a new IconTableModel, initially empty.
   */
  public IconTableModel ()
  {
    this.data = new java.util.ArrayList ();
  }

  /**
   * Adds a new entry to the table model.
   *
   * @param name The icon name.
   * @param category The category name.
   * @param icon The icon.
   * @param size The size of the icon image in bytes.
   */
  public void addIconEntry (String name, String category, Image icon, Long size)
  {
    Object[] item = new Object[4];
    item[0] = name;
    item[1] = category;
    item[2] = icon;
    item[3] = size;
    data.add (0, item);
  }

  /**
   * Returns the number of rows in the table model.
   *
   * @return The row count.
   */
  public int getRowCount ()
  {
    return data.size ();
  }

  /**
   * Returns the number of columns in the table model.
   *
   * @return The column count.
   */
  public int getColumnCount ()
  {
    return 4;
  }

  /**
   * Returns the data item at the specified row and column.
   *
   * @param row The row index.
   * @param column The column index.
   *
   * @return The data item.
   */
  public Object getValueAt (int row, int column)
  {
    Object[] item = (Object[]) data.get (row);
    return item[column];
  }

  /**
   * Returns the class of the specified column
   *
   * @param column The column index.
   *
   * @return The column class.
   */
  public Class getColumnClass (int column)
  {
    if (column == 2)
    {
      return java.awt.Image.class;
    }
    else
      return Object.class;
  }

  /**
   * Returns the name of the specified column.
   *
   * @param column The column.
   *
   * @return The column name.
   */
  public String getColumnName (int column)
  {
    switch (column)
    {
      case 0:
        return "Name";
      case 1:
        return "Category";
      case 2:
        return "Icon";
      case 3:
        return "Size";
      default:
        return "Error";
    }
  }

}