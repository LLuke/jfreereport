package org.jfree.layouting.layouter.style.resolver.computed.content;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSCounterValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.ElementContext;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class CounterIncrementResolveHandler implements ResolveHandler
{
  public CounterIncrementResolveHandler ()
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
            ContentStyleKeys.COUNTER_RESET,
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
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSValueList == false)
    {
      return; // do nothing.
    }
    if (currentNode instanceof LayoutNode == false)
    {
      return; // counters only apply to element nodes.
    }
    final LayoutElement element = (LayoutElement) currentNode;
    final ElementContext elementContext = element.getElementContext();
    final CSSValueList valueList = (CSSValueList) value;
    for (int i = 0; i < valueList.getLength(); i++)
    {
      final CSSValue item = valueList.getItem(i);
      if (item instanceof CSSCounterValue == false)
      {
        continue;
      }
      CSSCounterValue counter = (CSSCounterValue) item;
      final int counterValue = parseCounterValue(counter, element);
      elementContext.incrementCounter(counter.getIdentifier(), counterValue);
    }
  }

  private int parseCounterValue (final CSSCounterValue counter,
                                 final LayoutElement element)
  {
    final CSSValue rawValue = counter.getValue();
    if (rawValue instanceof CSSNumericValue)
    {
      final CSSNumericValue nval = (CSSNumericValue) rawValue;
      return nval.intValue();
    }
    if (rawValue instanceof CSSAttrFunction)
    {
      final CSSAttrFunction attrFunction = (CSSAttrFunction) rawValue;
      final String attrName = attrFunction.getName();
      final Object rawAttribute = element.getAttribute(attrName);
      if (rawAttribute instanceof Number)
      {
        final Number nAttr = (Number) rawAttribute;
        return nAttr.intValue();
      }
    }
    return 0;
  }
}
