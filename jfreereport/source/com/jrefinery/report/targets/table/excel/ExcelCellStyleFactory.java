
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
 * --------------------
 * ExcelCellStyleFactory.java
 * --------------------
 * (C)opyright 2002, by Hawesko GmbH & Co KG
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG)
 * Contributor(s):   -;
 * based on ideas and code from JRXlsExporter.java of JasperReports
 *
 * $Id: ExcelCellStyleFactory.java,v 1.3 2003/01/25 02:47:10 taqua Exp $
 *
 * Changes
 * -------
 * 06-Jan-2002 : initial version
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

/**
 * This class keeps track of the cell styles that we have used so far
 * @author Heiko Evermann
 */
public class ExcelCellStyleFactory
{
  /** White backgroud. Other backgrounds are not supported so far. */
  private static final short WHITE_INDEX = (new HSSFColor.WHITE()).getIndex();

  private HSSFDataFormat dataFormat;

	/** The list of fonts that we have used so far */
  private HSSFCellStyle emptyCellStyle;

  private HSSFWorkbook workbook;
  private Hashtable styleCache;
  private ExcelFontFactory fontFactory;

	/**
	 * Constructor for ExcelCellStyleFactory.
	 */
	public ExcelCellStyleFactory(HSSFWorkbook workbook)
  {
    if (workbook == null) throw new NullPointerException();
    this.workbook = workbook;
    this.styleCache = new Hashtable();
    this.fontFactory = new ExcelFontFactory(workbook);
	}

  private boolean isRectangleElement (Element e)
  {
    if (e.getContentType().startsWith("shape/") == false)
      return false;

    Object value = e.getValue();
    if (value == null)
      return false;

    if (value instanceof Rectangle2D)
    {
      return true;
    }
    return false;
  }

  private boolean isLineElement (Element e)
  {
    if (e.getContentType().startsWith("shape/") == false)
      return false;

    Object value = e.getValue();
    if (value == null)
      return false;

    if (value instanceof Line2D)
    {
      Line2D line = (Line2D) e.getValue();
      if ((line.getX1() == line.getX2()) ||
          (line.getY1() == line.getY1()))
      {
        return true;
      }
    }
    return false;
  }

  private BasicStroke getStroke (Element e)
  {
    Object stroke = e.getStyle().getStyleProperty(ElementStyleSheet.STROKE);
    if (stroke instanceof BasicStroke)
      return (BasicStroke) stroke;

    return null;
  }

  private short convertAlignment (ElementAlignment e)
  {
    if (e == ElementAlignment.LEFT) return HSSFCellStyle.ALIGN_LEFT;
    if (e == ElementAlignment.RIGHT) return HSSFCellStyle.ALIGN_RIGHT;
    if (e == ElementAlignment.CENTER) return HSSFCellStyle.ALIGN_CENTER;
    if (e == ElementAlignment.TOP) return HSSFCellStyle.VERTICAL_TOP;
    if (e == ElementAlignment.BOTTOM) return HSSFCellStyle.VERTICAL_BOTTOM;
    if (e == ElementAlignment.MIDDLE) return HSSFCellStyle.VERTICAL_CENTER;

    throw new IllegalArgumentException("Invalid alignment");
  }

  private short translateStroke (BasicStroke stroke)
  {
    float width = stroke.getLineWidth();
    if (width == 0)
    {
      return HSSFCellStyle.BORDER_NONE;
    }
    else if (width < 0.5)
    {
      return HSSFCellStyle.BORDER_HAIR;
    }
    else if (width < 1)
    {
      return HSSFCellStyle.BORDER_THIN;
    }
    else if (width < 1.5)
    {
      return HSSFCellStyle.BORDER_MEDIUM;
    }
    else if (width < 2)
    {
      return HSSFCellStyle.BORDER_DOUBLE;
    }
    else
    {
      return HSSFCellStyle.BORDER_THICK;
    }
  }

  private ExcelBackgroundCellStyle createRectangleStyle (Element element)
  {
    ExcelBackgroundCellStyle style = new ExcelBackgroundCellStyle();
    Color color = (Color) element.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    if (element.getStyle().getBooleanStyleProperty(ShapeElement.DRAW_SHAPE) == true)
    {
      BasicStroke stroke = getStroke(element);
      if (stroke != null)
      {
        short strokeTranslated = translateStroke (stroke);
        style.setColor(color);
        style.setBorderBottom(strokeTranslated);
        style.setBorderTop(strokeTranslated);
        style.setBorderRight(strokeTranslated);
        style.setBorderLeft(strokeTranslated);
      }
    }
    if (element.getStyle().getBooleanStyleProperty(ShapeElement.FILL_SHAPE) == true)
    {
      style.setColor(color);
    }
    return style;
  }

  private ExcelBackgroundCellStyle createLineStyle (Element element)
  {
    ExcelBackgroundCellStyle style = new ExcelBackgroundCellStyle();
    Line2D line = (Line2D) element.getValue();
    Color color = (Color) element.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    if (element.getStyle().getBooleanStyleProperty(ShapeElement.DRAW_SHAPE) == true)
    {
      BasicStroke stroke = getStroke(element);
      short strokeTranslated = translateStroke (stroke);
      if (stroke != null)
      {
        if (line.getY1() == line.getY2())
        {
          style.setBorderLeft(strokeTranslated);
          style.setColor(color);
        }
        else
        {
          style.setBorderTop(strokeTranslated);
          style.setColor(color);
        }
      }
    }
    if (element.getStyle().getBooleanStyleProperty(ShapeElement.FILL_SHAPE) == true)
    {
      style.setColor(color);
    }
    return style;
  }

  private ExcelDataCellStyle createTextStyle (Element element)
  {
    ElementAlignment horizontalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);
    ElementAlignment verticalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    FontDefinition awtFont = element.getStyle().getFontDefinitionProperty();
    Color color = (Color) element.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    ExcelDataCellStyle style = new ExcelDataCellStyle();
    style.setHorizontalAlignment(horizontalAlignment);
    style.setVerticalAlignment(verticalAlignment);
    style.setFontDefinition(awtFont);
    style.setTextColor(color);
    return style;
  }

	/**
	 *
	 */
	public ExcelDataCellStyle getExcelDataCellStyle (Element element)
  {
    return createTextStyle(element);
  }

  public ExcelBackgroundCellStyle getExcelBackgroundCellStyle (Element element)
  {
    if (isRectangleElement(element))
    {
      return createRectangleStyle(element);
    }
    else if (isLineElement(element))
    {
      return createLineStyle(element);
    }
    else
    {
      return null;
    }
	}

  public HSSFCellStyle getEmptyCellStyle ()
  {
    if (emptyCellStyle == null)
    {
      emptyCellStyle = workbook.createCellStyle();
      emptyCellStyle.setFillForegroundColor(WHITE_INDEX);
      emptyCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }
    return emptyCellStyle;
  }

  /**
   * This needs POI 1.9, the version 1.5.1 is not able to set userdefined data
   * formats
   *
   * @return
   */
  public HSSFDataFormat getDataFormat ()
  {
    if (dataFormat == null)
    {
      dataFormat = new HSSFDataFormat();
    }
    return dataFormat;
  }

  public HSSFCellStyle createCellStyle (ExcelDataCellStyle style)
  {
    if (styleCache.containsKey(style))
    {
      return (HSSFCellStyle) styleCache.get(style);
    }

    HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
    hssfCellStyle.setAlignment(convertAlignment(style.getHorizontalAlignment()));
    hssfCellStyle.setVerticalAlignment(convertAlignment(style.getVerticalAlignment()));
    hssfCellStyle.setFont(fontFactory.getExcelFont(style.getFontDefinition(), style.getTextColor()));
    hssfCellStyle.setWrapText(true);
    hssfCellStyle.setFillForegroundColor(WHITE_INDEX);
    hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    ExcelBackgroundCellStyle background =
        style.getBackgroundStyleDefinition();
    if (background != null)
    {
      hssfCellStyle.setBorderBottom(background.getBorderBottom());
      hssfCellStyle.setBorderTop(background.getBorderTop());
      hssfCellStyle.setBorderLeft(background.getBorderLeft());
      hssfCellStyle.setBorderRight(background.getBorderRight());
      hssfCellStyle.setFillForegroundColor(ExcelToolLibrary.getNearestColor(background.getColor()).getIndex());
    }

    styleCache.put(style, hssfCellStyle);
    return hssfCellStyle;
  }
}
