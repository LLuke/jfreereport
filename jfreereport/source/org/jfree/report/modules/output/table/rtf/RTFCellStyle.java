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
 * -----------------
 * RTFCellStyle.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFCellStyle.java,v 1.2 2003/08/18 18:28:01 taqua Exp $
 *
 * Changes
 * -------
 * 01-Feb-2003 : Initial version
 */
package org.jfree.report.modules.output.table.rtf;

import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import org.jfree.report.ElementAlignment;

/**
 * The RTFCellStyle is used to define the style for the generated RTF-TableCell.
 * <p>
 * iText only supports the predefined logical fonts for RTF documents.
 *
 * @author Thomas Morgner
 */
public class RTFCellStyle
{
  /** The vertical alignment of the cell content. */
  private ElementAlignment verticalAlignment;

  /** The horizontal alignment of the cell content. */
  private ElementAlignment horizontalAlignment;

  /**
   * Creates a new RTFCellStyle.
   *
   * @param verticalAlignment the vertical text alignment.
   * @param horizontalAlignment the horizontal text alignment.
   * @throws NullPointerException if one of the alignment parameters is null.
   */
  public RTFCellStyle(final ElementAlignment verticalAlignment, 
                      final ElementAlignment horizontalAlignment)
  {
    if (verticalAlignment == null)
    {
      throw new NullPointerException("VAlign");
    }
    if (horizontalAlignment == null)
    {
      throw new NullPointerException("HAlign");
    }

    this.verticalAlignment = verticalAlignment;
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Returns the cell's vertical alignment.
   *
   * @return the vertical alignment.
   */
  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  /**
   * Returns the cell's horizontal alignment.
   *
   * @return the horizontal alignment.
   */
  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  /**
   * Tests whether the given object is equal to this object.
   *
   * @param o the to be compared object.
   * @return true, if the object is a RTFCellStyle with the same attributes as this
   * object.
   */
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof RTFCellStyle))
    {
      return false;
    }

    final RTFCellStyle style = (RTFCellStyle) o;

    if (!horizontalAlignment.equals(style.horizontalAlignment))
    {
      return false;
    }
    if (!verticalAlignment.equals(style.verticalAlignment))
    {
      return false;
    }

    return true;
  }

  /**
   * Calculates an Hashcode for this cell style.
   *
   * @return the hashcode.
   */
  public int hashCode()
  {
    int result;
    result = verticalAlignment.hashCode();
    result = 29 * result + horizontalAlignment.hashCode();
    return result;
  }

  /**
   * Defines the content alignment for the given iText cell.
   *
   * @param cell the iText cell, that should be formated.
   */
  public void applyAlignment(final Cell cell)
  {
    if (horizontalAlignment == ElementAlignment.LEFT)
    {
      cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    }
    else if (horizontalAlignment == ElementAlignment.CENTER)
    {
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    }
    else if (horizontalAlignment == ElementAlignment.RIGHT)
    {
      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    }

    if (verticalAlignment == ElementAlignment.TOP)
    {
      cell.setVerticalAlignment(Element.ALIGN_TOP);
    }
    else if (verticalAlignment == ElementAlignment.MIDDLE)
    {
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }
    else if (verticalAlignment == ElementAlignment.BOTTOM)
    {
      cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    }
  }
}
