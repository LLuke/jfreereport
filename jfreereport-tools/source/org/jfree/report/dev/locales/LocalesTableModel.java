package org.jfree.report.dev.locales;

import javax.swing.table.AbstractTableModel;

public class LocalesTableModel extends AbstractTableModel
{
  private EditableProperties locales;
  private EditableProperties defaultLocale;
  private String[] keys;

  public LocalesTableModel (final EditableProperties locales,
                            final EditableProperties defaultLocale)
  {
    this.locales = locales;
    this.defaultLocale = defaultLocale;
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
    return 2;
  }

  /**
   * Returns a default name for the column using spreadsheet conventions: A, B, C, ... Z,
   * AA, AB, etc.  If <code>column</code> cannot be found, returns an empty string.
   *
   * @param column the column being queried
   * @return a string containing the default name of <code>column</code>
   */
  public String getColumnName (final int column)
  {
    if (column == 0)
    {
      return "Key";
    }
    return "Value";
  }

  /**
   * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
   *
   * @param columnIndex the column being queried
   * @return the Object.class
   */
  public Class getColumnClass (final int columnIndex)
  {
    return String.class;
  }

  /**
   * Returns false.  This is the default implementation for all cells.
   *
   * @param rowIndex    the row being queried
   * @param columnIndex the column being queried
   * @return false
   */
  public boolean isCellEditable (final int rowIndex, final int columnIndex)
  {
    return columnIndex == 1;
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
    if (keys == null)
    {
      keys = defaultLocale.getKeys();
    }
    return keys.length;
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
    if (keys == null)
    {
      keys = defaultLocale.getKeys();
    }

    if (columnIndex == 0)
    {
      return keys[rowIndex];
    }
    return locales.getProperty(keys[rowIndex], defaultLocale.getProperty(keys[rowIndex]));
  }

  public boolean isInheritedValue (final int rowIndex)
  {
    if (keys == null)
    {
      keys = defaultLocale.getKeys();
    }

    return locales.getProperty(keys[rowIndex]) == null;
  }

  /**
   * This empty implementation is provided so users don't have to implement this method if
   * their data model is not editable.
   *
   * @param aValue      value to assign to cell
   * @param rowIndex    row of cell
   * @param columnIndex column of cell
   */
  public void setValueAt (final Object aValue, final int rowIndex, final int columnIndex)
  {
    if (columnIndex != 1)
    {
      return;
    }
    if (keys == null)
    {
      keys = defaultLocale.getKeys();
    }
    locales.setProperty(keys[rowIndex], aValue.toString());
  }
}
