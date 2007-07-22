package org.jfree.fonts.itext;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontType;

/**
 * Creation-Date: 20.07.2007, 19:59:37
 *
 * @author Thomas Morgner
 */
public class ITextFontRecord implements FontRecord, FontIdentifier
{
  private FontFamily fontFamily;
  private boolean bold;
  private boolean italic;
  private boolean oblique;

  public ITextFontRecord(final FontFamily fontFamily,
                         final boolean bold,
                         final boolean italic,
                         final boolean oblique)
  {
    this.fontFamily = fontFamily;
    this.bold = bold;
    this.italic = italic;
    this.oblique = oblique;
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
    return oblique;
  }

  public FontIdentifier getIdentifier()
  {
    return this;
  }

  public boolean isScalable()
  {
    return true;
  }

  public FontType getFontType()
  {
    return FontType.OTHER;
  }
}
