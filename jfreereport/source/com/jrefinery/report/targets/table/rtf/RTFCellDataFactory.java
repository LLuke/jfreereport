/**
 * Date: Feb 1, 2003
 * Time: 7:52:20 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table.rtf;

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

public class RTFCellDataFactory extends AbstractTableCellDataFactory
{
  public RTFCellDataFactory()
  {
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
      RTFCellStyle style = new RTFCellStyle(font, color, valign, halign);
      return new RTFImageCellData(rect, (ImageReference) value, style);
    }

    if (value instanceof String)
    {
      RTFCellStyle style = new RTFCellStyle(font, color, valign, halign);
      return new RTFTextCellData(rect, (String) value, style);
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
