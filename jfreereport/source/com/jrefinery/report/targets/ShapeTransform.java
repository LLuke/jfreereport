/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * $Id: ShapeTransform.java,v 1.5 2003/03/08 16:08:06 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.targets;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
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
   * todo limit the shape dimension if no scaling is requested.
   *
   * @param s the shape that should be transformed
   * @param scale true, if the shape should be scaled, false otherwise
   * @param keepAR true, if the scaled shape should keep the aspect ratio
   * @param dim the target dimension.
   * @return the transformed shape
   */
  public static Shape transformShape (Shape s, boolean scale, boolean keepAR, Dimension2D dim)
  {
    AffineTransform af = AffineTransform.getTranslateInstance(0, 0);

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
        if (keepAR)
        {
          double scaleFact = Math.min (scaleX, scaleY);
          af.concatenate(AffineTransform.getScaleInstance(scaleFact, scaleFact));
        }
        else
        {
          af.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
        }
      }
    }
    return af.createTransformedShape(s);
  }
}
