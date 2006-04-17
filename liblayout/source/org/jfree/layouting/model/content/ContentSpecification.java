/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ContentSpecification.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.model.content;

public class ContentSpecification
{
  private static final QuotesPair[] EMPTY_QUOTES = new QuotesPair[0];
  private static final ContentsToken[] EMPTY_CONTENT = new ContentsToken[0];

  private QuotesPair[] quotes;
  private ContentToken[] contents;
  private boolean allowContentProcessing;
  private int quotingLevel;

  public ContentSpecification ()
  {
    quotes = EMPTY_QUOTES;
    contents = EMPTY_CONTENT;
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

  public boolean isAllowContentProcessing()
  {
    return allowContentProcessing;
  }

  public void setAllowContentProcessing(final boolean allowContentProcessing)
  {
    this.allowContentProcessing = allowContentProcessing;
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
