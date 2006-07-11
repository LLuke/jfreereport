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
 * FitPositionResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FitPositionResolveHandler.java,v 1.2 2006/04/17 20:51:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style.resolver.autovalue.box;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.Direction;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class FitPositionResolveHandler implements ResolveHandler
{
  private static CSSNumericValue LEFT_TOP = new CSSNumericValue(CSSNumericType.PERCENTAGE, 0);
  private static CSSNumericValue RIGHT_BOTTOM = new CSSNumericValue(CSSNumericType.PERCENTAGE, 100);

  public FitPositionResolveHandler ()
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
            TextStyleKeys.BLOCK_PROGRESSION,
            TextStyleKeys.DIRECTION
    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve (LayoutProcess process,
                       LayoutElement currentNode,
                       LayoutStyle style,
                       StyleKey key)
  {
    final boolean rightToLeft = Direction.RTL.equals(style.getValue(TextStyleKeys.DIRECTION));
    final CSSValue blockProgression = style.getValue(TextStyleKeys.BLOCK_PROGRESSION);
    // this might be invalid ...
    if (BlockProgression.TB.equals(blockProgression))
    {
      if (rightToLeft)
      {
        style.setValue(key, new CSSValuePair(RIGHT_BOTTOM, LEFT_TOP));
      }
      else
      {
        style.setValue(key, new CSSValuePair(LEFT_TOP, LEFT_TOP));
      }
    }
    else if (BlockProgression.RL.equals(blockProgression))
    {
      if (rightToLeft)
      {
        style.setValue(key, new CSSValuePair(LEFT_TOP, LEFT_TOP));
      }
      else
      {
        style.setValue(key, new CSSValuePair(RIGHT_BOTTOM, LEFT_TOP));
      }
    }
    else if (BlockProgression.LR.equals(blockProgression))
    {
      if (rightToLeft)
      {
        style.setValue(key, new CSSValuePair(RIGHT_BOTTOM, RIGHT_BOTTOM));
      }
      else
      {
        style.setValue(key, new CSSValuePair(LEFT_TOP, LEFT_TOP));
      }
    }
  }
}
