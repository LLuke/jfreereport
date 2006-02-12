package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.StyleKey;

public class DisplayModelResolveHandler extends ConstantsResolveHandler
{
  public DisplayModelResolveHandler ()
  {
    addNormalizeValue(DisplayModel.BLOCK_INSIDE);
    addNormalizeValue(DisplayModel.INLINE_INSIDE);
    addNormalizeValue(DisplayModel.RUBY);
    addNormalizeValue(DisplayModel.TABLE);
    setFallback(DisplayModel.INLINE_INSIDE);
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
    final DisplayModel model = (DisplayModel) resolveValue(process, currentNode, style, key);
    style.setValue(key, model);
    
    currentNode.getLayoutContext().getBoxSpecification().setDisplayModel(model);
  }
}
