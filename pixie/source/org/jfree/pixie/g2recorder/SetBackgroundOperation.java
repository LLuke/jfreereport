/**
 * Date: Mar 9, 2003
 * Time: 2:18:36 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Color;
import java.awt.Graphics2D;

public class SetBackgroundOperation implements G2Operation
{
  private Color background;

  public SetBackgroundOperation(Color background)
  {
    this.background = background;
  }

  public void draw(Graphics2D g2)
  {
    g2.setBackground(background);
  }
}
