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
 * -------------------
 * ShapeTransform.java
 * -------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeTransform.java,v 1.12 2003/06/19 18:44:10 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.targets;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * Utility class, which resizes a Shape.
 *
 * @author Thomas Morgner
 */
public class ShapeTransform
{
  /**
   * Translates the given shape. The shape is translated to the origin supplied
   * in <code>point</code>. If scaling is requested, the shape will also be scaled
   * using an AffineTransform.
   *
   * @param s the shape that should be transformed
   * @param scale true, if the shape should be scaled, false otherwise
   * @param keepAR true, if the scaled shape should keep the aspect ratio
   * @param dim the target dimension.
   * @return the transformed shape
   */
  public static Shape transformShape(Shape s, boolean scale, boolean keepAR, Dimension2D dim)
  {
    /**
     * Always scale to the maximum bounds ...
     */
    if (scale)
    {

      Rectangle2D boundsShape = s.getBounds2D();
      double w = boundsShape.getWidth();
      double h = boundsShape.getHeight();
      double scaleX = 1;
      double scaleY = 1;

      if (w != 0)
      {
        scaleX = dim.getWidth() / w;
      }
      if (h != 0)
      {
        scaleY = dim.getHeight() / h;
      }
      if (scaleX != 1 || scaleY != 1)
      {
        /**
         * Apply the normalisation shape transform ... bring the shape to pos (0,0)
         */
        Rectangle2D bounds = s.getBounds2D();
        AffineTransform af
            = AffineTransform.getTranslateInstance(0 - bounds.getX(), 0 - bounds.getY());
        // apply normalisation translation ...
        s = af.createTransformedShape(s);

        if (keepAR)
        {
          double scaleFact = Math.min(scaleX, scaleY);
          af = AffineTransform.getScaleInstance(scaleFact, scaleFact);
        }
        else
        {
          af = AffineTransform.getScaleInstance(scaleX, scaleY);
        }
        // apply scaling ...
        s = af.createTransformedShape(s);

        // now retranslate the shape to its original position ...
        af = AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY());
        return af.createTransformedShape(s);
      }
    }
    Area a = new Area(s);
    if (a.isEmpty())
    {
      // don't clip  ... Area does not like lines
      // operations with lines always result in an empty Bounds:(0,0,0,0) area
      //
      // there is no trivial workaround known ...
      // except by reimplementing the clipping, and this is not a option - never!
      return s;
    }


    // mask everything outside of the clipping area
    Rectangle2D shape = s.getBounds2D();
    Rectangle2D clip = new Rectangle2D.Double(shape.getX(),
        shape.getY(), dim.getWidth(), dim.getHeight());

    Area clipArea = new Area(clip);
    clipArea.subtract(new Area(clip.createIntersection(shape)));

    a.subtract(new Area(clipArea));
    return a;
  }
}
