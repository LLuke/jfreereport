package org.jfree.layouting.layouter.style.resolver.computed.position;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.input.style.keys.positioning.Position;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutContext;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class PositionResolveHandler extends ConstantsResolveHandler
{
  public PositionResolveHandler ()
  {
    addNormalizeValue(Position.ABSOLUTE);
    addNormalizeValue(Position.FIXED);
    addNormalizeValue(Position.RELATIVE);
    addNormalizeValue(Position.STATIC);
    setFallback(Position.STATIC);
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
            BoxStyleKeys.DISPLAY_MODEL,
            BoxStyleKeys.FLOAT
    };
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve (final LayoutProcess process,
                       final LayoutNode currentNode,
                       final LayoutStyle style,
                       final StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final BoxSpecification boxSpecs = layoutContext.getBoxSpecification();
    if (DisplayRole.NONE.equals(boxSpecs.getDisplayModel()))
    {
      // skip ... the element will not be displayed ...
      layoutContext.getPositionSpecification().setPosition(Position.STATIC);
      return;
    }

    if (Floating.NONE.equals(boxSpecs.getFloating()))
    {
      layoutContext.getPositionSpecification().setPosition(Position.STATIC);
      return;
    }

    final Position value = (Position) resolveValue(process, currentNode, style, key);
    layoutContext.getPositionSpecification().setPosition(value);
    if (Position.ABSOLUTE.equals(value) ||
        Position.FIXED.equals(value))
    {
      // http://www.w3.org/TR/REC-CSS2/visuren.html#propdef-float
      // this is specified in 9.7: Relationships between 'display', 'position', and 'float'
      boxSpecs.setDisplayModel(DisplayModel.BLOCK_INSIDE);
      boxSpecs.setDisplayRole(DisplayRole.BLOCK);
    }
  }
}
