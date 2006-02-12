package org.jfree.layouting.layouter.style.resolver.computed.line;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;

public class VerticalAlignResolveHandler extends ConstantsResolveHandler
{
  public VerticalAlignResolveHandler ()
  {
    addNormalizeValue(VerticalAlign.BASELINE);
    addNormalizeValue(VerticalAlign.BOTTOM);
    addNormalizeValue(VerticalAlign.CENTRAL);
    addNormalizeValue(VerticalAlign.MIDDLE);
    addNormalizeValue(VerticalAlign.SUB);
    addNormalizeValue(VerticalAlign.SUPER);
    addNormalizeValue(VerticalAlign.TEXT_BOTTOM);
    addNormalizeValue(VerticalAlign.TEXT_TOP);
    addNormalizeValue(VerticalAlign.TOP);
    // we do not detect scripts right now ...
    setFallback(VerticalAlign.BASELINE);
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
    final VerticalAlign va = (VerticalAlign) resolveValue(process, currentNode, style, key);
    currentNode.getLayoutContext().getLineSpecification().setVerticalAlign(va);
  }

}
