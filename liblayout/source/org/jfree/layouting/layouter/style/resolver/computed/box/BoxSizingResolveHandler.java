package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxSizing;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class BoxSizingResolveHandler extends ConstantsResolveHandler
{
  public BoxSizingResolveHandler ()
  {
    addNormalizeValue(BoxSizing.BORDER_BOX);
    addNormalizeValue(BoxSizing.CONTENT_BOX);
    setFallback(BoxSizing.CONTENT_BOX);
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
    BoxSizing bs = (BoxSizing)
            super.resolveValue(process, currentNode, style, key);
    style.setValue(key, bs);
    final boolean cbs = BoxSizing.CONTENT_BOX.equals(bs);
    currentNode.getLayoutContext().getBoxSpecification().setContentBoxSizing(cbs);
  }
}
