package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

public class TextDecorationWidth
{
  /**
   * A bold is basicly an auto, that is thicker than the normal
   * auto value.
   */
  public static final CSSConstant BOLD = new CSSConstant("bold");

  /**
   * A dash is basicly an auto, that is thinner than the normal 
   * auto value.
   */
  public static final CSSConstant DASH = new CSSConstant("dash");

  /**
   * The text decoration width is the normal text decoration width
   * for the nominal font. If no font characteristic exists for the
   * width of the text decoration in question, the user agent should
   * proceed as though 'auto' were specified.
   *
   * The computed value is 'normal'.
   */
  public static final CSSConstant NORMAL = new CSSConstant("normal");

  private TextDecorationWidth()
  {
  }
}
