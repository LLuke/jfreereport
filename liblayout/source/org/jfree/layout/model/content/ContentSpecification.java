package org.jfree.layouting.model.content;

public class ContentSpecification
{
  public static final QuotesPair[] EMPTY_QUOTES = new QuotesPair[0];
  public static final int NORMAL = 0;
  public static final int NONE = 1;
  public static final int INHIBIT = 2;

  private QuotesPair[] quotes;
  private ContentToken[] contents;
  private int contentProcessing;
  private int quotingLevel;

  public ContentSpecification ()
  {
    quotes = EMPTY_QUOTES;
  }

  public QuotesPair[] getQuotes ()
  {
    return (QuotesPair[]) quotes.clone();
  }

  public String getOpenQuote (int level)
  {
    if (level < 0)
    {
      return "";
    }
    if (level >= quotes.length)
    {
      if (quotes.length == 0)
      {
        return "";
      }
      return quotes[quotes.length - 1].getOpenQuote();
    }
    return quotes[level].getOpenQuote();
  }

  public String getCloseQuote (int level)
  {
    if (level < 0)
    {
      return "";
    }
    if (level >= quotes.length)
    {
      if (quotes.length == 0)
      {
        return "";
      }
      return quotes[quotes.length - 1].getCloseQuote();
    }
    return quotes[level].getCloseQuote();
  }

  public void setQuotes (QuotesPair[] quotes)
  {
    if (this.quotes == null)
    {
      throw new NullPointerException();
    }
    this.quotes = (QuotesPair[]) quotes.clone();
  }

  public ContentToken[] getContents ()
  {
    return (ContentToken[]) contents.clone();
  }

  public void setContents (ContentToken[] contents)
  {
    this.contents = (ContentToken[]) contents.clone();
  }

  public int getContentProcessing ()
  {
    return contentProcessing;
  }

  public void setContentProcessing (int contentProcessing)
  {
    this.contentProcessing = contentProcessing;
  }

  public int getQuotingLevel ()
  {
    return quotingLevel;
  }

  public void setQuotingLevel (int quotingLevel)
  {
    this.quotingLevel = quotingLevel;
  }
}
