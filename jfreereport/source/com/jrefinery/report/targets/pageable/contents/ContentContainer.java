/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * ---------------------
 * ContentContainer.java
 * ---------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ContentContainer.java,v 1.3 2002/12/07 20:53:13 taqua Exp $
 *
 * Changes
 * -------
 * 03-Dec-2002 : Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.contents;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * A container that contains generic content for a report.
 * <p>
 * Classes that implement this interface include:  TextParagraph.
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
    content = new ArrayList();
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
   * Returns the number of content items in the container.
   *
   * @return the item count.
   */
  public int getContentPartCount()
  {
    return content.size();
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
   * Returns the content type, in this case ContentType.CONTAINER.
   *
   * @return the content type.
   */
  public ContentType getContentType()
  {
    return ContentType.CONTAINER;
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
    ContentContainer cc = new ContentContainer(bounds);
    for (int i = 0; i < getContentPartCount(); i++)
    {
      Content contentPart = getContentPart (i);
      if (contentPart.getBounds().intersects(bounds))
      {
        Content retval = contentPart.getContentForBounds(bounds);
        if (retval != null)
        {
          Rectangle2D cbounds = retval.getBounds();
          if (cbounds.getHeight() != 0 && cbounds.getWidth() != 0)
          {
            cc.addContentPart (retval);
          }
        }
      }
    }
    return cc;
  }

  /**
   * Returns the minimum content size for the container.
   *
   * @return the minimum size.
   */
  public Rectangle2D getMinimumContentSize()
  {
    Rectangle2D retval = null;
    for (int i = 0; i < getContentPartCount(); i++)
    {
      Content contentPart = getContentPart (i);
      Rectangle2D minCBounds = contentPart.getMinimumContentSize();

      if (minCBounds == null)
        continue;

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
