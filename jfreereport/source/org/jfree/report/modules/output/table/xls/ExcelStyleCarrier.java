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
 * ExcelStyleCarrier.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExcelStyleCarrier.java,v 1.1 2003/07/14 17:40:06 taqua Exp $
 *
 * Changes
 * -------------------------
 * 13.07.2003 : Initial version
 *
 */

package org.jfree.report.modules.output.table.xls;

import org.jfree.report.modules.output.table.base.TableCellBackground;

/**
 * The style carrier is used to collect and compare fore- and background
 * style information of previously created cell styles.
 *
 * @author Thomas Morgner
 */
public class ExcelStyleCarrier
{
  /** The foreground style. */
  private ExcelDataCellStyle style;

  /** the background style. */
  private TableCellBackground background;

  /**
   * Creates a new StyleCarrier. The carrier collects background and foreground
   * and provides a unified interface to both format informations.
   *
   * @param style the foreground style.
   * @param background the background style.
   */
  public ExcelStyleCarrier(final ExcelDataCellStyle style, final TableCellBackground background)
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
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof ExcelStyleCarrier))
    {
      return false;
    }

    final ExcelStyleCarrier carrier = (ExcelStyleCarrier) o;

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
