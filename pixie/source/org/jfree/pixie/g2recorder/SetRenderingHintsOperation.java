/**
 * Date: Mar 9, 2003
 * Time: 2:39:19 PM
 *
 * $Id: SetRenderingHintsOperation.java,v 1.2 2003/07/03 16:13:36 taqua Exp $
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SetRenderingHintsOperation implements G2Operation
{
  private RenderingHints renderingHints;

  public SetRenderingHintsOperation (final RenderingHints renderingHints)
  {
    this.renderingHints = renderingHints;
  }

  public void draw (final Graphics2D g2)
  {
    g2.setRenderingHints(renderingHints);
  }
}
