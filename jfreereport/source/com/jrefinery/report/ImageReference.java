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
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageReference.java,v 1.4 2002/05/16 10:49:33 mungady Exp $
 *
 * Changes:
 * --------
 * 25-Apr-2002 : Version 1 (TM);
 * 16-May-2002 : Updated Javadoc comments (DG);
 * 16-May-2002 : Line Delimiters adjusted and imports organized (JS);
 *
 */

package com.jrefinery.report;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import gnu.bhresearch.pixie.wmf.WmfImageProducer;

/**
 * An image reference encapsulates the source of an image together with an
 * <code>java.awt.Image</code>. The source is used to create a higher resolution
 * version if needed. The source file/URL may also be inlined into the output
 * target, to create better results.
 * <p>
 * An ImageReference is always used in conjunction with an ImageElement.
 */

public class ImageReference
{

  /** The image. */
  private Image image;

  /** The image URL. */
  private URL url;

  /** The image bounds. */
  private Rectangle2D bounds;

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired
   * width. The image data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating reports.
   * @param width the desired width of the image in report-units (1/72 inch).
   * @param height the desired height of the image in report-units (1/72 inch).
   *
   * @throws IOException if the url could not be read.
   */

  public ImageReference(URL url, float width, float height) throws IOException
  {

    this(url, new Rectangle2D.Float(0, 0, width, height));
  }

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired
   * width. The image data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating
   * reports.
   * @param bounds The image size.
   *
   * @throws IOException if the url could not be read.
   */

  public ImageReference(URL url, Rectangle2D bounds) throws IOException
  {

    if (bounds.getWidth() < 1 || bounds.getHeight() < 1)
      throw new IllegalArgumentException("Width and height have to be > 0");

    InputStream is = null;
    setBounds(bounds);
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
        image = Toolkit.getDefaultToolkit().createImage(new WmfImageProducer(url));
      }
      else
      {
        this.url = url;
        image = Toolkit.getDefaultToolkit().getImage(url);
        WaitingImageObserver obs = new WaitingImageObserver(image);

        obs.run();
      }
    }
    finally
    {
      if (is != null)
      {
        is.close();
      }
    }
  }

  /**
   * Returns the current image instance.
   * <P>
   * The image is not scaled in any way.
   *
   * @returns The current image instance.
   */

  public Image getImage()
  {

    return image;
  }

  /**
   * Returns the source URL for the image.
   *
   * @returns The URL.
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
      throw new NullPointerException();

    this.url = surl;
  }

  /**
   * Sets the required size for the image.
   *
   * @param bounds The size.
   */

  protected void setBounds(Rectangle2D bounds)
  {

    if (bounds == null)
      throw new NullPointerException();

    this.bounds = bounds;
  }

  /**
   * Returns the required width of the image (in points, 1/72 inch).
   *
   * @returns The width.
   */

  public float getWidth()
  {

    return (float) bounds.getWidth();
  }

  /**
   * Returns the required height of the image (in points, 1/72 inch).
   *
   * @returns the desired height of the image.
   */

  public float getHeight()
  {

    return (float) bounds.getHeight();
  }

  /**
   * Returns the required left origin of the image.
   *
   * @returns The left origin.
   */

  public float getX()
  {

    return (float) bounds.getX();
  }

  /**
   * Returns the upper origin of the image.
   *
   * @returns The upper origin.
   */

  public float getY()
  {

    return (float) bounds.getY();
  }

  /**
   * Returns the required size of the image.
   *
   * @param carrier You can supply a rectangle in which to store the result, if you want to avoid
   *                creating new objects unnecessarily.
   *
   * @returns The size.
   */

  public Rectangle2D getBounds(Rectangle2D carrier)
  {

    if (carrier == null)
      carrier = new Rectangle2D.Float();

    carrier.setRect(bounds);

    return carrier;
  }

  /**
   * Returns the required size of the image.
   * <P>
   * See also the getBounds(Rectangle2D) method, if you prefer not to create a new Rectangle2D for
   * each call to this method.
   *
   * @return The size.
   */

  public Rectangle2D getBounds()
  {

    return getBounds(null);
  }

  /**
   * Returns a String representing this object.  Useful for debugging.
   *
   * @returns The string.
   */

  public String toString()
  {

    StringBuffer buf = new StringBuffer();

    buf.append("ImageReference={ URL=");
    buf.append(getSourceURL());
    buf.append(" bounds=");
    buf.append(getBounds());
    buf.append("}");

    return buf.toString();

  }
}