/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * SvgSalamanderDrawable.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 21.12.2005 : Initial version
 */
package org.jfree.report.ext.modules.svgimage;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.net.URI;

import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import org.jfree.ui.ExtendedDrawable;
import org.jfree.util.Log;

/**
 * Creation-Date: 21.12.2005, 20:25:29
 *
 * @author Thomas Morgner
 */
public class SvgSalamanderDrawable implements ExtendedDrawable
{
  private SVGUniverse svgUniverse;
  private URI svgKey;
  private boolean warned;

  public SvgSalamanderDrawable(final SVGUniverse svgUniverse, final URI svgKey)
  {
    if (svgUniverse == null)
    {
      throw new NullPointerException();
    }
    if (svgKey == null)
    {
      throw new NullPointerException();
    }
    this.svgUniverse = svgUniverse;
    this.svgKey = svgKey;
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio
   * aware, these bounds should be used to compute the preferred aspect ratio
   * for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    SVGDiagram diagram = svgUniverse.getDiagram(svgKey);
    float height = diagram.getHeight();
    float width = diagram.getWidth();

    return new Dimension((int) width, (int) height);
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the
   * drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(Graphics2D g2, Rectangle2D area)
  {
    g2.translate(-area.getX(), -area.getY());
    SVGDiagram diagram = svgUniverse.getDiagram(svgKey);
    float height = diagram.getHeight();
    float width = diagram.getWidth();

    final double sx = area.getWidth() / width;
    final double sy = area.getHeight() / height;
    final double sm = Math.min (sx, sy);
    g2.scale(sm, sm);

    try
    {
      diagram.render(g2);
    }
    catch (SVGException e)
    {
      // ignore ...
      if (!warned)
      {
        Log.warn ("Failed to render SVG image " + svgKey, e);
        warned = true;
      }
    }
  }
}