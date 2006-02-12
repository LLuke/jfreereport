package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.model.LayoutNode;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class DisplayRoleResolveHandler extends ConstantsResolveHandler
{
  public DisplayRoleResolveHandler ()
  {
    addNormalizeValue(DisplayRole.BLOCK);
    addNormalizeValue(DisplayRole.COMPACT);
    addNormalizeValue(DisplayRole.INLINE);
    addNormalizeValue(DisplayRole.LIST_ITEM);
    addNormalizeValue(DisplayRole.NONE);
    addNormalizeValue(DisplayRole.RUBY_BASE);
    addNormalizeValue(DisplayRole.RUBY_BASE_GROUP);
    addNormalizeValue(DisplayRole.RUBY_TEXT);
    addNormalizeValue(DisplayRole.RUBY_TEXT_GROUP);
    addNormalizeValue(DisplayRole.RUN_IN);
    addNormalizeValue(DisplayRole.TABLE_CAPTION);
    addNormalizeValue(DisplayRole.TABLE_CELL);
    addNormalizeValue(DisplayRole.TABLE_COLUMN);
    addNormalizeValue(DisplayRole.TABLE_COLUMN_GROUP);
    addNormalizeValue(DisplayRole.TABLE_FOOTER_GROUP);
    addNormalizeValue(DisplayRole.TABLE_HEADER_GROUP);
    addNormalizeValue(DisplayRole.TABLE_ROW);
    addNormalizeValue(DisplayRole.TABLE_ROW_GROUP);

    setFallback(DisplayRole.INLINE);
  }

  /**
   * Resolves a single property.
   *
   * @param style
   * @param currentNode
   */
  public void resolve (final LayoutProcess process, LayoutNode currentNode,
                       LayoutStyle style, StyleKey key)
  {
    final DisplayRole role = (DisplayRole) resolveValue(process, currentNode, style, key);
    style.setValue(key, role);
    
    currentNode.getLayoutContext().getBoxSpecification().setDisplayRole(role);
  }
}
