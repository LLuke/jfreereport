/**
 * Date: Mar 9, 2003
 * Time: 6:19:34 PM
 *
 * $Id: DemoDrawable.java,v 1.1 2003/03/09 17:19:28 taqua Exp $
 */
package com.jrefinery.report.demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.jrefinery.ui.Drawable;

public class DemoDrawable implements Drawable
{
  public DemoDrawable()
  {
  }

  public void draw(Graphics2D graphics, Rectangle2D bounds)
  {
    graphics.setColor(Color.black);
    graphics.drawString(bounds.toString(), 10, 10);
    graphics.draw(new Rectangle2D.Double(0,0,452,29));
    graphics.draw(bounds);
  }
}

