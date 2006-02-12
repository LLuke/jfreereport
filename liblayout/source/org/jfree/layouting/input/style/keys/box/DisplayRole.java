package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * The Display-Role property describes the role an element plays in the parent
 * algorithm. Seeing that property as general 'LayoutManagerConstraint' might
 * be apropriate.
 * <p/>
 * The RUBY_* properties are required for Japanese and other Asian font support
 * and not yet used.
 */
public class DisplayRole extends CSSConstant
{
  /**
   * The element is not rendered. The rendering is the same as if the element
   * had been removed from the document tree, except for possible effects on
   * counters (see [generated] or [paged]).
   * <p/>
   * Note that :before and :after pseudo elements of this element are also not
   * rendered, see [generated].)
   */
  public static final DisplayRole NONE = new DisplayRole("none");

  /**
   * The element is rendered as a rectangular block. See Collapsing margins
   * for its position relative to earlier boxes in the same flow. In paged
   * media [ref] or inside another element that has two or more columns,
   * the box may be split into several smaller boxes.
   */
  public static final DisplayRole BLOCK = new DisplayRole("block");

  /**
   * The element is rendered inside a line box. It may be split into several
   * boxes because of line breaking and bidi processing (see the Text module).
   */
  public static final DisplayRole INLINE = new DisplayRole("inline");

  /**
   * The element is rendered the same as if it had display-role 'block', but
   * in addition a marker is generated (see 'list-style').
   */
  public static final DisplayRole LIST_ITEM = new DisplayRole("list-item");

  /**
   * The effect depends on what comes after the element. If the next element
   * (in the depth-first, left to right tree traversal, so not necessarily a
   * sibling) has a 'display-model' of 'block-inside', the current element will
   * be rendered as if it had display-role 'inline' and was the first child
   * of that block element. Otherwise this element will be rendered as if it
   * had display-role 'block'. [Does this explain Ian's tests?]
   */
  public static final DisplayRole RUN_IN = new DisplayRole("run-in");

  /**
   * The effect depends on the intrinsic size of this element and on what comes
   * after it. If the next element has a 'display-role' of 'block', and the
   * intrinsic width of the compact element is less than or equal to the left
   * margin of that block (resp. the right margin, if the block's 'direction'
   * is 'rtl'), then the compact element is rendered in the left (right) margin
   * of the block at its intrinsic size and baseline aligned with the first
   * line box of the block. [Do we need a different alignment depending on
   * script?] In all other cases the compact element is rendered as if its
   * display-role was 'block'.
   */
  public static final DisplayRole COMPACT = new DisplayRole("compact");

  /** See the Tables module [CSS3TBL]. */
  public static final DisplayRole TABLE_ROW = new DisplayRole("table-row");
  public static final DisplayRole TABLE_CELL = new DisplayRole("table-cell");
  public static final DisplayRole TABLE_ROW_GROUP = new DisplayRole(
          "table-row-group");
  public static final DisplayRole TABLE_HEADER_GROUP = new DisplayRole(
          "table-header-group");

  public static final DisplayRole TABLE_FOOTER_GROUP = new DisplayRole(
          "table-footer-group");
  public static final DisplayRole TABLE_COLUMN = new DisplayRole(
          "table-column");
  public static final DisplayRole TABLE_COLUMN_GROUP = new DisplayRole(
          "table-column-group");
  public static final DisplayRole TABLE_CAPTION = new DisplayRole(
          "table-caption");

  /** Ruby is not yet used. */
  public static final DisplayRole RUBY_TEXT = new DisplayRole("ruby-text");
  public static final DisplayRole RUBY_BASE = new DisplayRole("ruby-base");
  public static final DisplayRole RUBY_BASE_GROUP = new DisplayRole(
          "ruby-base-group");
  public static final DisplayRole RUBY_TEXT_GROUP
          = new DisplayRole("ruby-text-group");

  /**
   * A JFreeReport compatibility setting. Enables the absolute positioning mode.
   */
  public static final DisplayRole ABSOLUTE
          = new DisplayRole("absolute");


  private DisplayRole(String name)
  {
    super(name);
  }
}
