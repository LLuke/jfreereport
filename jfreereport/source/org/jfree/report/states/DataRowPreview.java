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
 * -------------------
 * DataRowPreview.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DataRowPreview.java,v 1.3 2003/08/22 20:27:17 taqua Exp $
 *
 * Changes
 * -------
 * 06.04.2003 : Initial version
 *
 */
package org.jfree.report.states;

import javax.swing.table.TableModel;

import org.jfree.report.function.LevelledExpressionList;
import org.jfree.report.util.ReportPropertiesList;
import org.jfree.report.states.DataRowBackend;

/**
 * A 'preview' data row backend. Shows how the next row would look like if there
 * were no events thrown. This class is used to calculate the differences between
 * two states. As function columns as ReportEventListeners are dependent on an
 * valid state, these columns cannot be queried using this class. A query on such
 * an column will throw an InvalidStateException.
 *
 * @author Thomas Morgner
 */
public final class DataRowPreview extends DataRowBackend
{
  /**
   * Constructs a new DataRowPreview using the given DataRowBackend as base.
   *
   * @param db  the base.
   */
  public DataRowPreview(final DataRowBackend db)
  {
    super(db);
    if (db.getFunctions() != null)
    {
      LevelledExpressionList le = db.getFunctions().getPreviewInstance();
      super.setFunctions(le);
    }
  }

  /**
   * Updates this instance to be a preview of the given backend.
   * @param db the row.
   */
  public void update(final DataRowBackend db)
  {
    super.setCurrentRow(db.getCurrentRow() + 1);
  }

  /**
   * Sets the current row of the tablemodel. The current row is advanced while the Report is being
   * processed. This is a readonly implementation and will always throw an IllegalStateException
   *
   * @param currentRow the current row
   * @throws java.lang.IllegalStateException as this is a readonly implementation
   */
  public final void setCurrentRow(final int currentRow)
  {
    throw new IllegalStateException("This is a preview, not changable");
  }

  /**
   * Sets the function collection used in this DataRow. As the function collection is statefull,
   * a new instance of the function collection is set for every new ReportState.
   * This is a readonly implementation and will always throw an IllegalStateException
   *
   * @param functions the current function collection
   * @throws java.lang.IllegalStateException as this is a readonly implementation
   */
  public final void setFunctions(final LevelledExpressionList functions)
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
   * @throws java.lang.IllegalStateException as this is a readonly implementation
   */
  public final void setTablemodel(final TableModel tablemodel)
  {
    throw new IllegalStateException("This is a preview, not changable");
  }

  /**
   * Sets the report properties.
   *
   * @param properties  the report properties.
   */
  public final void setReportProperties(final ReportPropertiesList properties)
  {
    throw new IllegalStateException("This is a preview, not changable");
  }

  /**
   * Create a preview backend. Such datarows will have no access to functions (all functions
   * will return null).
   *
   * @return  The 'preview' DataRowBackend.
   */
  public final DataRowBackend previewNextRow()
  {
    throw new IllegalStateException("Is already a preview version!");
  }
}
