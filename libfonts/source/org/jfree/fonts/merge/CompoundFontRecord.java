package org.jfree.fonts.merge;

import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontIdentifier;

/**
 * Creation-Date: 20.07.2007, 18:55:08
 *
 * @author Thomas Morgner
 */
public class CompoundFontRecord implements FontRecord
{
  private FontRecord base;
  private CompoundFontFamily family;
  private FontIdentifier identifier;

  public CompoundFontRecord(final FontRecord base,
                            final CompoundFontFamily family)
  {
    this.base = base;
    this.family = family;
  }

  public FontRecord getBase()
  {
    return base;
  }

  public FontFamily getFamily()
  {
    return family;
  }

  public boolean isBold()
  {
    return base.isBold();
  }

  public boolean isItalic()
  {
    return base.isItalic();
  }

  public boolean isOblique()
  {
    return base.isOblique();
  }

  public synchronized FontIdentifier getIdentifier()
  {
    if (identifier == null)
    {
      identifier = new CompoundFontIdentifier(base.getIdentifier(), family.getRegistry());
    }
    return identifier;
  }
}
