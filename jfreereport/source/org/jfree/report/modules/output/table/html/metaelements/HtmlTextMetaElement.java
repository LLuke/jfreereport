/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * HtmlTextMetaElement.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: HtmlTextMetaElement.java,v 1.7 2005/03/29 20:04:54 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 09.03.2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.html.metaelements;

import java.io.PrintWriter;

import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.modules.output.table.html.HtmlFilesystem;
import org.jfree.report.modules.output.table.html.util.HtmlEncoderUtil;
import org.jfree.report.style.ElementStyleSheet;

public class HtmlTextMetaElement extends HtmlMetaElement
{
  public HtmlTextMetaElement (final RawContent elementContent,
                              final ElementStyleSheet style, 
                              final boolean usesXHTML)
  {
    super(elementContent, style, usesXHTML);
  }

  public void write (final PrintWriter pout, 
                     final HtmlFilesystem filesystem,
                     final boolean emptyCellsUseCSS)
  {
    final RawContent content = (RawContent) getContent();
    final String text = String.valueOf(content.getContent());
    if (text.trim().length() == 0)
    {
      if (emptyCellsUseCSS == false)
      {
        pout.write("&nbsp;");
      }
    }
    else
    {
      HtmlEncoderUtil.printText(pout, text, isUsesXHTML());
    }
  }
}
