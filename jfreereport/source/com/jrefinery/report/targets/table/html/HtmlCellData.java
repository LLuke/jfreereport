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
 * -----------------
 * HtmlCellData.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlCellData.java,v 1.7 2003/02/26 16:42:26 mungady Exp $
 *
 * Changes
 * -------
 * 18-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table.html;

import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;

import com.jrefinery.report.targets.table.TableCellData;

/**
 * The baseclass for all HTML-Content cells. The cell has a HtmlCellStyle assigned
 * and carries a flag to indicate whether to create XHTML output.
 * 
 * @author Thomas Morgner
 */
public abstract class HtmlCellData extends TableCellData
{
  /** The assigned cell style. */
  private HtmlCellStyle style;
  
  /** a flag indicating whether to generate XHTML for the output. */
  private boolean useXHTML;

  /**
   * Creates a new HtmlCellData-object. The cell data will encapsulate images or
   * text content.
   *
   * @param outerBounds the cell bounds.
   * @param style the assigned cell style for this data cell.
   * @param useXHTML a flag indicating whether to generate XHTML.
   */
  public HtmlCellData(Rectangle2D outerBounds, HtmlCellStyle style, boolean useXHTML)
  {
    super(outerBounds);
    this.style = style;
    this.useXHTML = useXHTML;
  }

  /**
   * Writes the content of this cell into the print writer. The assigned HtmlFilesystem
   * is used to create external references, if needed.
   *
   * @param pout the printwriter receiving the generated content.
   * @param filesystem the filesystem used to create the external content.
   */
  public abstract void write(PrintWriter pout, HtmlFilesystem filesystem);

  /**
   * Gets the assigned cell style.
   *
   * @return the assigned cell style.
   */
  public HtmlCellStyle getStyle()
  {
    return style;
  }

  /**
   * Gets the XHTML-generator flag.
   *
   * @return true, if this cell should create XHTML output.
   */
  public boolean isUseXHTML()
  {
    return useXHTML;
  }
}
