package org.jfree.report.modules.output.support.itext;

import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.DefaultFontFamily;
import org.jfree.fonts.registry.FontFamily;

/**
 * Creation-Date: 09.11.2005, 20:32:33
 *
 * @author Thomas Morgner
 */
public class MinimalFontRecord implements FontRecord
{
  private boolean bold;
  private boolean italics;
  private String fontFile;
  private String fontName;

  public MinimalFontRecord(final String fontName,
                           final String fontFile,
                           final boolean bold, final boolean italics)
  {
    this.fontName = fontName;
    this.fontFile = fontFile;
    this.bold = bold;
    this.italics = italics;
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalics()
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
    return false;
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
}
