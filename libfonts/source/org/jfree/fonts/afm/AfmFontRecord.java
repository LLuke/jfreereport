package org.jfree.fonts.afm;

import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.DefaultFontFamily;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontSource;
import org.jfree.fonts.registry.FontType;

/**
 * Creation-Date: 22.07.2007, 17:19:04
 *
 * @author Thomas Morgner
 */
public class AfmFontRecord implements FontSource, FontIdentifier
{
  private AfmFont font;
  private DefaultFontFamily fontFamily;
  private boolean bold;
  private boolean italic;

  public AfmFontRecord(final AfmFont font, final DefaultFontFamily fontFamily)
  {
    if (font == null)
    {
      throw new NullPointerException();
    }
    if (fontFamily == null)
    {
      throw new NullPointerException();
    }
    this.font = font;
    this.fontFamily = fontFamily;
    final AfmHeader header = font.getHeader();
    this.bold = header.getWeight() > 400;
    this.italic = font.getDirectionSection(0).getItalicAngle() != 0;
  }

  public FontFamily getFamily()
  {
    return fontFamily;
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalic()
  {
    return italic;
  }

  public boolean isOblique()
  {
    return false;
  }

  public FontIdentifier getIdentifier()
  {
    return this;
  }

  public String getFontFile()
  {
    return font.getFilename();
  }

  public FontDataInputSource getFontInputSource()
  {
    return font.getInput();
  }

  public boolean isEmbeddable()
  {
    return font.isEmbeddable();
  }

  public boolean isScalable()
  {
    return true;
  }

  public FontType getFontType()
  {
    return FontType.AFM;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final AfmFontRecord that = (AfmFontRecord) o;

    if (bold != that.bold)
    {
      return false;
    }
    if (italic != that.italic)
    {
      return false;
    }
    if (!font.equals(that.font))
    {
      return false;
    }
    if (!fontFamily.equals(that.fontFamily))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = font.hashCode();
    result = 29 * result + fontFamily.hashCode();
    result = 29 * result + (bold ? 1 : 0);
    result = 29 * result + (italic ? 1 : 0);
    return result;
  }
}
