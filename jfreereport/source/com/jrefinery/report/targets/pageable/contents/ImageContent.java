/**
 * Date: Nov 20, 2002
 * Time: 7:41:14 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.contents;

import com.jrefinery.report.ImageReference;

import java.awt.geom.Rectangle2D;

public class ImageContent implements Content
{
  private ImageReference reference;
  private Rectangle2D bounds;

  public ImageContent(ImageReference ref, Rectangle2D bounds)
  {
    this.reference = ref;
    this.bounds = bounds;
  }

  // get all contentParts making up that content or null, if this class
  // has no subcontents
  public Content getContentPart(int part)
  {
    return null;
  }

  public int getContentPartCount()
  {
    return 0;
  }

  public Rectangle2D getBounds()
  {
    return bounds;
  }

  public ContentType getContentType()
  {
    return ContentType.Image;
  }

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
      double x = bounds.getX() - this.bounds.getX();
      double y = bounds.getY() - this.bounds.getY();
      double w = myBounds.getWidth();
      double h = myBounds.getHeight();
      Rectangle2D imageArea = new Rectangle2D.Double(x,y,w,h);
      ref.setBoundsScaled(imageArea.createIntersection(ref.getBoundsScaled()));
      return new ImageContent(ref, myBounds);
    }
    catch (CloneNotSupportedException cne)
    {
      return null;
    }
  }

  public ImageReference getContent ()
  {
    return reference;
  }

  public static void main (String [] args)
  {
    ImageReference ref = new ImageReference(100, 100, new Rectangle2D.Float(0, 0, 100, 100));
    ImageContent ic = new ImageContent(ref, new Rectangle2D.Float(50, 50, 100, 100));
    ImageContent ic2= (ImageContent) ic.getContentForBounds(new Rectangle2D.Float(100, 50, 50, 50));

    System.out.println ("IC2: " + ic2.getBounds());
    System.out.println ("IC2: " + ic2.reference.getBounds());
  }

  public Rectangle2D getMinimumContentSize()
  {
    return getBounds();
  }
}
