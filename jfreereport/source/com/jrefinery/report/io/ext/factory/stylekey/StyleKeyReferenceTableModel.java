/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * --------------------------------
 * StyleKeyReferenceTableModel.java
 * --------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *  
 */

package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.style.StyleKey;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * A table model for the style key reference generator.
 * 
 * @author Thomas Morgner
 */
public class StyleKeyReferenceTableModel extends AbstractTableModel
{
  /**
   * Represents a row in the table model.
   */
  private class StylekeyDescriptionRow
  {
    /** The factory. */
    private StyleKeyFactory keyFactory;
    
    /** The key. */
    private StyleKey key;

    /**
     * Creates a new row.
     * 
     * @param keyFactory  the factory.
     * @param key  the key.
     */
    public StylekeyDescriptionRow(StyleKeyFactory keyFactory, StyleKey key)
    {
      this.keyFactory = keyFactory;
      this.key = key;
    }

    /**
     * Returns the factory.
     * 
     * @return The factory.
     */
    public StyleKeyFactory getKeyFactory()
    {
      return keyFactory;
    }

    /**
     * Returns the key.
     * 
     * @return The key.
     */
    public StyleKey getKey()
    {
      return key;
    }
  }

  /** The column names. */
  private static final String[] COLUMN_NAMES =
      {
        "stylekey-factory",
        "key-name",
        "key-class"
      };

  /** Storage for the rows. */
  private ArrayList rows;

  /**
   * Creates a new table model.
   * 
   * @param cf  the factory collection.
   */
  public StyleKeyReferenceTableModel(StyleKeyFactoryCollector cf)
  {
    rows = new ArrayList();
    addStyleKeyFactoryCollector(cf);
  }

  /**
   * Adds a factory.
   * 
   * @param cf  the factory.
   */
  private void addStyleKeyFactoryCollector (StyleKeyFactoryCollector cf)
  {
    Iterator it = cf.getFactories();
    while (it.hasNext())
    {
      StyleKeyFactory cfact = (StyleKeyFactory) it.next();
      if (cfact instanceof StyleKeyFactoryCollector)
      {
        addStyleKeyFactoryCollector((StyleKeyFactoryCollector) cfact);
      }
      else
      {
        addStyleKeyFactory(cfact);
      }
    }
  }

  /**
   * Adds a factory.
   * 
   * @param cf  the factory.
   */
  private void addStyleKeyFactory (StyleKeyFactory cf)
  {
    Iterator it = cf.getRegisteredKeys();
    ArrayList factories = new ArrayList();

    while (it.hasNext())
    {
      String c = (String) it.next();
      factories.add(c);
    }

    Collections.sort(factories);
    it = factories.iterator();

    while (it.hasNext())
    {
      String keyName = (String) it.next();
      StyleKey key = cf.getStyleKey(keyName);
      rows.add(new StylekeyDescriptionRow(cf, key));
    }
  }

  /**
   * Returns the number of rows in the model. A
   * <code>JTable</code> uses this method to determine how many rows it
   * should display.  This method should be quick, as it
   * is called frequently during rendering.
   *
   * @return the number of rows in the model
   * @see #getColumnCount
   */
  public int getRowCount()
  {
    return rows.size();
  }

  /**
   * Returns the number of columns in the model. A
   * <code>JTable</code> uses this method to determine how many columns it
   * should create and display by default.
   *
   * @return the number of columns in the model
   * @see #getRowCount
   */
  public int getColumnCount()
  {
    return COLUMN_NAMES.length;
  }

  /**
   * Returns the column name.
   * 
   * @param column  the column being queried
   * @return a string containing the default name of <code>column</code>
   */
  public String getColumnName(int column)
  {
    return COLUMN_NAMES[column];
  }

  /**
   *  Returns <code>String.class</code> regardless of <code>columnIndex</code>.
   *
   *  @param columnIndex  the column being queried
   *  @return the Object.class
   */
  public Class getColumnClass(int columnIndex)
  {
    return String.class;
  }

  /**
   * Returns the value for the cell at <code>columnIndex</code> and
   * <code>rowIndex</code>.
   *
   * @param rowIndex  the row whose value is to be queried
   * @param columnIndex  the column whose value is to be queried
   * 
   * @return the value Object at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    StylekeyDescriptionRow or = (StylekeyDescriptionRow) rows.get(rowIndex);
    switch (columnIndex)
    {
      case 0: return String.valueOf(or.getKeyFactory().getClass().getName());
      case 1: return String.valueOf(or.getKey().getName());
      case 2: return String.valueOf(or.getKey().getValueType().getName());
    }
    return null;
  }
}
