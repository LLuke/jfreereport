package org.jfree.layouting.model;

import org.jfree.layouting.input.style.keys.page.PagePolicy;
import org.jfree.layouting.input.style.CSSPageRule;
import org.jfree.layouting.layouter.counters.CounterStyle;

/**
 * The page context defines the printable contents for the
 * special page areas. It does not affect the content area.
 * <p>
 * Even if the page areas are redefined, these properties
 * are kept until a new page has been started.
 */
public interface PageContext
{
  public PagePolicy getStringPolicy (String name);
  public void setStringPolicy (String name, PagePolicy policy);
  public PagePolicy getCounterPolicy (String name);
  public void setCounterPolicy (String name, PagePolicy policy);

  public String getString (String name);
  public void setString (String name, String value);

  public int getCounterValue (String name);
  public void setCounterValue (String name, int value);

  public CounterStyle getCounterStyle (String name);
  public void setCounterStyle (String name, CounterStyle value);

  /**
   * Resets all internal states. All counters and strings
   * are in the initial state (before a page has been started).
   */
  public void reset();

  /**
   * This method must be called after the @page rule has been
   * applied for the current page (and all strings are set and
   * counters are reset/incremented/whatever). This starts the
   * state engine to freeze the elements on their current value
   * (if the pagepolicy is set to 'start'), after their first
   * change on that page (pagepolicy 'first') or never (if the
   * policy is 'last').
   */
  public void freeze();

  public boolean isFrozen ();

  public boolean isFirstPage ();

  public long getHeight ();

  public short getHorizontalPageSpan ();

  public boolean isOddPageIsLeft ();

  public int getPageNumber ();

  public CSSPageRule getPageRule ();

  public short getVerticalPageSpan ();

  public long getWidth ();

  public boolean isSinglePage ();

  public boolean isLeftPage ();
}
