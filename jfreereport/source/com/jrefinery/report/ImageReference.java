/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -------------
 * ImageReference.java
 * -------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: ImageReference.java,v 1.2 2002/05/14 21:35:02 taqua Exp $
 *
 * Changes (from 25-May-2002)
 * -------------------------
 * 25-Apr-2002 : Created the class
 *
 */
package com.jrefinery.report;

import gnu.bhresearch.pixie.wmf.WmfImageProducer;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
  private Image image;
  private URL url;
  private Rectangle2D bounds;

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired
   * width. The image data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating
   * reports.
   * @param width the desired width of the image in report-units (1/72 inch).
   * @param height the desired height of the image in report-units (1/72 inch).
   * @throws IOException if the url could not be read.
   */
  public ImageReference (URL url, float width, float height)
          throws IOException
  {
    this (url, new Rectangle2D.Float (0, 0, width, height));
  }

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired
   * width. The image data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating
   * reports.
   * @param width the desired width of the image in report-units (1/72 inch).
   * @param x the desired left border of the image within the image element.
   * @param y the desired upper border of the image within the image element.
   * @param height the desired height of the image in report-units (1/72 inch).
   * @throws IOException if the url could not be read.
   */
  public ImageReference (URL url, Rectangle2D bounds)
          throws IOException
  {
    if (bounds.getWidth () < 1 || bounds.getHeight () < 1)
      throw new IllegalArgumentException ("Width and height have to be > 0");
    InputStream is = null;
    setBounds (bounds);
    setSourceURL(url);
    try
    {
      is = url.openStream ();
      int c1 = is.read ();
      int c2 = is.read ();
      is.close ();

      is = null;
      if (c1 == 0xD7 && c2 == 0xCD)
      {
        image = Toolkit.getDefaultToolkit ().createImage (new WmfImageProducer (url));
      }
      else
      {
        this.url = url;
        image = Toolkit.getDefaultToolkit ().getImage (url);
        WaitingImageObserver obs = new WaitingImageObserver (image);
        obs.run ();
      }
    }
    finally
    {
      if (is != null)
      {
        is.close ();
      }
    }
  }

  /**
   * @returns the current image instance. The image is not scaled in any
   * way.
   */
  public Image getImage ()
  {
    return image;
  }

  /**
   * @returns the source url of the image.
   */
  public URL getSourceURL ()
  {
    return url;
  }

  protected void setSourceURL (URL surl)
  {
    if (surl == null) throw new NullPointerException();
    this.url = surl;
  }

  protected void setBounds (Rectangle2D bounds)
  {
    if (bounds == null) throw new NullPointerException();
    this.bounds = bounds;
  }

  /**
   * @returns the desired width of the image.
   */
  public float getWidth ()
  {
    return (float) bounds.getWidth ();
  }

  /**
   * @returns the desired height of the image.
   */
  public float getHeight ()
  {
    return (float) bounds.getHeight ();
  }

  /**
   * @returns the desired left origin of the image.
   */
  public float getX ()
  {
    return (float) bounds.getX ();
  }

  /**
   * @returns the desired upper origin of the image.
   */
  public float getY ()
  {
    return (float) bounds.getY ();
  }

  /**
   * @returns the bounds of the image as rectangle.
   */
  public Rectangle2D getBounds (Rectangle2D carrier)
  {
    if (carrier == null)
      carrier = new Rectangle2D.Float ();

    carrier.setRect (bounds);
    return carrier;
  }

  public Rectangle2D getBounds ()
  {
    return getBounds (null);
  }

  /**
   * @returns the string representation of this class for debugging purposes.
   */
  public String toString ()
  {
    StringBuffer buf = new StringBuffer ();
    buf.append ("ImageReference={ URL=");
    buf.append (getSourceURL ());
    buf.append (" bounds=");
    buf.append (getBounds ());
    buf.append ("}");
    return buf.toString ();
  }
}
