/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * -----------------
 * DemoDrawable.java
 * -----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DemoDrawable.java,v 1.2 2003/06/27 14:25:16 taqua Exp $
 *
 * Changes
 * -------
 * 07-Mar-2003 : Version 1 (DG);
 *
 */

package com.jrefinery.report.demo.helper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.ui.Drawable;


/**
 * A simple implementation of the Drawable interface, used for a report demonstration.
 *
 * @author Thomas Morgner.
 */
public class DemoDrawable implements Drawable
{
  /**
   * Default constructor.
   */
  public DemoDrawable()
  {
  }

  /**
   * Draws the item.
   *
   * @param graphics  the graphics implementation.
   * @param bounds  the bounds.
   */
  public void draw(final Graphics2D graphics, final Rectangle2D bounds)
  {
    graphics.setColor(Color.black);
    graphics.drawString(bounds.toString(), 10, 10);
    graphics.draw(new Rectangle2D.Double(0, 0, 452, 29));
    graphics.draw(bounds);
  }
}

