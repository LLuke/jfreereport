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
 * ----------------
 * SampleData4.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 *
 * $Id: SampleData4.java,v 1.9 2003/05/02 12:39:37 taqua Exp $
 *
 */
package com.jrefinery.report.demo;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

/**
 * A sample data source for the JFreeReport Demo Application.
 *
 * @author Thomas Morgner
 */
public class SampleData4 extends AbstractTableModel
{
  /**
   * Comparator for sorting fonts.
   */
  private class FontComparator implements Comparator
  {
    /**
     * Compares two fonts.
     *
     * @param o  font 1.
     * @param o1  font 2.
     *
     * @return an integer representing the relative order of the two fonts.
     */
    public int compare(Object o, Object o1)
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

  /** Storage for the fonts. */
  private Font[] fonts = null;

  /**
   * Sample dataset.
   */
  public SampleData4()
  {
    fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    Arrays.sort(fonts, new FontComparator());
  }

  /**
   * Returns the number of rows in the table model.
   *
   * @return the row count.
   */
  public int getRowCount()
  {
    return fonts.length;
  }

  /**
   * Returns the number of columns in the table model.
   *
   * @return the column count.
   */
  public int getColumnCount()
  {
    return 3;
  }

  /**
   * Returns the class of the data in the specified column.
   *
   * @param column  the column (zero-based index).
   *
   * @return the column class.
   */
  public Class getColumnClass(int column)
  {
    return String.class;
  }

  /**
   * Returns the name of the specified column.
   *
   * @param column  the column (zero-based index).
   *
   * @return the column name.
   */
  public String getColumnName(int column)
  {
    if (column == 0)
    {
      return "family";
    }
    else if (column == 1)
    {
      return "fontname";
    }
    else
    {
      return "sample";
    }
  }

  /**
   * Returns the data value at the specified row and column.
   *
   * @param row  the row index (zero based).
   * @param column  the column index (zero based).
   *
   * @return the value.
   */
  public Object getValueAt(int row, int column)
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
