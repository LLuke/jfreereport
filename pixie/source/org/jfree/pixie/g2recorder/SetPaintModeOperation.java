/**
 * Date: Mar 9, 2003
 * Time: 2:26:04 PM
 *
 * $Id: SetPaintModeOperation.java,v 1.1 2003/03/09 20:38:14 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;

public class SetPaintModeOperation implements G2Operation
{
  public SetPaintModeOperation()
  {
  }

  public void draw(final Graphics2D g2)
  {
    g2.setPaintMode();
  }
}
