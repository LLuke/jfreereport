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
 * RTFTextMetaElement.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: RTFTextMetaElement.java,v 1.2.2.1 2004/12/13 19:27:11 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Mar 14, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.rtf.metaelements;

import java.awt.Color;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.table.RtfCell;
import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.output.table.base.RawContent;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;

public class RTFTextMetaElement extends RTFMetaElement
{
  private BaseFont baseFont;

  public RTFTextMetaElement (final RawContent elementContent,
                             final ElementStyleSheet style,
                             final BaseFont baseFont)
  {
    super(elementContent, style);
    this.baseFont = baseFont;
  }

  /**
   * Creates a iText TableCell with text content in it.
   *
   * @return the cell with the content.
   * @throws com.lowagie.text.DocumentException if the cell could not be created.
   */
  public RtfCell getCell()
      throws DocumentException
  {
    final RtfCell cell = new RtfCell();
    cell.setBorderWidth(0);

    final RawContent rc = (RawContent) getContent();
    final Chunk chunk = new Chunk(String.valueOf(rc.getContent()));
    applyTextStyle(chunk);
    final Paragraph paragraph = new Paragraph();
    paragraph.add(chunk);
    paragraph.setAlignment(getHorizontalAlignment());
    cell.setVerticalAlignment(getVerticalAlignment());
    cell.addElement(paragraph);
    return cell;
  }

  private int getVerticalAlignment ()
  {
    final ElementAlignment alignment = (ElementAlignment)
            getProperty(ElementStyleSheet.VALIGNMENT, ElementAlignment.TOP);
    if (alignment.equals(ElementAlignment.MIDDLE))
    {
      return Element.ALIGN_MIDDLE;
    }
    if (alignment.equals(ElementAlignment.BOTTOM))
    {
      return Element.ALIGN_BOTTOM;
    }
    return Element.ALIGN_TOP;
  }

  private int getHorizontalAlignment ()
  {
    final ElementAlignment alignment = (ElementAlignment)
            getProperty(ElementStyleSheet.ALIGNMENT, ElementAlignment.LEFT);
    if (alignment.equals(ElementAlignment.CENTER))
    {
      return Element.ALIGN_CENTER;
    }
    if (alignment.equals(ElementAlignment.RIGHT))
    {
      return Element.ALIGN_RIGHT;
    }
    return Element.ALIGN_LEFT;
  }

  /**
   * Define the font for the given iText Chunk.
   *
   * @param p the iText chunk, which should be formated.
   */
  public void applyTextStyle(final Chunk p)
  {
    final FontDefinition font = getFontDefinitionProperty();
    final Color paint = (Color) getProperty(ElementStyleSheet.PAINT, Color.black);
    int style = Font.NORMAL;
    if (font.isBold())
    {
      style += Font.BOLD;
    }
    if (font.isItalic())
    {
      style += Font.ITALIC;
    }
    if (font.isStrikeThrough())
    {
      style += Font.STRIKETHRU;
    }
    if (font.isUnderline())
    {
      style += Font.UNDERLINE;
    }
    p.setFont(new Font(baseFont, font.getFontSize(), style, paint));
  }
  
}
