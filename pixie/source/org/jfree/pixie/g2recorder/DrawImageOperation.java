/**
 * Date: Mar 9, 2003
 * Time: 1:49:47 PM
 *
 * $Id: DrawImageOperation.java,v 1.1 2003/03/09 20:38:10 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;

public class DrawImageOperation implements G2Operation
{
  private Image image;
  private AffineTransform transform;
  private ImageObserver observer;

  public DrawImageOperation(final Image image, final AffineTransform transform, final ImageObserver observer)
  {
    this.image = image;
    this.transform = transform;
    this.observer = observer;
  }

  public void draw(final Graphics2D g2)
  {
    // wait until the image is loaded and completly drawn ...
    while (g2.drawImage(image, transform, observer) == false)
    {
      try
      {
        Thread.sleep(100);
      }
      catch (InterruptedException ie)
      {
        // this thread got a signal to die at once.
        return;
      }
    }
  }
}
