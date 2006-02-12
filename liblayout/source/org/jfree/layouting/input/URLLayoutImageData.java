/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * URLLayoutImageData.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: URLLayoutImageData.java,v 1.1 2006/02/12 21:54:26 taqua Exp $
 *
 * Changes
 * -------------------------
 * 14.12.2005 : Initial version
 */
package org.jfree.layouting.input;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import org.jfree.util.WaitingImageObserver;

/**
 * Creation-Date: 14.12.2005, 22:13:30
 *
 * @author Thomas Morgner
 */
public class URLLayoutImageData implements ExternalLayoutImageData
{
  private URL url;
  private String uri;
  private transient Image image;
  private WaitingImageObserver observer;

  public URLLayoutImageData(final URL url)
  {
    this(url, null);
  }

  public URLLayoutImageData(final URL url, final String uri)
  {
    if (url == null)
    {
      throw new NullPointerException();
    }

    this.url = url;
    if (uri == null)
    {
      this.uri = url.toExternalForm();
    }

    image = Toolkit.getDefaultToolkit().createImage(url);
    observer = new WaitingImageObserver(image);
    // indirectly trigger the loading of the image.
    // gives a few percentages on multiprocessor systems.
    image.getHeight(observer);
  }

  public Image getImage()
  {
    return image;
  }

  public URL getSource()
  {
    return url;
  }

  public String getUri()
  {
    return uri;
  }

  public int getWidth()
  {
    if (observer.isLoadingComplete() == false)
    {
      observer.waitImageLoaded();
    }
    return image.getWidth(null);
  }

  public int getHeight()
  {
    if (observer.isLoadingComplete() == false)
    {
      observer.waitImageLoaded();
    }
    return image.getHeight(null);
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio
   * aware, these bounds should be used to compute the preferred aspect ratio
   * for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension(getWidth(), getHeight());
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the
   * drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(Graphics2D g2, Rectangle2D area)
  {
    if (observer.isLoadingComplete() == false)
    {
      observer.waitImageLoaded();
    }
    g2.drawImage(image, 0, 0, null);
  }
}
