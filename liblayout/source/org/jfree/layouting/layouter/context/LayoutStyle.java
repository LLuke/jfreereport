package org.jfree.layouting.layouter.context;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.StyleKey;

/**
 * Creation-Date: 18.11.2006, 18:55:03
 *
 * @author Thomas Morgner
 */
public interface LayoutStyle
{
  CSSValue getValue(StyleKey key);

  void setValue(StyleKey key, CSSValue value);
}
