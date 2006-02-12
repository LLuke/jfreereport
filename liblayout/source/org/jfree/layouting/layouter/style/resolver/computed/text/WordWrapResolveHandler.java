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
 * WhitespaceCollapseResolveHandler.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: WordWrapResolveHandler.java,v 1.1 2006/02/12 21:49:33 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21.12.2005 : Initial version
 */
package org.jfree.layouting.layouter.style.resolver.computed.text;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.TextWrap;
import org.jfree.layouting.input.style.keys.text.WordWrap;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.text.TextProcessingSpecification;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

/**
 * Creation-Date: 21.12.2005, 15:12:04
 *
 * @author Thomas Morgner
 */
public class WordWrapResolveHandler extends ConstantsResolveHandler
{
  public WordWrapResolveHandler()
  {
    addNormalizeValue(WordWrap.BREAK_WORD);
    addNormalizeValue(WordWrap.NORMAL);
    setFallback(WordWrap.NORMAL);
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
    final TextWrap value =
            (TextWrap) resolveValue(process, currentNode, style, key);
    final TextSpecification textSpecification =
            currentNode.getLayoutContext().getTextSpecification();
    final TextProcessingSpecification processingSpecifcation =
            textSpecification.getProcessingSpecification();
    processingSpecifcation.setWordWrap(WordWrap.BREAK_WORD.equals(value));

  }
}
