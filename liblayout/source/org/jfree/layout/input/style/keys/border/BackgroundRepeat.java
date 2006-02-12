package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:14:47
 *
 * @author Thomas Morgner
 */
public class BackgroundRepeat  extends CSSConstant
{
  public static final BackgroundRepeat REPEAT = new BackgroundRepeat("repeat");
  public static final BackgroundRepeat NOREPEAT = new BackgroundRepeat("no-repeat");
  public static final BackgroundRepeat SPACE = new BackgroundRepeat("space");

  public static final CSSConstant REPEAT_X = new CSSConstant("repeat-y");
  public static final CSSConstant REPEAT_Y = new CSSConstant("repeat-x");

  private BackgroundRepeat(String name)
  {
    super(name);
  }

}
