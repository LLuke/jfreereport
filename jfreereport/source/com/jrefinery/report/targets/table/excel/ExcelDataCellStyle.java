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
 * -----------------------
 * ExcelDataCellStyle.java
 * -----------------------
 * (C)opyright 2003, by Hawesko GmbH & Co KG and Contributors.
 *
 * Original Author:  Heiko Evermann (for Hawesko GmbH & Co KG), based on ideas and code from
 *                   JRXlsExporter.java of JasperReports
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 *
 * $Id: ExcelDataCellStyle.java,v 1.6 2003/05/09 17:12:13 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : initial version
 * 25-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package com.jrefinery.report.targets.table.excel;

import java.awt.Color;

import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.targets.FontDefinition;

/**
 * The ExcelDataCellStyle is used to collect style information for
 * an excel cell. This information is later transformed into a excel
 * HSSFCellStyle using the ExcelCellStyleFactory.
 *
 * @author Heiko Evermann.
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

  /** the data style */
  private String dataStyle;

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

  /**
   * Gets the horizontal alignment for the cell.
   *
   * @return the horizontal alignment for the cell content.
   */
  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  /**
   * Sets the horizontal alignment for the cell.
   *
   * @param horizontalAlignment the horizontal alignment for the cell content.
   */
  public void setHorizontalAlignment(ElementAlignment horizontalAlignment)
  {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Gets the vertical alignment for the cell.
   *
   * @return the vertical alignment for the cell content.
   */
  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  /**
   * Defines the vertical alignment for the cell.
   *
   * @param verticalAlignment the vertical alignment for the cell content.
   */
  public void setVerticalAlignment(ElementAlignment verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  /**
   * Gets the font style for the text in the cell.
   *
   * @return the font definition for the text in the cell.
   */
  public FontDefinition getFontDefinition()
  {
    return fontDefinition;
  }

  /**
   * Defines the font style for the text in the cell.
   *
   * @param fontDefinition the font definition for the text in the cell.
   */
  public void setFontDefinition(FontDefinition fontDefinition)
  {
    this.fontDefinition = fontDefinition;
  }

  /**
   * Returns the text color for the cell.
   *
   * @return the text color.
   */
  public Color getTextColor()
  {
    return textColor;
  }

  /**
   * Defines the text color for the cell.
   *
   * @param textColor the text color.
   */
  public void setTextColor(Color textColor)
  {
    this.textColor = textColor;
  }

  /**
   * Returns the defined cell data style.
   *
   * @return the cell data style.
   */
  public String getDataStyle()
  {
    return dataStyle;
  }

  /**
   * Defines the cell data style.
   *
   * @param dataStyle the cell data style.
   */
  public void setDataStyle(String dataStyle)
  {
    this.dataStyle = dataStyle;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param   o   the reference object with which to compare.
   * @return  <code>true</code> if this object is the same as the obj
   *          argument; <code>false</code> otherwise.
   */
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof ExcelDataCellStyle))
    {
      return false;
    }

    final ExcelDataCellStyle style = (ExcelDataCellStyle) o;

    if (wrapText != style.wrapText)
    {
      return false;
    }
    if (fontDefinition != null ? !fontDefinition.equals(style.fontDefinition)
        : style.fontDefinition != null)
    {
      return false;
    }
    if (horizontalAlignment != null ? !horizontalAlignment.equals(style.horizontalAlignment)
        : style.horizontalAlignment != null)
    {
      return false;
    }
    if (textColor != null ? !textColor.equals(style.textColor) : style.textColor != null)
    {
      return false;
    }
    if (verticalAlignment != null ? !verticalAlignment.equals(style.verticalAlignment)
        : style.verticalAlignment != null)
    {
      return false;
    }
    if (dataStyle != null ? !dataStyle.equals(style.dataStyle)
        : style.dataStyle != null)
    {
      return false;
    }

    return true;
  }

  /**
   * Returns a hash code value for the object. This method is
   * supported for the benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   *
   * @return  a hash code value for this object.
   */
  public int hashCode()
  {
    int result;
    result = (wrapText ? 1 : 0);
    result = 29 * result + (horizontalAlignment != null ? horizontalAlignment.hashCode() : 0);
    result = 29 * result + (verticalAlignment != null ? verticalAlignment.hashCode() : 0);
    result = 29 * result + (fontDefinition != null ? fontDefinition.hashCode() : 0);
    result = 29 * result + (textColor != null ? textColor.hashCode() : 0);
    result = 29 * result + (dataStyle != null ? dataStyle.hashCode() : 0);
    return result;
  }
}
