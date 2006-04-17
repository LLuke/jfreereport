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
 * WidthResolveHandler.java
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

package org.jfree.layouting.layouter.style.resolver.autovalue.box;

import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.LayoutContext;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.positioning.PositioningStyleKeys;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;

public class WidthResolveHandler implements ResolveHandler
{
  public WidthResolveHandler ()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[] {
            BoxStyleKeys.DISPLAY_ROLE,
            BoxStyleKeys.DISPLAY_MODEL,
            BoxStyleKeys.FLOAT,
            PositioningStyleKeys.POSITION,
            TextStyleKeys.BLOCK_PROGRESSION
    };
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve (LayoutProcess process, LayoutNode currentNode, LayoutStyle style,
                       StyleKey key)
  {
    // if the current element is a block level element,
    final LayoutContext lc = currentNode.getLayoutContext();
    final DisplayRole role =
            lc.getBoxSpecification().getDisplayRole();
    if (DisplayRole.BLOCK.equals(role) == false)
    {
      return; // we cannot resolve that one. Auto keeps its special meaning...
    }
    // check whether we have vertical flow (as in asian scripts)
    final BlockProgression blockProgression =
            lc.getTextSpecification().getLayoutSpecification().getBlockProgression();
    if (BlockProgression.TB.equals(blockProgression) == false)
    {
      // in asian scripts, the width depends on the content if set to auto
      return;
    }
    // if so then set the width to 100%
    style.setValue(key, new CSSNumericValue(CSSNumericType.PERCENTAGE, 100));
  }
}
