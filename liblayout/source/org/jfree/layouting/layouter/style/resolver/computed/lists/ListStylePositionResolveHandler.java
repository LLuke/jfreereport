package org.jfree.layouting.layouter.style.resolver.computed.lists;

import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.lists.ListSpecification;
import org.jfree.layouting.input.style.keys.list.ListStylePosition;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.StyleKey;

public class ListStylePositionResolveHandler extends ConstantsResolveHandler
{
  public ListStylePositionResolveHandler ()
  {
    addNormalizeValue(ListStylePosition.INSIDE);
    addNormalizeValue(ListStylePosition.OUTSIDE);
    setFallback(ListStylePosition.OUTSIDE);
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
    CSSValue value = resolveValue(process, currentNode, style, key);
    ListSpecification listSpecification =
            currentNode.getLayoutContext().getListSpecification();
    listSpecification.setPosition((ListStylePosition) value);
  }
}
