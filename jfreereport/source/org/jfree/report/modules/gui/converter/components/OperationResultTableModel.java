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
 * ------------------------------
 * OperationResultTableModel.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: OperationResultTableModel.java,v 1.3 2003/09/10 18:20:25 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 25-Aug-2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.converter.components;

import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

import org.jfree.report.modules.gui.converter.resources.ConverterResources;
import org.jfree.report.modules.parser.base.OperationResult;

/**
 * The operation result tablemodel is used to display the parser and converter
 * results to the user.
 * 
 * @author Thomas Morgner
 */
public class OperationResultTableModel extends AbstractTableModel
{
  /** An internal column mapping. */
  private static final int COLUMN_SEVERITY = 0;
  /** An internal column mapping. */
  private static final int COLUMN_MESSAGE = 1;
  /** An internal column mapping. */
  private static final int COLUMN_LINE = 2;
  /** An internal column mapping. */
  private static final int COLUMN_COLUMN = 3;

  /** The operation results are read from the parser. */
  private OperationResult[] data;
  /** The resource bundle used to translate the column names. */
  private final ResourceBundle resources;

  /** The column name keys for the resource bundle. */
  private static final String[] COLUMN_NAMES =
      {
        "ResultTableModel.Severity",
        "ResultTableModel.Message",
        "ResultTableModel.Line",
        "ResultTableModel.Column"
      };

  /**
   * Creates a new and initially empty operation result table model.
   */
  public OperationResultTableModel()
  {
    this(new OperationResult[0]);
  }

  /**
   * Creates a new operation result table model which will be filled with
   * the given data.
   * 
   * @param data the operation result objects from the parser or writer.
   */
  public OperationResultTableModel(OperationResult[] data)
  {
    this.resources = ResourceBundle.getBundle(ConverterResources.class.getName());
    setData(data);
  }

  /**
   * Sets the data for the tablemodel.
   * 
   * @param data the data.
   */
  public void setData (OperationResult[] data)
  {
    if (data == null)
    {
      throw new NullPointerException();
    }
    if (data.length == 0)
    {
      this.data = new OperationResult[0];
    }
    else
    {
      this.data = new OperationResult[data.length];
      System.arraycopy(data, 0, this.data, 0, data.length);
      fireTableDataChanged();
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
    return data.length;
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
   * Returns the value for the cell at <code>columnIndex</code> and
   * <code>rowIndex</code>.
   *
   * @param rowIndex the row whose value is to be queried
   * @param columnIndex the column whose value is to be queried
   * @return the value Object at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    switch (columnIndex)
    {
      case COLUMN_SEVERITY: return data[rowIndex].getSeverity();
      case COLUMN_MESSAGE: return (data[rowIndex].getMessage());
      case COLUMN_LINE: return new Integer(data[rowIndex].getLine());
      case COLUMN_COLUMN: return new Integer(data[rowIndex].getColumn());
      default: throw new IndexOutOfBoundsException("The column index is invalid");
    }
  }

  /**
   *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
   *
   *  @param columnIndex  the column being queried
   *  @return the Object.class
   */
  public Class getColumnClass(int columnIndex)
  {
    switch (columnIndex)
    {
      case COLUMN_SEVERITY: return Object.class;
      case COLUMN_MESSAGE: return String.class;
      case COLUMN_LINE: return Integer.class;
      case COLUMN_COLUMN: return Integer.class;
      default: throw new IndexOutOfBoundsException("The column index is invalid");
    }
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
    return resources.getString(COLUMN_NAMES[column]);
  }
}
