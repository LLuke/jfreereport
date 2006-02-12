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
 * TargetNameResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed.hyperlinks;

import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.hyperlinks.TargetName;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSStringValue;

/**
 * Creation-Date: 21.12.2005, 11:32:33
 *
 * @author Thomas Morgner
 */
public class TargetNameResolveHandler extends ConstantsResolveHandler
{
  public TargetNameResolveHandler()
  {
    addNormalizeValue(TargetName.CURRENT);
    addNormalizeValue(TargetName.MODAL);
    addNormalizeValue(TargetName.NEW);
    addNormalizeValue(TargetName.PARENT);
    addNormalizeValue(TargetName.ROOT);
    setFallback(TargetName.CURRENT);
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve(final LayoutProcess process,
                      LayoutNode currentNode,
                      LayoutStyle style,
                      StyleKey key)
  {
    CSSValue value = style.getValue(key);
    if (value instanceof CSSConstant)
    {
      super.resolve(process, currentNode, style, key);
    }
    else if (value instanceof CSSStringValue)
    {
      // do nothing, accept it as is...
    }
    else
    {
      style.setValue(key, getFallback());
    }
  }

}
