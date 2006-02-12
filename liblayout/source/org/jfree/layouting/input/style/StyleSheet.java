/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * StyleSheet.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: StyleSheet.java,v 1.1 2006/02/12 21:54:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 23.11.2005 : Initial version
 */
package org.jfree.layouting.input.style;

import java.net.URL;
import java.util.ArrayList;

/**
 * A CSS stylesheet. Unlike the W3C stylesheet classes, this class is a minimal
 * set of attributes, designed with usablity and performance in mind.
 * <p/>
 * Stylesheets are resolved by looking at the elements. For the sake of
 * simplicity, stylesheet objects itself do not hold references to their parent
 * stylesheets.
 * <p/>
 * The W3C media list is omited - this library assumes the visual/print media.
 * The media would have been specified in the document anyway, so we do not
 * care.
 * <p/>
 * This class is a union of the W3C CSSStyleSheet and the CSSStyleRuleList. It
 * makes no sense to separate them in this context.
 *
 * @author Thomas Morgner
 */
public class StyleSheet
{
  private boolean readOnly;
  private URL href;
  private ArrayList rules;
  private ArrayList styleSheets;

  public StyleSheet()
  {
    this.rules = new ArrayList();
    this.styleSheets = new ArrayList();
  }

  public boolean isReadOnly()
  {
    return readOnly;
  }

  protected void setReadOnly(final boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  public URL getHref()
  {
    return href;
  }

  public void setHref(final URL href)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    this.href = href;
  }

  public void addRule(final StyleRule rule)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    rules.add(rule);
  }

  public void insertRule(final int index, final StyleRule rule)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    rules.add(index, rule);
  }

  public void deleteRule(final int index)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    rules.remove(index);
  }

  public int getRuleCount()
  {
    return rules.size();
  }

  public StyleRule getRule(int index)
  {
    return (StyleRule) rules.get(index);
  }

  public void addStyleSheet (StyleSheet styleSheet)
  {
    styleSheets.add(styleSheet);
  }

  public int getStyleSheetCount ()
  {
    return styleSheets.size();
  }

  public StyleSheet getStyleSheet (int index)
  {
    return (StyleSheet) styleSheets.get(index);
  }

  public void removeStyleSheet (StyleSheet styleSheet)
  {
    styleSheets.remove(styleSheet);
  }
}
