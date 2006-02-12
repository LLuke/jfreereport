package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

public class BoxSizing extends CSSConstant
{
  public static final BoxSizing CONTENT_BOX = new BoxSizing("content-box");
  public static final BoxSizing BORDER_BOX = new BoxSizing("border-box");

  private BoxSizing (final String constant)
  {
    super(constant);
  }
}
