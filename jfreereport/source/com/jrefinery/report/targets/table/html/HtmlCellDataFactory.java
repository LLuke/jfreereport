/**
 * Date: Jan 18, 2003
 * Time: 8:01:24 PM
 *
 * $Id: HtmlCellDataFactory.java,v 1.3 2003/01/25 20:34:12 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class HtmlCellDataFactory implements TableCellDataFactory
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
    Log.debug ("Element " + e + " ignored");
    return null;
  }
}
