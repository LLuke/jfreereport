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
 * CSSSilblingSelector.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 30.11.2005 : Initial version
 */
package org.jfree.layouting.input.style.selectors;

import java.io.Serializable;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

/**
 * We do not support DOM node types, we always assume elements here (or evaluate
 * both selectors to see if they match).
 *
 * @author Thomas Morgner
 */
public class CSSSilblingSelector extends AbstractSelector
        implements SiblingSelector, Serializable
{
  private short nodeType;
  private Selector selector;
  private SimpleSelector silblingSelector;

  public CSSSilblingSelector(final short nodeType,
                             final Selector selector,
                             final SimpleSelector silblingSelector)
  {
    this.nodeType = nodeType;
    this.selector = selector;
    this.silblingSelector = silblingSelector;
  }

  /**
   * The node type to considered in the siblings list. All DOM node types are
   * supported. In order to support the "any" node type, the code ANY_NODE is
   * added to the DOM node types.
   */
  public short getNodeType()
  {
    return nodeType;
  }

  /** Returns the first selector. */
  public Selector getSelector()
  {
    return selector;
  }

  /*
  * Returns the second selector.
  */
  public SimpleSelector getSiblingSelector()
  {
    return silblingSelector;
  }

  /** An integer indicating the type of <code>Selector</code> */
  public short getSelectorType()
  {
    return SAC_DIRECT_ADJACENT_SELECTOR;
  }

  protected SelectorWeight createWeight()
  {
    if (silblingSelector instanceof CSSSelector == false ||
        selector instanceof CSSSelector == false)
    {
      throw new ClassCastException("Invalid selector implementation!");
    }
    CSSSelector anchestor = (CSSSelector) silblingSelector;
    CSSSelector simple = (CSSSelector) selector;
    return new SelectorWeight(anchestor.getWeight(), simple.getWeight());
  }
}
