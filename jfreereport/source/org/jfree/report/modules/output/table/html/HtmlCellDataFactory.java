/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------
 * HtmlCellDataFactory.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlCellDataFactory.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package org.jfree.report.modules.output.table.html;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ImageReference;
import org.jfree.report.modules.output.table.base.AbstractTableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableCellData;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;

/**
 * The cell data factory is responsible for converting elements into
 * HTML cell data. The element style is converted using an external
 * style factory. This factory reuses previously defined styles if
 * possible, to increase the file creating efficiency.
 *
 * @author Thomas Morgner
 */
public class HtmlCellDataFactory extends AbstractTableCellDataFactory
{
  /** The StyleCollection stores all previously created CellStyles for later reuse. */
  private HtmlStyleCollection styleCollection;
  /** A flag indicating whether to use XHTML. */
  private final boolean useXHTML;

  /**
   * Creates a new HTMLCellDataFactory, using the given StyleCollection to store
   * the generated Styles.
   *
   * @param styleCollection the used style collection.
   * @param useXHTML a flag indicating whether to generate XHTML.
   */
  public HtmlCellDataFactory(final HtmlStyleCollection styleCollection, final boolean useXHTML)
  {
    if (styleCollection == null)
    {
      throw new NullPointerException();
    }
    this.styleCollection = styleCollection;
    this.useXHTML = useXHTML;
  }

  /**
   * Creates the TableCellData for the given Element. The generated CellData
   * should contain copies of all needed element attributes, as the element instance
   * will be reused in the later report processing.
   * <p>
   * If the tablemodel does not support the element type, return null.
   * <p>
   * This implementation handles Shapes as backgrounds, Images and String contents.
   *
   * @param e the element that should be converted into TableCellData.
   * @param rect the elements bounds within the table. The bounds are specified
   * in points.
   * @return null if element type is not supported or the generated TableCellData object.
   */
  public TableCellData createCellData(final Element e, final Rectangle2D rect)
  {
    if (e.isVisible() == false)
    {
      return null;
    }

    if (e instanceof Band)
    {
      return createBandCell(rect);
    }

    final Object value = e.getValue();

    final FontDefinition font = e.getStyle().getFontDefinitionProperty();
    final Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    final ElementAlignment valign
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    final ElementAlignment halign
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);

    if (value instanceof ImageReference)
    {
      final HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
      styleCollection.addStyle(style);
      return new HtmlImageCellData(rect, (ImageReference) value, style, useXHTML);
    }

    if (value instanceof String)
    {
      final HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
      styleCollection.addStyle(style);
      return new HtmlTextCellData(rect, (String) value, style, useXHTML);
    }

    if (value instanceof Shape)
    {
      // check backgrounds
      return createBackground(e, (Shape) value, rect);
    }

    //Log.debug ("Element " + e + " ignored");
    return null;
  }

}
