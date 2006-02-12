package org.jfree.layouting.model.line;

import org.jfree.layouting.input.style.keys.line.TextHeight;
import org.jfree.layouting.input.style.keys.line.LineStackingStrategy;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;

/**
 * The vertical align should be split up as specified in
 * the linebox module.
 */
public class LineSpecification
{
  private TextHeight textHeight;
  private long lineHeight;
  private LineStackingStrategy lineStackingStrategy;
  private VerticalAlign verticalAlign;

  public LineSpecification ()
  {
  }

  public TextHeight getTextHeight ()
  {
    return textHeight;
  }

  public void setTextHeight (TextHeight textHeight)
  {
    this.textHeight = textHeight;
  }

  public long getLineHeight ()
  {
    return lineHeight;
  }

  public void setLineHeight (long lineHeight)
  {
    this.lineHeight = lineHeight;
  }

  public LineStackingStrategy getLineStackingStrategy ()
  {
    return lineStackingStrategy;
  }

  public void setLineStackingStrategy (LineStackingStrategy lineStackingStrategy)
  {
    this.lineStackingStrategy = lineStackingStrategy;
  }

  public VerticalAlign getVerticalAlign ()
  {
    return verticalAlign;
  }

  public void setVerticalAlign (VerticalAlign verticalAlign)
  {
    this.verticalAlign = verticalAlign;
  }
}
