/**
 * Date: Mar 9, 2003
 * Time: 2:17:57 PM
 *
 * $Id: SetStrokeOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Stroke;

public class SetStrokeOperation implements G2Operation
{
  private Stroke stroke;

  public SetStrokeOperation (final Stroke stroke)
  {
    this.stroke = stroke;
  }

  public void draw (final Graphics2D g2)
  {
    g2.setStroke(stroke);
  }
}
