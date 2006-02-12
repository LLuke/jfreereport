package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.FloatDisplace;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class FloatDisplaceResolveHandler extends ConstantsResolveHandler
{
  public FloatDisplaceResolveHandler ()
  {
    addNormalizeValue(FloatDisplace.BLOCK);
    addNormalizeValue(FloatDisplace.BLOCK_WITHIN_PAGE);
    addNormalizeValue(FloatDisplace.INDENT);
    addNormalizeValue(FloatDisplace.LINE);
    setFallback(FloatDisplace.LINE);
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
    FloatDisplace f = (FloatDisplace) resolveValue(process, currentNode, style, key);
    style.setValue(key, f);

    final BoxSpecification boxSpecification = currentNode.getLayoutContext().getBoxSpecification();
    boxSpecification.setFloatDisplace(f);
  }

}
