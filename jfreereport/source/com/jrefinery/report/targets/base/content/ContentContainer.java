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
 * ---------------------
 * ContentContainer.java
 * ---------------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ContentContainer.java,v 1.4 2003/03/18 22:35:21 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.base.content;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * A report content item that contains other report content items.
 *
 * @author Thomas Morgner
 */
public class ContentContainer implements Content
{
  /** Storage for the content items. */
  private ArrayList content;

  /** The content bounds. */
  private Rectangle2D bounds;

  /**
   * Creates a new content container.
   *
   * @param bounds  the content bounds.
   */
  public ContentContainer (Rectangle2D bounds)
  {
    this.bounds = new Rectangle2D.Float();
    this.bounds.setRect(bounds);
    content = new ArrayList(5);
  }

  /**
   * Returns the content type, in this case 
   * {@link com.jrefinery.report.targets.base.content.ContentType#CONTAINER}.
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
  public Rectangle2D getBounds()
  {
    return bounds.getBounds();
  }

  /**
   * Sets the bounds of the content.
   *
   * @param bounds  the new bounds.
   */
  protected void setBounds (Rectangle2D bounds)
  {
    this.bounds.setRect(bounds);
  }

  /**
   * Adds content to the container.
   *
   * @param cp  the content to add.
   */
  public void addContentPart (Content cp)
  {
    content.add (cp);
  }

  /**
   * Returns the number of content items in the container.
   *
   * @return the item count.
   */
  public int getContentPartCount()
  {
    return content.size();
  }

  /**
   * Returns a content item from the container.
   *
   * @param part  the content index (zero-based).
   *
   * @return the content.
   */
  public Content getContentPart(int part)
  {
    return (Content) content.get(part);
  }

  /**
   * Returns the content items from the container that intersect with the specified area.
   *
   * @param bounds  the area.
   *
   * @return a container holding the content items.
   */
  public Content getContentForBounds(Rectangle2D bounds)
  {
    ContentContainer cc = null;
    for (int i = 0; i < getContentPartCount(); i++)
    {
      Content contentPart = getContentPart (i);
      if (contentPart.getBounds().intersects(bounds) == false)
      {
        continue;
      }

      Content retval = contentPart.getContentForBounds(bounds);
      if (retval == null)
      {
        continue;
      }

      Rectangle2D cbounds = retval.getBounds();
      if (cbounds.getHeight() != 0 && cbounds.getWidth() != 0)
      {
        if (cc == null)
        {
          cc = new ContentContainer(bounds);
        }
        cc.addContentPart (retval);
      }
    }
    return cc;
  }

  /**
   * Returns the minimum content size for the container.
   *
   * @return the minimum size or null, if this container has no content.
   */
  public Rectangle2D getMinimumContentSize()
  {
    Rectangle2D retval = null;
    for (int i = 0; i < getContentPartCount(); i++)
    {
      Content contentPart = getContentPart (i);
      Rectangle2D minCBounds = contentPart.getMinimumContentSize();

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
  public String toString ()
  {
    StringBuffer container = new StringBuffer();
    container.append(getClass().getName());
    container.append("={\n");
    for (int i = 0; i < getContentPartCount(); i++)
    {
      container.append (getContentPart(i));
      container.append("\n");
    }
    container.append("}\n");
    return container.toString();
  }
}
