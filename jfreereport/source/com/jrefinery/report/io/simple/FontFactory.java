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
 * ----------------
 * FontFactory.java
 * ----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * $Id: FontFactory.java,v 1.9 2003/05/11 13:39:17 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 20-May-2002 : The default font is no longer hardcoded into the factory. It uses the Bands
 *               default font as fallback.
 */
package com.jrefinery.report.io.simple;

import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.ParserUtil;
import org.xml.sax.Attributes;

/**
 * Parses the font specifications for bands and text elements.
 *
 * @author Thomas Morgner
 */
public class FontFactory implements ReportDefinitionTags
{
  /**
   * The FontInformation class is used to store the font definition, until it
   * can be applied to a stylesheet. Parameters that are not defined, are null.
   */
  public static class FontInformation
  {
    /** the font name. */
    private String fontname;

    /** the font size. */
    private Integer fontSize;

    /** the bold flag for the font. */
    private Boolean isBold;

    /** the italic flag for the font. */
    private Boolean isItalic;

    /** the strikeThrough flag for the font. */
    private Boolean isStrikeThrough;

    /** the underlined flag for the font. */
    private Boolean isUnderlined;

    /** the embedded flag for the font. */
    private Boolean isEmbedded;

    /** the font encoding for the font. */
    private String fontencoding;

    /** the line height for the font. */
    private Float lineHeight;

    /**
     * Creates a new FontInformation.
     */
    public FontInformation()
    {
    }

    /**
     * Gets the font name or null, if no font name is defined.
     *
     * @return the font name or null.
     */
    public String getFontname()
    {
      return fontname;
    }

    /**
     * Defines the font name or set to null, to indicate that no font name is defined.
     *
     * @param fontname the defined font name or null.
     */
    public void setFontname(String fontname)
    {
      this.fontname = fontname;
    }

    /**
     * Gets the font size or null, if no font size is defined.
     *
     * @return the font size or null.
     */
    public Integer getFontSize()
    {
      return fontSize;
    }

    /**
     * Defines the font size or set to null, to indicate that no font size is defined.
     *
     * @param fontSize the defined font size or null.
     */
    public void setFontSize(Integer fontSize)
    {
      this.fontSize = fontSize;
    }

    /**
     * Gets the bold flag or null, if this flag is undefined.
     *
     * @return the bold flag or null.
     */
    public Boolean getBold()
    {
      return isBold;
    }

    /**
     * Defines the bold flag or set to null, to indicate that this flag is undefined.
     *
     * @param bold the defined bold flag or null.
     */
    public void setBold(Boolean bold)
    {
      isBold = bold;
    }

    /**
     * Gets the italic flag or null, if this flag is undefined.
     *
     * @return the italic flag or null.
     */
    public Boolean getItalic()
    {
      return isItalic;
    }

    /**
     * Defines the italic flag or set to null, to indicate that this flag is undefined.
     *
     * @param italic the defined italic flag or null.
     */
    public void setItalic(Boolean italic)
    {
      isItalic = italic;
    }

    /**
     * Gets the strikeThrough flag or null, if this flag is undefined.
     *
     * @return the strikeThrough flag or null.
     */
    public Boolean getStrikeThrough()
    {
      return isStrikeThrough;
    }

    /**
     * Defines the strikeThrough flag or set to null, to indicate that this flag is undefined.
     *
     * @param strikeThrough the defined strikeThrough flag or null.
     */
    public void setStrikeThrough(Boolean strikeThrough)
    {
      isStrikeThrough = strikeThrough;
    }

    /**
     * Gets the underlined flag or null, if this flag is undefined.
     *
     * @return the underlined flag or null.
     */
    public Boolean getUnderlined()
    {
      return isUnderlined;
    }

    /**
     * Defines the underlined flag or set to null, to indicate that this flag is undefined.
     *
     * @param underlined the defined underlined flag or null.
     */
    public void setUnderlined(Boolean underlined)
    {
      isUnderlined = underlined;
    }

    /**
     * Gets the underlined flag or null, if this flag is undefined.
     *
     * @return the underlined flag or null.
     */
    public Boolean getEmbedded()
    {
      return isEmbedded;
    }

    /**
     * Defines the embedded flag or set to null, to indicate that this flag is undefined.
     *
     * @param embedded the defined embedded flag or null.
     */
    public void setEmbedded(Boolean embedded)
    {
      isEmbedded = embedded;
    }

    /**
     * Returns the defined character encoding for this font, or null, if no encoding is defined.
     *
     * @return the defined character encoding or null.
     */
    public String getFontencoding()
    {
      return fontencoding;
    }

    /**
     * Defines the character encoding for this font, or null, if no encoding is defined.
     *
     * @param fontencoding the character encoding or null.
     */
    public void setFontencoding(String fontencoding)
    {
      this.fontencoding = fontencoding;
    }

    /**
     * Returns the line height for this font, or null, if the line height is undefined.
     *
     * @return the defined line height or null.
     */
    public Float getLineHeight()
    {
      return lineHeight;
    }

    /**
     * Defines the line height for this font, or null, if the line height is undefined.
     *
     * @param lineHeight the defined line height or null.
     */
    public void setLineHeight(Float lineHeight)
    {
      this.lineHeight = lineHeight;
    }
  }

  /**
   * Default constructor.
   */
  public FontFactory()
  {
  }

  /**
   * Applies the font information to the ElementStyleSheet.
   *
   * @param es the element style sheet that should receive the font definition.
   * @param fi the previously parsed font information.
   */
  public static void applyFontInformation(ElementStyleSheet es, FontInformation fi)
  {
    if (fi.getFontname() != null)
    {
      es.setStyleProperty(ElementStyleSheet.FONT, fi.getFontname());
    }
    if (fi.getFontSize() != null)
    {
      es.setStyleProperty(ElementStyleSheet.FONTSIZE, fi.getFontSize());
    }
    if (fi.getItalic() != null)
    {
      es.setStyleProperty(ElementStyleSheet.ITALIC, fi.getItalic());
    }
    if (fi.getBold() != null)
    {
      es.setStyleProperty(ElementStyleSheet.BOLD, fi.getBold());
    }
    if (fi.getUnderlined() != null)
    {
      es.setStyleProperty(ElementStyleSheet.UNDERLINED, fi.getUnderlined());
    }
    if (fi.getStrikeThrough() != null)
    {
      es.setStyleProperty(ElementStyleSheet.STRIKETHROUGH, fi.getStrikeThrough());
    }
    if (fi.getEmbedded() != null)
    {
      es.setStyleProperty(ElementStyleSheet.EMBEDDED_FONT, fi.getEmbedded());
    }
    if (fi.getFontencoding() != null)
    {
      es.setStyleProperty(ElementStyleSheet.FONTENCODING, fi.getFontencoding());
    }
    if (fi.getLineHeight() != null)
    {
      es.setStyleProperty(ElementStyleSheet.LINEHEIGHT, fi.getLineHeight());
    }
  }

  /**
   * Reads an attribute as int and returns <code>def</code> if that fails.
   *
   * @param attr  the element attributes.
   * @param name  the attribute name.
   *
   * @return the int value.
   */
  protected Integer readInt(Attributes attr, String name)
  {
    String val = attr.getValue(name);
    if (val == null)
    {
      return null;
    }
    try
    {
      return new Integer(val);
    }
    catch (NumberFormatException e)
    {
      // swallow the exception, the default value will be returned
    }
    return null;
  }

  /**
   * Reads the fontstyle for an attribute set. The font style is appended to the given
   * font style definition and the font information is also returned to the caller.
   *
   * @param attr  the element attributes.
   * @param target the font information, that should be used to store the defined values.
   * @return the read font information.
   */
  private FontInformation readSimpleFontStyle(Attributes attr, FontInformation target)
  {
    if (target == null)
    {
      target = new FontInformation();
    }
    String fontStyle = attr.getValue(FONT_STYLE_ATT);

    if (fontStyle != null)
    {
      if (fontStyle.equals("bold"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.FALSE);
      }
      else if (fontStyle.equals("italic"))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.TRUE);
      }
      else if (fontStyle.equals("bold-italic"))
      {
        target.setBold(Boolean.TRUE);
        target.setItalic(Boolean.TRUE);
      }
      else if (fontStyle.equals("plain"))
      {
        target.setBold(Boolean.FALSE);
        target.setItalic(Boolean.FALSE);
      }
    }

    if (attr.getValue(FS_BOLD) != null)
    {
      target.setBold(getBoolean(ParserUtil.parseBoolean(attr.getValue(FS_BOLD), false)));
    }

    if (attr.getValue(FS_ITALIC) != null)
    {
      target.setItalic(getBoolean(ParserUtil.parseBoolean(attr.getValue(FS_ITALIC), false)));
    }

    if (attr.getValue(FS_STRIKETHR) != null)
    {
      target.setStrikeThrough(getBoolean(ParserUtil.parseBoolean(attr.getValue(FS_STRIKETHR),
          false)));
    }

    if (attr.getValue(FS_UNDERLINE) != null)
    {
      target.setUnderlined(getBoolean(ParserUtil.parseBoolean(attr.getValue(FS_UNDERLINE),
          false)));
    }

    if (attr.getValue(FS_EMBEDDED) != null)
    {
      target.setEmbedded(getBoolean(ParserUtil.parseBoolean(attr.getValue(FS_EMBEDDED), false)));
    }

    if (attr.getValue(FS_ENCODING) != null)
    {
      target.setFontencoding(attr.getValue(FS_UNDERLINE));
    }

    if (attr.getValue(LINEHEIGHT) != null)
    {
      target.setLineHeight(new Float(ParserUtil.parseFloat(attr.getValue(LINEHEIGHT), 0)));
    }

    return target;
  }

  /**
   * Parses an element font. Missing attributes are replaces with the default font's attributes.
   *
   * @param attr  the element attributes.
   * @param target the target element style sheet, that should receive the created font definition.
   * @throws ElementDefinitionException if the font cannot be created.
   */
  public void createFont(Attributes attr, ElementStyleSheet target)
      throws ElementDefinitionException
  {
    // get the font name...
    String elementFontName = attr.getValue(FONT_NAME_ATT);
    if (elementFontName != null)
    {
      target.setStyleProperty(ElementStyleSheet.FONT, elementFontName);
    }

    FontInformation fi = new FontInformation();
    // get the font style...
    applyFontInformation(target, readSimpleFontStyle(attr, fi));

    // get the font size...
    Integer elementFontSize = readInt(attr, FONT_SIZE_ATT);
    if (elementFontSize != null)
    {
      target.setStyleProperty(ElementStyleSheet.FONTSIZE, elementFontSize);
    }
  }

  /**
   * Parses an element font. Missing attributes are replaces with the default font's attributes.
   *
   * @param attr  the element attributes.
   * @return the created font information.
   * @throws ElementDefinitionException if the font cannot be created.
   */
  public FontInformation createFont(Attributes attr)
      throws ElementDefinitionException
  {
    // get the font name...
    FontInformation fi = new FontInformation();

    String elementFontName = attr.getValue(FONT_NAME_ATT);
    if (elementFontName != null)
    {
      fi.setFontname(elementFontName);
    }

    // get the font style...
    readSimpleFontStyle(attr, fi);

    // get the font size...
    Integer elementFontSize = readInt(attr, FONT_SIZE_ATT);
    if (elementFontSize != null)
    {
      fi.setFontSize(elementFontSize);
    }
    return fi;
  }

  /**
   * Returns the correct Boolean object for the given primitive boolean variable.
   *
   * @param bool the primitive boolean.
   * @return the Boolean object.
   */
  private Boolean getBoolean (boolean bool)
  {
    if (bool == true)
    {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
