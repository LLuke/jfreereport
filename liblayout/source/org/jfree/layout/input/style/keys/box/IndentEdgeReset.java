package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 18:27:41
 *
 * @author Thomas Morgner
 */
public class IndentEdgeReset extends CSSConstant
{
  public static final IndentEdgeReset NONE =
          new IndentEdgeReset("none");
  public static final IndentEdgeReset MARGIN_EDGE =
          new IndentEdgeReset("margin-edge");
  public static final IndentEdgeReset BORDER_EDGE =
          new IndentEdgeReset("border-edge");
  public static final IndentEdgeReset PADDING_EDGE =
          new IndentEdgeReset("padding-edge");
  public static final IndentEdgeReset CONTENT_EDGE =
          new IndentEdgeReset("content-edge");

  private IndentEdgeReset(String name)
  {
    super(name);
  }

}
