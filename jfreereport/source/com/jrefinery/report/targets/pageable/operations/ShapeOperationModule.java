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
 * $Id: ShapeOperationModule.java,v 1.2 2003/02/02 23:43:51 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updated Javadocs (DG);
 *
 */

package com.jrefinery.report.targets.pageable.operations;

import com.jrefinery.report.Element;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.targets.base.content.Content;
import com.jrefinery.report.targets.base.content.ShapeContent;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
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
    Color paint = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

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
}
