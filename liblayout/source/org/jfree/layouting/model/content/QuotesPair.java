package org.jfree.layouting.model.content;

public class QuotesPair
{
  private String openQuote;
  private String closeQuote;

  public QuotesPair (String openQuote, String closeQuote)
  {
    if (openQuote == null)
    {
      throw new NullPointerException();
    }
    if (closeQuote == null)
    {
      throw new NullPointerException();
    }
    this.openQuote = openQuote;
    this.closeQuote = closeQuote;
  }

  public String getCloseQuote ()
  {
    return closeQuote;
  }

  public String getOpenQuote ()
  {
    return openQuote;
  }
}
