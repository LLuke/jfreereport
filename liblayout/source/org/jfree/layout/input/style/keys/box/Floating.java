package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Defines the floating property. Floating elements create a new flow inside
 * an existing flow.
 * <p>
 * The properties left and top are equivalent, as as right and bottom.
 * All properties in the specification can be reduced to either left or
 * right in the computation phase.
 * <p>
 * Floating images cannot leave their containing block vertically or horizontally.
 * If negative margins are given, they may be shifted outside the content area,
 * but vertical margins will increase the 'empty-space' between the blocks instead
 * of messing up the previous element.
 *
 * @author Thomas Morgner
 */
public class Floating extends CSSConstant
{
  public static final Floating LEFT = new Floating("left");
  public static final Floating RIGHT = new Floating("right");
  public static final Floating TOP = new Floating("top");
  public static final Floating BOTTOM = new Floating("bottom");
  public static final Floating INSIDE = new Floating("inside");
  public static final Floating OUTSIDE = new Floating("outside");
  public static final Floating START = new Floating("start");
  public static final Floating END = new Floating("end");
  public static final Floating NONE = new Floating("none");

  // from the column stuff
  public static final Floating IN_COLUMN = new Floating("in-column");
  public static final Floating MID_COLUMN = new Floating("mid-column");

  private Floating(String name)
  {
    super(name);
  }

}
