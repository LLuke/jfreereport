/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: StyleKeyReferenceTableModel.java,v 1.4 2003/08/25 14:29:31 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.misc.referencedoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;

import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.style.StyleKey;

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
    private final StyleKeyFactory keyFactory;

    /** The key. */
    private final StyleKey key;

    /**
     * Creates a new row.
     *
     * @param keyFactory  the factory.
     * @param key  the key.
     */
    public StylekeyDescriptionRow(final StyleKeyFactory keyFactory, final StyleKey key)
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
  private final ArrayList rows;

  /**
   * Creates a new table model.
   *
   * @param cf  the factory collection.
   */
  public StyleKeyReferenceTableModel(final StyleKeyFactoryCollector cf)
  {
    rows = new ArrayList();
    addStyleKeyFactoryCollector(cf);
  }

  /**
   * Adds a factory.
   *
   * @param cf  the factory.
   */
  private void addStyleKeyFactoryCollector(final StyleKeyFactoryCollector cf)
  {
    final Iterator it = cf.getFactories();
    while (it.hasNext())
    {
      final StyleKeyFactory cfact = (StyleKeyFactory) it.next();
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
  private void addStyleKeyFactory(final StyleKeyFactory cf)
  {
    Iterator it = cf.getRegisteredKeys();
    final ArrayList factories = new ArrayList();

    while (it.hasNext())
    {
      final String c = (String) it.next();
      factories.add(c);
    }

    Collections.sort(factories);
    it = factories.iterator();

    while (it.hasNext())
    {
      final String keyName = (String) it.next();
      final StyleKey key = cf.getStyleKey(keyName);
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
  public String getColumnName(final int column)
  {
    return COLUMN_NAMES[column];
  }

  /**
   *  Returns <code>String.class</code> regardless of <code>columnIndex</code>.
   *
   *  @param columnIndex  the column being queried
   *  @return the Object.class
   */
  public Class getColumnClass(final int columnIndex)
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
  public Object getValueAt(final int rowIndex, final int columnIndex)
  {
    final StylekeyDescriptionRow or = (StylekeyDescriptionRow) rows.get(rowIndex);
    switch (columnIndex)
    {
      case 0:
        return String.valueOf(or.getKeyFactory().getClass().getName());
      case 1:
        return String.valueOf(or.getKey().getName());
      case 2:
        return String.valueOf(or.getKey().getValueType().getName());
      default:
        return null;
    }
  }
}
