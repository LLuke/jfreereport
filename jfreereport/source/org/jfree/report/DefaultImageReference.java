/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * Contributor(s):   David Gilbert (for Object Refinery Limited);Stefan Prange
 *
 * $Id: DefaultImageReference.java,v 1.6 2005/02/23 21:04:29 taqua Exp $
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
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.util.WaitingImageObserver;

/**
 * An image reference encapsulates the source of an image together with a
 * <code>java.awt.Image</code>. The source is used to create a higher resolution version
 * if needed. The source file/URL may also be inlined into the output target, to create
 * better results.
 * <p/>
 * An ImageReference is always used in conjunction with an ImageElement.
 *
 * @author Thomas Morgner
 */
public class DefaultImageReference
        implements Serializable, URLImageContainer, LocalImageContainer
{

  /**
   * The image.
   */
  private Image image;

  /**
   * The image URL.
   */
  private URL url;

  /**
   * The width of the (unscaled) image.
   */
  private int width;

  /**
   * The height of the (unscaled) image.
   */
  private int height;

  /**
   * The scale factor.
   */
  private float scaleX = 1.0f;

  /**
   * The scale factor.
   */
  private float scaleY = 1.0f;

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired width. The image
   * data is read from the given URL.
   *
   * @param url the source url. The url must be readable while generating reports.
   * @throws IOException if the url could not be read.
   */
  public DefaultImageReference (final URL url)
          throws IOException
  {
    if (url == null)
    {
      throw new NullPointerException("URL must not be null.");
    }
    this.url = url;
    this.image = ImageFactory.getInstance().createImage(url);
    final WaitingImageObserver wob = new WaitingImageObserver(image);
    wob.waitImageLoaded();
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  /**
   * Creates a new ImageReference without an assigned URL for the Image.
   *
   * @param img the image for this reference
   * @throws NullPointerException if the image is null
   */
  public DefaultImageReference (final Image img)
  {
    if (img == null)
    {
      throw new NullPointerException();
    }
    this.image = img;
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  /**
   * Creates a new image reference.
   *
   * @param w the width of the unscaled image.
   * @param h the height of the unscaled image.
   */
  public DefaultImageReference (final int w, final int h)
  {
    this.width = w;
    this.height = h;
  }

  public DefaultImageReference (final DefaultImageReference parent)
  {
    this.width = parent.width;
    this.height = parent.height;
    this.image = parent.image;
    this.url = parent.url;
  }

  /**
   * Returns the current image instance. <P> The image is not scaled in any way.
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
   * Returns a String representing this object.  Useful for debugging.
   *
   * @return The string.
   */
  public String toString ()
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
   * @return true if the specified object is equal to this one.
   */
  public boolean equals (final Object o)
  {
    if (o == null)
    {
      return false;
    }
    if (o instanceof DefaultImageReference == false)
    {
      return false;
    }
    final DefaultImageReference ref = (DefaultImageReference) o;
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
  public int hashCode ()
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
  public Object clone ()
          throws CloneNotSupportedException
  {
    final DefaultImageReference ref = (DefaultImageReference) super.clone();
    return ref;
  }

  /**
   * Returns the (unscaled) image width.
   *
   * @return the image width.
   */
  public int getImageWidth ()
  {
    return width;
  }

  /**
   * Returns the (unscaled) image height.
   *
   * @return the image height.
   */
  public int getImageHeight ()
  {
    return height;
  }

  /**
   * Checks, whether this image reference is loadable. A default image reference is
   * loadable, if a valid URL has been set.
   *
   * @return true, if it is loadable, false otherwise.
   */
  public boolean isLoadable ()
  {
    return getSourceURL() != null;
  }

  /**
   * Returns the identity information. This instance returns the URL of the image, if
   * any.
   *
   * @return the image identifier.
   */
  public Object getIdentity ()
  {
    return url;
  }

  /**
   * Returns the name of this image reference. If an URL has been set, this will return
   * the URL of the image, else null is returned.
   *
   * @return the name.
   */
  public String getName ()
  {
    if (url != null)
    {
      return url.toExternalForm();
    }
    return null;
  }

  /**
   * Checks, whether this image has a assigned identity. Two identities should be equal,
   * if the image contents are equal.
   *
   * @return true, if that image contains contains identity information, false otherwise.
   */
  public boolean isIdentifiable ()
  {
    return url != null;
  }

  /**
   * Returns a predefined scaling factor. That scaling will be applied before any layout
   * specific scaling is done.
   *
   * @return the scale factor.
   */
  public float getScaleX ()
  {
    return scaleX;
  }

  /**
   * Returns a predefined scaling factor. That scaling will be applied before any layout
   * specific scaling is done.
   *
   * @return the scale factor.
   */
  public float getScaleY ()
  {
    return scaleY;
  }

  /**
   * Defines a predefined scaling factor. That scaling will be applied before any layout
   * specific scaling is done.
   *
   * @param sx the scale factor.
   * @param sy the scale factor.
   */
  public void setScale (final float sx, final float sy)
  {
    this.scaleX = sx;
    this.scaleY = sy;
  }
}
