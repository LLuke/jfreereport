/**
 * Date: Mar 9, 2003
 * Time: 2:17:57 PM
 *
 * $Id: SetStrokeOperation.java,v 1.1 2003/03/09 20:38:15 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Stroke;
import java.awt.Graphics2D;

public class SetStrokeOperation implements G2Operation
{
  private Stroke stroke;

  public SetStrokeOperation(final Stroke stroke)
  {
    this.stroke = stroke;
  }

  public void draw(final Graphics2D g2)
  {
    g2.setStroke(stroke);
  }
}
