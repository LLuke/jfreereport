/**
 * Date: Mar 9, 2003
 * Time: 2:20:56 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class SetTransformOperation implements G2Operation
{
  private AffineTransform transform;

  public SetTransformOperation(AffineTransform transform)
  {
    this.transform = transform;
  }

  public void draw(Graphics2D g2)
  {
    g2.setTransform(transform);
  }
}
