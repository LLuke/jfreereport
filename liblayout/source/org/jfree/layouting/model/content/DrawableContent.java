package org.jfree.layouting.model.content;

import org.jfree.ui.Drawable;

public class DrawableContent implements ContentToken
{
  private Drawable content;

  public DrawableContent (Drawable content)
  {
    if (content == null)
    {
      throw new NullPointerException();
    }
    this.content = content;
  }

  public Drawable getContent ()
  {
    return content;
  }

  public int getType ()
  {
    return ContentToken.DRAWABLE_CONTENT;
  }
}
