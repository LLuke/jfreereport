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
 * BackgroundPositionResolveHandler.java
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
package org.jfree.layouting.layouter.style.resolver.percentages.border;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.border.BackgroundSpecification;
import org.jfree.util.Log;

/**
 * The background position cannot be fully resolved at this point. The standard
 * defines that percentages refer to the layouted size of the box. This is
 * not nice, but at least it is non-fatal, as backgrounds do not influence the
 * overall layout.
 *
 * @author Thomas Morgner
 */
public class BackgroundPositionResolveHandler implements ResolveHandler
{
  public BackgroundPositionResolveHandler()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[] {
            BoxStyleKeys.WIDTH,
            BoxStyleKeys.HEIGHT,
    };
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve(LayoutProcess process,
                      LayoutNode currentNode,
                      LayoutStyle style,
                      StyleKey key)
  {
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSValueList == false)
    {
      return;
    }
    final BackgroundSpecification backgroundSpecification =
            currentNode.getLayoutContext().getBackgroundSpecification();

    final CSSValueList list = (CSSValueList) value;
    final int length = list.getLength();
    for (int i = 0; i < length; i++)
    {
      final CSSValue maybePair = list.getItem(i);
      if (maybePair instanceof CSSValuePair == false)
      {
        Log.debug ("Parse error for Background-position: CSSValuePair expected.");
        continue; // parse error.
      }

      backgroundSpecification.setBackgroundPosition(i, (CSSValuePair) maybePair);
    }
  }
}
