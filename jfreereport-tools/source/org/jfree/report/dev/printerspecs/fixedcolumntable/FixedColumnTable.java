package org.jfree.report.dev.printerspecs.fixedcolumntable;

import java.awt.Dimension;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;

public class FixedColumnTable extends JScrollPane
{
  private class FixedTableModel extends AbstractTableModel
          implements TableModelListener
  {
    private int colCount;
    private TableModel parent;

    public FixedTableModel (final TableModel parent, final int colCount)
    {
      this.parent = parent;
      this.colCount = colCount;
      this.parent.addTableModelListener(this);
    }

    /**
     * Returns the most specific superclass for all the cell values in the column.  This is
     * used by the <code>JTable</code> to set up a default renderer and editor for the
     * column.
     *
     * @param columnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class getColumnClass (final int columnIndex)
    {
      if (columnIndex >= this.colCount)
      {
        throw new IndexOutOfBoundsException();
      }
      return parent.getColumnClass(columnIndex);
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
      return Math.min (parent.getColumnCount(), colCount);
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used to
     * initialize the table's column header name.  Note: this name does not need to be
     * unique; two columns in a table can have the same name.
     *
     * @return the name of the column
     *
     * @param	columnIndex	the index of the column
     */
    public String getColumnName (final int columnIndex)
    {
      if (columnIndex >= this.colCount)
      {
        throw new IndexOutOfBoundsException();
      }
      return parent.getColumnName(columnIndex);
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
      return parent.getRowCount();
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
      if (columnIndex >= this.colCount)
      {
        throw new IndexOutOfBoundsException();
      }
      return parent.getValueAt(rowIndex, columnIndex);
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and <code>columnIndex</code> is
     * editable.  Otherwise, <code>setValueAt</code> on the cell will not change the value
     * of that cell.
     *
     * @param	rowIndex	the row whose value to be queried
     * @param	columnIndex	the column whose value to be queried
     * @return	true if the cell is editable
     * @see #setValueAt
     */
    public boolean isCellEditable (final int rowIndex, final int columnIndex)
    {
      if (columnIndex >= this.colCount)
      {
        throw new IndexOutOfBoundsException();
      }
      return parent.isCellEditable(rowIndex, columnIndex);
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to
     * <code>aValue</code>.
     *
     * @param	aValue		 the new value
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    public void setValueAt (final Object aValue, final int rowIndex, final int columnIndex)
    {
      if (columnIndex >= this.colCount)
      {
        throw new IndexOutOfBoundsException();
      }
      parent.setValueAt(aValue, rowIndex, columnIndex);
    }


    /**
     * This fine grain notification tells listeners the exact range of cells, rows, or
     * columns that changed.
     */
    public void tableChanged (final TableModelEvent e)
    {
      final int column = Math.min (e.getColumn(), colCount);
      final TableModelEvent event = new TableModelEvent
              (this, e.getFirstRow(), e.getLastRow(), column, e.getType());
      fireTableChanged(event);
      if (e.getFirstRow() == TableModelEvent.HEADER_ROW)
      {
        updateTableWidth(totalWidth);
      }
    }
  }

  private class DataTableModel extends AbstractTableModel implements TableModelListener
  {
    private int colCount;
    private TableModel parent;

    public DataTableModel (final TableModel parent, final int colCount)
    {
      this.parent = parent;
      this.colCount = colCount;
      this.parent.addTableModelListener(this);
    }

    /**
     * Returns the most specific superclass for all the cell values in the column.  This is
     * used by the <code>JTable</code> to set up a default renderer and editor for the
     * column.
     *
     * @param columnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class getColumnClass (final int columnIndex)
    {
      return parent.getColumnClass(colCount + columnIndex);
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
      final int colCount = parent.getColumnCount();
      return Math.max (0, colCount - this.colCount);
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used to
     * initialize the table's column header name.  Note: this name does not need to be
     * unique; two columns in a table can have the same name.
     *
     * @return the name of the column
     *
     * @param	columnIndex	the index of the column
     */
    public String getColumnName (final int columnIndex)
    {
      return parent.getColumnName(colCount + columnIndex);
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
      return parent.getRowCount();
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
      return parent.getValueAt(rowIndex, colCount + columnIndex);
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and <code>columnIndex</code> is
     * editable.  Otherwise, <code>setValueAt</code> on the cell will not change the value
     * of that cell.
     *
     * @param	rowIndex	the row whose value to be queried
     * @param	columnIndex	the column whose value to be queried
     * @return	true if the cell is editable
     * @see #setValueAt
     */
    public boolean isCellEditable (final int rowIndex, final int columnIndex)
    {
      return parent.isCellEditable(rowIndex, colCount + columnIndex);
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to
     * <code>aValue</code>.
     *
     * @param	aValue		 the new value
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    public void setValueAt (final Object aValue, final int rowIndex, final int columnIndex)
    {
      parent.setValueAt(aValue, rowIndex, colCount + columnIndex);
    }


    /**
     * This fine grain notification tells listeners the exact range of cells, rows, or
     * columns that changed.
     */
    public void tableChanged (final TableModelEvent e)
    {
      final int column;
      if (e.getColumn() < 0)
      {
        column = e.getColumn();
      }
      else if (e.getColumn() < colCount)
      {
        // none of my events ...
        return;
      }
      else
      {
        column = e.getColumn() - colCount;
      }

      final TableModelEvent event = new TableModelEvent
              (this, e.getFirstRow(), e.getLastRow(), column, e.getType());
      fireTableChanged(event);

      if (getColumnCount() > 0)
      {
        setViewportView(dataTable);
      }
      else
      {
        setViewportView(new JPanel());
      }
    }

  }


  private JTable fixedTable;
  private JTable dataTable;
  private int fixedColumns;
  private int totalWidth;
  /**
   * Creates an empty (no viewport view) <code>JScrollPane</code> where both horizontal
   * and vertical scrollbars appear when needed.
   */
  public FixedColumnTable (final TableModel model, final int cols,
                           final int preferredFixedWidth)
  {
    this.totalWidth = preferredFixedWidth;
    this.fixedColumns = cols;
    this.dataTable = new JTable(new DataTableModel(model, cols));
    this.fixedTable = new JTable(new FixedTableModel(model, cols));

    final ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
    this.fixedTable.setSelectionModel(listSelectionModel);
    this.fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    updateTableWidth(preferredFixedWidth);

    this.dataTable.setSelectionModel(listSelectionModel);
    this.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    final JViewport viewport = new JViewport();
    viewport.setView(fixedTable);
    final Dimension preferredSize = fixedTable.getPreferredSize();
    preferredSize.width = Math.max (preferredSize.width, preferredFixedWidth);
    viewport.setPreferredSize(preferredSize);
    setRowHeaderView(viewport);

    final JTableHeader th =  fixedTable.getTableHeader();
    setCorner(JScrollPane.UPPER_LEFT_CORNER, th);

    if (dataTable.getColumnCount() > 0)
    {
      setViewportView(dataTable);
    }
    else
    {
      setViewportView(new JPanel());
      setColumnHeaderView(new JLabel(" "));
    }
  }

  public int getFixedColumns ()
  {
    return fixedColumns;
  }

  public JTable getDataTable ()
  {
    return dataTable;
  }

  public JTable getFixedTable ()
  {
    return fixedTable;
  }

  public int getSelectedRow ()
  {
    return dataTable.getSelectedRow();
  }

  public int getSelectedRowCount ()
  {
    return dataTable.getSelectedRowCount();
  }

  public int[] getSelectedRows ()
  {
    return dataTable.getSelectedRows();
  }

  private void updateTableWidth (final int totalWidth)
  {
    final int width = totalWidth / fixedTable.getColumnCount();
    final int extraWidth = totalWidth - (width * fixedTable.getColumnCount());
    for (int i = 0; i < fixedTable.getColumnCount(); i++)
    {
      final TableColumn column = fixedTable.getColumnModel().getColumn(i);
      if (i == (fixedTable.getColumnCount() - 1))
      {
        column.setMinWidth(width + extraWidth);
      }
      else
      {
        column.setMinWidth(width);
      }
    }
  }

}
