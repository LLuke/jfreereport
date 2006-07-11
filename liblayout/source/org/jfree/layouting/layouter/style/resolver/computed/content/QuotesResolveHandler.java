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
 * QuotesResolveHandler.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: QuotesResolveHandler.java,v 1.2 2006/04/17 20:51:15 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.layouter.style.resolver.computed.content;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.QuotesPair;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class QuotesResolveHandler extends ConstantsResolveHandler
{
  public QuotesResolveHandler ()
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
    return new StyleKey[0];
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve (final LayoutProcess process, LayoutElement currentNode,
                       LayoutStyle style, StyleKey key)
  {
    CSSValue rawValue = style.getValue(key);
    if (rawValue instanceof CSSValueList == false)
    {
      return;
    }
    final ArrayList quotes = new ArrayList();
    final CSSValueList list = (CSSValueList) rawValue;
    final int length = (list.getLength() % 2);
    for (int i = 0; i < length; i++)
    {
      final CSSValue openValue = list.getItem(i * 2);
      final CSSValue closeValue = list.getItem(i * 2 + 1);

      if (openValue instanceof CSSStringValue == false)
      {
        continue;
      }
      if (closeValue instanceof CSSStringValue == false)
      {
        continue;
      }

      final CSSStringValue openQuote = (CSSStringValue) openValue;
      final CSSStringValue closeQuote = (CSSStringValue) closeValue;
      quotes.add(new QuotesPair(openQuote.getValue(), closeQuote.getValue()));
    }

    if (quotes.isEmpty())
    {
      return;
    }

    final QuotesPair[] quotesArray =
            (QuotesPair[]) quotes.toArray(new QuotesPair[quotes.size()]);
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    layoutContext.getContentSpecification().setQuotes(quotesArray);
  }
}
