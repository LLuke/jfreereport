package org.jfree.layouting.input.style.keys.color;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 18:58:52
 *
 * @author Thomas Morgner
 */
public class RenderingIntent extends CSSConstant
{
  public static final RenderingIntent PERCEPTUAL = new RenderingIntent(
          "perceptual");
  public static final RenderingIntent RELATIVE_COLORIMETRIC = new RenderingIntent(
          "relative-colorimetric");
  public static final RenderingIntent SATURATION = new RenderingIntent(
          "saturation");
  public static final RenderingIntent ABSOLUTE_COLORIMETRIC = new RenderingIntent(
          "absolute-colorimetric");

  private RenderingIntent(String name)
  {
    super(name);
  }
}
