package org.jfree.layouting.layouter.style;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Unlike the old JFreeReport stylesheet, this implementation has no inheritance
 * at all. It needs to be resolved manually, which is no bad thing, as we have
 * to do this anyway during the computation.
 *
 * @author Thomas Morgner
 */
public class LayoutStyle
{
  private boolean resolved;
  private CSSValue[] values;

  public LayoutStyle()
  {
    final int keyCount = StyleKeyRegistry.getRegistry().getKeyCount();
    values = new CSSValue[keyCount];
  }

  public CSSValue getValue (StyleKey key)
  {
    return values[key.getIndex()];
  }

  public void setValue (StyleKey key, CSSValue value)
  {
    values[key.getIndex()] = value;
  }

  public void setResolved(final boolean resolved)
  {
    this.resolved = resolved;
  }

  /**
   * A flag indicating whether this style definition has been fully resolved.
   * A fully resolved style does not depend on external datasources and does
   * not contain relative values anymore.
   *
   * @return true, if all style properties have been resolved, false otherwise. 
   */
  public boolean isResolved()
  {
    return resolved;
  }
}
