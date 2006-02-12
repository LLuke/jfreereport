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
 * FontSmoothResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: FontSmoothResolveHandler.java,v 1.1 2006/02/12 21:49:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 18.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.percentages.fonts;

import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.resolver.percentages.LengthResolverUtility;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 18.12.2005, 20:29:20
 *
 * @author Thomas Morgner
 */
public class FontSmoothResolveHandler implements ResolveHandler
{
  public FontSmoothResolveHandler()
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
            FontStyleKeys.FONT_SIZE
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
    CSSValue value = style.getValue(key);
    if (value instanceof CSSNumericValue == false)
    {
      return;
    }

    final FontSpecification fs =
            currentNode.getLayoutContext().getFontSpecification();
    final long length = LengthResolverUtility.convertLengthToInternal
            ((CSSNumericValue) value, currentNode, process.getOutputMetaData());
    if (StrictGeomUtility.toInternalValue(fs.getFontSize()) < length)
    {
      fs.setAntiAliased(false);
    }
    else
    {
      fs.setAntiAliased(true);
    }
  }
}