/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: ShapeOperationModule.java,v 1.3 2003/08/24 15:03:59 taqua Exp $
 *
 * Changes
 * -------
 * 02-Dec-2002 : Initial version
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 07-Feb-2003 : ContentCreation extracted into separate package
 */

package org.jfree.report.modules.output.pageable.base.operations;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ShapeElement;
import org.jfree.report.content.Content;
import org.jfree.report.content.ShapeContent;
import org.jfree.report.style.ElementStyleSheet;

/**
 * Creates the required operations to display/print shape content in the output target.
 *
 * @see ShapeContent
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
   * @param col  the operations collector.
   * @param e  the element.
   * @param value  the content.
   * @param bounds  the bounds.
   *
   */
  public void createOperations(final PhysicalOperationsCollector col, final Element e,
                               final Content value, final Rectangle2D bounds)
  {
    final Stroke stroke = (Stroke) e.getStyle().getStyleProperty(ElementStyleSheet.STROKE);
    final Color paint = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);

    final boolean shouldDraw = e.getStyle().getBooleanStyleProperty(ShapeElement.DRAW_SHAPE);
    final boolean shouldFill = e.getStyle().getBooleanStyleProperty(ShapeElement.FILL_SHAPE);

    if (shouldFill == false && shouldDraw == false)
    {
      return;
    }

    final ShapeContent sc = (ShapeContent) value.getContentForBounds(bounds);
    if (sc == null)
    {
      return;
    }

    final Shape s = sc.getShape();
    col.addOperation(new PhysicalOperation.SetBoundsOperation(bounds));
    col.addOperation(new PhysicalOperation.SetStrokeOperation(stroke));
    col.addOperation(new PhysicalOperation.SetPaintOperation(paint));
    if (shouldDraw == true)
    {
      col.addOperation(new PhysicalOperation.PrintShapeOperation(s));
    }

    if (shouldFill == true)
    {
      col.addOperation(new PhysicalOperation.PrintFilledShapeOperation(s));
    }
  }
}
