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
 * RTFMetaElement.java
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
 * Mar 14, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.rtf.metaelements;

import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.content.Content;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.ElementAlignment;
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

public abstract class RTFMetaElement extends MetaElement
{
  public RTFMetaElement (final Content elementContent,
                         final ElementStyleSheet style)
  {
    super(elementContent, style);
  }

    /**
   * Defines the content alignment for the given iText cell.
   *
   * @param cell the iText cell, that should be formated.
   */
  protected void applyAlignment(final Cell cell)
  {
    final ElementAlignment horizontalAlignment = (ElementAlignment)
            getProperty(ElementStyleSheet.ALIGNMENT);

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

    final ElementAlignment verticalAlignment = (ElementAlignment)
            getProperty(ElementStyleSheet.VALIGNMENT);

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

  /**
   * Creates a iText TableCell with some content in it.
   *
   * @return the cell with the content.
   * @throws DocumentException if there is a problem adding the cell to the document.
   */
  public abstract Cell getCell() throws DocumentException;
}
