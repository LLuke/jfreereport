package org.jfree.layouting.layouter.style.resolver;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutNode;

/**
 * Not yet used. Needs to be implemented. Its my fast resolver, but that
 * one needs more thinking and more tweaking. 
 */
public class FlatStyleResolver extends AbstractStyleResolver
{
  public FlatStyleResolver ()
  {
  }

  public StyleResolver deriveInstance ()
  {
    return this;
  }

  public void initialize (LayoutProcess layoutProcess)
  {
    super.initialize(layoutProcess);

  }

  /**
   * Resolves the style. This is guaranteed to be called in the order of the document
   * elements traversing the document tree using the 'deepest-node-first' strategy.
   *
   * @param node
   */
  public void resolveStyle (LayoutElement node)
  {
    // this is a three stage process
    final LayoutStyle style = node.getStyle();
    final StyleKey[] keys = getKeys();

    // Stage 0: Initialize with the built-in defaults
    // Stage 1a: Add the parent styles (but only the one marked as inheritable).
    final LayoutStyle initialStyle = getInitialStyle();

    // initialize the style ...
    for (int i = 0; i < keys.length; i++)
    {
      StyleKey key = keys[i];
      style.setValue(key, initialStyle.getValue(key));
    }

    // Stage 2: Search for all class attributes, and lookup the corresponding
    // style. The sty


  }


}
