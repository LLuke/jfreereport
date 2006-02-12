package org.jfree.layouting.layouter.style.resolver.computed.content;

import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.content.ContentToken;
import org.jfree.layouting.model.content.StringContent;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentSequence;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSStringType;

public class StringSetResolveHandler implements ResolveHandler
{
  public StringSetResolveHandler ()
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
            ContentStyleKeys.COUNTER_INCREMENT,
            ContentStyleKeys.QUOTES,
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
    if (currentNode instanceof LayoutElement == false)
    {
      return; // we ignore non-elements, as they cannot have
              // the string-set property.
    }

    final LayoutElement element = (LayoutElement) currentNode;
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSValueList == false)
    {
      return; // cant handle that one
    }

    final CSSValueList list = (CSSValueList) value;
    final int size = list.getLength();
    for (int i = 0; i < size; i++)
    {
      ContentSequence sequence = (ContentSequence) list.getItem(i);
      CSSValue[] contents = sequence.getContents();
      for (int j = 0; j < contents.length; j++)
      {
        CSSValue content = contents[j];
        // todo This is not yet implemented. The model needs some refinement
      }
    }
  }


  private ContentToken createToken (CSSValue content)
  {
    if (content instanceof CSSStringValue)
    {
      CSSStringValue sval = (CSSStringValue) content;
      if (CSSStringType.STRING.equals(sval.getType()))
      {
        return new StringContent(sval.getValue());
      }
      else
      {
        // this is an external URL, so try to load it.
      }
    }

    return null; // todo
  }
}
