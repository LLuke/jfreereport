package org.jfree.layouting.model.content;

public class CloseQuoteToken implements ContentToken
{
  private boolean surpress;

  public CloseQuoteToken (boolean surpress)
  {
    this.surpress = surpress;
  }

  public boolean isSurpressQuoteText ()
  {
    return surpress;
  }

  public int getType ()
  {
    return CLOSE_QUOTE;
  }
}
