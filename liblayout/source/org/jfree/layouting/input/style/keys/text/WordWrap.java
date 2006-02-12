package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 20:11:13
 *
 * @author Thomas Morgner
 */
public class WordWrap extends CSSConstant
{
  public static final WordWrap NORMAL = new WordWrap("normal");
  public static final WordWrap BREAK_WORD = new WordWrap("break-word");

  private WordWrap(String name)
  {
    super(name);
  }
}
