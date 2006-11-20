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
 * TextKashidaSpaceResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TextKashidaSpaceResolveHandler.java,v 1.3 2006/07/11 13:29:52 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver.computed.text;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.context.LayoutContext;

/**
 * Creation-Date: 21.12.2005, 15:00:43
 *
 * @author Thomas Morgner
 */
public class TextKashidaSpaceResolveHandler implements ResolveHandler
{
  public TextKashidaSpaceResolveHandler()
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
    return new StyleKey[0];
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve(LayoutProcess process,
                      LayoutElement currentNode,
                      StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    CSSValue value = layoutContext.getValue(key);
    if ((value instanceof CSSNumericValue) == false)
    {
      return;
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    if (CSSNumericType.PERCENTAGE.equals(nval.getType()) == false)
    {
      return;
    }
    double percentage = nval.getValue();
    if (percentage < 0)
    {
      percentage = 0;
    }
    if (percentage > 100)
    {
      percentage = 100;
    }
    layoutContext.setValue(TextStyleKeys.TEXT_KASHIDA_SPACE,
            new CSSNumericValue(CSSNumericType.PERCENTAGE, percentage));
  }
}
