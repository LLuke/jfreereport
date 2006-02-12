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
 * ListOfConstantsResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.util.Log;

/**
 * Creation-Date: 14.12.2005, 23:08:14
 *
 * @author Thomas Morgner
 */
public abstract class ListOfConstantsResolveHandler extends ConstantsResolveHandler
{
  public ListOfConstantsResolveHandler()
  {
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
    if (value == null)
    {
      return;
    }
    if (value instanceof CSSValueList == false)
    {
      return;
    }

    CSSValueList list = (CSSValueList) value;
    final int length = list.getLength();
    if (length == 0)
    {
      return;
    }

    for (int i = 0; i < length; i++)
    {
      CSSValue item = list.getItem(i);
      if (item instanceof CSSConstant == false)
      {
        resolveInvalidItem(process, currentNode, style, key, i);
      }
      else
      {
        resolveItem(process, currentNode, style, key, i, (CSSConstant) item);
      }
    }
  }

  protected void resolveInvalidItem (final LayoutProcess process,
                                     LayoutNode currentNode,
                                     LayoutStyle style,
                                     StyleKey key,
                                     int index)
  {
    Log.warn ("Encountered invalid item in Style " + key + " at index " + index);
  }

  protected abstract boolean resolveItem(final LayoutProcess process,
                                         LayoutNode currentNode,
                                         LayoutStyle style,
                                         StyleKey key,
                                         int index,
                                         CSSConstant item);
}
