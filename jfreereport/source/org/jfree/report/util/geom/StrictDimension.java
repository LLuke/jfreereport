package org.jfree.report.util.geom;

import java.io.Serializable;

public class StrictDimension implements Serializable, Cloneable
{
  private long width;
  private long height;

  public StrictDimension ()
  {
  }

  public StrictDimension (final long width, final long height)
  {
    this.width = width;
    this.height = height;
  }

  public long getHeight ()
  {
    return height;
  }

  public long getWidth ()
  {
    return width;
  }

  /**
   * Sets the size of this <code>Dimension</code> object to the specified width and
   * height. This method is included for completeness, to parallel the {@link
   * java.awt.Component#getSize getSize} method of {@link java.awt.Component}.
   *
   * @param width  the new width for the <code>Dimension</code> object
   * @param height the new height for the <code>Dimension</code> object
   */
  public void setSize (final long width, final long height)
  {
    this.width = width;
    this.height = height;
  }

  public void setHeight (final long height)
  {
    this.height = height;
  }

  public void setWidth (final long width)
  {
    this.width = width;
  }

  public Object clone ()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError("Clone must always be supported.");
    }
  }
}
