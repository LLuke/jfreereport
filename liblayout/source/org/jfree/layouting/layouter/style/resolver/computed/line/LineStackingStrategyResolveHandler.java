package org.jfree.layouting.layouter.style.resolver.computed.line;

import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.input.style.keys.line.LineStackingStrategy;
import org.jfree.layouting.input.style.StyleKey;

public class LineStackingStrategyResolveHandler extends ConstantsResolveHandler
{
  public LineStackingStrategyResolveHandler ()
  {
    addNormalizeValue(LineStackingStrategy.BLOCK_LINE_HEIGHT);
    addNormalizeValue(LineStackingStrategy.GRID_HEIGHT);
    addNormalizeValue(LineStackingStrategy.INLINE_LINE_HEIGHT);
    addNormalizeValue(LineStackingStrategy.MAX_LINE_HEIGHT);
    setFallback(LineStackingStrategy.INLINE_LINE_HEIGHT);
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
    LineStackingStrategy ls = (LineStackingStrategy) resolveValue(process, currentNode, style, key);
    style.setValue(key, ls);
    currentNode.getLayoutContext().getLineSpecification().setLineStackingStrategy(ls);
  }
}
