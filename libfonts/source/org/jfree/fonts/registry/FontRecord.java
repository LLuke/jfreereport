package org.jfree.fonts.registry;

import java.io.Serializable;

/**
 * Creation-Date: 07.11.2005, 19:07:09
 *
 * @author Thomas Morgner
 */
public interface FontRecord extends Serializable
{
  /**
   * Returns the family for this record.
   *
   * @return the font family.
   */
  public FontFamily getFamily();

  /**
   * Returns true, if this font corresponds to a bold version of the font. A
   * font that does not provide a bold face must emulate the boldness using
   * other means.
   *
   * @return true, if the font provides bold glyphs, false otherwise.
   */
  public boolean isBold();

  public boolean isItalics ();

  public String getFontFile ();

  public boolean isEmbeddable();

  public String getName ();

  public String[] getAllNames ();

  public String getVariant ();

  public String[] getAllVariants ();
}
