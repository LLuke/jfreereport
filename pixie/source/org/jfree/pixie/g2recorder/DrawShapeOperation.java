/**
 * Date: Mar 9, 2003
 * Time: 1:40:01 PM
 *
 * $Id: DrawShapeOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Shape;

public class DrawShapeOperation implements G2Operation
{
  private Shape shape;

  public DrawShapeOperation (final Shape shape)
  {
    if (shape == null)
    {
      throw new NullPointerException();
    }
    this.shape = shape;
  }

  public void draw (final Graphics2D g2)
  {
    g2.draw(shape);
  }
}
