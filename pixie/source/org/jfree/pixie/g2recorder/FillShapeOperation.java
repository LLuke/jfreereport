/**
 * Date: Mar 9, 2003
 * Time: 2:06:43 PM
 *
 * $Id: FillShapeOperation.java,v 1.1 2003/03/09 20:38:11 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Shape;

public class FillShapeOperation implements G2Operation
{
  private Shape shape;

  public FillShapeOperation(final Shape shape)
  {
    this.shape = shape;
  }

  public void draw(final Graphics2D g2)
  {
    g2.fill(shape);
  }
}
