/**
 * Date: Mar 9, 2003
 * Time: 2:26:04 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;

public class SetPaintModeOperation implements G2Operation
{
  public SetPaintModeOperation()
  {
  }

  public void draw(Graphics2D g2)
  {
    g2.setPaintMode();
  }
}
