/**
 * Date: Jan 18, 2003
 * Time: 8:01:24 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.Element;

import java.awt.geom.Rectangle2D;
import java.awt.Font;
import java.awt.Color;

public class HtmlCellDataFactory implements TableCellDataFactory
{
  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    String value = String.valueOf(e.getValue());

    Font font = e.getStyle().getFontStyleProperty();
    Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    HtmlCellStyle style = new HtmlCellStyle(font, color);
    return new HtmlCellData(rect,value, style);
  }
}
