/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: ExcelDataCellStyle.java,v 1.3 2003/08/31 19:27:58 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : initial version
 * 25-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package org.jfree.report.modules.output.table.xls;

import java.awt.Color;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.FontDefinition;
import org.jfree.util.ObjectUtils;

/**
 * The ExcelDataCellStyle is used to collect style information for
 * an excel cell. This information is later transformed into a excel
 * HSSFCellStyle using the ExcelCellStyleFactory.
 *
 * @author Heiko Evermann.
 */
public final class ExcelDataCellStyle
{
  /** A flag indicating whether to enable excels word wrapping. */
  private boolean wrapText;

  /** the horizontal text alignment. */
  private ElementAlignment horizontalAlignment;

  /** the vertical text alignment. */
  private ElementAlignment verticalAlignment;

  /** the font definition for the cell. */
  private FontDefinition fontDefinition;

  /** the text color. */
  private Color textColor;

  /** the data style. */
  private String dataStyle;

  /** A cached hashcode. */
  private int hashCode;

  /**
   * Creates a new ExcelDataCellStyle object with the given attributes.
   * 
   * @param fontDefinition the font definition used in the cell.
   * @param textColor the text color of the cell.
   * @param horizontal the horizontal alignment of the cell contents.
   * @param vertical the vertical alignment of the cell contents.
   */
  public ExcelDataCellStyle(final FontDefinition fontDefinition, final Color textColor,
                            final ElementAlignment horizontal, final ElementAlignment vertical)
  {
    this (fontDefinition, textColor, horizontal, vertical, null, false);
  }


  /**
   * Creates a new ExcelDataCellStyle definition.
   *
   * @param fontDefinition the font definition used in the cell.
   * @param textColor the text color of the cell.
   * @param horizontal the horizontal alignment of the cell contents.
   * @param vertical the vertical alignment of the cell contents.
   * @param dataStyle the data style definition or null, if the default should be used.
   * @param wrapText defines whether to wrap text in excel.
   */
  public ExcelDataCellStyle(final FontDefinition fontDefinition, final Color textColor,
                            final ElementAlignment horizontal, final ElementAlignment vertical,
                            final String dataStyle, final boolean wrapText)
  {
    if (fontDefinition == null)
    {
      throw new NullPointerException("FontDefinition is null.");
    }
    if (textColor == null)
    {
      throw new NullPointerException("TextColor is null.");
    }
    if (horizontal == null)
    {
      throw new NullPointerException("HorizontalAlignment is null.");
    }
    if (vertical == null)
    {
      throw new NullPointerException("VerticalAlignment is null.");
    }
    this.wrapText = wrapText;
    this.dataStyle = dataStyle;
    this.fontDefinition = fontDefinition;
    this.textColor = textColor;
    this.horizontalAlignment = horizontal;
    this.verticalAlignment = vertical;

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
   * Gets the horizontal alignment for the cell.
   *
   * @return the horizontal alignment for the cell content.
   */
  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
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
   * Gets the font style for the text in the cell.
   *
   * @return the font definition for the text in the cell.
   */
  public FontDefinition getFontDefinition()
  {
    return fontDefinition;
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
   * Returns the defined cell data style.
   *
   * @return the cell data style.
   */
  public String getDataStyle()
  {
    return dataStyle;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param   o   the reference object with which to compare.
   * @return  <code>true</code> if this object is the same as the obj
   *          argument; <code>false</code> otherwise.
   */
  public boolean equals(final Object o)
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
    if (!fontDefinition.equals(style.fontDefinition))
    {
      return false;
    }
    if (!horizontalAlignment.equals(style.horizontalAlignment))
    {
      return false;
    }
    if (!textColor.equals(style.textColor))
    {
      return false;
    }
    if (!verticalAlignment.equals(style.verticalAlignment))
    {
      return false;
    }
    if (ObjectUtils.equalOrBothNull(dataStyle, style.dataStyle) == false)
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
    if (hashCode == 0)
    {
      int result;
      result = (wrapText ? 1 : 0);
      result = 29 * result + horizontalAlignment.hashCode();
      result = 29 * result + verticalAlignment.hashCode();
      result = 29 * result + fontDefinition.hashCode();
      result = 29 * result + textColor.hashCode();
      result = 29 * result + (dataStyle != null ? dataStyle.hashCode() : 0);
      hashCode = result;
    }
    return hashCode;
  }
}
