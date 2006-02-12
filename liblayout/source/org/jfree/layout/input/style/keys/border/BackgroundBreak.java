package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:34:41
 *
 * @author Thomas Morgner
 */
public class BackgroundBreak extends CSSConstant
{
  public static final BackgroundBreak BOUNDING_BOX = new BackgroundBreak(
          "bounding-box");
  public static final BackgroundBreak EACH_BOX = new BackgroundBreak(
          "each-box");
  public static final BackgroundBreak CONTINUOUS = new BackgroundBreak(
          "continuous");

  private BackgroundBreak(String name)
  {
    super(name);
  }
}
