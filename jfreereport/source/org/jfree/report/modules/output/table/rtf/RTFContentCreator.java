/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * RTFContentCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: RTFContentCreator.java,v 1.1 2004/03/16 16:03:37 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 14, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.rtf;

import java.awt.Color;
import java.io.OutputStream;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.GenericObjectTable;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.SheetLayoutCollection;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableContentCreator;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.rtf.metaelements.RTFMetaElement;
import org.jfree.report.util.NoCloseOutputStream;

public class RTFContentCreator extends TableContentCreator
{
  private boolean open;
  private Document document;
  private OutputStream outputStream;

  public RTFContentCreator (final SheetLayoutCollection sheetLayoutCollection,
                            final OutputStream outputStream)
  {
    super(sheetLayoutCollection);
    this.outputStream = outputStream;
  }

  protected void handleBeginTable (final ReportDefinition reportDefinition)
  {
    // we do nothing here ...
  }

  protected void handleClose ()
  {
    open = false;
    document.close();
  }

  protected void handleEndTable () throws ReportProcessingException
  {
    final SheetLayout layout = getCurrentLayout();
    if (layout.isEmpty())
    {
      // the table is empty
      return;
    }

    try
    {
      final GenericObjectTable go = getBackend();
      final int height = go.getRowCount();
      final int width = go.getColumnCount();

      final Table table = new Table(width, height);
      table.setAutoFillEmptyCells(false);

      for (int y = 0; y < height; y++)
      {
        // there is no need to create rows, this is handled automaticly...
        for (int x = 0; x < width; x++)
        {
          final TableCellBackground background = layout.getElementAt(y, x);
          final MetaElement element = (MetaElement) go.getObject(y, x);

          if (element == null)
          {
            final Cell cell = new Cell();
            cell.setBorderWidth(0);
            cell.setWidth(String.valueOf (layout.getCellWidth(x, x + 1)));

            if (background != null)
            {
              // iText cell width is a string, why?
              setCellBackgroundStyle(cell, background);
            }

            table.addCell(cell, y, x);
            continue;
          }

          final TableRectangle rectangle =
                  layout.getTableBounds(element, getLookupRectangle());
          if (rectangle.isOrigin(x, y) == false)
          {
            // this is a spanned cell - ignore it completly
            continue;
          }

          final RTFMetaElement cellData = (RTFMetaElement) element;
          final Cell cell = cellData.getCell();
          cell.setBorderWidth(0);

          if (background != null)
          {
            setCellBackgroundStyle(cell, background);
          }

          if (rectangle.getRowSpan() > 1)
          {
            cell.setRowspan(rectangle.getRowSpan());
          }
          if (rectangle.getColumnSpan() > 1)
          {
            cell.setColspan(rectangle.getColumnSpan());
          }

          table.addCell(cell, y, x);
          x += rectangle.getColumnSpan() - 1;
        }
      }

      document.add(table);
    }
    catch(BadElementException be)
    {
      throw new ReportProcessingException("Failed!", be);
    }
    catch(DocumentException be)
    {
      throw new ReportProcessingException("Failed!", be);
    }

  }

  /**
   * Defines the cell background style for the given cell. The cell background
   * is defined in the background list.
   *
   * @param cell the cell that should be defined.
   * @param bg the background definition for the cell.
   */
  private void setCellBackgroundStyle(final Cell cell,
                                      final TableCellBackground bg)
  {
    if (bg == null)
    {
      return;
    }
    final Color color = bg.getColor();
    if (color != null)
    {
      cell.setBackgroundColor(color);
    }

    // Cell Borders are only added if all borders are defined and equal,
    // as iText-RTF does not support different borders ...
    final Color bT = bg.getColorTop();
    final Color bB = bg.getColorBottom();
    final Color bL = bg.getColorLeft();
    final Color bR = bg.getColorRight();
    if (bT == null || bB == null || bL == null || bR == null)
    {
      return;
    }
    if (bT.equals(bB) && bT.equals(bL) && bT.equals(bR)
        && bg.getBorderSizeBottom() == bg.getBorderSizeTop()
        && bg.getBorderSizeBottom() == bg.getBorderSizeLeft()
        && bg.getBorderSizeBottom() == bg.getBorderSizeRight())
    {
      cell.setBorderColor(bT);
      cell.setBorderWidth(bg.getBorderSizeTop());
    }
  }

  protected void handleOpen (final ReportDefinition reportDefinition)
  {
    // rtf does not support PageFormats or other meta data...
    document = new Document();
    RtfWriter.getInstance(document, new NoCloseOutputStream(outputStream));
    document.open();
    open = true;
  }

  /**
   * Commits all bands. See the class description for details on the flushing process.
   *
   * @return always false, as we generate the layout when the table is
   * closed.
   */
  public boolean flush ()
  {
    return false;
  }

  /**
   * Checks, whether the report processing has started.
   *
   * @return true, if the report is open, false otherwise.
   */
  public boolean isOpen ()
  {
    return open;
  }
}
