/**
 * Creation-Date: 26.10.2005, 14:48:43
 * 
 * @author Thomas Morgner
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;

public class FontVariant extends CSSConstant
{
  public static final FontVariant NORMAL = new FontVariant("normal");
  public static final FontVariant SMALL_CAPS = new FontVariant("small-caps");

  private FontVariant(String name)
  {
    super(name);
  }
}
