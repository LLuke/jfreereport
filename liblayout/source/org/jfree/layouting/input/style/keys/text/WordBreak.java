package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 20:06:54
 *
 * @author Thomas Morgner
 */
public class WordBreak extends CSSConstant
{
  public static final WordBreak NORMAL = new WordBreak("normal");
  public static final WordBreak KEEP_ALL = new WordBreak("keep-all");
  public static final WordBreak LOOSE = new WordBreak("loose");
  public static final WordBreak BREAK_STRICT = new WordBreak("break-strict");
  public static final WordBreak BREAK_ALL = new WordBreak("break-all");

  private WordBreak(String name)
  {
    super(name);
  }
}
