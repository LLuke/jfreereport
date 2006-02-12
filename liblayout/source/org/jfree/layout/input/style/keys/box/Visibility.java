package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 18:43:27
 *
 * @author Thomas Morgner
 */
public class Visibility extends CSSConstant
{
  public static final Visibility VISIBLE = new Visibility("visible");
  public static final Visibility HIDDEN = new Visibility("hidden");
  public static final Visibility COLLAPSE = new Visibility("collapse");

  private Visibility(String name)
  {
    super(name);
  }
}
