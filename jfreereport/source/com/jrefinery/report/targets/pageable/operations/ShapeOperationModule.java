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
 * ShapeOperationModule.java
 * ------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeOperationModule.java,v 1.2 2002/12/16 17:31:05 mungady Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.ElementLayoutInformation;
import com.jrefinery.report.targets.pageable.contents.Content;
import com.jrefinery.report.targets.pageable.contents.ShapeContent;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of operations that relate to shapes.
 *
 * @author Thomas Morgner
 */
public class ShapeOperationModule extends OperationModule
{
  /**
   * Default constructor.
   */
  public ShapeOperationModule()
  {
    super("shape/*");
  }

  /**
   * Creates a list of operations that will render report content within the specified bounds.
   *
   * @param e  the element.
   * @param value  the content.
   * @param bounds  the bounds.
   *
   * @return a list of operations.
   */
  public List createOperations (Element e, Content value, Rectangle2D bounds)
  {
    Stroke stroke = (Stroke) e.getStyle().getStyleProperty(ElementStyleSheet.STROKE);
    Paint paint = (Paint) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    boolean shouldDraw = e.getStyle().getBooleanStyleProperty(ShapeElement.DRAW_SHAPE);
    boolean shouldFill = e.getStyle().getBooleanStyleProperty(ShapeElement.FILL_SHAPE);

    if (shouldFill == false && shouldDraw == false)
    {
      return null;
    }

    ShapeContent sc = (ShapeContent) value.getContentForBounds(bounds);
    Shape s = sc.getNormalizedShape();
    ArrayList array = new ArrayList ();
    array.add (new PhysicalOperation.SetBoundsOperation (bounds));
    array.add (new PhysicalOperation.SetStrokeOperation (stroke));
    array.add (new PhysicalOperation.SetPaintOperation(paint));
    if (shouldDraw == true)
    {
      array.add (new PhysicalOperation.PrintShapeOperation(s));
    }

    if (shouldFill == true)
    {
      array.add (new PhysicalOperation.PrintFilledShapeOperation(s));
    }

    return array;
  }

  /**
   * Creates content for an element.
   *
   * @param e  the element.
   * @param bounds  the bounds.
   * @param ot  the output target.
   *
   * @return the content.
   *
   * @throws OutputTargetException if there is a problem with the OutputTarget.
   */
  public Content createContentForElement(Element e, ElementLayoutInformation bounds, OutputTarget ot)
      throws OutputTargetException
  {
    Point2D point = bounds.getAbsolutePosition();
    Dimension2D iBounds = ElementLayoutInformation.unionMin(bounds.getMaximumSize(),
                                                            bounds.getPreferredSize());

    AffineTransform af = AffineTransform.getTranslateInstance(point.getX(), point.getY());
    Shape s = (Shape) e.getValue();

    /**
     * Always scale to the maximum bounds ...
     */
    if (e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE))
    {
      Rectangle2D boundsShape = s.getBounds2D();
      double w = boundsShape.getWidth();
      double h = boundsShape.getHeight();
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
          double scale = Math.min (scaleX, scaleY);
          af.concatenate(AffineTransform.getScaleInstance(scale, scale));
        }
        else
        {
          af.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
        }
      }
    }
    Shape retval =  af.createTransformedShape(s);
    return new ShapeContent (retval);
  }
}
