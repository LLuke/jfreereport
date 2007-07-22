package org.jfree.fonts.merge;

import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontType;
import org.jfree.fonts.registry.FontRegistry;

/**
 * Creation-Date: 20.07.2007, 19:31:00
 *
 * @author Thomas Morgner
 */
public class CompoundFontIdentifier implements FontIdentifier
{
  private FontIdentifier identifier;
  private FontRegistry registry;

  public CompoundFontIdentifier(final FontIdentifier identifier,
                                final FontRegistry registry)
  {
    this.registry = registry;
    this.identifier = identifier;
  }

  public FontIdentifier getIdentifier()
  {
    return identifier;
  }

  public boolean equals(final Object o)
  {
    return identifier.equals(o);
  }

  public int hashCode()
  {
    return identifier.hashCode();
  }

  public boolean isScalable()
  {
    return identifier.isScalable();
  }

  public FontType getFontType()
  {
    return identifier.getFontType();
  }

  public FontRegistry getRegistry()
  {
    return registry;
  }
}
