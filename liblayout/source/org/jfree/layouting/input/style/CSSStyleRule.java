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
 * CSSStyleRule.java
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
package org.jfree.layouting.input.style;

import java.util.Iterator;

import org.jfree.layouting.input.style.selectors.CSSSelector;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 23.11.2005, 10:59:26
 *
 * @author Thomas Morgner
 */
public class CSSStyleRule extends CSSDeclarationRule
{
  private CSSSelector selector;

  public CSSStyleRule(final StyleSheet parentStyle,
                      final StyleRule parentRule)
  {
    super(parentStyle, parentRule);
  }

  public CSSSelector getSelector()
  {
    return selector;
  }

  public void setSelector(final CSSSelector selector)
  {
    if (isReadOnly())
    {
      throw new UnmodifiableStyleSheetException();
    }
    this.selector = selector;
  }

  public void merge(final CSSStyleRule elementRule)
  {
    Iterator it = elementRule.getPropertyKeys();
    while (it.hasNext())
    {
      StyleKey key = (StyleKey) it.next();
      CSSValue value = elementRule.getPropertyCSSValue(key);
      setPropertyValue(key, value);
    }
  }
}
