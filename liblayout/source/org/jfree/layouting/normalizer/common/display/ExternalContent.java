package org.jfree.layouting.normalizer.common.display;

import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.normalizer.common.display.ContentNode;

public class ExternalContent extends ContentNode
{
  private DisplayRole displayRole;

  public ExternalContent (final LayoutNode layoutElement,
                          final DisplayRole displayRole)
  {
    super(layoutElement);
    this.displayRole = displayRole;
  }

  /**
   * Is a enum in the real world ..
   *
   * @return
   */
  public DisplayRole getDisplayRole ()
  {
    return displayRole;
  }
}
