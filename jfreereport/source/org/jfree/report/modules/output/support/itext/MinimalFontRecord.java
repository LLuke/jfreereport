package org.jfree.report.modules.output.support.itext;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.io.FontDataInputSource;

/**
 * Creation-Date: 09.11.2005, 20:32:33
 *
 * @author Thomas Morgner
 */
public class MinimalFontRecord implements FontRecord
{
  private boolean bold;
  private boolean italics;
  private boolean embeddable;
  private String fontFile;
  private String fontName;

  public MinimalFontRecord(final String fontName,
                           final String fontFile,
                           final boolean bold,
                           final boolean italics,
                           final boolean embeddable)
  {
    this.fontName = fontName;
    this.fontFile = fontFile;
    this.bold = bold;
    this.italics = italics;
    this.embeddable = embeddable;
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalic()
  {
    return italics;
  }

  /**
   * Returns tue, if this font's italic mode is in fact some sort of being
   * oblique. An oblique font's glyphs are sheared, but they are not made to
   * look more script like.
   *
   * iText or JFreeReport's current code does not use this hint, so it is safe
   * to return false here.
   *
   * @return always false
   */
  public boolean isOblique()
  {
    return italics;
  }

  public String getFontFile()
  {
    return fontFile;
  }

  public FontFamily getFamily()
  {
    return null;
  }

  public boolean isEmbeddable()
  {
    return embeddable;
  }

  public String getName()
  {
    return fontName;
  }

  public String[] getAllNames()
  {
    return new String[] { fontName };
  }

  public String getVariant()
  {
    return "";
  }

  public String[] getAllVariants()
  {
    return new String[] { "" };
  }

  public FontDataInputSource getFontInputSource()
  {
    return null;
  }

  public FontIdentifier getIdentifier()
  {
    return null;
  }
}
