/**
 * Date: Mar 9, 2003
 * Time: 2:18:36 PM
 *
 * $Id: SetBackgroundOperation.java,v 1.1 2003/03/09 20:38:12 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Color;
import java.awt.Graphics2D;

public class SetBackgroundOperation implements G2Operation
{
  private Color background;

  public SetBackgroundOperation(final Color background)
  {
    this.background = background;
  }

  public void draw(final Graphics2D g2)
  {
    g2.setBackground(background);
  }
}
