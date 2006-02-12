package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:22:18
 *
 * @author Thomas Morgner
 */
public class BackgroundOrigin extends CSSConstant
{
  public static final BackgroundOrigin BORDER = new BackgroundOrigin("border");
  public static final BackgroundOrigin PADDING = new BackgroundOrigin("padding");
  public static final BackgroundOrigin CONTENT = new BackgroundOrigin("content");

  private BackgroundOrigin(String name)
  {
    super(name);
  }
}
