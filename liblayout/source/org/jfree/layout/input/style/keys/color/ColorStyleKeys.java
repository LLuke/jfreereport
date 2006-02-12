package org.jfree.layouting.input.style.keys.color;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 30.10.2005, 18:47:30
 *
 * @author Thomas Morgner
 */
public class ColorStyleKeys
{
  public static final StyleKey COLOR =
          StyleKeyRegistry.getRegistry().createKey
          ("color", false, true, false);

  /**
   * Not sure whether we can implement this one. It is a post-processing
   * operation, and may or may not be supported by the output target.
   */
  public static final StyleKey OPACITY =
          StyleKeyRegistry.getRegistry().createKey
          ("opacity", false, false, false);

  /**
   * For now, we do not care about color profiles. This might have to do with
   * me being clueless about the topic, but also with the cost vs. usefullness
   * calculation involved.
   */
  public static final StyleKey COLOR_PROFILE =
          StyleKeyRegistry.getRegistry().createKey
          ("color-profile", false, true, false);
  public static final StyleKey RENDERING_INTENT =
          StyleKeyRegistry.getRegistry().createKey
          ("rendering-intent", false, true, false);

  private ColorStyleKeys() {}
}
