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
 * TextElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TextElementFactory.java,v 1.6 2003/08/25 14:29:28 taqua Exp $
 *
 * Changes
 * -------------------------
 * 06-Jul-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.Color;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;

/**
 * The text element factory is the abstract base class for all
 * text element factory implementations. It provides common properties
 * shared among all text elements.
 *
 * @author Thomas Morgner
 */
public abstract class TextElementFactory extends ElementFactory
{
  /** The name of the font used to print the text. */
  private String fontName;
  /** A flag defining whether to use the bold font style. */
  private Boolean bold;
  /** A flag defining whether to use the italic font style. */
  private Boolean italic;
  /** A flag defining whether to use the underline font style. */
  private Boolean underline;
  /** A flag defining whether to use the strikethough font style. */
  private Boolean strikethrough;
  /** A flag defining whether to use the embed the font where possible. */
  private Boolean embedFont;
  /** Defines the font size of the used font. */
  private Integer fontSize;
  /** Defines the lineheight. The lineheight must be >= the font size, or it is ignored. */
  private Integer lineHeight;
  /** Defines the text color. */
  private Color color;
  /** Defines the font encoding used when writing the text. */
  private String encoding;
  /** Defines the vertical alignment of the content. */
  private ElementAlignment verticalAlignment;
  /** Defines the horizontal alignment of the content. */
  private ElementAlignment horizontalAlignment;
  /** The reserved literal. */
  private String reservedLiteral;

  /**
   * Default Constructor.
   */
  public TextElementFactory()
  {
  }

  /**
   * Returns the font embedding flag for the new text elements. Font embedding
   * is only used in some output targets.
   *
   * @return the font embedding flag.
   */
  public Boolean getEmbedFont()
  {
    return embedFont;
  }

  /**
   * Defines that the font should be embedded if possible.
   *
   * @param embedFont embedds the font if possible.
   */
  public void setEmbedFont(final Boolean embedFont)
  {
    this.embedFont = embedFont;
  }

  /**
   * Returns the name of the font that should be used to print the text.
   *
   * @return the font name.
   */
  public String getFontName()
  {
    return fontName;
  }

  /**
   * Defines the name of the font that should be used to print the text.
   *
   * @param fontName the name of the font.
   */
  public void setFontName(final String fontName)
  {
    this.fontName = fontName;
  }

  /**
   * Returns the state of the bold flag for the font. This method may return
   * null to indicate that that value should be inherited from the parents.
   *
   * @return the bold-flag.
   */
  public Boolean getBold()
  {
    return bold;
  }

  /**
   * Defines the state of the bold flag for the font. This value may be set to
   * null to indicate that that value should be inherited from the parents.
   *
   * @param bold the bold-flag.
   */
  public void setBold(final Boolean bold)
  {
    this.bold = bold;
  }

  /**
   * Returns the state of the italic flag for the font. This method may return
   * null to indicate that that value should be inherited from the parents.
   *
   * @return the italic-flag.
   */
  public Boolean getItalic()
  {
    return italic;
  }

  /**
   * Defines the state of the italic flag for the font. This value may be set to
   * null to indicate that that value should be inherited from the parents.
   *
   * @param italic the italic-flag.
   */
  public void setItalic(final Boolean italic)
  {
    this.italic = italic;
  }

  /**
   * Returns the state of the underline flag for the font. This method may return
   * null to indicate that that value should be inherited from the parents.
   *
   * @return the underline-flag.
   */
  public Boolean getUnderline()
  {
    return underline;
  }

  /**
   * Defines the state of the underline flag for the font. This value may be set to
   * null to indicate that that value should be inherited from the parents.
   *
   * @param underline the underline-flag.
   */
  public void setUnderline(final Boolean underline)
  {
    this.underline = underline;
  }

  /**
   * Returns the state of the strike through flag for the font. This method may return
   * null to indicate that that value should be inherited from the parents.
   *
   * @return the strike-through-flag.
   */
  public Boolean getStrikethrough()
  {
    return strikethrough;
  }

  /**
   * Defines the state of the strike through flag for the font. This value may be set to
   * null to indicate that that value should be inherited from the parents.
   *
   * @param strikethrough the strikethrough-flag.
   */
  public void setStrikethrough(final Boolean strikethrough)
  {
    this.strikethrough = strikethrough;
  }

  /**
   * Returns the font size in points.
   *
   * @return the font size.
   */
  public Integer getFontSize()
  {
    return fontSize;
  }

  /**
   * Returns the font size in points.
   *
   * @param fontSize the font size.
   */
  public void setFontSize(final Integer fontSize)
  {
    this.fontSize = fontSize;
  }

  /**
   * Returns the lineheight defined for the text element. The lineheight must be greater
   * than the font size, or this value will be ignored.
   *
   * @return the line height.
   */
  public Integer getLineHeight()
  {
    return lineHeight;
  }

  /**
   * Defines the lineheight defined for the text element. The lineheight must be greater
   * than the font size, or this value will be ignored.
   *
   * @param lineHeight the line height.
   */
  public void setLineHeight(final Integer lineHeight)
  {
    this.lineHeight = lineHeight;
  }

  /**
   * Returns the text color for the new element.
   *
   * @return the text color.
   */
  public Color getColor()
  {
    return color;
  }

  /**
   * Defines the text color for the new element.
   *
   * @param color the text color.
   */
  public void setColor(final Color color)
  {
    this.color = color;
  }

  /**
   * Returns the font encoding used to write the text. This parameter is only
   * used by some output targets and will be ignored otherwise.
   *
   * @return the font encoding.
   */
  public String getEncoding()
  {
    return encoding;
  }

  /**
   * Defines the font encoding used to write the text. This parameter is only
   * used by some output targets and will be ignored otherwise.
   *
   * @param encoding the font encoding.
   */
  public void setEncoding(final String encoding)
  {
    this.encoding = encoding;
  }

  /**
   * Returns the vertical alignment for the content of this text element.
   *
   * @return the vertical alignment.
   */
  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  /**
   * Defines the vertical alignment for the content of this text element.
   *
   * @param verticalAlignment the vertical alignment.
   */
  public void setVerticalAlignment(final ElementAlignment verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  /**
   * Returns the horizontal alignment for the content of this text element.
   *
   * @return the horizontal alignment.
   */
  public ElementAlignment getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  /**
   * Defines the horizontal alignment for the content of this text element.
   *
   * @param horizontalAlignment the vertical alignment.
   */
  public void setHorizontalAlignment(final ElementAlignment horizontalAlignment)
  {
    this.horizontalAlignment = horizontalAlignment;
  }

  public String getReservedLiteral()
  {
    return reservedLiteral;
  }

  public void setReservedLiteral(String reservedLiteral)
  {
    this.reservedLiteral = reservedLiteral;
  }

  /**
   * Applies the defined element style to the given stylesheet. This is a
   * helper function to reduce the code size of the implementors.
   *
   * @param style the stlyesheet.
   */
  protected void applyStyle(final ElementStyleSheet style)
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
    style.setStyleProperty(ElementStyleSheet.RESERVED_LITERAL, getReservedLiteral());
  }
}
