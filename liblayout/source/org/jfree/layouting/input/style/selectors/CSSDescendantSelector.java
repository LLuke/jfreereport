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
 * CSSDescendantSelector.java
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
package org.jfree.layouting.input.style.selectors;

import java.io.Serializable;

import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

/**
 * Creation-Date: 30.11.2005, 15:38:58
 *
 * @author Thomas Morgner
 */
public class CSSDescendantSelector extends AbstractSelector  implements DescendantSelector, Serializable
{
  private Selector anchestorSelector;
  private SimpleSelector simpleSelector;
  private boolean childRelation;

  public CSSDescendantSelector(final SimpleSelector simpleSelector,
                               final Selector anchestorSelector,
                               final boolean childRelation)
  {
    this.simpleSelector = simpleSelector;
    this.anchestorSelector = anchestorSelector;
    this.childRelation = childRelation;
  }

  /** Returns the parent selector. */
  public Selector getAncestorSelector()
  {
    return anchestorSelector;
  }

  protected SelectorWeight createWeight()
  {
    if (anchestorSelector instanceof CSSSelector == false ||
        simpleSelector instanceof CSSSelector == false)
    {
      throw new ClassCastException("Invalid selector implementation!");
    }
    CSSSelector anchestor = (CSSSelector) anchestorSelector;
    CSSSelector simple = (CSSSelector) simpleSelector;
    return new SelectorWeight(anchestor.getWeight(), simple.getWeight());
  }

  /*
  * Returns the simple selector.
  */
  public SimpleSelector getSimpleSelector()
  {
    return simpleSelector;
  }

  /** An integer indicating the type of <code>Selector</code> */
  public short getSelectorType()
  {
    if (childRelation)
    {
      return Selector.SAC_CHILD_SELECTOR;
    }
    else
    {
      return Selector.SAC_DESCENDANT_SELECTOR;
    }
  }
}
