package org.jfree.fonts.pfm;

import org.jfree.fonts.io.FontDataInputSource;
import org.jfree.fonts.registry.DefaultFontFamily;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontSource;
import org.jfree.fonts.registry.FontType;

/**
 * Creation-Date: 21.07.2007, 19:12:20
 *
 * @author Thomas Morgner
 */
public class PfmFontRecord implements FontSource, FontIdentifier
{
  private PfmFont font;
  private DefaultFontFamily fontFamily;
  private boolean bold;
  private boolean italic;

  public PfmFontRecord(final PfmFont font, final DefaultFontFamily fontFamily)
  {
    this.font = font;
    this.fontFamily = fontFamily;
    final PfmFontHeader header = font.getHeader();
    this.bold = header.getWeight() > 400;
    this.italic = header.isItalic();
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
    return FontType.PFM;
  }
}
