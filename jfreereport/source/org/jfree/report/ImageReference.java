/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * -------------------
 * ImageReference.java
 * -------------------
 * (C)opyright 2002, 2003, by Thomas Morgner.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);Stefan Prange
 *
 * $Id: ImageReference.java,v 1.34 2003/06/29 16:59:23 taqua Exp $
 *
 * Changes:
 * --------
 * 25-Apr-2002 : Version 1 (TM);
 * 16-May-2002 : Updated Javadoc comments (DG);
 * 16-May-2002 : Line Delimiters adjusted and imports organized (JS);
 * 14-Jul-2002 : BugFixed: WaitingImageObserver dead-locked (bugfix by Stefan Prange)
 * 15-Jul-2002 : Fixed a bug in the constructor (DG);
 * 06-Dec-2002 : Updated Javadocs (DG);
 * 04-Feb-2002 : Implemented hashcode method.
 */

package org.jfree.report;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import org.jfree.pixie.wmf.WmfFile;
import org.jfree.report.util.WaitingImageObserver;

/**
 * An image reference encapsulates the source of an image together with a
 * <code>java.awt.Image</code>. The source is used to create a higher resolution
 * version if needed. The source file/URL may also be inlined into the output
 * target, to create better results.
 * <p>
 * An ImageReference is always used in conjunction with an ImageElement.
 *
 * @author Thomas Morgner
 */
public class ImageReference implements Serializable, Cloneable
{

  /** The image. */
  private Image image;

  /** The image URL. */
  private URL url;

  /** The area of the unscaled image that hould be displayed. */
  private Rectangle2D bounds = new Rectangle2D.Float();

  /** The width scale factor. */
  private float scaleX = 1.0f;

  /** The height scale factor. */
  private float scaleY = 1.0f;

  /** The width of the (unscaled) image. */
  private int width;

  /** The height of the (unscaled) image. */
  private int height;

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired
   * width. The image data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating reports.
   *
   * @throws IOException if the url could not be read.
   */
  public ImageReference(final URL url) throws IOException
  {
    InputStream is = null;
    setSourceURL(url);

    try
    {
      is = url.openStream();
      final int c1 = is.read();
      final int c2 = is.read();
      is.close();
      is = null;

      if (c1 == 0xD7 && c2 == 0xCD)
      {
        final WmfFile wmfFile = new WmfFile(url);
        image = wmfFile.replay();
      }
      else
      {
        this.url = url;
        image = Toolkit.getDefaultToolkit().createImage(url);
        if (image == null)
        {
          throw new IOException("Image could not be instantiated.");
        }
        final WaitingImageObserver obs = new WaitingImageObserver(image);
        obs.waitImageLoaded();
      }
    }
    finally
    {
      if (is != null)
      {
        is.close();
      }
    }

    setBounds(new Rectangle2D.Float(0, 0, image.getWidth(null), image.getHeight(null)));
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  /**
   * Creates a new ImageReference without an assigned URL for the Image.
   *
   * @param img the image for this reference
   * @throws NullPointerException if the image is null
   */
  public ImageReference(final Image img)
  {
    if (img == null)
    {
      throw new NullPointerException();
    }
    this.image = img;
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();
    setBounds(new Rectangle2D.Float(0, 0, image.getWidth(null), image.getHeight(null)));
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  /**
   * Creates a new image reference.
   *
   * @param w  the width of the unscaled image.
   * @param h  the height of the unscaled image.
   * @param bounds  the area of the unscaled image to draw.
   */
  public ImageReference(final int w, final int h, final Rectangle2D bounds)
  {
    this.width = w;
    this.height = h;
    this.bounds = bounds;
  }

  /**
   * Return the bounds of the image contained in that reference. These bounds are
   * the natural bounds of the raw image, no scaling applied. These bounds may define
   * a sub-area of the real image contained in that Reference.
   *
   * @return the bounds.
   */
  public Rectangle2D getBounds()
  {
    return bounds.getBounds2D();
  }

  /**
   * Define the bounds of the image contained in that reference.
   *
   * @param bounds  the bounds.
   */
  public void setBounds(final Rectangle2D bounds)
  {
    this.bounds.setRect(bounds);
  }

  /**
   * Returns the x scale factor.
   *
   * @return the x scale factor.
   */
  public float getScaleX()
  {
    return scaleX;
  }

  /**
   * Sets the x scale factor.
   *
   * @param scaleX  the new factor.
   */
  public void setScaleX(final float scaleX)
  {
    if (scaleX == 0)
    {
      throw new IllegalArgumentException("Scale factor must not be 0");
    }
    this.scaleX = scaleX;
  }

  /**
   * Returns the y scale factor.
   *
   * @return the y scale factor.
   */
  public float getScaleY()
  {
    return scaleY;
  }

  /**
   * Sets the y scale factor.
   *
   * @param scaleY  the y scale factor.
   */
  public void setScaleY(final float scaleY)
  {
    if (scaleY == 0)
    {
      throw new IllegalArgumentException("Scale factor must not be 0");
    }
    this.scaleY = scaleY;
  }

  /**
   * Sets the scaled bounds.
   *
   * @param bounds  the bounds.
   */
  public void setBoundsScaled(final Rectangle2D bounds)
  {
    final Rectangle2D boundsNew = getBounds();
    boundsNew.setRect(bounds.getX() / getScaleX(),
        bounds.getY() / getScaleY(),
        bounds.getWidth() / getScaleX(),
        bounds.getHeight() / getScaleY());
    setBounds(boundsNew);
  }

  /**
   * Returns the scaled bounds.
   *
   * @return the scaled bounds.
   */
  public Rectangle2D getBoundsScaled()
  {
    final Rectangle2D bounds = getBounds();
    bounds.setRect(bounds.getX() * getScaleX(),
        bounds.getY() * getScaleY(),
        bounds.getWidth() * getScaleX(),
        bounds.getHeight() * getScaleY());
    return bounds;
  }

  /**
   * Returns the current image instance.
   * <P>
   * The image is not scaled in any way.
   *
   * @return The current image instance.
   */
  public Image getImage()
  {
    return image;
  }

  /**
   * Returns the source URL for the image.
   *
   * @return The URL.
   */
  public URL getSourceURL()
  {
    return url;
  }

  /**
   * Sets the source URL for the image.
   *
   * @param surl The URL.
   */
  protected void setSourceURL(final URL surl)
  {
    if (surl == null)
    {
      throw new NullPointerException();
    }
    this.url = surl;
  }

  /**
   * Returns a String representing this object.  Useful for debugging.
   *
   * @return The string.
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();

    buf.append("ImageReference={ URL=");
    buf.append(getSourceURL());
    buf.append(", image=");
    buf.append(getImage());
    buf.append("}");

    return buf.toString();
  }

  /**
   * Checks for equality.
   *
   * @param o the object to test.
   *
   * @return true if the specified object is equal to this one.
   */
  public boolean equals(final Object o)
  {
    if (o == null)
    {
      return false;
    }
    if (o instanceof ImageReference == false)
    {
      return false;
    }
    final ImageReference ref = (ImageReference) o;
    if (ref.url == null && url == null)
    {
      return true;
    }
    if (ref.url == null)
    {
      return false;
    }
    if (url == null)
    {
      return false;
    }
    return ref.url.equals(url);
  }

  /**
   * compute a hashcode for this imageReference.
   *
   * @return the hashcode
   */
  public int hashCode()
  {
    int result;
    result = (image != null ? image.hashCode() : 0);
    result = 29 * result + (url != null ? url.hashCode() : 0);
    return result;
  }

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException this should never be thrown.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ImageReference ref = (ImageReference) super.clone();
    ref.bounds = bounds.getBounds2D();
    return ref;
  }

  /**
   * Returns the (unscaled) image width.
   *
   * @return the image width.
   */
  public int getImageWidth()
  {
    return width;
  }

  /**
   * Returns the (unscaled) image height.
   *
   * @return the image height.
   */
  public int getImageHeight()
  {
    return height;
  }
}
