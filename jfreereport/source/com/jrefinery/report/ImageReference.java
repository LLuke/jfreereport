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
 * $Id: ImageReference.java,v 1.18 2002/09/13 15:38:04 mungady Exp $
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

import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Image;
import java.awt.Toolkit;
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

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired
   * width. The image data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating reports.
   *
   * @throws IOException if the url could not be read.
   */
  public ImageReference (URL url) throws IOException
  {
    InputStream is = null;
    setSourceURL (url);

    try
    {
      is = url.openStream ();
      int c1 = is.read ();
      int c2 = is.read ();
      is.close ();
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
        image = Toolkit.getDefaultToolkit ().createImage (url);
        if (image == null)
          throw new IOException("Image could not be instantiated.");

        WaitingImageObserver obs = new WaitingImageObserver (image);
        obs.waitImageLoaded ();
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
   * Creates a new ImageReference without an assigned URL for the Image.
   *
   * @param img the image for this reference
   * @throws NullPointerException if the image is null
   */
  public ImageReference (Image img)
  {
    if (img == null)
    {
      throw new NullPointerException ();
    }
    this.image = img;
  }

  /**
   * Returns the current image instance.
   * <P>
   * The image is not scaled in any way.
   *
   * @return The current image instance.
   */
  public Image getImage ()
  {

    return image;
  }

  /**
   * Returns the source URL for the image.
   *
   * @return The URL.
   */
  public URL getSourceURL ()
  {

    return url;
  }

  /**
   * Sets the source URL for the image.
   *
   * @param surl The URL.
   */
  protected void setSourceURL (URL surl)
  {

    if (surl == null)
    {
      throw new NullPointerException ();
    }
    this.url = surl;
  }

  /**
   * Returns a String representing this object.  Useful for debugging.
   *
   * @return The string.
   */
  public String toString ()
  {

    StringBuffer buf = new StringBuffer ();

    buf.append ("ImageReference={ URL=");
    buf.append (getSourceURL ());
    buf.append (", image=");
    buf.append (getImage());
    buf.append ("}");

    return buf.toString ();
  }

  /**
   * Checks for equality.
   *
   * @param o the object to test.
   *
   * @return true if the specified object is equal to this one.
   */
  public boolean equals (Object o)
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
    return ref.url.equals (url);
  }

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException this should never be thrown.
   */
  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }
}
