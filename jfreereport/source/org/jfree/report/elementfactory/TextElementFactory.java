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
 * TextElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 06.07.2003 : Initial version
 *  
 */

package org.jfree.report.elementfactory;

import java.awt.Color;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;

public abstract class TextElementFactory extends ElementFactory
{
  private String fontName;
  private Boolean bold;
  private Boolean italic;
  private Boolean underline;
  private Boolean strikethrough;
  private Boolean embedFont;
  private Integer fontSize;
  private Integer lineHeight;
  private Color color;
  private String encoding;
  private ElementAlignment verticalAlignment;
  private ElementAlignment horizontalAlignment;

  public TextElementFactory()
  {
  }

  public Boolean getEmbedFont()
  {
    return embedFont;
  }

  public void setEmbedFont(Boolean embedFont)
  {
    this.embedFont = embedFont;
  }

  public String getFontName()
  {
    return fontName;
  }

  public void setFontName(String fontName)
  {
    this.fontName = fontName;
  }

  public Boolean getBold()
  {
    return bold;
  }

  public void setBold(Boolean bold)
  {
    this.bold = bold;
  }

  public Boolean getItalic()
  {
    return italic;
  }

  public void setItalic(Boolean italic)
  {
    this.italic = italic;
  }

  public Boolean getUnderline()
  {
    return underline;
  }

  public void setUnderline(Boolean underline)
  {
    this.underline = underline;
  }

  public Boolean getStrikethrough()
  {
    return strikethrough;
  }

  public void setStrikethrough(Boolean strikethrough)
  {
    this.strikethrough = strikethrough;
  }

  public Integer getFontSize()
  {
    return fontSize;
  }

  public void setFontSize(Integer fontSize)
  {
    this.fontSize = fontSize;
  }

  public Integer getLineHeight()
  {
    return lineHeight;
  }

  public void setLineHeight(Integer lineHeight)
  {
    this.lineHeight = lineHeight;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }

  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public void setVerticalAlignment(ElementAlignment verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  public void setHorizontalAlignment(ElementAlignment horizontalAlignment)
  {
    this.horizontalAlignment = horizontalAlignment;
  }

  protected void applyStyle (ElementStyleSheet style)
  {
    style.setStyleProperty(StaticLayoutManager.ABSOLUTE_POS, getAbsolutePosition());
    style.setStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT, getDynamicHeight());
    style.setStyleProperty(ElementStyleSheet.MAXIMUMSIZE, getMaximumSize());
    style.setStyleProperty(ElementStyleSheet.MINIMUMSIZE, getMinimumSize());
    style.setStyleProperty(ElementStyleSheet.PREFERREDSIZE, getPreferredSize());
    style.setStyleProperty(ElementStyleSheet.ALIGNMENT, getHorizontalAlignment());
    style.setStyleProperty(ElementStyleSheet.BOLD, getBold());
    style.setStyleProperty(ElementStyleSheet.EMBEDDED_FONT, getEmbedFont());
    style.setStyleProperty(ElementStyleSheet.FONT, getFontName());
    style.setStyleProperty(ElementStyleSheet.FONTENCODING, getEncoding());
    style.setStyleProperty(ElementStyleSheet.FONTSIZE, getFontSize());
    style.setStyleProperty(ElementStyleSheet.ITALIC, getItalic());
    style.setStyleProperty(ElementStyleSheet.LINEHEIGHT, getLineHeight());
    style.setStyleProperty(ElementStyleSheet.PAINT, getColor());
    style.setStyleProperty(ElementStyleSheet.STRIKETHROUGH, getStrikethrough());
    style.setStyleProperty(ElementStyleSheet.UNDERLINED, getUnderline());
    style.setStyleProperty(ElementStyleSheet.VALIGNMENT, getVerticalAlignment());
  }
}
