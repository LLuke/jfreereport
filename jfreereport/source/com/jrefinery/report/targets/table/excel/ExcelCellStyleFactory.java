
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
 * $Id: ExcelCellStyleFactory.java,v 1.4 2003/01/25 20:34:12 taqua Exp $
 *
 * Changes
 * -------
 * 06-Jan-2002 : initial version
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.TableCellBackground;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.awt.Color;
import java.util.Hashtable;

/**
 * This class keeps track of the cell styles that we have used so far
 * @author Heiko Evermann
 */
public class ExcelCellStyleFactory
{
  private static class StyleCarrier
  {
    private ExcelDataCellStyle style;
    private TableCellBackground background;

    public StyleCarrier(ExcelDataCellStyle style, TableCellBackground background)
    {
      this.style = style;
      this.background = background;
    }

    public ExcelDataCellStyle getStyle()
    {
      return style;
    }

    public TableCellBackground getBackground()
    {
      return background;
    }

    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (!(o instanceof StyleCarrier)) return false;

      final StyleCarrier carrier = (StyleCarrier) o;

      if (background != null ? !background.equals(carrier.background) : carrier.background != null) return false;
      if (style != null ? !style.equals(carrier.style) : carrier.style != null) return false;

      return true;
    }

    public int hashCode()
    {
      int result;
      result = (style != null ? style.hashCode() : 0);
      result = 29 * result + (background != null ? background.hashCode() : 0);
      return result;
    }
  }


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

  protected short convertAlignment (ElementAlignment e)
  {
    if (e == ElementAlignment.LEFT) return HSSFCellStyle.ALIGN_LEFT;
    if (e == ElementAlignment.RIGHT) return HSSFCellStyle.ALIGN_RIGHT;
    if (e == ElementAlignment.CENTER) return HSSFCellStyle.ALIGN_CENTER;
    if (e == ElementAlignment.TOP) return HSSFCellStyle.VERTICAL_TOP;
    if (e == ElementAlignment.BOTTOM) return HSSFCellStyle.VERTICAL_BOTTOM;
    if (e == ElementAlignment.MIDDLE) return HSSFCellStyle.VERTICAL_CENTER;

    throw new IllegalArgumentException("Invalid alignment");
  }

  protected short translateStroke (float width)
  {
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

  public HSSFCellStyle createCellStyle (ExcelDataCellStyle style, TableCellBackground bg)
  {
    StyleCarrier carrier = new StyleCarrier(style, bg);
    if (styleCache.containsKey(carrier))
    {
      return (HSSFCellStyle) styleCache.get(carrier);
    }

    HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
    hssfCellStyle.setWrapText(true);
    hssfCellStyle.setFillForegroundColor(WHITE_INDEX);
    hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    if (style != null)
    {
      hssfCellStyle.setAlignment(convertAlignment(style.getHorizontalAlignment()));
      hssfCellStyle.setVerticalAlignment(convertAlignment(style.getVerticalAlignment()));
      hssfCellStyle.setFont(fontFactory.getExcelFont(style.getFontDefinition(), style.getTextColor()));
    }

    if (bg != null)
    {
      if (bg.getColorBottom() != null)
      {
        hssfCellStyle.setBorderBottom(translateStroke(bg.getBorderSizeBottom()));
        hssfCellStyle.setBottomBorderColor(ExcelToolLibrary.getNearestColor(bg.getColorBottom()));
      }
      if (bg.getColorTop() != null)
      {
        hssfCellStyle.setBorderTop(translateStroke(bg.getBorderSizeTop()));
        hssfCellStyle.setTopBorderColor(ExcelToolLibrary.getNearestColor(bg.getColorTop()));
      }
      if (bg.getColorLeft() != null)
      {
        hssfCellStyle.setBorderLeft(translateStroke(bg.getBorderSizeLeft()));
        hssfCellStyle.setLeftBorderColor(ExcelToolLibrary.getNearestColor(bg.getColorLeft()));
      }
      if (bg.getColorRight() != null)
      {
        hssfCellStyle.setBorderRight(translateStroke(bg.getBorderSizeRight()));
        hssfCellStyle.setRightBorderColor(ExcelToolLibrary.getNearestColor(bg.getColorRight()));
      }
      if (bg.getColor() != null)
      {
        hssfCellStyle.setFillForegroundColor(ExcelToolLibrary.getNearestColor(bg.getColor()));
      }
    }

    styleCache.put(carrier, hssfCellStyle);
    return hssfCellStyle;
  }
}
