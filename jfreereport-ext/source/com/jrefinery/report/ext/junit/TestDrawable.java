/**
 * Date: Mar 7, 2003
 * Time: 3:57:54 PM
 *
 * $Id: TestDrawable.java,v 1.2 2003/05/14 22:36:46 taqua Exp $
 */
package com.jrefinery.report.ext.junit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.report.util.Log;
import org.jfree.ui.Drawable;

public class TestDrawable implements Drawable
{
  public TestDrawable()
  {
  }

  public void draw(Graphics2D graphics, Rectangle2D bounds)
  {
    Log.debug("Drawable: " + bounds);
    graphics.setColor(Color.black);
    graphics.drawString(bounds.toString(), 10, 10);
    graphics.draw(new Rectangle2D.Double(0, 0, 453, 30));
    graphics.draw(bounds);
  }
}
