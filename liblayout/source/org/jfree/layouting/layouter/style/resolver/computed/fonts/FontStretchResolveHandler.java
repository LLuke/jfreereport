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
 * FontStretchResolveHandler.java
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
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStretch;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.font.FontSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.util.Log;

/**
 * Creation-Date: 18.12.2005, 20:33:42
 *
 * @author Thomas Morgner
 */
public class FontStretchResolveHandler extends ConstantsResolveHandler
{
  public FontStretchResolveHandler()
  {
    addNormalizeValue(FontStretch.CONDENSED);
    addNormalizeValue(FontStretch.EXPANDED);
    addNormalizeValue(FontStretch.EXTRA_CONDENSED);
    addNormalizeValue(FontStretch.EXTRA_EXPANDED);
    addNormalizeValue(FontStretch.NORMAL);
    addNormalizeValue(FontStretch.SEMI_CONDENSED);
    addNormalizeValue(FontStretch.SEMI_EXPANDED);
    addNormalizeValue(FontStretch.ULTRA_CONDENSED);
    addNormalizeValue(FontStretch.ULTRA_EXPANDED);
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
    final FontSpecification fs =
            currentNode.getLayoutContext().getFontSpecification();
    if (FontStretch.WIDER.equals(value))
    {
      // ask the parent ...
      FontStretch parentStretch = queryParent(currentNode.getParent());
      fs.setFontStretch(FontStretch.getByOrder(parentStretch.getOrder() + 1));
    }
    else if (FontStretch.NARROWER.equals(value))
    {
      // ask the parent ...
      FontStretch parentStretch = queryParent(currentNode.getParent());
      fs.setFontStretch(FontStretch.getByOrder(parentStretch.getOrder() - 1));
    }
    else if (value instanceof CSSConstant)
    {
      FontStretch stretch = (FontStretch) lookupValue((CSSConstant) value);
      if (stretch != null)
      {
        fs.setFontStretch(stretch);
      }
      else
      {
        fs.setFontStretch(FontStretch.NORMAL);
      }
    }

  }

  private FontStretch queryParent(final LayoutElement parent)
  {
    if (parent == null)
    {
      return FontStretch.NORMAL;
    }
    final FontStretch parentValue =
            parent.getLayoutContext().getFontSpecification().getFontStretch();
    if (parentValue == null)
    {
      Log.error("Assertation failed: Parent stretch is null");
      return FontStretch.NORMAL;
    }
    return parentValue;
  }
}
