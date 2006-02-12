package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.Fit;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class FitResolveHandler extends ConstantsResolveHandler
{
  public FitResolveHandler ()
  {
    addNormalizeValue(Fit.FILL);
    addNormalizeValue(Fit.MEET);
    addNormalizeValue(Fit.NONE);
    addNormalizeValue(Fit.SLICE);
    setFallback(Fit.FILL);
  }

  public void resolve (final LayoutProcess process, LayoutNode currentNode,
                       LayoutStyle style, StyleKey key)
  {
    final Fit value = (Fit) resolveValue(process, currentNode, style, key);
    style.setValue(key, value);

    currentNode.getLayoutContext().getReplacedElementSpecification().setFit(value);
  }

}
