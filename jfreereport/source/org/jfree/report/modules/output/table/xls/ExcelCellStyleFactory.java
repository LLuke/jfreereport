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
 * --------------------------
 * ExcelCellStyleFactory.java
 * --------------------------
 * (C)opyright 2003, by Hawesko GmbH & Co KG and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG), based on ideas and code from
 *                   JRXlsExporter.java of JasperReports;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelCellStyleFactory.java,v 1.2 2003/07/14 17:37:08 taqua Exp $
 *
 * Changes
 * -------
 * 06-Jan-2002 : initial version
 */
package org.jfree.report.modules.output.table.xls;

import java.awt.Color;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;

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

  /**
   * Constructor for ExcelCellStyleFactory.
   *
   * @throws NullPointerException if the workbook is null.
   */
  public ExcelCellStyleFactory()
  {
  }

  /**
   * Converts the given element and the assigned style into an excel style.
   *
   * @param element the element that should be converted into the excel style.
   * @return the generated excel style, never null.
   */
  public ExcelDataCellStyle getExcelDataCellStyle(final Element element)
  {
    return getExcelDataCellStyle(element, "TEXT");
  }

  /**
   * Converts the given element and the assigned style into an excel style.
   *
   * @param element the element that should be converted into the excel style.
   * @param format the format string for the cell.
   * @return the generated excel style, never null.
   */
  public ExcelDataCellStyle getExcelDataCellStyle(final Element element, final String format)
  {
    final ElementAlignment horizontalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.LEFT);
    final ElementAlignment verticalAlignment = (ElementAlignment)
        element.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.TOP);
    final FontDefinition awtFont = element.getStyle().getFontDefinitionProperty();
    final Color color = (Color) element.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    final ExcelDataCellStyle style = new ExcelDataCellStyle();
    style.setHorizontalAlignment(horizontalAlignment);
    style.setVerticalAlignment(verticalAlignment);
    style.setFontDefinition(awtFont);
    style.setTextColor(color);
    style.setDataStyle(format);
    return style;
  }

}
