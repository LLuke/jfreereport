/**
 * Date: Mar 9, 2003
 * Time: 1:59:28 PM
 *
 * $Id: DrawRenderedImageOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;

public class DrawRenderedImageOperation implements G2Operation
{
  private RenderedImage image;
  private AffineTransform transform;

  public DrawRenderedImageOperation (final RenderedImage image,
                                     final AffineTransform transform)
  {
    this.image = image;
    this.transform = transform;
  }

  public void draw (final Graphics2D g2)
  {
    g2.drawRenderedImage(image, transform);
  }
}
