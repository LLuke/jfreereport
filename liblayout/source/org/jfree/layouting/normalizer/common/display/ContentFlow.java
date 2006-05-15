package org.jfree.layouting.normalizer.common.display;

import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.input.style.keys.box.DisplayRole;

public class ContentFlow extends ContentBox
{
  public ContentFlow (final LayoutElement core)
  {
    super(core, new BlockCompositionStrategy());
  }

  /**
   * Is a enum in the real world ..
   *
   * @return the display role for this node
   */
  public DisplayRole getDisplayRole ()
  {
    return DisplayRole.BLOCK;
  }

  /**
   * This flag signals, that is element cannot compute its layout yet. For an element to
   * be layoutable, at least the width (in latin layouts) must be known.
   * <p/>
   * After we know the width, determining the height should not be a problem.
   *
   * @return true, if the element can be layouted (even if it is temporary) or false, if
   *         we need more content for the layouting process.
   */
  public boolean isLayoutable ()
  {
    return true;
  }
}
