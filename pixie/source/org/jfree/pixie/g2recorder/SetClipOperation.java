/**
 * Date: Mar 9, 2003
 * Time: 2:48:41 PM
 *
 * $Id: SetClipOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.Shape;

public class SetClipOperation implements G2Operation
{
  private Shape clip;

  public SetClipOperation (final Shape clip)
  {
    this.clip = clip;
  }

  public void draw (final Graphics2D g2)
  {
    g2.setClip(clip);
  }
}
