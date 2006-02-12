package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * OK, that property is confusing. Lets ignore it for now.
 *
 * @author Thomas Morgner
 */
public class BorderImageValue implements CSSValue
{
  private CSSStringValue uri;
  private CSSNumericValue topEdgeHeight;
  private CSSNumericValue rightEdgeWidth;
  private CSSNumericValue bottomEdgeHeight;
  private CSSNumericValue leftEdgeWidth;

  private CSSNumericValue topBorder;
  private CSSNumericValue leftBorder;
  private CSSNumericValue bottomBorder;
  private CSSNumericValue rightBorder;

  private BorderImageStretchType verticalScale;
  private BorderImageStretchType horizontalScale;


  public BorderImageValue(final CSSStringValue uri,
                          final CSSNumericValue topEdgeHeight,
                          final CSSNumericValue rightEdgeWidth,
                          final CSSNumericValue bottomEdgeHeight,
                          final CSSNumericValue leftEdgeWidth,
                          final CSSNumericValue topBorder,
                          final CSSNumericValue leftBorder,
                          final CSSNumericValue bottomBorder,
                          final CSSNumericValue rightBorder,
                          final BorderImageStretchType verticalScale,
                          final BorderImageStretchType horizontalScale)
  {
    this.uri = uri;
    this.topEdgeHeight = topEdgeHeight;
    this.rightEdgeWidth = rightEdgeWidth;
    this.bottomEdgeHeight = bottomEdgeHeight;
    this.leftEdgeWidth = leftEdgeWidth;

    this.topBorder = topBorder;
    if (leftBorder == null)
    {
      this.leftBorder = topBorder;
    }
    else
    {
      this.leftBorder = leftBorder;
    }
    if (bottomBorder == null)
    {
      this.bottomBorder = topBorder;
    }
    else
    {
      this.bottomBorder = bottomBorder;
    }
    if (rightBorder == null)
    {
      this.rightBorder = leftBorder;
    }
    else
    {
      this.rightBorder = rightBorder;
    }

    this.verticalScale = verticalScale;
    if (horizontalScale != null)
    {
      this.horizontalScale = horizontalScale;
    }
    else
    {
      this.horizontalScale = verticalScale;
    }
  }

  public CSSNumericValue getTopEdgeHeight()
  {
    return topEdgeHeight;
  }

  public CSSNumericValue getRightEdgeWidth()
  {
    return rightEdgeWidth;
  }

  public CSSNumericValue getBottomEdgeHeight()
  {
    return bottomEdgeHeight;
  }

  public CSSNumericValue getLeftEdgeWidth()
  {
    return leftEdgeWidth;
  }

  public CSSNumericValue getTopBorder()
  {
    return topBorder;
  }

  public CSSNumericValue getLeftBorder()
  {
    return leftBorder;
  }

  public CSSNumericValue getBottomBorder()
  {
    return bottomBorder;
  }

  public CSSNumericValue getRightBorder()
  {
    return rightBorder;
  }

  public BorderImageStretchType getVerticalScale()
  {
    return verticalScale;
  }

  public BorderImageStretchType getHorizontalScale()
  {
    return horizontalScale;
  }

  public CSSStringValue getUri()
  {
    return uri;
  }

  public String getCSSText()
  {
    final StringBuffer b = new StringBuffer();
    b.append(uri);
    b.append(" ");
    return null;
  }
}
