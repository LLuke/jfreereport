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
 * ---------------------
 * ContentContainer.java
 * ---------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ContentContainer.java,v 1.7 2005/01/24 23:57:53 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package org.jfree.report.content;

import org.jfree.report.util.geom.StrictBounds;

/**
 * A report content item that contains other report content items.
 *
 * @author Thomas Morgner
 */
public class ContentContainer implements MultipartContent
{
  /** Storage for the content items. */
  private Content[] content;
  private int size;

  /** The content bounds. */
  private final StrictBounds bounds;

  /**
   * Creates a new content container.
   *
   * @param bounds  the content bounds.
   */
  protected ContentContainer(final StrictBounds bounds)
  {
    this.bounds = (StrictBounds) bounds.clone();
  }

  /**
   * Returns the content type, in this case
   * {@link org.jfree.report.content.ContentType#CONTAINER}.
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.CONTAINER;
  }

  /**
   * Returns the bounds for the content.
   *
   * @return the bounds.
   */
  public StrictBounds getBounds()
  {
    return (StrictBounds) bounds.clone();
  }

  /**
   * Sets the bounds of the content.
   *
   * @param x  the x-coordinate.
   * @param y  the y-coordinate.
   * @param width  the width.
   * @param height  the height.
   */
  protected void setBounds(final long x, final long y, final long width, final long height)
  {
    this.bounds.setRect(x, y, width, height);
  }

  /**
   * Adds content to the container.
   *
   * @param cp  the content to add.
   */
  protected void addContentPart(final Content cp)
  {
    if (size == 0)
    {
      content = new Content[10];
      content[0] = cp;
      size = 1;
    }
    else
    {
      // check, whether the new element will fit in ..
      if ((size + 1) >= content.length)
      {
        final Content[] newContent = new Content[size + 10];
        System.arraycopy(content, 0, newContent, 0, size);
        content = newContent;
      }
      content[size] = cp;
      size += 1;
    }
  }

  /**
   * Returns the number of content items in the container.
   *
   * @return the item count.
   */
  public int getContentPartCount()
  {
    return size;
  }

  /**
   * Returns a content item from the container.
   *
   * @param part  the content index (zero-based).
   *
   * @return the content.
   */
  public Content getContentPart(final int part)
  {
    return content[part];
  }

  /**
   * Returns the content items from the container that intersect with the specified area.
   *
   * @param bounds  the area.
   *
   * @return a container holding the content items.
   */
  public Content getContentForBounds(final StrictBounds bounds)
  {
    ContentContainer cc = null;
    for (int i = 0; i < getContentPartCount(); i++)
    {
      final Content contentPart = getContentPart(i);
      if (StrictBounds.intersects(contentPart.getBounds(), bounds) == false)
      {
        continue;
      }

      final Content retval = contentPart.getContentForBounds(bounds);
      if (retval instanceof EmptyContent)
      {
        continue;
      }

      final StrictBounds cbounds = retval.getBounds();
      if (cbounds.getHeight() != 0 && cbounds.getWidth() != 0)
      {
        if (cc == null)
        {
          cc = new ContentContainer(bounds);
        }
        cc.addContentPart(retval);
      }
    }
    if (cc == null)
    {
      return new EmptyContent();
    }
    return cc;
  }

  /**
   * Returns the minimum content size for the container.
   *
   * @return the minimum size or null, if this container has no content.
   */
  public StrictBounds getMinimumContentSize()
  {
    StrictBounds retval = null;
    for (int i = 0; i < getContentPartCount(); i++)
    {
      final Content contentPart = getContentPart(i);
      final StrictBounds minCBounds = contentPart.getMinimumContentSize();

      if (minCBounds == null)
      {
        continue;
      }
      if (retval != null)
      {
        retval.add(minCBounds);
      }
      else
      {
        retval = minCBounds;
      }
    }
    return retval;
  }

  /**
   * Returns a string describing this object.
   *
   * @return The string.
   */
  public String toString()
  {
    final StringBuffer container = new StringBuffer();
    container.append(getClass().getName());
    container.append("={\n");
    for (int i = 0; i < getContentPartCount(); i++)
    {
      container.append(getContentPart(i));
      container.append("\n");
    }
    container.append("}\n");
    return container.toString();
  }
}
