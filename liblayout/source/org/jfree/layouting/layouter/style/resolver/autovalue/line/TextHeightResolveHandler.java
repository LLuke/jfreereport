package org.jfree.layouting.layouter.style.resolver.autovalue.line;

import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.line.TextHeight;

public class TextHeightResolveHandler implements ResolveHandler
{
  public TextHeightResolveHandler ()
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
    // right now, none, later maybe the script type ...
    return new StyleKey[0];
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve (LayoutProcess process, LayoutNode currentNode, LayoutStyle style,
                       StyleKey key)
  {
    style.setValue(key, TextHeight.FONT_SIZE);
  }
}
