/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * TableModelInfo.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TableModelInfo.java,v 1.4 2003/06/27 14:25:23 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Added Javadocs (DG);
 *
 */

package com.jrefinery.report.tablemodel;

import javax.swing.table.TableModel;

/**
 * A utility class that prints out information about a TableModel.
 *
 * @author Thomas Morgner
 */
public class TableModelInfo
{
  /**
   * Prints a table model to standard output.
   *
   * @param mod  the model.
   */
  public static void printTableModel(final TableModel mod)
  {
    System.out.println("Tablemodel contains " + mod.getRowCount() + " rows.");
    for (int i = 0; i < mod.getColumnCount(); i++)
    {
      System.out.println("Column: " + i + " Name = " + mod.getColumnName(i) + "; DataType = "
          + mod.getColumnClass(i));
    }

    System.out.println("Checking the data inside");
    for (int rows = 0; rows < mod.getRowCount(); rows++)
    {
      for (int i = 0; i < mod.getColumnCount(); i++)
      {
        final Object value = mod.getValueAt(rows, i);
        final Class c = mod.getColumnClass(i);
        if (value == null)
        {
          System.out.println("ValueAt (" + rows + ", " + i + ") is null");
        }
        else
        {
          if (c.isAssignableFrom(value.getClass()) == false)
          {
            System.out.println
                ("ValueAt (" + rows + ", " + i + ") is not assignable from " + c);
          }
          if (c.equals(Object.class))
          {
            System.out.println
                ("ValueAt (" + rows + ", " + i + ") is in a generic column and is of "
                + "type " + value.getClass());
          }
        }
      }
    }
  }

  /**
   * Prints a table model to standard output.
   *
   * @param mod  the model.
   */
  public static void printTableModelContents(final TableModel mod)
  {
    System.out.println("Tablemodel contains " + mod.getRowCount() + " rows.");
    for (int i = 0; i < mod.getColumnCount(); i++)
    {
      System.out.println("Column: " + i + " Name = " + mod.getColumnName(i) + "; DataType = "
          + mod.getColumnClass(i));
    }

    System.out.println("Checking the data inside");
    for (int rows = 0; rows < mod.getRowCount(); rows++)
    {
      for (int i = 0; i < mod.getColumnCount(); i++)
      {
        final Object value = mod.getValueAt(rows, i);
        final Class c = mod.getColumnClass(i);
        System.out.println("ValueAt (" + rows + ", " + i + ") is " + value);
      }
    }
  }
}
