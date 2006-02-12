package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * http://www.w3.org/TR/css3-box/
 * <p/>
 * Creation-Date: 27.10.2005, 21:12:57
 *
 * @author Thomas Morgner
 */
public class BoxStyleKeys
{
  public static StyleKey BOX_SIZING =
          StyleKeyRegistry.getRegistry().createKey
                  ("box-sizing", false, false, false);

  /**
   * The 'display-model' property determines the algorithm with which an element
   * lays out its children.
   */
  public static StyleKey DISPLAY_MODEL =
          StyleKeyRegistry.getRegistry().createKey
                  ("display-model", false, false, false);

  /**
   * 'display-role' specifies what role an element has in its parent's
   * algorithm.
   */
  public static StyleKey DISPLAY_ROLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("display-role", false, false, false);

  /**
   * Set the padding around the content of a box. Padding is between content and
   * border. Background expands over the padding up to the border.
   * <p/>
   * Values given may not be negative. If percentages are given, all paddings
   * are relative to the <strong>width</strong> of the parent (if the flow is
   * horizontal, else the height is used).
   */
  public static StyleKey PADDING_TOP =
          StyleKeyRegistry.getRegistry().createKey
                  ("padding-top", false, false, false);

  public static StyleKey PADDING_LEFT =
          StyleKeyRegistry.getRegistry().createKey
                  ("padding-left", false, false, false);

  public static StyleKey PADDING_BOTTOM =
          StyleKeyRegistry.getRegistry().createKey
                  ("padding-bottom", false, false, false);

  public static StyleKey PADDING_RIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("padding-right", false, false, false);

  /** The border is defined in the border module. */

  /**
   * These properties set the thickness of the margin. The value may be
   * negative, but the UA may impose a lower bound.
   * <p/>
   * 'Margin' is a shorthand to set top, right, bottom and left together. If
   * four values are given, they set top, right, bottom and left in that order.
   * If left is omitted, it is the same as right. If bottom is omitted, it is
   * the same as top. If right is omitted it is the same as top.
   * <p/>
   * The meaning of 'auto' on 'margin-left' '-right', '-top' and '-bottom' is as
   * follows:
   * <p/>
   * on floating and inline-level elements, 'auto' is equal to 0 on positioned
   * elements: see the positioning module[ref] on normal-flow elements, if the
   * containing block is horizontal: o 'auto' on 'margin-top' and
   * 'margin-bottom' is equal to 0 o on 'margin-right' and 'margin-left': see
   * equation (1) below on normal-flow elements, if the containing block is
   * vertical : o 'auto' on 'margin-right' and 'margin-left' is equal to 0 o on
   * 'margin-top' and 'margin-bottom': see equation (1) below
   * <p/>
   * Margins must satisfy certain constraints, which means that the computed
   * value may be different from the specified value. See equation (1) below.
   * <p/>
   * Note that in a horizontal flow, percentages on 'margin-top' and
   * 'margin-bottom' are relative to the width of the containing block, not the
   * height (and in vertical flow, 'margin-left' and 'margin-right' are relative
   * to the height, not the width).
   * <p/>
   * Note that 'margin-top' and 'margin-bottom' do not apply to non-replaced,
   * inline elements (in horizontal flow); see [CSS3LINE].
   */
  public static StyleKey MARGIN_TOP =
          StyleKeyRegistry.getRegistry().createKey
                  ("margin-top", false, false, false);

  public static StyleKey MARGIN_LEFT =
          StyleKeyRegistry.getRegistry().createKey
                  ("margin-left", false, false, false);

  public static StyleKey MARGIN_BOTTOM =
          StyleKeyRegistry.getRegistry().createKey
                  ("margin-bottom", false, false, false);

  public static StyleKey MARGIN_RIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("margin-right", false, false, false);

  /** These values may be auto. */
  public static StyleKey WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("width", false, false, false);

  /** These values may be auto. */
  public static StyleKey HEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("height", false, false, false);

  /** These values may be auto. If specified, this overrides the width.*/
  public static StyleKey BOX_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("box-width", false, false, false);

  /** These values may be auto. If specified, this overrides the height.*/
  public static StyleKey BOX_HEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("box-height", false, false, false);

  // The Box-Sizing property is not implemented here.

  public static StyleKey MAX_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("max-width", false, false, false);

  public static StyleKey MAX_HEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("max-height", false, false, false);

  public static StyleKey MIN_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("min-width", false, false, false);

  public static StyleKey MIN_HEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("min-height", false, false, false);


  public static StyleKey FIT =
          StyleKeyRegistry.getRegistry().createKey
                  ("fit", false, true, false);

  /**
   * A pair of values.
   */
  public static StyleKey FIT_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("fit-position", false, true, false);

  /**
   * We alter the semantics a bit. When crop is computed, absolute values are
   * used. Inset-Rect is voodoo, so ignore it for now.
   */
  public static StyleKey CROP =
          StyleKeyRegistry.getRegistry().createKey
                  ("crop", false, false, false);

  public static StyleKey FLOAT =
          StyleKeyRegistry.getRegistry().createKey
                  ("float", false, false, false);
  /**
   * Optional: (The old idea from 1996; allow shapes).
   * Do not implement it for now, assume 'box'
   */
  public static StyleKey FLOAT_TYPE =
          StyleKeyRegistry.getRegistry().createKey
                  ("float-type", false, false, false);

  public static StyleKey CLEAR =
          StyleKeyRegistry.getRegistry().createKey
                  ("clear", false, false, false);

  public static StyleKey CLEAR_AFTER =
          StyleKeyRegistry.getRegistry().createKey
                  ("clear-after", false, false, false);

  public static StyleKey FLOAT_DISPLACE =
          StyleKeyRegistry.getRegistry().createKey
                  ("float-displace", false, true, false);

  public static StyleKey INDENT_EDGE_RESET =
          StyleKeyRegistry.getRegistry().createKey
                  ("indent-edge-reset", false, false, false);

  /** The plain overflow behaviour can be constructed using these both properties. */
  public static StyleKey OVERFLOW_X =
          StyleKeyRegistry.getRegistry().createKey
                  ("overflow-x", false, false, false);

  public static StyleKey OVERFLOW_Y =
          StyleKeyRegistry.getRegistry().createKey
                  ("overflow-y", false, false, false);
  // todo...

  public static StyleKey OVERFLOW_CLIP =
          StyleKeyRegistry.getRegistry().createKey
                  ("overflow-clip", false, false, false);


  /**
   * The Marquee-Property set is not used. Scrolling or animations are useless
   * when printing.
   */

  public static StyleKey VISIBILITY =
          StyleKeyRegistry.getRegistry().createKey
                  ("visibility", false, true, false);

  private BoxStyleKeys()
  {
  }

}
