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
 * $Id: RTFLayoutInfo.java,v 1.1 2003/07/14 17:40:06 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 13.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.rtf;

import org.jfree.report.modules.output.support.itext.BaseFontSupport;
import org.jfree.report.modules.output.table.base.TableLayoutInfo;

public class RTFLayoutInfo extends TableLayoutInfo
{
  /** The baseFontSupport is used to handle truetype fonts in iText. */
  private BaseFontSupport baseFontSupport;

  public RTFLayoutInfo(boolean globalLayout)
  {
    super(globalLayout);
    baseFontSupport = new BaseFontSupport();
  }

  public BaseFontSupport getBaseFontSupport()
  {
    return baseFontSupport;
  }
}
