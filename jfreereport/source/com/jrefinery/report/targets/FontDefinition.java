/**
 * Date: Jan 24, 2003
 * Time: 4:08:26 PM
 *
 * $Id: FontDefinition.java,v 1.4 2003/02/02 22:46:44 taqua Exp $
 */
package com.jrefinery.report.targets;

import com.jrefinery.report.util.StringUtil;

import java.awt.Font;
import java.io.Serializable;

public class FontDefinition implements Serializable, Cloneable
{
  public static boolean BOLD = true;
  public static boolean ITALIC = true;
  public static boolean UNDERLINE = true;
  public static boolean STRIKETHROUGH = true;
  public static boolean PLAIN = false;

  private String fontEncoding;
  private String fontName;
  private int fontSize;
  private boolean isBold;
  private boolean isItalic;
  private boolean isUnderline;
  private boolean isStrikeThrough;
  private transient Font font;
  private boolean embeddedFont;
  // color is defined elsewhere

  public FontDefinition(String fontName, int fontSize, boolean bold, boolean italic, boolean underline, boolean strikeThrough, String encoding, boolean embedded)
  {
    if (fontName == null) throw new NullPointerException();
    if (fontSize < 0) throw new IllegalArgumentException();
    this.fontName = fontName;
    this.fontSize = fontSize;
    isBold = bold;
    isItalic = italic;
    isUnderline = underline;
    isStrikeThrough = strikeThrough;
    this.fontEncoding = encoding;
    this.embeddedFont = embedded;
  }

  public FontDefinition(String fontName, int fontSize, boolean bold, boolean italic, boolean underline, boolean strikeThrough)
  {
    this (fontName, fontSize, bold, italic, underline, strikeThrough, null, false);
  }

  public FontDefinition(String fontName, int fontSize)
  {
    this (fontName, fontSize, false, false, false, false, null, false);
  }

  public String getFontName()
  {
    return fontName;
  }

  public int getFontSize()
  {
    return fontSize;
  }

  public boolean isEmbeddedFont()
  {
    return embeddedFont;
  }

  public boolean isBold()
  {
    return isBold;
  }

  public boolean isItalic()
  {
    return isItalic;
  }

  public boolean isUnderline()
  {
    return isUnderline;
  }

  public boolean isStrikeThrough()
  {
    return isStrikeThrough;
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone();
  }

  private int getFontStyle ()
  {
    int fontstyle = Font.PLAIN;
    if (isBold())
    {
      fontstyle += Font.BOLD;
    }
    if (isItalic())
    {
      fontstyle += Font.ITALIC;
    }
    return fontstyle;
  }

  public Font getFont ()
  {
    if (font == null)
    {
      font = new Font (getFontName(), getFontStyle(), getFontSize());
    }
    return font;
  }

  public String getFontEncoding (String defaultEncoding)
  {
    if (this.fontEncoding == null)
      return defaultEncoding;
    else
      return fontEncoding;
  }


  public String toString ()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("FontDefinition='fontname=\"");
    buffer.append(fontName);
    buffer.append("; fontSize=" + fontSize);
    buffer.append("; bold=" + isBold);
    buffer.append("; italic=" + isItalic);
    buffer.append("; underline=" + isUnderline);
    buffer.append("; strike=" + isStrikeThrough);
    buffer.append("; embedded=" + embeddedFont);
    buffer.append ("'");
    return buffer.toString();
  }

  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof FontDefinition)) return false;

    final FontDefinition definition = (FontDefinition) o;

    if (embeddedFont != definition.embeddedFont) return false;
    if (fontSize != definition.fontSize) return false;
    if (isBold != definition.isBold) return false;
    if (isItalic != definition.isItalic) return false;
    if (isStrikeThrough != definition.isStrikeThrough) return false;
    if (isUnderline != definition.isUnderline) return false;
    if (fontEncoding != null ? !fontEncoding.equals(definition.fontEncoding) : definition.fontEncoding != null) return false;
    if (!fontName.equals(definition.fontName)) return false;

    return true;
  }

  public int hashCode()
  {
    int result;
    result = (fontEncoding != null ? fontEncoding.hashCode() : 0);
    result = 29 * result + fontName.hashCode();
    result = 29 * result + fontSize;
    result = 29 * result + (isBold ? 1 : 0);
    result = 29 * result + (isItalic ? 1 : 0);
    result = 29 * result + (isUnderline ? 1 : 0);
    result = 29 * result + (isStrikeThrough ? 1 : 0);
    result = 29 * result + (embeddedFont ? 1 : 0);
    return result;
  }

  /**
   * Returns true if the logical font name is equivalent to 'SansSerif', and false otherwise.
   *
   * @return true or false.
   */
  public boolean isSansSerif ()
  {
    return StringUtil.startsWithIgnoreCase(fontName, "SansSerif")
        || StringUtil.startsWithIgnoreCase(fontName, "Dialog");
  }

  /**
   * Returns true if the logical font name is equivalent to 'Courier', and false otherwise.
   *
   * @return true or false.
   */
  public boolean isCourier ()
  {
     return (StringUtil.startsWithIgnoreCase(fontName, "dialoginput")
        || StringUtil.startsWithIgnoreCase(fontName, "monospaced"));
  }

  /**
   * Returns true if the logical font name is equivalent to 'Serif', and false otherwise.
   *
   * @return true or false.
   */
  public boolean isSerif ()
  {
    return (StringUtil.startsWithIgnoreCase(fontName, "serif"));
  }


}
