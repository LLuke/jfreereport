package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 20:11:13
 *
 * @author Thomas Morgner
 */
public class TextWrap extends CSSConstant
{
  public static final TextWrap NORMAL = new TextWrap("normal");
  public static final TextWrap UNRESTRICTED = new TextWrap("unrestricted");
  public static final TextWrap NONE = new TextWrap("none");
  public static final TextWrap SUPPRESS = new TextWrap("suppress");

  private TextWrap(String name)
  {
    super(name);
  }
}
