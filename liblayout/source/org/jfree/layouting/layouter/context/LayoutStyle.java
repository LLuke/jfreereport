package org.jfree.layouting.layouter.context;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 18.11.2006, 18:55:03
 *
 * @author Thomas Morgner
 */
public interface LayoutStyle
{
  public CSSValue getValue(StyleKey key);

  public void setValue(StyleKey key, CSSValue value);

  public boolean copyFrom(LayoutStyle style);
}
