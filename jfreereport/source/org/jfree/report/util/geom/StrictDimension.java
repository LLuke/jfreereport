package org.jfree.report.util.geom;

import java.io.Serializable;

public class StrictDimension implements Serializable, Cloneable
{
  private long width;
  private long height;
  private boolean locked;

  public StrictDimension ()
  {
  }

  public StrictDimension (final long width, final long height)
  {
    this.width = width;
    this.height = height;
  }

  public boolean isLocked ()
  {
    return locked;
  }

  public StrictDimension getLockedInstance ()
  {
    final StrictDimension retval = (StrictDimension) clone();
    retval.locked = true;
    return retval;
  }

  public StrictDimension getUnlockedInstance ()
  {
    final StrictDimension retval = (StrictDimension) clone();
    retval.locked = false;
    return retval;
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
    if (locked)
    {
      throw new IllegalStateException();
    }

    this.width = width;
    this.height = height;
  }

  public void setHeight (final long height)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    this.height = height;
  }

  public void setWidth (final long width)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    this.width = width;
  }

  public Object clone ()
  {
    try
    {
      final StrictDimension sdim = (StrictDimension) super.clone();
      return sdim;
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError("Clone must always be supported.");
    }
  }


  public String toString ()
  {
    return "org.jfree.report.util.geom.StrictDimension{" +
            "width=" + width +
            ", height=" + height +
            "}";
  }
}
