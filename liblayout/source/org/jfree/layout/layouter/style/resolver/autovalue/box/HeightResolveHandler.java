package org.jfree.layouting.layouter.style.resolver.autovalue.box;

import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.LayoutContext;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.positioning.PositioningStyleKeys;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;

public class HeightResolveHandler implements ResolveHandler
{
  public HeightResolveHandler ()
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
    return new StyleKey[] {
            BoxStyleKeys.DISPLAY_ROLE,
            BoxStyleKeys.DISPLAY_MODEL,
            BoxStyleKeys.FLOAT,
            PositioningStyleKeys.POSITION,
            TextStyleKeys.BLOCK_PROGRESSION
    };
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
    // if the current element is a block level element,
    final LayoutContext lc = currentNode.getLayoutContext();
    final DisplayRole role =
            lc.getBoxSpecification().getDisplayRole();
    if (DisplayRole.BLOCK.equals(role) == false)
    {
      return; // we cannot resolve that one. Auto keeps its special meaning...
    }
    // check whether we have vertical flow (as in asian scripts)
    final BlockProgression blockProgression =
            lc.getTextSpecification().getLayoutSpecification().getBlockProgression();
    if (BlockProgression.TB.equals(blockProgression))
    {
      // in roman scripts, the height depends on the content if set to auto
      return;
    }
    // if so then set the height to 100%
    style.setValue(key, new CSSNumericValue(CSSNumericType.PERCENTAGE, 100));
  }
}
