/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * -----------------
 * ImageContent.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageContent.java,v 1.4 2003/02/25 18:46:55 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 * 07-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable.content.
 *
 */
package com.jrefinery.report.targets.base.content;

import com.jrefinery.report.ImageReference;

import java.awt.geom.Rectangle2D;

/**
 * Image content.
 *
 * @author Thomas Morgner.
 */
public class ImageContent implements Content
{
  /** The image reference. */
  private ImageReference reference;

  /** The bounds. */
  private Rectangle2D bounds;

  /**
   * Creates a new image content.
   *
   * @param ref  the image reference.
   * @param bounds  the content bounds.
   */
  public ImageContent(ImageReference ref, Rectangle2D bounds)
  {
    this.reference = ref;
    this.bounds = bounds;
  }

  /**
   * Returns the content type, in this case 
   * {@link com.jrefinery.report.targets.base.content.ContentType#IMAGE}.
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.IMAGE;
  }

  /**
   * This class does not store sub-content items, so this method always returns zero.
   *
   * @return always zero, image content does never contains multiple parts.
   */
  public int getContentPartCount()
  {
    return 0;
  }

  /**
   * This class does not store sub-content items, so this method always returns <code>null</code>.
   *
   * @param part  ignored.
   *
   * @return <code>null</code>.
   */
  public Content getContentPart(int part)
  {
    return null;
  }

  /**
   * Returns the content bounds.
   *
   * @return the content bounds.
   */
  public Rectangle2D getBounds()
  {
    return bounds;
  }

  /**
   * Returns content that falls within the specified bounds.
   *
   * @param bounds  the bounds.
   *
   * @return the content.
   */
  public Content getContentForBounds(Rectangle2D bounds)
  {
    if (bounds.intersects(getBounds()) == false)
    {
      return null;
    }

    Rectangle2D myBounds = bounds.createIntersection(getBounds());
    try
    {
      ImageReference ref = (ImageReference) reference.clone();
      float x = (float) (bounds.getX() - this.bounds.getX());
      float y = (float) (bounds.getY() - this.bounds.getY());
      float w = (float) myBounds.getWidth();
      float h = (float) myBounds.getHeight();
      Rectangle2D imageArea = new Rectangle2D.Float(x, y, w, h);
      ref.setBoundsScaled(imageArea.createIntersection(ref.getBoundsScaled()));
      return new ImageContent(ref, myBounds);
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }

  /**
   * Returns the image contents.
   *
   * @return the image.
   */
  public ImageReference getContent ()
  {
    return reference;
  }

  /**
   * Returns the minimum content size.
   *
   * @return the minimum content size.
   */
  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }

}
