/**
 * Date: Feb 12, 2003
 * Time: 5:54:44 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.stylekey;

import com.jrefinery.report.targets.style.StyleKey;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class StyleKeyReferenceTableModel extends AbstractTableModel
{
  private class StylekeyDescriptionRow
  {
    private StyleKeyFactory keyFactory;
    private StyleKey key;

    public StylekeyDescriptionRow(StyleKeyFactory keyFactory, StyleKey key)
    {
      this.keyFactory = keyFactory;
      this.key = key;
    }

    public StyleKeyFactory getKeyFactory()
    {
      return keyFactory;
    }

    public StyleKey getKey()
    {
      return key;
    }
  }

  private static final String[] COLUMN_NAMES =
      {
        "stylekey-factory",
        "key-name",
        "key-class"
      };

  private ArrayList rows;

  public StyleKeyReferenceTableModel(StyleKeyFactoryCollector cf)
  {
    rows = new ArrayList();
    addStyleKeyFactoryCollector(cf);
  }

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
   *  Returns a default name for the column using spreadsheet conventions:
   *  A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
   *  returns an empty string.
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
   * @param	rowIndex	the row whose value is to be queried
   * @param	columnIndex 	the column whose value is to be queried
   * @return	the value Object at the specified cell
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
