package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 30.10.2005, 19:53:45
 *
 * @author Thomas Morgner
 */
public class BoxShadowValue implements CSSValue
{
  private CSSColorValue color;
  private CSSNumericValue horizontalOffset;
  private CSSNumericValue verticalOffset;
  private CSSNumericValue blurRadius;

  public BoxShadowValue(final CSSColorValue color,
                        final CSSNumericValue horizontalOffset,
                        final CSSNumericValue verticalOffset,
                        final CSSNumericValue blurRadius)
  {
    this.color = color;
    this.horizontalOffset = horizontalOffset;
    this.verticalOffset = verticalOffset;
    this.blurRadius = blurRadius;
  }

  public CSSColorValue getColor()
  {
    return color;
  }

  public CSSNumericValue getHorizontalOffset()
  {
    return horizontalOffset;
  }

  public CSSNumericValue getVerticalOffset()
  {
    return verticalOffset;
  }

  public CSSNumericValue getBlurRadius()
  {
    return blurRadius;
  }

  public String getCSSText()
  {
    return horizontalOffset + " " + verticalOffset + " " + blurRadius + " " + color;
  }
}
