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
 * PageContext.java
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
