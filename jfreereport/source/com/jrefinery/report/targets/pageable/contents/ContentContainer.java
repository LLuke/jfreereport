/**
 * Date: Nov 20, 2002
 * Time: 4:33:15 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.contents;

import com.jrefinery.report.util.Log;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ContentContainer implements Content
{
  private ArrayList content;
  private Rectangle2D bounds;

  public ContentContainer (Rectangle2D bounds)
  {
    this.bounds = new Rectangle2D.Float();
    this.bounds.setRect(bounds);
    content = new ArrayList();
  }

  public void addContentPart (Content cp)
  {
    content.add (cp);
  }

  // get all contentParts making up that content or null, if this class
  // has no subcontents
  public Content getContentPart(int part)
  {
    return (Content) content.get(part);
  }

  public int getContentPartCount()
  {
    return content.size();
  }

  public Rectangle2D getBounds()
  {
    return bounds.getBounds();
  }

  protected void setBounds (Rectangle2D bounds)
  {
    this.bounds.setRect(bounds);
  }

  public ContentType getContentType()
  {
    return ContentType.Container;
  }

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
          cc.addContentPart (retval);
        }
      }
    }
    return cc;
  }

  public Rectangle2D getMinimumContentSize()
  {
    Rectangle2D retval = null;
    for (int i = 0; i < getContentPartCount(); i++)
    {
      Content contentPart = getContentPart (i);
      if (retval == null)
      {
        retval = contentPart.getMinimumContentSize();
      }
      else
      {
        Rectangle2D rect = contentPart.getMinimumContentSize();
        if (rect != null)
        {
          retval.add(rect);
        }
      }
    }
    return retval;
  }
}
