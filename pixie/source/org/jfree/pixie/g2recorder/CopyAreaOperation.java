/**
 * Date: Mar 9, 2003
 * Time: 2:51:03 PM
 *
 * $Id: CopyAreaOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;

public class CopyAreaOperation implements G2Operation
{
  private int x;
  private int y;
  private int width;
  private int height;
  private int dx;
  private int dy;

  public CopyAreaOperation (final int x, final int y, final int width, final int height,
                            final int dx, final int dy)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.dx = dx;
    this.dy = dy;
  }

  public void draw (final Graphics2D g2)
  {
    g2.copyArea(x, y, width, height, dx, dy);
  }
}
