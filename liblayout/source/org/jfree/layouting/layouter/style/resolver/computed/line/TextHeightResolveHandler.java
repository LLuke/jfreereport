package org.jfree.layouting.layouter.style.resolver.computed.line;

import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.input.style.keys.line.TextHeight;
import org.jfree.layouting.input.style.StyleKey;

public class TextHeightResolveHandler extends ConstantsResolveHandler
{
  public TextHeightResolveHandler ()
  {
    addNormalizeValue(TextHeight.FONT_SIZE);
    addNormalizeValue(TextHeight.MAX_SIZE);
    addNormalizeValue(TextHeight.TEXT_SIZE);
    setFallback(TextHeight.FONT_SIZE);
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
    final TextHeight th = (TextHeight) resolveValue(process, currentNode, style, key);
    currentNode.getLayoutContext().getLineSpecification().setTextHeight(th);
  }
}
