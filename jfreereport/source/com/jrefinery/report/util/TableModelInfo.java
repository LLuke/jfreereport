/**
 * Date: Nov 20, 2002
 * Time: 11:12:46 PM
 *
 * $Id$
 */
package com.jrefinery.report.util;

import javax.swing.table.TableModel;

public class TableModelInfo
{
  public static void printTableModel (TableModel mod)
  {
    System.out.println ("Tablemodel contains " + mod.getRowCount() + " rows.");
    for (int i = 0; i < mod.getColumnCount(); i++)
    {
      System.out.println ("Column: " + i + " Name = " + mod.getColumnName(i) + "; DataType = " + mod.getColumnClass(i));
    }

    System.out.println ("Checking the data inside");
    for (int rows = 0; rows < mod.getRowCount(); rows++)
    {
      for (int i = 0; i < mod.getColumnCount(); i++)
      {
        Object value = mod.getValueAt(rows, i);
        Class c = mod.getColumnClass(i);
        if (c.isAssignableFrom(value.getClass()) == false)
        {
          System.out.println ("ValueAt (" + rows + ", " + i + ") is not assignable from " + c);
        }
        if (c.equals(Object.class))
        {
          System.out.println ("ValueAt (" + rows + ", " + i + ") is in a generic column and is of type " + value.getClass());
        }
      }
    }
  }
}
