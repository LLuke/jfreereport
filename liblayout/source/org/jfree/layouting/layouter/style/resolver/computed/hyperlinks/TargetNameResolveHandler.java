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
 * TargetNameResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: TargetNameResolveHandler.java,v 1.3 2006/07/11 13:29:52 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.layouter.style.resolver.computed.hyperlinks;

import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.context.LayoutContext;
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
   * @param currentNode
   * @param style
   */
  public void resolve(final LayoutProcess process,
                      LayoutElement currentNode,
                      StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    CSSValue value = layoutContext.getValue(key);
    if (value instanceof CSSConstant)
    {
      super.resolve(process, currentNode, key);
    }
    else if (value instanceof CSSStringValue)
    {
      // do nothing, accept it as is...
    }
    else
    {
      layoutContext.setValue(key, getFallback());
    }
  }

}
