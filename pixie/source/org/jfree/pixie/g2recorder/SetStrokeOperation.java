/**
 * Date: Mar 9, 2003
 * Time: 2:17:57 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Stroke;
import java.awt.Graphics2D;

public class SetStrokeOperation implements G2Operation
{
  private Stroke stroke;

  public SetStrokeOperation(Stroke stroke)
  {
    this.stroke = stroke;
  }

  public void draw(Graphics2D g2)
  {
    g2.setStroke(stroke);
  }
}
