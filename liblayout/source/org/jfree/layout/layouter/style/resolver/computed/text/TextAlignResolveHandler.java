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
 * TextAlignResolveHandler.java
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
package org.jfree.layouting.layouter.style.resolver.computed.text;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.keys.text.TextAlign;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.text.TextAlignmentSpecifcation;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

/**
 * Creation-Date: 21.12.2005, 14:17:42
 *
 * @author Thomas Morgner
 */
public class TextAlignResolveHandler extends ConstantsResolveHandler
{
  public TextAlignResolveHandler()
  {
    addNormalizeValue(TextAlign.CENTER);
    addNormalizeValue(TextAlign.END);
    addNormalizeValue(TextAlign.JUSTIFY);
    addNormalizeValue(TextAlign.LEFT);
    addNormalizeValue(TextAlign.RIGHT);
    addNormalizeValue(TextAlign.START);
    setFallback(TextAlign.START);
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
    final TextSpecification textSpecification =
            currentNode.getLayoutContext().getTextSpecification();
    final TextAlignmentSpecifcation alignmentSpecifcation =
            textSpecification.getAlignmentSpecifcation();

    CSSValue value = style.getValue(key);
    if (value instanceof CSSStringValue)
    {
      CSSStringValue sval = (CSSStringValue) value;
      alignmentSpecifcation.setSubStringAlignment(sval.getValue());
      alignmentSpecifcation.setTextAlign(TextAlign.START);
      return;
    }

    final TextAlign alignValue =
            (TextAlign) resolveValue(process, currentNode, style, key);
    alignmentSpecifcation.setTextAlign(alignValue);
  }
}
