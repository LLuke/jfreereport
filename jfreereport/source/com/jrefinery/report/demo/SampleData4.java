/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * SampleData4.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.demo;

import javax.swing.table.AbstractTableModel;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.util.Comparator;
import java.util.Arrays;

public class SampleData4 extends AbstractTableModel
{
  private class FontComparator implements Comparator
  {
    public int compare (Object o, Object o1)
    {
      Font f1 = (Font) o;
      Font f2 = (Font) o1;
      int comp = f1.getFamily().compareTo(f2.getFamily());
      if (comp == 0)
      {
        comp = f1.getName().compareTo(f2.getName());
      }
      return comp;
    }
  }

  private Font[] fonts = null;

  public SampleData4 ()
  {
    fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    Arrays.sort(fonts, new FontComparator());
  }

  /**
   * Returns the number of rows in the table model.
   */
  public int getRowCount ()
  {
    return fonts.length;
  }

  /**
   * Returns the number of columns in the table model.
   */
  public int getColumnCount ()
  {
    return 3;
  }

  /**
   * Returns the class of the data in the specified column.
   */
  public Class getColumnClass (int columnIndex)
  {
    return String.class;
  }

  /**
   * Returns the name of the specified column.
   */
  public String getColumnName (int columnIndex)
  {
    if (columnIndex == 0)
      return "family";
    else if (columnIndex == 1)
      return "fontname";
    else
      return "sample";
  }

  /**
   * Returns the data value at the specified row and column.
   */
  public Object getValueAt (int row, int column)
  {
    if (column == 0)
    {
      return fonts[row].getFamily();
    }
    else if (column == 1)
    {
      return fonts[row].getName();
    }
    return "The five boxing wizards jump quickly.";
  }


}
