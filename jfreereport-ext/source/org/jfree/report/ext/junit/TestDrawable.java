/**
 * Date: Mar 7, 2003
 * Time: 3:57:54 PM
 *
 * $Id: TestDrawable.java,v 1.2 2005/08/08 15:55:59 taqua Exp $
 */
package org.jfree.report.ext.junit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.ui.Drawable;
import org.jfree.util.Log;

public class TestDrawable implements Drawable
{
  public TestDrawable()
  {
  }

  public void draw(final Graphics2D graphics, final Rectangle2D bounds)
  {
    Log.debug("Drawable: " + bounds);
    graphics.setColor(Color.black);
    graphics.drawString(bounds.toString(), 10, 10);
    graphics.draw(new Rectangle2D.Double(0, 0, 453, 30));
    graphics.draw(bounds);
  }
}
