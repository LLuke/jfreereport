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
 * $Id: HtmlSheetLayout.java,v 1.1 2004/03/16 18:03:37 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;

import org.jfree.report.ElementAlignment;
import org.jfree.report.content.ContentType;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.TableCellBackground;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.base.GenericObjectTable;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;

/**
 * The Html Sheet layout collects the CSS-information for the generated
 * cells of a single page. This class will transform the element style
 * into a HTML Cascading Stylesheet definition.
 * <p>
 * Currently, one global stylesheet is created for all tables.
 */
public strictfp class HtmlSheetLayout extends SheetLayout
{
  private HtmlStyleCollection styleCollection;
  private TableRectangle rectangle;
  private GenericObjectTable backgroundStyleTable;
  private GenericObjectTable contentStyleTable;

  public HtmlSheetLayout (final boolean strict, final HtmlStyleCollection collection)
  {
    super(strict);
    if (collection == null)
    {
      throw new NullPointerException();
    }
    styleCollection = collection;
    rectangle = new TableRectangle();
    contentStyleTable = new GenericObjectTable();
    backgroundStyleTable = new GenericObjectTable();
  }

  /**
   * Adds the bounds of the given TableCellData to the grid. The bounds given must be the same as the bounds of the
   * element, or the layouting might produce surprising results.
   *
   * @param element the position that should be added to the grid (might be null).
   * @throws NullPointerException if the bounds are null
   */
  public void add (final MetaElement element)
  {
    super.add(element);
    if (element instanceof TableCellBackground)
    {
      final int width = (int) element.getBounds().getWidth();
      final TableRectangle rect = getTableBounds(element, rectangle);
      final HtmlTableCellStyle style =
              new HtmlTableCellStyle((TableCellBackground) element, width);
      styleCollection.addCellStyle(style);
      final String styleName = style.getName();
      for (int y = rect.getY1(); y < rect.getY2(); y++)
      {
        for (int x = rect.getX1(); x < rect.getX2(); x++)
        {
          backgroundStyleTable.setObject(y, x, styleName);
        }
      }
    }
    else if (element.getContent().getContentType().equals(ContentType.TEXT))
    {
      final FontDefinition font = element.getFontDefinitionProperty();
      final Color color = (Color) element.getProperty(ElementStyleSheet.PAINT);
      final ElementAlignment valign
          = (ElementAlignment) element.getProperty(ElementStyleSheet.VALIGNMENT);
      final ElementAlignment halign
          = (ElementAlignment) element.getProperty(ElementStyleSheet.ALIGNMENT);

      final HtmlContentStyle style =
              new HtmlContentStyle(font, color, valign, halign);
      final String styleName = style.getName();
      styleCollection.addContentStyle(style);

      final TableRectangle rect = getTableBounds(element, rectangle);
      for (int y = rect.getY1(); y < rect.getY2(); y++)
      {
        for (int x = rect.getX1(); x < rect.getX2(); x++)
        {
          contentStyleTable.setObject(y, x, styleName);
        }
      }
    }
    // all other elements have no effect on the CSS-definitions
    // or are not yet cachable
  }

  /**
   * A Callback method to inform the sheet layout, that the current page
   * is complete, and no more content will be added.
   * <p>
   * This computes the row styles.
   */
  public void pageCompleted ()
  {
    final Integer[] yCuts = getYCuts();
    if (yCuts.length == 0)
    {
      return;
    }
    float beginRow = yCuts[0].floatValue();
    for (int i = 1; i < yCuts.length; i++)
    {
      final float end = yCuts[i].floatValue();
      styleCollection.addRowStyle(new HtmlTableRowStyle((int) (end - beginRow)));
      beginRow = end;
    }
  }

  /**
   * Returns the name of the style assigned to the given cell
   * position. The style itself is storef in the stylecollection.
   *
   * @param row
   * @param column
   * @return
   */
  public String getContentStyleAt (final int row, final int column)
  {
    return (String) contentStyleTable.getObject(row, column);
  }

  /**
   * Returns the name of the style assigned to the given cell
   * position. The style itself is storef in the stylecollection.
   *
   * @param row
   * @param column
   * @return
   */
  public String getBackgroundStyleAt (final int row, final int column)
  {
    return (String) backgroundStyleTable.getObject(row, column);
  }

  public HtmlStyleCollection getStyleCollection ()
  {
    return styleCollection;
  }
}