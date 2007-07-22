package org.jfree.fonts.merge;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontRegistry;

/**
 * Creation-Date: 20.07.2007, 18:54:28
 *
 * @author Thomas Morgner
 */
public class CompoundFontFamily implements FontFamily
{
  private FontFamily base;
  private FontRegistry registry;

  public CompoundFontFamily(final FontFamily base,
                            final FontRegistry registry)
  {
    if (registry instanceof CompoundFontRegistry)
    {
      throw new IllegalStateException();
    }
    this.base = base;
    this.registry = registry;
  }

  public FontRegistry getRegistry()
  {
    return registry;
  }

  public String getFamilyName()
  {
    return base.getFamilyName();
  }

  public String[] getAllNames()
  {
    return base.getAllNames();
  }

  public FontRecord getFontRecord(final boolean bold, final boolean italics)
  {
    return new CompoundFontRecord(base.getFontRecord(bold, italics), this);
  }
}
