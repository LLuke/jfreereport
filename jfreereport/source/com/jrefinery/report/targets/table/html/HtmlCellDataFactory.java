/**
 * Date: Jan 18, 2003
 * Time: 8:01:24 PM
 *
 * $Id: HtmlCellDataFactory.java,v 1.1 2003/01/18 20:47:36 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class HtmlCellDataFactory implements TableCellDataFactory
{
  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    Object value = e.getValue();

    FontDefinition font = e.getStyle().getFontDefinitionProperty();
    Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ElementAlignment valign = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    ElementAlignment halign = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);
    HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
    return new HtmlCellData(rect,value, style);
  }
}
