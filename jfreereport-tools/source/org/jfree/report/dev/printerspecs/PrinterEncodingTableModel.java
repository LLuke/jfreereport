package org.jfree.report.dev.printerspecs;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

import org.jfree.report.modules.output.pageable.plaintext.PrinterEncoding;

public class PrinterEncodingTableModel extends AbstractTableModel
{
  private static final int NAMES_PREFIX = 1;

  private ArrayList encodings;
  private ArrayList mappers;

  public PrinterEncodingTableModel ()
  {
    this.mappers = new ArrayList();
    this.encodings = new ArrayList();
  }

  public void addPrinter (final ModifiablePrinterSpecification encodingMapper)
  {
    mappers.add (encodingMapper);
    fireTableStructureChanged();
  }

  public void addEncoding (final PrinterEncoding encoding)
  {
    encodings.add (encoding);
    fireTableDataChanged();
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
    return mappers.size() + NAMES_PREFIX;
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
    return encodings.size();
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
      return "Encoding";
    }

    final ModifiablePrinterSpecification pspec =
            (ModifiablePrinterSpecification) mappers.get(column - NAMES_PREFIX);
    return pspec.getName();
//    final PrinterEncoding enc = (PrinterEncoding) encodings.get(column - NAMES_PREFIX);
//    return enc.getDisplayName();
  }

  /**
   * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
   *
   * @param columnIndex the column being queried
   * @return the Object.class
   */
  public Class getColumnClass (final int columnIndex)
  {
    if (columnIndex == 0)
    {
      return String.class;
    }
    return Boolean.class;
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
    if (columnIndex == 0)
    {
      return false;
    }
    return true;
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
    // first column holds the encoding names (display names)
    if (columnIndex == 0)
    {
      final PrinterEncoding encoding =
              (PrinterEncoding) encodings.get(rowIndex);
      return encoding.getDisplayName();
    }

    final ModifiablePrinterSpecification mapper =
            (ModifiablePrinterSpecification) mappers.get(columnIndex - NAMES_PREFIX);
    final PrinterEncoding enc = (PrinterEncoding) encodings.get(rowIndex);
    return new Boolean(mapper.contains(enc));
  }

  public synchronized void clear ()
  {
    encodings.clear();
    mappers.clear();
    fireTableStructureChanged();
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
    // first column holds the encoding names (display names) which are unmodifiable
    if (columnIndex == 0)
    {
      return;
    }

    final ModifiablePrinterSpecification mapper =
            (ModifiablePrinterSpecification) mappers.get(columnIndex - NAMES_PREFIX);

    final PrinterEncoding enc = (PrinterEncoding) encodings.get(rowIndex);
    if (Boolean.TRUE.equals(aValue))
    {
      mapper.addEncoding(enc);
    }
    else
    {
      mapper.removeEncoding(enc);
    }
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  public void removePrinter (final int selectedColumn)
  {
    mappers.remove(selectedColumn);
    fireTableStructureChanged();
  }

  public ModifiablePrinterSpecification getPrinter (final int index)
  {
    return (ModifiablePrinterSpecification) mappers.get(index);
  }

  public int getPrinterCount ()
  {
    return mappers.size();
  }

  public PrinterEncoding getEncoding (final int index)
  {
    return (PrinterEncoding) encodings.get(index);
  }

  public int getEncodingCount ()
  {
    return encodings.size();
  }
}
