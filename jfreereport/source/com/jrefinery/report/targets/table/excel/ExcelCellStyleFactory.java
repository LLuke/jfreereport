/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * --------------------------
 * ExcelCellStyleFactory.java
 * --------------------------
 * (C)opyright 2003, by Hawesko GmbH & Co KG and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG), based on ideas and code from 
 *                   JRXlsExporter.java of JasperReports;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelCellStyleFactory.java,v 1.11 2003/04/09 16:07:10 mungady Exp $
 *
 * Changes
 * -------
 * 06-Jan-2002 : initial version
 */
package com.jrefinery.report.targets.table.excel;

import java.awt.Color;
import java.util.HashMap;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.TableCellBackground;
import com.jrefinery.report.util.Log;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * The CellStyle factory is used to convert JFreeReport style information
 * into excel styles. This class also keeps track of the cell styles that
 * we have used so far, as excel has a limitation on the usable amount of
 * defined styles. If equal styles are defined, we recycle the previously
 * generated styles.
 *
 * @author Heiko Evermann
 */
public class ExcelCellStyleFactory
{
  /** An instance counter. */
  private static int instanceCounter = 0;
  
  /** A usage counter. */
  private static int usageCounter = 0;

  /**
   * Prints the instance and usage counters.
   */
  public static void print ()
  {
    Log.debug ("InstanceCount: " + instanceCounter + " Usage: " + usageCounter);
  }

  /**
   * The style carrier is used to collect and compare fore- and background
   * style information of previously created cell styles.
   */
  private static class StyleCarrier
  {
    /** The foreground style. */
    private ExcelDataCellStyle style;
    
    /** the background style */
    private TableCellBackground background;

    /**
     * Creates a new StyleCarrier. The carrier collects background and foreground
     * and provides a unified interface to both format informations.
     *
     * @param style the foreground style.
     * @param background the background style.
     */
    public StyleCarrier(ExcelDataCellStyle style, TableCellBackground background)
    {
      this.style = style;
      this.background = background;
    }

    /**
     * Retuns the foreground style used in this carrier.
     *
     * @return the foreground style.
     */
    public ExcelDataCellStyle getStyle()
    {
      return style;
    }

    /**
     * Gets the background style information.
     *
     * @return the background style.
     */
    public TableCellBackground getBackground()
    {
      return background;
    }

    /**
     * Checks for equality. The Object is equal, if the fore and
     * the background style are equal.
     *
     * @param o the compared object.
     * @return true, if both styles are equal, false otherwise.
     */
    public boolean equals(Object o)
    {
      if (this == o) 
      {
        return true;
      }
      if (!(o instanceof StyleCarrier)) 
      {
        return false;
      }

      final StyleCarrier carrier = (StyleCarrier) o;

      if (background != null ? !background.equals(carrier.background) : carrier.background != null)
      {
        return false;
      }
      if (style != null ? !style.equals(carrier.style) : carrier.style != null) 
      {
        return false;
      }

      return true;
    }

    /**
     * Calculates an hashcode for the cell style carrier.
     *
     * @return the hashcode.
     */
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

  /** POI 2_0: the data format is used to create format strings. */
  private HSSFDataFormat dataFormat;

  /** The list of fonts that we have used so far */
  private HSSFCellStyle emptyCellStyle;

  /** The workbook, which creates all cells and styles. */
  private HSSFWorkbook workbook;

  /** The cache for all generated styles */
  private HashMap styleCache;

  /** the font factory is used to create excel fonts. */
  private ExcelFontFactory fontFactory;

  /**
   * Constructor for ExcelCellStyleFactory.
   *
   * @param workbook the workbook for which the styles should be created.
   * @throws NullPointerException if the workbook is null.
   */
  public ExcelCellStyleFactory(HSSFWorkbook workbook)
  {
    if (workbook == null) 
    {
      throw new NullPointerException();
    }
    this.workbook = workbook;
    this.styleCache = new HashMap();
    this.fontFactory = new ExcelFontFactory(workbook);
  }

  /**
   * Converts the given element alignment into one of the HSSFCellStyle-constants.
   *
   * @param e the JFreeReport element alignment.
   * @return the HSSFCellStyle-Alignment.
   * @throws IllegalArgumentException if an Unknown JFreeReport alignment is given.
   */
  protected short convertAlignment (ElementAlignment e)
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

  /**
   * Converts the given element and the assigned style into an excel style.
   *
   * @param element the element that should be converted into the excel style.
   * @return the generated excel style, never null.
   */
  public ExcelDataCellStyle getExcelDataCellStyle (Element element)
  {
    ElementAlignment horizontalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.LEFT);
    ElementAlignment verticalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.TOP);
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
   * Gets the default style, which is used for empty cells.
   *
   * @return the default style for empty cells.
   */
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
   * Gets the data format implementation of HSSF.
   * <p>
   * This needs at least POI 1.9, the version 1.5.1 is not able to set
   * userdefined data formats
   *
   * @return the data format implementation.
   */
  public HSSFDataFormat getDataFormat ()
  {
    if (dataFormat == null)
    {
      dataFormat = new HSSFDataFormat();
    }
    return dataFormat;
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
  public HSSFCellStyle createCellStyle (ExcelDataCellStyle style, TableCellBackground bg)
  {
    StyleCarrier carrier = new StyleCarrier(style, bg);
    usageCounter++;
    if (styleCache.containsKey(carrier))
    {
      return (HSSFCellStyle) styleCache.get(carrier);
    }

    HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
    instanceCounter++;
    hssfCellStyle.setWrapText(true);
    hssfCellStyle.setFillForegroundColor(WHITE_INDEX);
    hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

    if (style != null)
    {
      hssfCellStyle.setAlignment(convertAlignment(style.getHorizontalAlignment()));
      hssfCellStyle.setVerticalAlignment(convertAlignment(style.getVerticalAlignment()));
      hssfCellStyle.setFont(fontFactory.getExcelFont(style.getFontDefinition(), 
                                                     style.getTextColor()));
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
