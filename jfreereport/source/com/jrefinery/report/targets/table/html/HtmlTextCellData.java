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
 * -------------------
 * HtmlTextCellData.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlTextCellData.java,v 1.3 2003/02/20 00:39:37 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;

/**
 * A wrapper for text content within the generated HtmlTable.
 */
public class HtmlTextCellData extends HtmlCellData
{
  /** the text content that should be printed within the cell. */
  private String value;

  /**
   * Creates a new HtmlTextCellData for the given content.
   *
   * @param outerBounds the cell bounds.
   * @param value the text content.
   * @param style the style definition for the cell.
   * @param useXHTML a flag indicating whether to use XHTML.
   */
  public HtmlTextCellData(Rectangle2D outerBounds, String value, HtmlCellStyle style, boolean useXHTML)
  {
    super(outerBounds, style, useXHTML);
    if (value == null) throw new NullPointerException();
    this.value = value;
  }

  /**
   * Writes the (X)HTML-Code to print the Text-Content.
   *
   * @param pout the print writer, which receives the generated HTML-Code.
   * @param filesystem not used.
   */
  public void write(PrintWriter pout, HtmlFilesystem filesystem)
  {
    HtmlProducer.printText(pout, value, isUseXHTML());
  }

  /**
   * Gets a flag, which indicates whether this cell contains background definitions.
   *
   * @return false, as this is no background cell.
   */
  public boolean isBackground()
  {
    return false;
  }
}
