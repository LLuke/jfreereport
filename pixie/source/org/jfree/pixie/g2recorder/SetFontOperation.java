/**
 * Date: Mar 9, 2003
 * Time: 2:29:11 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Font;

public class SetFontOperation implements G2Operation
{
  private Font font;

  public SetFontOperation(Font font)
  {
    this.font = font;
  }

  public void draw(Graphics2D g2)
  {
    g2.setFont(font);
  }
}
