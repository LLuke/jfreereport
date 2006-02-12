package org.jfree.layouting.input.style.keys.color;

import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * A set of color mappings which map the constants to system default values.
 * This *could* also include colors which map to standard UI colors, like
 * 'caption background' etc.
 *
 * @author Thomas Morgner
 */
public class CSSSystemColors
{
  private CSSSystemColors()
  {
  }

  public static CSSColorValue TRANSPARENT = new CSSColorValue(0,0,0,0);
  public static CSSConstant CURRENT_COLOR = new CSSConstant("currentColor");
}
