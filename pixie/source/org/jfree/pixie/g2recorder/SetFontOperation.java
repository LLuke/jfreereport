/**
 * Date: Mar 9, 2003
 * Time: 2:29:11 PM
 *
 * $Id: SetFontOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Font;
import java.awt.Graphics2D;

public class SetFontOperation implements G2Operation
{
  private Font font;

  public SetFontOperation (final Font font)
  {
    this.font = font;
  }

  public void draw (final Graphics2D g2)
  {
    g2.setFont(font);
  }
}
