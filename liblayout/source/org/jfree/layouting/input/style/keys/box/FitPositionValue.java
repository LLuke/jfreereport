package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSValue;

public class FitPositionValue implements CSSValue
{
  private CSSValue horizontal;
  private CSSValue vertical;

  public FitPositionValue (CSSValue horizontal, CSSValue vertical)
  {
    if (horizontal == null)
    {
      throw new NullPointerException();
    }
    this.horizontal = horizontal;
    if (vertical == null)
    {
      this.vertical = vertical;
    }
    else
    {
      this.vertical = vertical;
    }
  }

  public CSSValue getHorizontal ()
  {
    return horizontal;
  }

  public CSSValue getVertical ()
  {
    return vertical;
  }

  public String getCSSText ()
  {
    return horizontal + " " + vertical;
  }

  public String toString ()
  {
    return getCSSText();
  }
}
