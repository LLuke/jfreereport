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
 * ------------------------------
 * HtmlLayoutInfo.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: HtmlLayoutInfo.java,v 1.4 2003/08/20 17:24:34 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html;

import java.awt.print.PageFormat;

import org.jfree.report.modules.output.table.base.TableLayoutInfo;

/**
 * The HTML Layout info collects grid and cell style information.
 * 
 * @author Thomas Morgner
 */
public class HtmlLayoutInfo extends TableLayoutInfo
{
  /** The HtmlStyleCollection used to store the cell style information. */
  private HtmlStyleCollection styleCollection;

  /**
   * Creates a new HTML Layout info object. This object collects the
   * grid positions and the cell styles during the repagination.
   * 
   * @param globalLayout a flag indicating whether to generate a global
   * layout for all pages.
   * @param format the page format used to generate the report.
   */
  public HtmlLayoutInfo(boolean globalLayout, PageFormat format)
  {
    super(globalLayout, format);
  }

  /**
   * Returns the HtmlStyleCollection assigned with that layout info.
   * 
   * @return the HtmlStyleCollection.
   */
  public HtmlStyleCollection getStyleCollection()
  {
    return styleCollection;
  }

  /**
   * Defines a HtmlStyleCollection for this layout information. This 
   * collection is used to collect stylesheet information.
   * 
   * @param styleCollection the style collection.
   */
  public void setStyleCollection(HtmlStyleCollection styleCollection)
  {
    this.styleCollection = styleCollection;
  }
}
