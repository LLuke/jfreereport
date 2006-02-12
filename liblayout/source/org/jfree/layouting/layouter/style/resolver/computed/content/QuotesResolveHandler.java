package org.jfree.layouting.layouter.style.resolver.computed.content;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutContext;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.content.QuotesPair;
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
   * @param style
   * @param currentNode
   */
  public void resolve (final LayoutProcess process, LayoutNode currentNode,
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
