package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

public class FitPosition extends CSSConstant
{
  public static final FitPosition TOP = new FitPosition("top");
  public static final FitPosition LEFT = new FitPosition("left");
  public static final FitPosition BOTTOM = new FitPosition("bottom");
  public static final FitPosition RIGHT = new FitPosition("right");
  public static final FitPosition CENTER = new FitPosition("center");

  private FitPosition (final String constant)
  {
    super(constant);
  }
}
