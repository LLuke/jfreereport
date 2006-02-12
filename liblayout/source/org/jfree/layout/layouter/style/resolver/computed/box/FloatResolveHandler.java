package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class FloatResolveHandler extends ConstantsResolveHandler
{
  public FloatResolveHandler ()
  {
    addNormalizeValue(Floating.BOTTOM);
    addNormalizeValue(Floating.LEFT);
    addNormalizeValue(Floating.END);
    addNormalizeValue(Floating.INSIDE);
    addNormalizeValue(Floating.IN_COLUMN);
    addNormalizeValue(Floating.MID_COLUMN);
    addNormalizeValue(Floating.NONE);
    addNormalizeValue(Floating.OUTSIDE);
    addNormalizeValue(Floating.RIGHT);
    addNormalizeValue(Floating.START);
    addNormalizeValue(Floating.TOP);
    setFallback(Floating.NONE);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[]{
            BoxStyleKeys.DISPLAY_ROLE
    };
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
    final BoxSpecification boxSpecification = currentNode.getLayoutContext().getBoxSpecification();
    if (DisplayRole.NONE.equals(boxSpecification.getDisplayRole()))
    {
      style.setValue(key, Floating.NONE);
      boxSpecification.setFloating(Floating.NONE);
      return;
    }

    Floating f = (Floating) resolveValue(process, currentNode, style, key);
    style.setValue(key, f);
    boxSpecification.setFloating(f);
    if (Floating.NONE.equals(f) == false)
    {
      boxSpecification.setDisplayModel(DisplayModel.BLOCK_INSIDE);
      boxSpecification.setDisplayRole(DisplayRole.BLOCK);
    }
  }
}
