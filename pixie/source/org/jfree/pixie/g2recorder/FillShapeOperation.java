/**
 * Date: Mar 9, 2003
 * Time: 2:06:43 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Shape;

public class FillShapeOperation implements G2Operation
{
  private Shape shape;

  public FillShapeOperation(Shape shape)
  {
    this.shape = shape;
  }

  public void draw(Graphics2D g2)
  {
    g2.fill(shape);
  }
}
