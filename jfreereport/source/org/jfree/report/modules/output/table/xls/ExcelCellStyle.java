/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
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
 * HSSFCellStyle.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * Mar 15, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.xls;

import java.awt.Color;

import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.output.table.base.TableCellBackground;

public class ExcelCellStyle
{
  /** A flag indicating whether to enable excels word wrapping. */
  private boolean wrapText;

  /** the horizontal text alignment. */
  private ElementAlignment horizontalAlignment;

  /** the vertical text alignment. */
  private ElementAlignment verticalAlignment;

  /** the font definition for the cell. */
  private HSSFFontWrapper fontDefinition;

  /** the text color. */
  private Color textColor;

  /** the data style. */
  private String dataStyle;

  private TableCellBackground background;

  /** A cached hashcode. */
  private Integer hashCode;

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
  public ExcelCellStyle(final HSSFFontWrapper fontDefinition, final Color textColor,
                       final ElementAlignment horizontal, final ElementAlignment vertical,
                       final String dataStyle, final boolean wrapText,
                       final TableCellBackground background)
  {
    this.background = background;
    this.wrapText = wrapText;
    this.dataStyle = dataStyle;
    this.fontDefinition = fontDefinition;
    this.textColor = textColor;
    this.horizontalAlignment = horizontal;
    this.verticalAlignment = vertical;
  }

  public ExcelCellStyle (final TableCellBackground background)
  {
    this.background = background;
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
  public HSSFFontWrapper getFontDefinition()
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
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof ExcelCellStyle))
    {
      return false;
    }

    final ExcelCellStyle excelCellStyle = (ExcelCellStyle) o;

    if (wrapText != excelCellStyle.wrapText)
    {
      return false;
    }
    if (background != null ? !background.equals(excelCellStyle.background) : excelCellStyle.background != null)
    {
      return false;
    }
    if (dataStyle != null ? !dataStyle.equals(excelCellStyle.dataStyle) : excelCellStyle.dataStyle != null)
    {
      return false;
    }
    if (fontDefinition != null ? !fontDefinition.equals(excelCellStyle.fontDefinition) : excelCellStyle.fontDefinition != null)
    {
      return false;
    }
    if (horizontalAlignment != null ? !horizontalAlignment.equals(excelCellStyle.horizontalAlignment) : excelCellStyle.horizontalAlignment != null)
    {
      return false;
    }
    if (textColor != null ? !textColor.equals(excelCellStyle.textColor) : excelCellStyle.textColor != null)
    {
      return false;
    }
    if (verticalAlignment != null ? !verticalAlignment.equals(excelCellStyle.verticalAlignment) : excelCellStyle.verticalAlignment != null)
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
  public int hashCode ()
  {
    if (hashCode == null)
    {
      int result;
      result = (wrapText ? 1 : 0);
      result = 29 * result + (horizontalAlignment != null ? horizontalAlignment.hashCode() : 0);
      result = 29 * result + (verticalAlignment != null ? verticalAlignment.hashCode() : 0);
      result = 29 * result + (fontDefinition != null ? fontDefinition.hashCode() : 0);
      result = 29 * result + (textColor != null ? textColor.hashCode() : 0);
      result = 29 * result + (dataStyle != null ? dataStyle.hashCode() : 0);
      result = 29 * result + (background != null ? background.hashCode() : 0);
      hashCode = new Integer(result);
      return result;
    }
    return hashCode.intValue();
  }

}
