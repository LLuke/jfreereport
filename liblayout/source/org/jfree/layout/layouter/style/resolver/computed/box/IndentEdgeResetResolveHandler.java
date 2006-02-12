package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.IndentEdgeReset;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class IndentEdgeResetResolveHandler extends ConstantsResolveHandler
{
  public IndentEdgeResetResolveHandler ()
  {
    addNormalizeValue(IndentEdgeReset.BORDER_EDGE);
    addNormalizeValue(IndentEdgeReset.CONTENT_EDGE);
    addNormalizeValue(IndentEdgeReset.MARGIN_EDGE);
    addNormalizeValue(IndentEdgeReset.NONE);
    addNormalizeValue(IndentEdgeReset.PADDING_EDGE);
    setFallback(IndentEdgeReset.NONE);
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
    IndentEdgeReset ir = (IndentEdgeReset) resolveValue(process, currentNode, style, key);
    style.setValue(key, ir);

    final BoxSpecification boxSpecification = currentNode.getLayoutContext().getBoxSpecification();
    boxSpecification.setIndentEdgeReset(ir);
  }


}
