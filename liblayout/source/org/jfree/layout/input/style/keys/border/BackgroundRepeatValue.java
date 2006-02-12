package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 30.10.2005, 19:14:34
 *
 * @author Thomas Morgner
 */
public class BackgroundRepeatValue  implements CSSValue
{
  public static final BackgroundRepeatValue DEFAULT_REPEAT =
          new BackgroundRepeatValue(BackgroundRepeat.NOREPEAT, BackgroundRepeat.NOREPEAT);

  private BackgroundRepeat horizontal;
  private BackgroundRepeat vertical;

  public BackgroundRepeatValue(final BackgroundRepeat horizontal,
                               final BackgroundRepeat vertical)
  {
    this.horizontal = horizontal;
    if (vertical == null)
    {
      this.vertical = horizontal;
    }
    else
    {
      this.vertical = vertical;
    }
  }

  public BackgroundRepeat getHorizontal()
  {
    return horizontal;
  }

  public BackgroundRepeat getVertical()
  {
    return vertical;
  }

  public String getCSSText()
  {
    return horizontal + " " + vertical;
  }

  public String toString ()
  {
    return getCSSText();
  }
  
}
