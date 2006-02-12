package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 18:34:39
 *
 * @author Thomas Morgner
 */
public class Overflow extends CSSConstant
{
  public static final Overflow VISIBLE = new Overflow("visible");
  public static final Overflow HIDDEN = new Overflow("hidden");
  public static final Overflow SCROLL = new Overflow("scroll");

  private Overflow(String name)
  {
    super(name);
  }
}
