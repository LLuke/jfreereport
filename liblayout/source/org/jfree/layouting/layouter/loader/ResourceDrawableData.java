package org.jfree.layouting.layouter.loader;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import org.jfree.ui.Drawable;
import org.jfree.ui.ExtendedDrawable;
import org.jfree.layouting.input.ExternalResourceData;

public class ResourceDrawableData
        implements ExtendedDrawable, ExternalResourceData
{
  private Drawable image;
  private ExtendedDrawable extImage;
  private URL url;

  public ResourceDrawableData (final Drawable image, URL url)
  {
    if (image == null)
    {
      throw new NullPointerException();
    }
    if (url == null)
    {
      throw new NullPointerException();
    }
    this.image = image;
    if (image instanceof ExtendedDrawable)
    {
      this.extImage = (ExtendedDrawable) image;
    }
    this.url = url;
  }

  public URL getSource ()
  {
    return url;
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio aware,
   * these bounds should be used to compute the preferred aspect ratio for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize ()
  {
    if (extImage != null)
    {
      return extImage.getPreferredSize();
    }
    return new Dimension(0,0);
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio ()
  {
    if (extImage != null)
    {
      return extImage.isPreserveAspectRatio();
    }
    return false;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw (Graphics2D g2, Rectangle2D area)
  {
    image.draw(g2, area);
  }
}
