package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 27.10.2005, 22:21:37
 *
 * @author Thomas Morgner
 */
public class FloatDisplace extends CSSConstant
{
  /**
   * Line boxes should be shortened and moved so as to avoid floats. The margin,
   * border, padding and background of the element are not affected by floats.
   * (This is the behavior as described in CSS2.)
   */
  public static final FloatDisplace LINE = new FloatDisplace("line");
  /**
   * The distance between the margin edge of the floats and the start of the
   * line box is set to the distance between the active reference indent edge
   * (see the 'indent-edge-reset' property) and the content edge of the block
   * box. This ensures that relative indents are preserved in the presence of
   * floats.
   */
  public static final FloatDisplace INDENT = new FloatDisplace("indent");
  /**
   * The containing block's width as used by the horizontal formatting model is
   * reduced by the width of the floats intruding upon its content area (not
   * taking into account floating descendants or floating elements that appear
   * later in the document tree). The block is then flowed in this reduced
   * containing block width.
   * <p/>
   * If the effective containing block width is, by the algorithm given above,
   * reduced to a value smaller than the sum of the margin-left,
   * border-width-left, padding-left, width, padding-right, border-width-right,
   * and margin-right values (treating any 'auto' values as zero) then the
   * margin-top of the block is increased until the effective containing block
   * width is no longer so constrained or until all floats have been cleared,
   * whichever occurs first.
   */
  public static final FloatDisplace BLOCK = new FloatDisplace("block");
  /**
   * As for the 'block' value, but the determination of intrusions that adjust
   * the width of the block is done separately on each page on which the block
   * appears. Thus, the block may be narrowed on the first page due to one or
   * more intrusions, but may expand (or contract) its width on subsequent pages
   * with different intrusions. The computed value of the 'width' property for
   * this case is...?
   */
  public static final FloatDisplace BLOCK_WITHIN_PAGE =
          new FloatDisplace("block-within-page");

  private FloatDisplace(String name)
  {
    super(name);
  }
}
