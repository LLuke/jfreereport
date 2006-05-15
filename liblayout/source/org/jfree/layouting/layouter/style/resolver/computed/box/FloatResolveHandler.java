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
 * FloatResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FloatResolveHandler.java,v 1.2 2006/04/17 20:51:15 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class FloatResolveHandler extends ConstantsResolveHandler
{
  public FloatResolveHandler ()
  {
    addNormalizeValue(Floating.BOTTOM);
    addNormalizeValue(Floating.LEFT);
    addNormalizeValue(Floating.END);
    addNormalizeValue(Floating.INSIDE);
    addNormalizeValue(Floating.IN_COLUMN);
    addNormalizeValue(Floating.MID_COLUMN);
    addNormalizeValue(Floating.NONE);
    addNormalizeValue(Floating.OUTSIDE);
    addNormalizeValue(Floating.RIGHT);
    addNormalizeValue(Floating.START);
    addNormalizeValue(Floating.TOP);
    setFallback(Floating.NONE);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[]{
            BoxStyleKeys.DISPLAY_ROLE
    };
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve (final LayoutProcess process, LayoutNode currentNode,
                       LayoutStyle style, StyleKey key)
  {
    final BoxSpecification boxSpecification = currentNode.getLayoutContext().getBoxSpecification();
    if (DisplayRole.NONE.equals(boxSpecification.getDisplayRole()))
    {
      style.setValue(key, Floating.NONE);
      boxSpecification.setFloating(Floating.NONE);
      return;
    }

    Floating f = (Floating) resolveValue(process, currentNode, style, key);
    style.setValue(key, f);
    boxSpecification.setFloating(f);
    if (Floating.NONE.equals(f) == false)
    {
      //boxSpecification.setDisplayModel(DisplayModel.BLOCK_INSIDE);
      boxSpecification.setDisplayRole(DisplayRole.BLOCK);
    }
  }
}
