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
 * RTFLayoutInfo.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: RTFLayoutInfo.java,v 1.3 2003/08/20 14:06:36 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.rtf;

import java.awt.print.PageFormat;

import org.jfree.report.modules.output.support.itext.BaseFontSupport;
import org.jfree.report.modules.output.table.base.TableLayoutInfo;

/**
 * The RTF Layout info collects grid and font information for the
 * RTF output.
 * 
 * @author Thomas Morgner
 */
public class RTFLayoutInfo extends TableLayoutInfo
{
  /** The baseFontSupport is used to handle truetype fonts in iText. */
  private BaseFontSupport baseFontSupport;

  /**
   * Creates a new RTF Layout info object. This object collects the
   * grid positions and the font definitions used during the repagination.
   * 
   * @param globalLayout a flag indicating whether to generate a global
   * layout for all pages.
   * @param format the page format used to generate the report.
   */
  public RTFLayoutInfo(boolean globalLayout, PageFormat format)
  {
    super(globalLayout, format);
    baseFontSupport = new BaseFontSupport();
  }

  /**
   * Returns the base font support used to create the fonts.
   * 
   * @return the base font support.
   */
  public BaseFontSupport getBaseFontSupport()
  {
    return baseFontSupport;
  }
}
