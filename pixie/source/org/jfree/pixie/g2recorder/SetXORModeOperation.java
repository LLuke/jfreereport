/**
 * Date: Mar 9, 2003
 * Time: 2:26:35 PM
 *
 * $Id: SetXORModeOperation.java,v 1.1 2003/03/09 20:38:15 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Color;

public class SetXORModeOperation implements G2Operation
{
  private Color xorColor;

  public SetXORModeOperation(final Color xorColor)
  {
    this.xorColor = xorColor;
  }

  public void draw(final Graphics2D g2)
  {
    g2.setXORMode(xorColor);
  }
}
