/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ------------------------
 * ImageOperationModule.java
 * ------------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ImageOperationModule.java,v 1.1 2002/12/12 20:20:27 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.ElementLayoutInformation;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.contents.Content;
import com.jrefinery.report.targets.pageable.contents.ImageContent;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of operations that relate to images.
 *
 * @author Thomas Morgner
 */
public class ImageOperationModule extends OperationModule
{
  /**
   * Default constructor.
   */
  public ImageOperationModule()
  {
    super("image/*");
  }

  /**
   * Creates a list of operations.
   *
   * @param e  the element.
   * @param value  the content.
   * @param bounds  the bounds.
   *
   * @return a list of operations.
   */
  public List createOperations (Element e, Content value, Rectangle2D bounds)
  {
    Paint paint = (Paint) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ImageContent ic = (ImageContent) value.getContentForBounds(bounds);

    ArrayList ops = new ArrayList();
    ops.add (new PhysicalOperation.SetBoundsOperation (bounds));
    ops.add (new PhysicalOperation.SetPaintOperation(paint));
    ops.add (new PhysicalOperation.PrintImageOperation(ic.getContent()));
    return ops;
  }

  /**
   * ??.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   */
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, OutputTarget ot)
      throws OutputTargetException
  {
    Point2D point = bounds.getAbsolutePosition();
    Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                            bounds.getPreferredSize());

    ImageReference ir = (ImageReference) e.getValue();
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE))
    {
      double w = ir.getImageWidth();
      double h = ir.getImageHeight();
      double scaleX = 1;
      double scaleY = 1;

      if (w != 0)
      {
        scaleX = iBounds.getWidth() / w;
      }
      if (h != 0)
      {
        scaleY = iBounds.getHeight() / h;
      }
      if (scaleX != 1 || scaleY != 1)
      {
        if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO))
        {
          float scale = (float) Math.min (scaleX, scaleY);
          ir.setScaleX(scale);
          ir.setScaleY(scale);
        }
        else
        {
          ir.setScaleX((float) scaleX);
          ir.setScaleY((float) scaleY);
        }
      }
    }
    Rectangle2D irBounds = new Rectangle2D.Double(point.getX(),
                                                  point.getY(),
                                                  ir.getBoundsScaled().getWidth(),
                                                  ir.getBoundsScaled().getHeight());
    return new ImageContent(ir, irBounds);
  }

}
