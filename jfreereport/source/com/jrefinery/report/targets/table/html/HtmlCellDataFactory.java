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
 * -------------------
 * HtmlCellDataFactory.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlCellDataFactory.java,v 1.7 2003/02/02 23:43:52 taqua Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.AbstractTableCellDataFactory;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class HtmlCellDataFactory extends AbstractTableCellDataFactory
{
  private HtmlStyleCollection styleCollection;
  private boolean useXHTML;

  public HtmlCellDataFactory(HtmlStyleCollection styleCollection, boolean useXHTML)
  {
    this.styleCollection = styleCollection;
    this.useXHTML = useXHTML;
  }

  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    if (e.isVisible() == false)
    {
      return null;
    }

    if (e instanceof Band)
    {
      return createBandCell(e, rect);
    }

    Object value = e.getValue();

    FontDefinition font = e.getStyle().getFontDefinitionProperty();
    Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ElementAlignment valign = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    ElementAlignment halign = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);

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

    Log.debug ("Element " + e + " ignored");
    return null;
  }

}
