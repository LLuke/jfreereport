package org.jfree.fonts.registry;

import java.io.Serializable;

/**
 * Creation-Date: 07.11.2005, 19:06:25
 *
 * @author Thomas Morgner
 */
public interface FontFamily extends Serializable
{
  /**
   * Returns the name of the font family (in english).
   *
   * @return
   */
  public String getFamilyName ();

  public String[] getAllNames ();

  public FontRecord getFontRecord (final boolean bold, final boolean italics);
}
