/**
 * Date: Jan 18, 2003
 * Time: 8:01:24 PM
 *
 * $Id: HtmlCellDataFactory.java,v 1.5 2003/01/27 18:24:54 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.Band;
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
