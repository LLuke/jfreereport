/**
 * Date: Mar 9, 2003
 * Time: 2:18:36 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Paint;

public class SetPaintOperation implements G2Operation
{
  private Paint paint;

  public SetPaintOperation(Paint paint)
  {
    this.paint = paint;
  }

  public void draw(Graphics2D g2)
  {
    g2.setPaint(paint);
  }
}
