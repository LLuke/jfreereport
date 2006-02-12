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
 * HtmlSheetLayout.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlSheetLayout.java,v 1.15 2006/02/09 22:04:50 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.GenericObjectTable;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.html.metaelements.HtmlTextMetaElement;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.ObjectUtilities;

/**
 * The Html Sheet layout collects the CSS-information for the generated cells of a single
 * page. This class will transform the element style into a HTML Cascading Stylesheet
 * definition.
 * <p/>
 * Currently, one global stylesheet is created for all tables.
 */
public class HtmlSheetLayout extends SheetLayout
{
  private HtmlStyleCollection styleCollection;
  private TableRectangle rectangle;

  /**
   * A table of style names; these names are the internal names.
   */
  private GenericObjectTable backgroundStyleTable;
  private GenericObjectTable contentStyleTable;
  private GenericObjectTable verticalAlignmentTable;
  private boolean tableRowBorderDefinition;

  public HtmlSheetLayout (final boolean strict,
                          final HtmlStyleCollection collection,
                          final boolean tableRowBorderDefinition)
  {
    super(strict);
    if (collection == null)
    {
      throw new NullPointerException();
    }
    styleCollection = collection;
    rectangle = new TableRectangle();
    contentStyleTable = new GenericObjectTable(20, 5);
    backgroundStyleTable = new GenericObjectTable(20, 5);
    verticalAlignmentTable = new GenericObjectTable(20, 5);
    this.tableRowBorderDefinition = tableRowBorderDefinition;
  }


  /**
   * Adds the bounds of the given TableCellData to the grid. The bounds given must be the
   * same as the bounds of the element, or the layouting might produce surprising
   * results.
   *
   * @param element the position that should be added to the grid (might be null).
   * @throws NullPointerException if the bounds are null
   */
  public void add (final MetaElement element)
  {
    super.add(element);
    if (element instanceof TableCellBackground)
    {
      return;
    }
    if (element instanceof HtmlTextMetaElement)
    {
      addStringContentStyle(element);
    }
    // all other elements have no effect on the CSS-definitions
    // or are not yet cachable
//    else
//    {
//      Log.debug ("Ignoring content of " + element.getContent() +
//              "(" + element.getName() + ")");
//    }
  }

  private void addStringContentStyle (final MetaElement element)
  {
    final FontDefinition font = element.getFontDefinitionProperty();
    final Color color = (Color) element.getProperty(ElementStyleSheet.PAINT);
    final ElementAlignment halign
            = (ElementAlignment) element.getProperty(ElementStyleSheet.ALIGNMENT);
    final ElementAlignment valign
            = (ElementAlignment) element.getProperty(ElementStyleSheet.VALIGNMENT);
    final HtmlContentStyle style =
            new HtmlContentStyle(font, color, halign);
    final String styleName = styleCollection.addContentStyle(style);

    final TableRectangle rect = getTableBounds(element, rectangle);
    for (int y = rect.getY1(); y < rect.getY2(); y++)
    {
      final int row = mapRow(y);
      for (int x = rect.getX1(); x < rect.getX2(); x++)
      {
        final int column = mapColumn(x);
        if (contentStyleTable.getObject(row, column) == null)
        {
          contentStyleTable.setObject(row, column, styleName);
          verticalAlignmentTable.setObject(row, column, valign);
        }
      }
    }
  }

  protected void columnInserted (final long coordinate, final int oldColumn,
                                 final int newColumn)
  {
    super.columnInserted(coordinate, oldColumn, newColumn);
    contentStyleTable.copyColumn(oldColumn, newColumn);
    backgroundStyleTable.copyColumn(oldColumn, newColumn);
    verticalAlignmentTable.copyColumn(oldColumn, newColumn);
  }

  protected void rowInserted (final long coordinate, final int oldRow, final int newRow)
  {
    super.rowInserted(coordinate, oldRow, newRow);
    contentStyleTable.copyRow(oldRow, newRow);
    backgroundStyleTable.copyRow(oldRow, newRow);
    verticalAlignmentTable.copyRow(oldRow, newRow);
  }

  /**
   * A Callback method to inform the sheet layout, that the current page is complete, and
   * no more content will be added.
   * <p/>
   * This computes the row styles.
   */
  public void pageCompleted ()
  {
    removeAuxilaryBounds();

    final Long[] yCuts = getYCuts();
    if (yCuts.length == 0)
    {
      clearObjectIdTable();
      return;
    }

    // Process all elements; duplicate entries will not be processed twice
    // Spanned elements are only stored in their upper left corner, as all
    // other cells will be skipped anyway ..

    final int rowCount = getRowCount();
    final int columnCount = getColumnCount();

    TableRectangle rect = null;
    for (int layoutRow = 0; layoutRow < rowCount; layoutRow++)
    {
      Color rowColor = null;
      Color borderTop = null;
      Color borderBottom = null;
      Stroke borderTopSize = null;
      Stroke borderBottomSize = null;

      for (int layoutCol = 0; layoutCol < columnCount; layoutCol++)
      {
        final TableCellBackground bg;
        final CellReference reference = getContentAt(layoutRow, layoutCol);
        if (reference != null)
        {
          // it's a spanning cell - we have to do a bit more than usual ..
          rect = getTableBounds(reference.getBounds(), rect);
          if (rect.getColumnSpan() == 1 && rect.getRowSpan() == 1)
          {
            bg = getElementAt(layoutRow, layoutCol);
          }
          else
          {
            bg = getRegionBackground(rect);
          }
        }
        else
        {
          bg = getElementAt(layoutRow, layoutCol);
        }

        final ElementAlignment verticalAlignment =
                getVerticalAlignmentAt(layoutRow, layoutCol);

        if (bg == null)
        {
          // there is no background
          // that also means, that there is no color and no border defined,
          // therefore reset the whole thing!
          rowColor = null;
          borderTop = null;
          borderTopSize = null;
          borderBottom = null;
          borderBottomSize = null;
        }
        else
        {
          final Color bgColor = bg.getColor();
          if (layoutCol == 0)
          {
            // if this is the first column, initialize the row background ..
            rowColor = bgColor;
            borderTopSize = bg.getBorderStrokeTop();
            borderTop = bg.getColorTop();
            borderBottom = bg.getColorBottom();
            borderBottomSize = bg.getBorderStrokeBottom();
          }
          else
          {
            // on all other columns: Check whether that thing is still valid ..

            if (ObjectUtilities.equal(bgColor, rowColor) == false)
            {
              // no common color ... therefore reset ..
              rowColor = null;
            }
            if (ObjectUtilities.equal(borderTop, bg.getColorTop()) == false)
            {
              borderTop = null;
              borderTopSize = null;
            }
            if (ObjectUtilities.equal(borderBottom, bg.getColorBottom()) == false)
            {
              borderBottom = null;
              borderBottomSize = null;
            }
          }
        }

        final HtmlTableCellStyle style =
                new HtmlTableCellStyle(bg, verticalAlignment);

        final String styleName = styleCollection.addCellStyle(style);
        backgroundStyleTable.setObject(layoutRow, layoutCol, styleName);
      }

      final int height = (int) Math.ceil
              (StrictGeomUtility.toExternalValue(getRowHeight(layoutRow)));
      final HtmlTableRowStyle style = new HtmlTableRowStyle
              (height, rowColor, tableRowBorderDefinition);
      style.setBorderTop(borderTop, borderTopSize);
      style.setBorderBottom(borderBottom, borderBottomSize);
      styleCollection.addRowStyle(style);
    }
    clearObjectIdTable();
    verticalAlignmentTable.clear();
  }

  protected ElementAlignment getVerticalAlignmentAt(final int row, final int column)
  {
    final int mrow = mapRow(row);
    final int mcolumn = mapColumn(column);
    final ElementAlignment ea = (ElementAlignment)
            verticalAlignmentTable.getObject(mrow, mcolumn);
    if (ea == null)
    {
      return ElementAlignment.LEFT;
    }
    return ea;
  }

  /**
   * Returns true, if the table row contains border definitions. This is a
   * workaround for a 'renderer weakness' of the mozilla browser family.
   *
   * @return true, if table rows might contain border definitions, false otherwise.
   */
  public boolean isTableRowBorderDefinition ()
  {
    return tableRowBorderDefinition;
  }

  /**
   * Returns the name of the style assigned to the given cell position. The style itself
   * is stored in the stylecollection.
   *
   * @param row
   * @param column
   * @return
   */
  public String getContentStyleAt (final int row, final int column)
  {
    return (String) contentStyleTable.getObject(mapRow(row), mapColumn(column));
  }

  /**
   * Returns the name of the style assigned to the given cell position. The style itself
   * is storef in the stylecollection.
   *
   * @param row
   * @param column
   * @return
   */
  public String getBackgroundStyleAt (final int row, final int column)
  {
    // no mapping necessary, as the background table is filled at the
    // end-of-the-page
    return (String) backgroundStyleTable.getObject(row, column);
  }

  public HtmlStyleCollection getStyleCollection ()
  {
    return styleCollection;
  }
}
