package org.jfree.layouting.layouter.style.resolver.autovalue.text;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class TextDecorationWidthResolveHandler implements ResolveHandler
{
  public TextDecorationWidthResolveHandler ()
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
  public void resolve (LayoutProcess process,
                       LayoutElement currentNode,
                       StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    layoutContext.setValue(key, new CSSNumericValue(CSSNumericType.PT, 1));
  }
}
