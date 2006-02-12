package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:22:18
 *
 * @author Thomas Morgner
 */
public class BackgroundClip extends CSSConstant
{
  public static final BackgroundClip BORDER = new BackgroundClip("border");
  public static final BackgroundClip PADDING = new BackgroundClip("padding");

  private BackgroundClip(String name)
  {
    super(name);
  }
}
