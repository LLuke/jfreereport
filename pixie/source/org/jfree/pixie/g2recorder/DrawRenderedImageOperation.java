/**
 * Date: Mar 9, 2003
 * Time: 1:59:28 PM
 *
 * $Id: DrawRenderedImageOperation.java,v 1.1 2003/03/09 20:38:10 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.image.RenderedImage;
import java.awt.geom.AffineTransform;

public class DrawRenderedImageOperation implements G2Operation
{
  private RenderedImage image;
  private AffineTransform transform;

  public DrawRenderedImageOperation(final RenderedImage image, final AffineTransform transform)
  {
    this.image = image;
    this.transform = transform;
  }

  public void draw(final Graphics2D g2)
  {
    g2.drawRenderedImage(image, transform);
  }
}
