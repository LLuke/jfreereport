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
 * ExcelDataCellStyle.java
 * --------------------
 * (C)opyright 2002, by Hawesko GmbH & Co KG
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG)
 * Contributor(s):   -;
 * based on ideas and code from JRXlsExporter.java of JasperReports
 *
 * $Id: ExcelDataCellStyle.java,v 1.2 2003/01/28 22:05:28 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2002 : initial version
 */
package com.jrefinery.report.targets.table.excel;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;

import java.awt.Color;

/**
 * The ExcelDataCellStyle is used to collect style information for
 * an excel cell. This information is later transformed into a excel
 * HSSFCellStyle using the ExcelCellStyleFactory.
 */
public class ExcelDataCellStyle
{
  /** A flag indicating whether to enable excels word wrapping. */
  private boolean wrapText;
  /** the horizontal text alignment */
  private ElementAlignment horizontalAlignment;
  /** the vertical text alignment */
  private ElementAlignment verticalAlignment;
  /** the font definition for the cell. */
  private FontDefinition fontDefinition;
  /** the text color */
  private Color textColor;

  /**
   * DefaultConstructor.
   */
  public ExcelDataCellStyle()
  {
  }

  /**
   * Gets the word wrap setting for the cell.
   *
   * @return true, if wordwrapping is enabled, false otherwise.
   */
  public boolean isWrapText()
  {
    return wrapText;
  }

  /**
   * Defines the word wrap setting for the cell.
   *
   * @param wrapText set to true, if wordwrapping is enabled, false otherwise.
   */
  public void setWrapText(boolean wrapText)
  {
    this.wrapText = wrapText;
  }

  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  public void setHorizontalAlignment(ElementAlignment horizontalAlignment)
  {
    this.horizontalAlignment = horizontalAlignment;
  }

  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public void setVerticalAlignment(ElementAlignment verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  public FontDefinition getFontDefinition()
  {
    return fontDefinition;
  }

  public void setFontDefinition(FontDefinition fontDefinition)
  {
    this.fontDefinition = fontDefinition;
  }

  public Color getTextColor()
  {
    return textColor;
  }

  public void setTextColor(Color textColor)
  {
    this.textColor = textColor;
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ExcelDataCellStyle)) return false;

    final ExcelDataCellStyle style = (ExcelDataCellStyle) o;

    if (wrapText != style.wrapText) return false;
    if (fontDefinition != null ? !fontDefinition.equals(style.fontDefinition) : style.fontDefinition != null) return false;
    if (horizontalAlignment != null ? !horizontalAlignment.equals(style.horizontalAlignment) : style.horizontalAlignment != null) return false;
    if (textColor != null ? !textColor.equals(style.textColor) : style.textColor != null) return false;
    if (verticalAlignment != null ? !verticalAlignment.equals(style.verticalAlignment) : style.verticalAlignment != null) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = (wrapText ? 1 : 0);
    result = 29 * result + (horizontalAlignment != null ? horizontalAlignment.hashCode() : 0);
    result = 29 * result + (verticalAlignment != null ? verticalAlignment.hashCode() : 0);
    result = 29 * result + (fontDefinition != null ? fontDefinition.hashCode() : 0);
    result = 29 * result + (textColor != null ? textColor.hashCode() : 0);
    return result;
  }
}
