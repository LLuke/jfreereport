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
 * $Id: RTFCellDataFactory.java,v 1.9 2003/06/26 19:55:57 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package com.jrefinery.report.targets.table.rtf;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.support.itext.BaseFontRecord;
import com.jrefinery.report.targets.support.itext.BaseFontSupport;
import com.jrefinery.report.targets.table.AbstractTableCellDataFactory;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.util.Log;

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

  /** Default Constructor. */
  public RTFCellDataFactory()
  {
    baseFontSupport = new BaseFontSupport();
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
    ElementAlignment valign =
        (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    ElementAlignment halign =
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
        BaseFontRecord bf = baseFontSupport.createBaseFont(font, "Cp1252", false);
        RTFTextCellStyle style = new RTFTextCellStyle
            (font, bf.getBaseFont(), color, valign, halign);
        return new RTFTextCellData(rect, (String) value, style);
      }
      catch (OutputTargetException ex)
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
