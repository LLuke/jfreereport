
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
 * $Id: PDFOutputTarget.java,v 1.8 2002/12/11 01:10:41 mungady Exp $
 *
 * Changes
 * -------
 * 06-Jan-2002 : initial version
 */
package com.jrefinery.report.targets.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFont;

import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import java.awt.Paint;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.style.ElementStyleSheet;


/**
 * This class keeps track of the cell styles that we have used so far
 * @author Heiko Evermann
 */
public class ExcelCellStyleFactory
{
  /** White backgroud. Other backgrounds are not supported so far. */
  private static final short whiteIndex = (new HSSFColor.WHITE()).getIndex();

  private ExcelFontFactory fontFactory;
  private HSSFWorkbook workbook;

	/** The list of fonts that we have used so far */
	private ArrayList cellStyleList = null;
  private HSSFCellStyle emptyCellStyle;

	/**
	 * Constructor for ExcelCellStyleFactory.
	 */
	public ExcelCellStyleFactory(HSSFWorkbook workbook)
  {
    if (workbook == null) throw new NullPointerException();

    this.cellStyleList = new ArrayList();
    this.workbook = workbook;
    this.fontFactory = new ExcelFontFactory(workbook);

	}

	/**
	 *
	 */
	public HSSFCellStyle getExcelCellStyle (Element element)
  {
    ElementAlignment horizontalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);
    ElementAlignment verticalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    Font awtFont = element.getStyle().getFontStyleProperty();
    Paint paint = (Paint) element.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    Color color = Color.black;
    if (paint instanceof Color)
    {
      color = (Color) paint;
    }

    HSSFFont font = fontFactory.getExcelFont(awtFont, color);

		// Have we already had this style before?
		for (int i = 0; i < cellStyleList.size(); i++)
    {
			HSSFCellStyle cellStyle = (HSSFCellStyle) cellStyleList.get(i);
			if (cellStyle.getAlignment() == convertAlignment(horizontalAlignment) &&
          cellStyle.getVerticalAlignment() == convertAlignment(verticalAlignment) &&
				  cellStyle.getFontIndex() == font.getIndex())
      {
				return cellStyle;
			}
		}
		// no, get a new one
		HSSFCellStyle  cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(convertAlignment(horizontalAlignment));
    cellStyle.setVerticalAlignment(convertAlignment(verticalAlignment));
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);
		cellStyle.setFillForegroundColor(whiteIndex); // white background

    // todo add support for data format ... needs template implementation ...
		cellStyleList.add( cellStyle );
		return cellStyle;
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

  public HSSFCellStyle getEmptyCellStyle ()
  {
    if (emptyCellStyle == null)
    {
      emptyCellStyle = workbook.createCellStyle();
      emptyCellStyle.setFillForegroundColor(
          (new HSSFColor.WHITE()).getIndex());
      emptyCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }
    return emptyCellStyle;
  }
}
