package org.jfree.layouting.layouter.style.resolver.computed.text;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BorderWidth;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextDecorationWidth;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.layouter.model.LayoutElement;

public class TextDecorationWidthResolveHandler extends ConstantsResolveHandler
{
  public TextDecorationWidthResolveHandler ()
  {
    addValue(BorderWidth.THIN, new CSSNumericValue(CSSNumericType.PT, 0.5));
    addValue(BorderWidth.MEDIUM, new CSSNumericValue(CSSNumericType.PT, 1));
    addValue(BorderWidth.THICK, new CSSNumericValue(CSSNumericType.PT, 1.5));
    addValue(TextDecorationWidth.DASH, new CSSNumericValue(CSSNumericType.PT, 0.75));
    addValue(TextDecorationWidth.BOLD, new CSSNumericValue(CSSNumericType.PT, 1.25));
    setFallback(CSSNumericValue.ZERO_LENGTH);
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
            FontStyleKeys.FONT_SIZE
    };
  }

  protected CSSValue resolveValue (final LayoutProcess process,
                                   final LayoutElement currentNode,
                                   final LayoutStyle style,
                                   final StyleKey key)
  {
    CSSValue value = style.getValue(key);
    if (value instanceof CSSConstant)
    {
      return super.resolveValue(process, currentNode, style, key);
    }
    return value;
  }
}
