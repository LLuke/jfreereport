/**
 * Date: Mar 9, 2003
 * Time: 2:05:21 PM
 *
 * $Id: DrawStringOperation.java,v 1.1 2003/03/09 20:38:11 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;

public class DrawStringOperation implements G2Operation
{
  private String string;
  private float x;
  private float y;

  public DrawStringOperation(final String string, final float x, final float y)
  {
    this.string = string;
    this.x = x;
    this.y = y;
  }

  public void draw(final Graphics2D g2)
  {
    g2.drawString (string, x,y);
  }
}
