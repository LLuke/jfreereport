package org.jfree.layouting.input.style.keys.line;

import org.jfree.layouting.input.style.values.CSSConstant;

public class LineHeight extends CSSConstant
{
  public static final LineHeight NORMAL = new LineHeight("normal");
  public static final LineHeight NONE = new LineHeight("none");

  private LineHeight (final String constant)
  {
    super(constant);
  }
}
