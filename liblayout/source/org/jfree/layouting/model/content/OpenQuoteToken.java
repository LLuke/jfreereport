package org.jfree.layouting.model.content;

public class OpenQuoteToken implements ContentToken
{
  private boolean surpress;

  public OpenQuoteToken (boolean surpress)
  {
    this.surpress = surpress;
  }

  public boolean isSurpressQuoteText ()
  {
    return surpress;
  }

  public int getType ()
  {
    return OPEN_QUOTE;
  }
}
