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
 * -----------------------
 * RTFCellDataFactory.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFCellDataFactory.java,v 1.4 2003/07/20 19:31:15 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package org.jfree.report.modules.output.table.rtf;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.output.support.itext.BaseFontCreateException;
import org.jfree.report.modules.output.support.itext.BaseFontRecord;
import org.jfree.report.modules.output.support.itext.BaseFontSupport;
import org.jfree.report.modules.output.table.base.AbstractTableCellDataFactory;
import org.jfree.report.modules.output.table.base.TableCellData;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;

/**
 * Creates a {@link TableCellData} object from the given {@link Element}
 * and the element's bounds. The factory handles text and image elements,
 * shape elements get converted into background information if possible.
 *
 * @author Thomas Morgner
 */
public class RTFCellDataFactory extends AbstractTableCellDataFactory
{
  /** The baseFontSupport is used to handle truetype fonts in iText. */
  private BaseFontSupport baseFontSupport;

  public RTFCellDataFactory(BaseFontSupport baseFontSupport)
  {
    this.baseFontSupport = baseFontSupport;
  }

  /**
   * Creates a {@link TableCellData} for an {@link Element}. The generated CellData
   * should contain copies of all needed element attributes, as the element instance
   * will be reused in the later report processing.
   * <p>
   * If the tablemodel does not support the element type, return <code>null</code>.
   *
   * @param e  the element that should be converted into TableCellData.
   * @param rect  the element's bounds within the table (specified in points).
   *
   * @return The generated TableCellData object, or <code>null</code> if element type is
   *         not supported.
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
    final ElementAlignment valign =
        (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    final ElementAlignment halign =
        (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);
    /**
     * Images cause OutOfMemoryError so they get removed ...
     */
/*
    if (value instanceof ImageReference)
    {
      RTFCellStyle style = new RTFCellStyle(valign, halign);
      return new RTFImageCellData(rect, (ImageReference) value, style);
    }
*/

    if (value instanceof String)
    {
      try
      {
        final BaseFontRecord bf = baseFontSupport.createBaseFont(font, "Cp1252", false);
        final RTFTextCellStyle style = new RTFTextCellStyle
            (font, bf.getBaseFont(), color, valign, halign);
        return new RTFTextCellData(rect, (String) value, style);
      }
      catch (BaseFontCreateException ex)
      {
        Log.debug("Unable to create font: ", ex);
        return null;
      }
    }

    if (value instanceof Shape)
    {
      // check backgrounds
      return createBackground(e, (Shape) value, rect);
    }

    return null;
  }
}
