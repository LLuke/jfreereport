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
 * ------------------------
 * HtmlCellDataFactory.java
 * ------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlCellDataFactory.java,v 1.13 2003/05/14 22:26:40 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.AbstractTableCellDataFactory;
import com.jrefinery.report.targets.table.TableCellData;

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
  public HtmlCellDataFactory(HtmlStyleCollection styleCollection, boolean useXHTML)
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
  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    if (e.isVisible() == false)
    {
      return null;
    }

    if (e instanceof Band)
    {
      return createBandCell(rect);
    }

    Object value = e.getValue();

    FontDefinition font = e.getStyle().getFontDefinitionProperty();
    Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ElementAlignment valign
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    ElementAlignment halign
        = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);

    if (value instanceof ImageReference)
    {
      HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
      styleCollection.addStyle(style);
      return new HtmlImageCellData(rect, (ImageReference) value, style, useXHTML);
    }

    if (value instanceof String)
    {
      HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
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
