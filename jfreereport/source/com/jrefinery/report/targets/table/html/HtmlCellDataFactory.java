/**
 * Date: Jan 18, 2003
 * Time: 8:01:24 PM
 *
 * $Id: HtmlCellDataFactory.java,v 1.2 2003/01/25 02:47:10 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.StringUtil;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class HtmlCellDataFactory implements TableCellDataFactory
{
  private HtmlStyleCollection styleCollection;

  public HtmlCellDataFactory(HtmlStyleCollection styleCollection)
  {
    this.styleCollection = styleCollection;
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
      styleCollection.addStyle(style, StringUtil.encodeCSS(e.getName()));
      return new HtmlImageCellData(rect, (ImageReference) value, style);
    }
    if (value instanceof String)
    {
      HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
      styleCollection.addStyle(style, StringUtil.encodeCSS(e.getName()));
      return new HtmlTextCellData(rect, (String) value, style);
    }
    Log.debug ("Element " + e + " ignored");
    return null;
  }
}
