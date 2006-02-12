package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * This is an optional (future) implementation placeholder.
 *
 * @author Thomas Morgner
 */
public class FloatingType extends CSSConstant
{
  public static final FloatingType BOX = new FloatingType("box");
  public static final FloatingType SHAPE = new FloatingType("shape");

  private FloatingType(String name)
  {
    super(name);
  }

}
