package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:59:54
 *
 * @author Thomas Morgner
 */
public class WhitespaceCollapse extends CSSConstant
{
  public static final WhitespaceCollapse PRESERVE = new WhitespaceCollapse(
          "preserve");
  public static final WhitespaceCollapse COLLAPSE = new WhitespaceCollapse(
          "collapse");
  public static final WhitespaceCollapse PRESERVE_BREAKS = new WhitespaceCollapse(
          "preserve-breaks");
  public static final WhitespaceCollapse DISCARD = new WhitespaceCollapse(
          "discard");

  private WhitespaceCollapse(String name)
  {
    super(name);
  }
}
