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
 * ------------------------------
 * HSSFCellStyleProducer.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HSSFCellStyleProducer.java,v 1.2 2003/07/23 16:02:21 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.xls;

import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.output.table.base.TableCellBackground;

/**
 * The cellstyle producer converts the JFreeReport content into excel
 * cell styles. This class is able to use the POI 2.0 features to build
 * data cells.
 * 
 * @author Thomas Morgner
 */
public class HSSFCellStyleProducer
{
  /** The workbook wide singleton instance of an empty cell. */
  private HSSFCellStyle emptyCellStyle;

  /** the font factory is used to create excel fonts. */
  private ExcelFontFactory fontFactory;

  /** The workbook, which creates all cells and styles. */
  private HSSFWorkbook workbook;

  /** The data format is used to create format strings. */
  private HSSFDataFormat dataFormat;

  /** White background. This is the default background if not specified otherwise. */
  private static final short WHITE_INDEX = (new HSSFColor.WHITE()).getIndex();

  /** The cache for all generated styles. */
  private HashMap styleCache;

  /**
   * The class does the dirty work of creating the HSSF-objects.
   *
   * @param workbook the workbook for which the styles should be created.
   */
  public HSSFCellStyleProducer(final HSSFWorkbook workbook)
  {
    if (workbook == null)
    {
      throw new NullPointerException();
    }
    this.styleCache = new HashMap();
    this.workbook = workbook;
    this.fontFactory = new ExcelFontFactory(workbook);
    this.dataFormat = workbook.createDataFormat();
  }

  /**
   * Gets the default style, which is used for empty cells.
   *
   * @return the default style for empty cells.
   */
  public HSSFCellStyle getEmptyCellStyle()
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
   * Creates a HSSFCellStyle based on the given ExcelDataCellStyle.
   * If a similiar cell style was previously generated, then reuse that
   * cached result.
   *
   * @param style the excel style that was used to collect the foreground cellstyle information.
   * @param bg the background style for the table cell.
   * @return the generated or cached HSSFCellStyle.
   */
  public HSSFCellStyle createCellStyle(final ExcelDataCellStyle style, final TableCellBackground bg)
  {
    final ExcelStyleCarrier carrier = new ExcelStyleCarrier(style, bg);
    if (styleCache.containsKey(carrier))
    {
      return (HSSFCellStyle) styleCache.get(carrier);
    }

    final HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
    hssfCellStyle.setWrapText(true);
    hssfCellStyle.setFillForegroundColor(WHITE_INDEX);
    hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    if (style != null)
    {
      hssfCellStyle.setAlignment(convertAlignment(style.getHorizontalAlignment()));
      hssfCellStyle.setVerticalAlignment(convertAlignment(style.getVerticalAlignment()));
      hssfCellStyle.setFont(fontFactory.getExcelFont(style.getFontDefinition(),
          style.getTextColor()));
      hssfCellStyle.setDataFormat(dataFormat.getFormat(style.getDataStyle()));
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

  /**
   * Converts the given element alignment into one of the HSSFCellStyle-constants.
   *
   * @param e the JFreeReport element alignment.
   * @return the HSSFCellStyle-Alignment.
   * @throws IllegalArgumentException if an Unknown JFreeReport alignment is given.
   */
  protected short convertAlignment(final ElementAlignment e)
  {
    if (e == ElementAlignment.LEFT)
    {
      return HSSFCellStyle.ALIGN_LEFT;
    }
    if (e == ElementAlignment.RIGHT)
    {
      return HSSFCellStyle.ALIGN_RIGHT;
    }
    if (e == ElementAlignment.CENTER)
    {
      return HSSFCellStyle.ALIGN_CENTER;
    }
    if (e == ElementAlignment.TOP)
    {
      return HSSFCellStyle.VERTICAL_TOP;
    }
    if (e == ElementAlignment.BOTTOM)
    {
      return HSSFCellStyle.VERTICAL_BOTTOM;
    }
    if (e == ElementAlignment.MIDDLE)
    {
      return HSSFCellStyle.VERTICAL_CENTER;
    }

    throw new IllegalArgumentException("Invalid alignment");
  }

  /**
   * Tries to translate the given stroke width into one of the
   * predefined excel border styles.
   *
   * @param width the AWT-Stroke-Width.
   * @return the translated excel border width.
   */
  protected short translateStroke(final float width)
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


}
