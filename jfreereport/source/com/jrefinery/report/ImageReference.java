/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2002, by Thomas Morgner.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);Stefan Prange
 *
 * $Id: ImageReference.java,v 1.19 2002/10/17 16:14:00 taqua Exp $
 *
 * Changes:
 * --------
 * 25-Apr-2002 : Version 1 (TM);
 * 16-May-2002 : Updated Javadoc comments (DG);
 * 16-May-2002 : Line Delimiters adjusted and imports organized (JS);
 * 14-Jul-2002 : BugFixed: WaitingImageObserver dead-locked (bugfix by Stefan Prange)
 * 15-Jul-2002 : Fixed a bug in the constructor (DG);
 */

package com.jrefinery.report;

import com.jrefinery.report.util.WaitingImageObserver;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * An image reference encapsulates the source of an image together with a
 * <code>java.awt.Image</code>. The source is used to create a higher resolution
 * version if needed. The source file/URL may also be inlined into the output
 * target, to create better results.
 * <p>
 * An ImageReference is always used in conjunction with an ImageElement.
 *
 * @author TM
 */

public class ImageReference implements Serializable, Cloneable
{

  /** The image. */
  private Image image;

  /** The image URL. */
  private URL url;

  private Rectangle2D bounds = new Rectangle2D.Double(); // the area of the unscaled image that should be displayed
  private float scaleX = 1.0f; // the scale factor for the width
  private float scaleY = 1.0f; // the scale factor for the height

  private int width;  // width of the unscaled image
  private int height; // height of the unscaled image

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired
   * width. The image data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating reports.
   *
   * @throws IOException if the url could not be read.
   */
  public ImageReference(URL url) throws IOException
  {
    InputStream is = null;
    setSourceURL(url);

    try
    {
      is = url.openStream();
      int c1 = is.read();
      int c2 = is.read();
      is.close();
      is = null;

      if (c1 == 0xD7 && c2 == 0xCD)
      {
        WmfFile wmfFile = new WmfFile(url);
        image = wmfFile.replay();
        //image = Toolkit.getDefaultToolkit ().createImage (new WmfImageProducer (url));
      }
      else
      {
        this.url = url;
        image = Toolkit.getDefaultToolkit().createImage(url);
        if (image == null)
          throw new IOException("Image could not be instantiated.");

        WaitingImageObserver obs = new WaitingImageObserver(image);
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

    setBounds (new Rectangle2D.Double(0, 0, image.getWidth(null), image.getHeight(null)));
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  /**
   * Creates a new ImageReference without an assigned URL for the Image.
   *
   * @param img the image for this reference
   * @throws NullPointerException if the image is null
   */
  public ImageReference(Image img)
  {
    if (img == null)
    {
      throw new NullPointerException();
    }
    WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();
    this.image = img;
    setBounds (new Rectangle2D.Double(0, 0, image.getWidth(null), image.getHeight(null)));
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  public ImageReference (int w, int h, Rectangle2D bounds)
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
   * @return
   */
  public Rectangle2D getBounds()
  {
    return bounds.getBounds2D();
  }

  /**
   * Define the bounds of the image contained in that reference
   * @param bounds
   */
  public void setBounds (Rectangle2D bounds)
  {
    this.bounds.setRect(bounds);
  }

  public float getScaleX()
  {
    return scaleX;
  }

  public void setScaleX(float scaleX)
  {
    if (scaleX == 0) throw new IllegalArgumentException("Scale factor must not be 0");
    this.scaleX = scaleX;
  }

  public float getScaleY()
  {
    return scaleY;
  }

  public void setScaleY(float scaleY)
  {
    if (scaleY == 0) throw new IllegalArgumentException("Scale factor must not be 0");
    this.scaleY = scaleY;
  }

  public void setBoundsScaled (Rectangle2D bounds)
  {
    Rectangle2D boundsNew = getBounds();
    boundsNew.setRect(bounds.getX() / getScaleX(),
                   bounds.getY() / getScaleY(),
                   bounds.getWidth() / getScaleX(),
                   bounds.getHeight() / getScaleY());
    setBounds(boundsNew);
  }

  public Rectangle2D getBoundsScaled ()
  {
    Rectangle2D bounds = getBounds();
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
  protected void setSourceURL(URL surl)
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
    StringBuffer buf = new StringBuffer();

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
  public boolean equals(Object o)
  {
    if (o == null)
    {
      return false;
    }
    if (o instanceof ImageReference == false)
    {
      return false;
    }
    ImageReference ref = (ImageReference) o;
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
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException this should never be thrown.
   */
  public Object clone() throws CloneNotSupportedException
  {
    ImageReference ref = (ImageReference) super.clone();
    ref.bounds = bounds.getBounds2D();
    return ref;
  }

  public int getImageWidth ()
  {
    return width;
  }

  public int getImageHeight ()
  {
    return height;
  }
}
