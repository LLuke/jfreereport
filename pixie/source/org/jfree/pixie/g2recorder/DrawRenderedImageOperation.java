/**
 * Date: Mar 9, 2003
 * Time: 1:59:28 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.image.RenderedImage;
import java.awt.geom.AffineTransform;

public class DrawRenderedImageOperation implements G2Operation
{
  private RenderedImage image;
  private AffineTransform transform;

  public DrawRenderedImageOperation(RenderedImage image, AffineTransform transform)
  {
    this.image = image;
    this.transform = transform;
  }

  public void draw(Graphics2D g2)
  {
    g2.drawRenderedImage(image, transform);
  }
}
