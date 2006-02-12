package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:18:06
 *
 * @author Thomas Morgner
 */
public class BackgroundAttachment extends CSSConstant
{
  public static final BackgroundAttachment SCROLL = new BackgroundAttachment(
          "scroll");
  public static final BackgroundAttachment FIXED = new BackgroundAttachment(
          "fixed");
  public static final BackgroundAttachment LOCAL = new BackgroundAttachment(
          "local");

  private BackgroundAttachment(String name)
  {
    super(name);
  }
}
