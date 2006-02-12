package org.jfree.layouting.layouter.style.resolver.autovalue.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.FitPositionValue;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.text.TextSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class FitPositionResolveHandler implements ResolveHandler
{
  private static CSSNumericValue LEFT_TOP = new CSSNumericValue(CSSNumericType.PERCENTAGE, 0);
  private static CSSNumericValue RIGHT_BOTTOM = new CSSNumericValue(CSSNumericType.PERCENTAGE, 100);

  public FitPositionResolveHandler ()
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
            TextStyleKeys.BLOCK_PROGRESSION,
            TextStyleKeys.DIRECTION
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
    final TextSpecification textSpecification =
            currentNode.getLayoutContext().getTextSpecification();
    final boolean rightToLeft =
            textSpecification.getLayoutSpecification().isRightToLeft();
    final BlockProgression blockProgression =
            textSpecification.getLayoutSpecification().getBlockProgression();
    // this might be invalid ...
    if (BlockProgression.TB.equals(blockProgression))
    {
      if (rightToLeft)
      {
        style.setValue(key, new FitPositionValue(RIGHT_BOTTOM, LEFT_TOP));
      }
      else
      {
        style.setValue(key, new FitPositionValue(LEFT_TOP, LEFT_TOP));
      }
    }
    else if (BlockProgression.RL.equals(blockProgression))
    {
      if (rightToLeft)
      {
        style.setValue(key, new FitPositionValue(LEFT_TOP, LEFT_TOP));
      }
      else
      {
        style.setValue(key, new FitPositionValue(RIGHT_BOTTOM, LEFT_TOP));
      }
    }
    else if (BlockProgression.LR.equals(blockProgression))
    {
      if (rightToLeft)
      {
        style.setValue(key, new FitPositionValue(RIGHT_BOTTOM, RIGHT_BOTTOM));
      }
      else
      {
        style.setValue(key, new FitPositionValue(LEFT_TOP, LEFT_TOP));
      }
    }
  }
}
