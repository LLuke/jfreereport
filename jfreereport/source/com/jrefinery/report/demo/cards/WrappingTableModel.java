/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * -----------------------
 * WrappingTableModel.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: WrappingTableModel.java,v 1.4 2003/05/11 13:38:57 taqua Exp $
 *
 * Changes
 * -------
 * 02.04.2003 : Initial version
 */
package com.jrefinery.report.demo.cards;

import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.jrefinery.report.tablemodel.TableModelInfo;

/**
 * A wrapping table model.
 *
 * @author Thomas Morgner
 */
public class WrappingTableModel implements TableModel
{
  /**
   * A helper class, that translates tableevents received from the wrapped table model
   * and forwards them with changed indices to the regitered listeners.
   */
  private class TableEventTranslator implements TableModelListener
  {
    /** the registered listeners. */
    private ArrayList listeners;

    /**
     * Default Constructor.
     */
    public TableEventTranslator()
    {
      listeners = new ArrayList();
    }

    /**
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed. The received rows are
     * translated to fit the external tablemodel size.
     *
     * @param e the event, that should be translated.
     */
    public void tableChanged(TableModelEvent e)
    {
      // inefficient, but necessary ...
      int columnIndex = TableModelEvent.ALL_COLUMNS;

      int firstRow = e.getFirstRow();
      int lastRow = e.getLastRow();

      int firstRowIndex = (firstRow / 2);
      int lastRowIndex = (lastRow / 2);

      TableModelEvent event =
          new TableModelEvent(WrappingTableModel.this, firstRowIndex, lastRowIndex,
              columnIndex, e.getType());

      for (int i = 0; i < listeners.size(); i++)
      {
        TableModelListener l = (TableModelListener) listeners.get(i);
        l.tableChanged(event);
      }

    }

    /**
     * Adds the TableModelListener to this Translator.
     *
     * @param l the tablemodel listener
     */
    public void addTableModelListener(TableModelListener l)
    {
      listeners.add(l);
    }

    /**
     * Removes the TableModelListener from this Translator.
     *
     * @param l the tablemodel listener
     */
    public void removeTableModelListener(TableModelListener l)
    {
      listeners.remove(l);
    }
  }

  /** A table event translator. */
  private TableEventTranslator translator;

  /** The column prefix 1. */
  private String columnPrefix1;

  /** The column prefix 2. */
  private String columnPrefix2;

  /** The table model. */
  private TableModel model;

  /**
   * Creates a new wrapping table model.
   *
   * @param model  the underlying table model.
   */
  public WrappingTableModel(TableModel model)
  {
    this(model, "Column1_", "Column2_");
  }

  /**
   * Creates a new wrapping table model.
   *
   * @param model  the underlying table model.
   * @param prefix1  the first column prefix.
   * @param prefix2  the second column prefix.
   */
  public WrappingTableModel(TableModel model, String prefix1, String prefix2)
  {
    if (prefix1 == null)
    {
      throw new NullPointerException();
    }
    if (prefix2 == null)
    {
      throw new NullPointerException();
    }
    if (prefix1.equals(prefix2))
    {
      throw new IllegalArgumentException("Prefix 1 and 2 are identical");
    }
    this.model = model;
    this.columnPrefix1 = prefix1;
    this.columnPrefix2 = prefix2;
    this.translator = new TableEventTranslator();
  }

  /**
   * Returns column prefix 1.
   *
   * @return Column prefix 1.
   */
  public String getColumnPrefix1()
  {
    return columnPrefix1;
  }

  /**
   * Returns column prefix 2.
   *
   * @return Column prefix 2.
   */
  public String getColumnPrefix2()
  {
    return columnPrefix2;
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
    return (int) Math.ceil(model.getRowCount() / 2.0);
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
    return 2 * model.getColumnCount();
  }

  /**
   * Returns the name of the column at <code>columnIndex</code>.  This is used
   * to initialize the table's column header name.  Note: this name does
   * not need to be unique; two columns in a table can have the same name.
   *
   * @param columnIndex  the index of the column
   *
   * @return the name of the column
   */
  public String getColumnName(int columnIndex)
  {
    int tmpColumnIndex = (columnIndex % model.getColumnCount());
    if (columnIndex < model.getColumnCount())
    {
      return getColumnPrefix1() + model.getColumnName(tmpColumnIndex);
    }
    else
    {
      return getColumnPrefix2() + model.getColumnName(tmpColumnIndex);
    }
  }

  /**
   * Returns the most specific superclass for all the cell values
   * in the column.  This is used by the <code>JTable</code> to set up a
   * default renderer and editor for the column.
   *
   * @param columnIndex  the index of the column
   * @return the common ancestor class of the object values in the model.
   */
  public Class getColumnClass(int columnIndex)
  {
    int tmpColumnIndex = (columnIndex % model.getColumnCount());
    return model.getColumnClass(tmpColumnIndex);
  }

  /**
   * Returns true if the cell at <code>rowIndex</code> and
   * <code>columnIndex</code>
   * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
   * change the value of that cell.
   *
   * @param rowIndex  the row whose value to be queried
   * @param columnIndex  the column whose value to be queried
   *
   * @return true if the cell is editable
   * @see #setValueAt
   */
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    int tmpColumnIndex = (columnIndex % model.getColumnCount());
    int tmpRowIndex = calculateRow(rowIndex, columnIndex);
    if (tmpRowIndex >= model.getRowCount())
    {
      return false;
    }
    return model.isCellEditable(tmpRowIndex, tmpColumnIndex);
  }

  /**
   * Calculates the physical row.
   *
   * @param row  the (logical) row index.
   * @param column  the column index.
   *
   * @return The physical row.
   */
  private int calculateRow(int row, int column)
  {
    if (column < model.getColumnCount())
    {
      // high row ...
      return row * 2;
    }
    else
    {
      // low row ...
      return (row * 2) + 1;
    }
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
    int tmpColumnIndex = (columnIndex % model.getColumnCount());
    int tmpRowIndex = calculateRow(rowIndex, columnIndex);
    if (tmpRowIndex >= model.getRowCount())
    {
      return null;
    }
    return model.getValueAt(tmpRowIndex, tmpColumnIndex);
  }

  /**
   * Sets the value in the cell at <code>columnIndex</code> and
   * <code>rowIndex</code> to <code>aValue</code>.
   *
   * @param aValue  the new value
   * @param rowIndex  the row whose value is to be changed
   * @param columnIndex  the column whose value is to be changed
   * @see #getValueAt
   * @see #isCellEditable
   */
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    int tmpColumnIndex = (columnIndex % model.getColumnCount());
    int tmpRowIndex = calculateRow(rowIndex, columnIndex);
    if (tmpRowIndex >= model.getRowCount())
    {
      return;
    }
    model.setValueAt(aValue, tmpRowIndex, tmpColumnIndex);
  }

  /**
   * Adds a listener to the list that is notified each time a change
   * to the data model occurs.
   *
   * @param l  the TableModelListener
   */
  public void addTableModelListener(TableModelListener l)
  {
    translator.addTableModelListener(l);
  }

  /**
   * Removes a listener from the list that is notified each time a
   * change to the data model occurs.
   *
   * @param l  the TableModelListener
   */
  public void removeTableModelListener(TableModelListener l)
  {
    translator.removeTableModelListener(l);
  }

  /**
   * Test code - please ignore.
   *
   * @param args  ignored.
   */
  public static void main(String[] args)
  {
    TableModelInfo.printTableModel(new WrappingTableModel(new CardTableModel()));
  }
}
