/**
 * Date: Mar 9, 2003
 * Time: 2:26:04 PM
 *
 * $Id: SetPaintModeOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;

public class SetPaintModeOperation implements G2Operation
{
  public SetPaintModeOperation ()
  {
  }

  public void draw (final Graphics2D g2)
  {
    g2.setPaintMode();
  }
}
