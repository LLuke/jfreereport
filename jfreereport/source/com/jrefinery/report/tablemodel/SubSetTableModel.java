/**
 * Date: Jan 26, 2003
 * Time: 3:55:42 PM
 *
 * $Id: SubSetTableModel.java,v 1.3 2003/02/02 23:43:51 taqua Exp $
 */
package com.jrefinery.report.tablemodel;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class SubSetTableModel implements TableModel
{
  private class TableEventTranslator implements TableModelListener
  {
    private ArrayList listeners;

    public TableEventTranslator()
    {
      listeners = new ArrayList();
    }

    /**
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed.
     */
    public void tableChanged(TableModelEvent e)
    {
      int firstRow = e.getFirstRow();
      if (e.getFirstRow() > 0)
      {
        firstRow -= start;
      }

      int lastRow = e.getLastRow();
      if (lastRow > 0)
      {
        lastRow -=start;
        lastRow -= (model.getRowCount() - end);
      }
      int type = e.getType() ;
      int column = e.getColumn();

      TableModelEvent event =
          new TableModelEvent(SubSetTableModel.this, firstRow, lastRow, column, type);

      for (int i = 0; i < listeners.size(); i++)
      {
        TableModelListener l = (TableModelListener) listeners.get(i);
        l.tableChanged(event);
      }
    }

    public void addTableModelListener (TableModelListener l)
    {
      listeners.add(l);
    }

    public void removeTableModelListener (TableModelListener l)
    {
      listeners.remove(l);
    }
  }

  private int start;
  private int end;
  private TableModel model;
  private TableEventTranslator eventHandler;

  public SubSetTableModel (int start, int end, TableModel model)
  {
    if (start < 0) throw new IllegalArgumentException("Start < 0");
    if (end < start) throw new IllegalArgumentException("end < start");
    if (model == null) throw new NullPointerException();
    if (end >= model.getRowCount()) throw new IllegalArgumentException("End >= Model.RowCount");

    this.start = start;
    this.end = end;
    this.model = model;
    this.eventHandler = new TableEventTranslator();
  }

  private int getClientRowIndex (int rowIndex)
  {
    return rowIndex + start;
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
    int rowCount = model.getRowCount();
    return rowCount - start - (rowCount - end);
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
    return model.getColumnCount();
  }

  /**
   * Returns the name of the column at <code>columnIndex</code>.  This is used
   * to initialize the table's column header name.  Note: this name does
   * not need to be unique; two columns in a table can have the same name.
   *
   * @param	columnIndex	the index of the column
   * @return  the name of the column
   */
  public String getColumnName(int columnIndex)
  {
    return model.getColumnName(columnIndex);
  }

  /**
   * Returns the most specific superclass for all the cell values
   * in the column.  This is used by the <code>JTable</code> to set up a
   * default renderer and editor for the column.
   *
   * @param columnIndex  the index of the column
   * @return the base ancestor class of the object values in the model.
   */
  public Class getColumnClass(int columnIndex)
  {
    return getColumnClass(columnIndex);
  }

  /**
   * Returns true if the cell at <code>rowIndex</code> and
   * <code>columnIndex</code>
   * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
   * change the value of that cell.
   *
   * @param	rowIndex	the row whose value to be queried
   * @param	columnIndex	the column whose value to be queried
   * @return	true if the cell is editable
   * @see #setValueAt
   */
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return model.isCellEditable(getClientRowIndex(rowIndex), columnIndex);
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
    return model.getValueAt(getClientRowIndex(rowIndex), columnIndex);
  }

  /**
   * Sets the value in the cell at <code>columnIndex</code> and
   * <code>rowIndex</code> to <code>aValue</code>.
   *
   * @param	aValue		 the new value
   * @param	rowIndex	 the row whose value is to be changed
   * @param	columnIndex 	 the column whose value is to be changed
   * @see #getValueAt
   * @see #isCellEditable
   */
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    model.setValueAt(aValue, getClientRowIndex(rowIndex), columnIndex);
  }

  /**
   * Adds a listener to the list that is notified each time a change
   * to the data model occurs.
   *
   * @param	l		the TableModelListener
   */
  public void addTableModelListener(TableModelListener l)
  {
    eventHandler.addTableModelListener(l);
  }

  /**
   * Removes a listener from the list that is notified each time a
   * change to the data model occurs.
   *
   * @param	l		the TableModelListener
   */
  public void removeTableModelListener(TableModelListener l)
  {
    eventHandler.removeTableModelListener(l);
  }
}
