/**
 * Date: Mar 9, 2003
 * Time: 2:20:56 PM
 *
 * $Id: SetTransformOperation.java,v 1.1 2003/03/09 20:38:15 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class SetTransformOperation implements G2Operation
{
  private AffineTransform transform;

  public SetTransformOperation(final AffineTransform transform)
  {
    this.transform = transform;
  }

  public void draw(final Graphics2D g2)
  {
    g2.setTransform(transform);
  }
}
