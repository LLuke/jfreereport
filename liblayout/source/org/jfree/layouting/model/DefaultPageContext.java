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
 * DefaultPageContext.java
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

package org.jfree.layouting.model;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.layouting.input.style.CSSPageRule;
import org.jfree.layouting.input.style.keys.page.PagePolicy;
import org.jfree.layouting.layouter.counters.CounterStyle;

public class DefaultPageContext implements PageContext
{
  private class StringCarrier
  {
    private PagePolicy pagePolicy;
    private boolean locked;
    private String value;

    public StringCarrier (PagePolicy pagePolicy, String value)
    {
      this.pagePolicy = pagePolicy;
      this.value = value;
    }

    public void unlock ()
    {
      this.locked = false;
    }

    public PagePolicy getPagePolicy ()
    {
      return pagePolicy;
    }

    public void setPagePolicy (PagePolicy pagePolicy)
    {
      this.pagePolicy = pagePolicy;
    }

    public String getValue ()
    {
      return value;
    }

    public void setValue (String value)
    {
      if (isFrozen())
      {
        if (locked)
        {
          return;
        }

        if (PagePolicy.FIRST.equals(pagePolicy))
        {
          locked = true;
        }
      }
      this.value = value;
    }

    public void lock ()
    {
      locked = true;
    }
  }

  private class CounterCarrier
  {
    private CounterStyle counterStyle;
    private PagePolicy pagePolicy;
    private boolean locked;
    private int value;

    public CounterCarrier (PagePolicy pagePolicy, int value)
    {
      this.pagePolicy = pagePolicy;
      this.value = value;
    }

    public void unlock ()
    {
      this.locked = false;
    }

    public PagePolicy getPagePolicy ()
    {
      return pagePolicy;
    }

    public void setPagePolicy (PagePolicy pagePolicy)
    {
      this.pagePolicy = pagePolicy;
    }

    public int getValue ()
    {
      return value;
    }

    public void setValue (int value)
    {
      if (isFrozen())
      {
        if (locked)
        {
          return;
        }

        if (PagePolicy.FIRST.equals(pagePolicy))
        {
          locked = true;
        }
      }
      this.value = value;
    }

    public void lock ()
    {
      locked = true;
    }

    public CounterStyle getCounterStyle ()
    {
      return counterStyle;
    }

    public void setCounterStyle (CounterStyle counterStyle)
    {
      this.counterStyle = counterStyle;
    }
  }

  private HashMap strings;
  private HashMap counters;
  private boolean frozen;

  private CSSPageRule pageRule;

  private long width;
  private long height;
  private short horizontalPageSpan;
  private short verticalPageSpan;
  private boolean firstPage;
  private int pageNumber;
  private boolean oddPageIsLeft;
  // page areas?

  public DefaultPageContext ()
  {
    strings = new HashMap();
    counters = new HashMap();

    width = 500000;
    height = 120000;
  }

  public boolean isFrozen ()
  {
    return frozen;
  }

  /**
   * This method must be called after the @page rule has been applied for the current page
   * (and all strings are set and counters are reset/incremented/whatever). This starts
   * the state engine to freeze the elements on their current value (if the pagepolicy is
   * set to 'start'), after their first change on that page (pagepolicy 'first') or never
   * (if the policy is 'last').
   */
  public void freeze ()
  {
    frozen = true;
    Iterator stringsIterator = strings.entrySet().iterator();
    while (stringsIterator.hasNext())
    {
      StringCarrier carrier = (StringCarrier) stringsIterator.next();
      if (PagePolicy.START.equals(carrier.getPagePolicy()))
      {
        carrier.lock();
      }
    }

    Iterator counterIterator = counters.entrySet().iterator();
    while (counterIterator.hasNext())
    {
      CounterCarrier carrier = (CounterCarrier) counterIterator.next();
      if (PagePolicy.START.equals(carrier.getPagePolicy()))
      {
        carrier.lock();
      }
    }
  }

  public PagePolicy getCounterPolicy (String name)
  {
    CounterCarrier cc = (CounterCarrier) counters.get(name);
    if (cc == null)
    {
      return PagePolicy.START;
    }
    return cc.getPagePolicy();
  }

  public CounterStyle getCounterStyle (String name)
  {
    CounterCarrier cc = (CounterCarrier) counters.get(name);
    if (cc == null)
    {
      return null;
    }
    return cc.getCounterStyle();
  }

  public int getCounterValue (String name)
  {
    CounterCarrier cc = (CounterCarrier) counters.get(name);
    if (cc == null)
    {
      return 0;
    }
    return cc.getValue();
  }

  public String getString (String name)
  {
    StringCarrier sc = (StringCarrier) strings.get(name);
    if (sc == null)
    {
      return null;
    }
    return sc.getValue();
  }

  public PagePolicy getStringPolicy (String name)
  {
    StringCarrier sc = (StringCarrier) strings.get(name);
    if (sc == null)
    {
      return PagePolicy.START;
    }
    return sc.getPagePolicy();
  }

  /**
   * Resets all internal states. All counters and strings are in the initial state (before
   * a page has been started).
   */
  public void reset ()
  {
    frozen = false;
    Iterator stringsIterator = strings.entrySet().iterator();
    while (stringsIterator.hasNext())
    {
      StringCarrier carrier = (StringCarrier) stringsIterator.next();
      carrier.unlock();
    }

    Iterator counterIterator = counters.entrySet().iterator();
    while (counterIterator.hasNext())
    {
      CounterCarrier carrier = (CounterCarrier) counterIterator.next();
      carrier.unlock();
    }
  }

  public void setCounterStyle (String name, CounterStyle value)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    if (value == null)
    {
      throw new NullPointerException();
    }

    CounterCarrier cc = (CounterCarrier) counters.get(name);
    if (cc == null)
    {
      cc = new CounterCarrier(PagePolicy.START, 0);
      counters.put(name, cc);
    }
    cc.setCounterStyle(value);
  }

  public void setCounterPolicy (String name, PagePolicy policy)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    if (policy == null)
    {
      throw new NullPointerException();
    }
    if (frozen)
    {
      throw new IllegalStateException("Cannot alter the page policy while being frozen");
    }

    CounterCarrier cc = (CounterCarrier) counters.get(name);
    if (cc == null)
    {
      counters.put(name, new CounterCarrier(policy, 0));
    }
    else
    {
      cc.setPagePolicy(policy);
    }
  }

  public void setCounterValue (String name, int value)
  {
    CounterCarrier cc = (CounterCarrier) counters.get(name);
    if (cc == null)
    {
      cc = new CounterCarrier(PagePolicy.START, 0);
      counters.put(name, cc);
    }
    cc.setValue(value);
  }

  public void setString (String name, String value)
  {
    StringCarrier cc = (StringCarrier) strings.get(name);
    if (cc == null)
    {
      cc = new StringCarrier(PagePolicy.START, null);
      counters.put(name, cc);
    }
    cc.setValue(value);
  }

  public void setStringPolicy (String name, PagePolicy policy)
  {
    StringCarrier cc = (StringCarrier) strings.get(name);
    if (cc == null)
    {
      counters.put(name, new StringCarrier(PagePolicy.START, null));
    }
    else
    {
      cc.setPagePolicy(policy);
    }
  }

  public boolean isFirstPage ()
  {
    return firstPage;
  }

  public void setFirstPage (boolean firstPage)
  {
    this.firstPage = firstPage;
  }

  public long getHeight ()
  {
    return height;
  }

  public void setHeight (long height)
  {
    this.height = height;
  }

  public short getHorizontalPageSpan ()
  {
    return horizontalPageSpan;
  }

  public void setHorizontalPageSpan (short horizontalPageSpan)
  {
    this.horizontalPageSpan = horizontalPageSpan;
  }

  public boolean isOddPageIsLeft ()
  {
    return oddPageIsLeft;
  }

  public void setOddPageIsLeft (boolean oddPageIsLeft)
  {
    this.oddPageIsLeft = oddPageIsLeft;
  }

  public int getPageNumber ()
  {
    return pageNumber;
  }

  public void setPageNumber (int pageNumber)
  {
    this.pageNumber = pageNumber;
  }

  public CSSPageRule getPageRule ()
  {
    return pageRule;
  }

  public void setPageRule (CSSPageRule pageRule)
  {
    this.pageRule = pageRule;
  }

  public short getVerticalPageSpan ()
  {
    return verticalPageSpan;
  }

  public void setVerticalPageSpan (short verticalPageSpan)
  {
    this.verticalPageSpan = verticalPageSpan;
  }

  public long getWidth ()
  {
    return width;
  }

  public void setWidth (long width)
  {
    this.width = width;
  }

  public boolean isSinglePage ()
  {
    return horizontalPageSpan == 1 && verticalPageSpan == 1;
  }

  public boolean isLeftPage ()
  {
    if (oddPageIsLeft)
    {
      return (pageNumber % 2 != 0);
    }
    else
    {
      return (pageNumber % 2 == 0);
    }
  }
}
