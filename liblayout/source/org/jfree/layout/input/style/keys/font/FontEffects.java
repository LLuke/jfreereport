/**
 * Creation-Date: 26.10.2005, 14:11:39
 * 
 * @author Thomas Morgner
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;

public class FontEffects extends CSSConstant
{
  public static final FontEffects NONE = new FontEffects("none");
  public static final FontEffects EMBOSS = new FontEffects("emboss");
  public static final FontEffects ENGRAVE = new FontEffects("engrave");
  public static final FontEffects OUTLINE = new FontEffects("outline");

  private FontEffects(String name)
  {
    super(name);
  }
}
