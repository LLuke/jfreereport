/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * DataRowPreview.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 06.04.2003 : Initial version
 */
package com.jrefinery.report;

import javax.swing.table.TableModel;

import com.jrefinery.report.function.LevelledExpressionList;

/**
 * A 'preview' data row backend. Shows how the next row would look like if there
 * were no events thrown. This class is used to calculate the differences between
 * two states. As function columns as ReportEventListeners are dependent on an
 * valid state, these columns cannot be queried using this class. A query on such
 * an column will throw an InvalidStateException.
 */
public class DataRowPreview extends DataRowBackend
{
  /**
   * Constructs a new DataRowPreview using the given DataRowBackend as base.
   *
   * @param db  the base.
   */
  public DataRowPreview(DataRowBackend db)
  {
    super(db);
  }

  /**
   * Defines the next row that should be previewed.
   * @param row the row.
   */
  public void setPreviewRow (int row)
  {
    super.setCurrentRow(row);
  }

  /**
   * Returns true to indicate that this is a 'preview' version of the DataRowBackEnd.
   *
   * @return true, as this is a preview version of a DataRowBackend, and not all functionality
   * may be available.
   */
  public boolean isPreviewMode()
  {
    return true;
  }

  /**
   * Sets the current row of the tablemodel. The current row is advanced while the Report is being
   * processed. This is a readonly implementation and will always throw an IllegalStateException
   *
   * @param currentRow the current row
   * @throws IllegalStateException as this is a readonly implementation
   */
  public void setCurrentRow(int currentRow)
  {
    throw new IllegalStateException("This is a preview, not changable");
  }

  /**
   * Sets the function collection used in this DataRow. As the function collection is statefull,
   * a new instance of the function collection is set for every new ReportState.
   * This is a readonly implementation and will always throw an IllegalStateException
   *
   * @param functions the current function collection
   * @throws IllegalStateException as this is a readonly implementation
   */
  public void setFunctions(LevelledExpressionList functions)
  {
    throw new IllegalStateException("This is a preview, not changable");
  }

  /**
   * Sets the tablemodel used in this DataRow. The tablemodel contains the base values for the
   * report and the currentRow-property contains a pointer to the current row within the
   * tablemodel. This is a readonly implementation and will always throw an
   * IllegalStateException.
   *
   * @param tablemodel the tablemodel used as base for the reporting.
   *
   * @throws IllegalStateException as this is a readonly implementation
   */
  public void setTablemodel(TableModel tablemodel)
  {
    throw new IllegalStateException("This is a preview, not changable");
  }

  /**
   * Create a preview backend. Such datarows will have no access to functions (all functions
   * will return null).
   *
   * @return  The 'preview' DataRowBackend.
   */
  public DataRowBackend previewNextRow()
  {
    throw new IllegalStateException("Is already a preview version!");
  }
}
