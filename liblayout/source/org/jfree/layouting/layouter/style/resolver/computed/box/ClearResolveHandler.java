package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.Clear;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutContext;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.PageContext;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class ClearResolveHandler implements ResolveHandler
{
  public ClearResolveHandler ()
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
            TextStyleKeys.DIRECTION
    };
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve (LayoutProcess process,
                       LayoutNode currentNode,
                       LayoutStyle style,
                       StyleKey key)
  {
    final PageContext pageContext = process.getPageContext();
    final boolean singlePage = pageContext.isSinglePage();
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final boolean leftToRight =
            layoutContext.getTextSpecification().getLayoutSpecification().isRightToLeft();
    final BoxSpecification bs = layoutContext.getBoxSpecification();

    CSSValue value = style.getValue(key);
    if (Clear.END.equals(value))
    {
      if (leftToRight)
      {
        value = Clear.RIGHT;
      }
      else
      {
        value = Clear.LEFT;
      }
    }
    else if (Clear.START.equals(value))
    {
      if (leftToRight)
      {
        value = Clear.LEFT;
      }
      else
      {
        value = Clear.RIGHT;
      }
    }
    else if (singlePage && Clear.INSIDE.equals(value))
    {
      if (pageContext.isLeftPage())
      {
        value = Clear.LEFT;
      }
      else
      {
        value = Clear.RIGHT;
      }
    }
    else if (singlePage && Clear.OUTSIDE.equals(value))
    {
      if (pageContext.isLeftPage())
      {
        value = Clear.RIGHT;
      }
      else
      {
        value = Clear.LEFT;
      }
    }

    if (Clear.BOTH.equals(value))
    {
      bs.setClearLeft(true);
      bs.setClearRight(true);
      return;
    }
    if (Clear.TOP.equals(value) || Clear.LEFT.equals(value))
    {
      bs.setClearLeft(true);
      bs.setClearRight(false);
      return;
    }
    if (Clear.BOTTOM.equals(value) || Clear.RIGHT.equals(value))
    {
      bs.setClearLeft(false);
      bs.setClearRight(true);
      return;
    }
    if (Clear.NONE.equals(value))
    {
      bs.setClearLeft(false);
      bs.setClearRight(false);
      return;
    }

  }
}
