/**
 * Date: Mar 9, 2003
 * Time: 2:41:14 PM
 *
 * $Id$
 */
package org.jfree.pixie.g2recorder;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class AddRenderingHintsOperation implements G2Operation
{
  private RenderingHints renderingHints;

  public AddRenderingHintsOperation(RenderingHints renderingHints)
  {
    this.renderingHints = renderingHints;
  }

  public void draw(Graphics2D g2)
  {
    g2.addRenderingHints(renderingHints);
  }
}
