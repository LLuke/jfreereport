package org.jfree.layouting.layouter.style.resolver.percentages.line;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineHeight;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutContext;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.resolver.percentages.LengthResolverUtility;
import org.jfree.layouting.util.geom.StrictGeomUtility;

public class LineHeightResolveHandler implements ResolveHandler
{
  public LineHeightResolveHandler ()
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
            FontStyleKeys.FONT_SIZE,
            FontStyleKeys.FONT_SIZE_ADJUST,

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
    CSSValue value = style.getValue(key);
    if (LineHeight.NONE.equals(value))
    {
      // query the anchestor, if there's one ..
      handleNone(currentNode);
      return;
    }

    if (LineHeight.NORMAL.equals(value))
    {
      handleNormal(currentNode);
      return;
    }

    if (value instanceof CSSNumericValue == false)
    {
      // fall back to normal ..
      handleNormal(currentNode);
      return;
    }
    CSSNumericValue nval = (CSSNumericValue) value;
    final LayoutContext layoutContext = currentNode.getLayoutContext();

    if (LengthResolverUtility.isLengthValue(nval))
    {
      // we can convert it directly ..
      long val = LengthResolverUtility.convertLengthToInternal
              (nval, currentNode, process.getOutputMetaData());
      layoutContext.getLineSpecification().setLineHeight(val);
      return;
    }

    final double factor;
    if (nval.getType().equals(CSSNumericType.PERCENTAGE))
    {
      factor = nval.getValue() / 100d;
    }
    else if (nval.getType().equals(CSSNumericType.NUMBER))
    {
      factor = nval.getValue();
    }
    else
    {
      handleNormal(currentNode);
      return;
    }

    final double fontSize =
            layoutContext.getFontSpecification().getFontSize();
    layoutContext.getLineSpecification().setLineHeight
          (StrictGeomUtility.toInternalValue(fontSize * factor));

  }

  private void handleNormal (LayoutNode currentNode)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final double fontSize =
            layoutContext.getFontSpecification().getFontSize();
    if (fontSize < 10)
    {
      layoutContext.getLineSpecification().setLineHeight
            (StrictGeomUtility.toInternalValue(fontSize * 1.2));
    }
    else if (fontSize < 24)
    {
      layoutContext.getLineSpecification().setLineHeight
            (StrictGeomUtility.toInternalValue(fontSize * 1.1));
    }
    else
    {
      layoutContext.getLineSpecification().setLineHeight
            (StrictGeomUtility.toInternalValue(fontSize * 1.05));
    }

  }

  private void handleNone (LayoutNode currentNode)
  {
    final double fontSize;
    final LayoutElement parent = currentNode.getParent();
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    if (parent == null)
    {
      // fall back to normal;
      fontSize = layoutContext.getFontSpecification().getFontSize();
    }
    else
    {
      fontSize = parent.getLayoutContext().getFontSpecification().getFontSize();
    }

    layoutContext.getLineSpecification().setLineHeight
            (StrictGeomUtility.toInternalValue(fontSize));
  }
}
