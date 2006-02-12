package org.jfree.layouting.model.position;

import org.jfree.layouting.input.style.keys.positioning.Position;

public class PositionSpecification
{
  private Position position;
  private Long top;
  private Long left;
  private Long bottom;
  private Long right;

  private int z;

  public PositionSpecification ()
  {
  }

  public Long getBottom ()
  {
    return bottom;
  }

  public void setBottom (Long bottom)
  {
    this.bottom = bottom;
  }

  public Long getLeft ()
  {
    return left;
  }

  public void setLeft (Long left)
  {
    this.left = left;
  }

  public Position getPosition ()
  {
    return position;
  }

  public void setPosition (Position position)
  {
    this.position = position;
  }

  public Long getRight ()
  {
    return right;
  }

  public void setRight (Long right)
  {
    this.right = right;
  }

  public Long getTop ()
  {
    return top;
  }

  public void setTop (Long top)
  {
    this.top = top;
  }

  public int getZ ()
  {
    return z;
  }

  public void setZ (int z)
  {
    this.z = z;
  }
}
