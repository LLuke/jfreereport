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
 * -------------------
 * AbstractTableCellDataFactory.java
 * -------------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractTableCellDataFactory.java,v 1.4 2003/02/10 21:23:40 taqua Exp $
 *
 * Changes
 * -------
 * 28-Jan-2003 : Initial version
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.Element;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.ShapeTransform;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An default implementation of the TableCellDataFactory, which is able to handle
 * Background- and Band-cells.
 * <p>
 * @see TableBandArea
 * @see TableCellData
 */
public abstract class AbstractTableCellDataFactory implements TableCellDataFactory
{
  /**
   * Create a band cell for the given element. A Band cell is used to create
   * borders for the band bounds, and contains no other data or formats.
   *
   * @param e the band for which the band area should be created.
   * @param rect the bounds of the element.
   * @return the band area or null, if the band has a height or width of 0.
   */
  public TableCellData createBandCell(Element e, Rectangle2D rect)
  {
    if (rect.getHeight() == 0 || rect.getWidth() == 0)
    {
      // bands with a height of 0 are ignored ...
      return null;
    }
    return new TableBandArea(rect);
  }

  /**
   * Handles the creation of background cells. This implementation translates lines
   * and rectangles into border and background color specifications.
   * <p>
   * The shape must be either a horizontal or vertical line or a rectangle. Other
   * shape types are ignored, as they cannot be translated into the table cell space.
   *
   * @param e the element that defines the background, ususally a ShapeElement.
   * @param shape the shape that should be used as background.
   * @param bounds the element's bounds within the table.
   * @return the generated TableCellBackground or null if the background shape is not
   * supported.
   */
  public TableCellBackground createBackground (Element e, Shape shape, Rectangle2D bounds)
  {
    TableCellBackground bg = null;

    Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    Point2D point = new Point2D.Double(bounds.getX(), bounds.getY());
    Dimension2D dim = new FloatDimension(bounds.getWidth(), bounds.getHeight());

    Shape s = ShapeTransform.transformShape(shape,
                                            e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE),
                                            e.getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO),
                                            point,
                                            dim);
    Rectangle2D shapeBounds = s.getBounds2D();
    shapeBounds.setRect(point.getX(),
                        point.getY(),
                        shapeBounds.getWidth(),
                        shapeBounds.getHeight());

    if (e.getStyle().getBooleanStyleProperty(ShapeElement.DRAW_SHAPE))
    {
      BasicStroke stroke = (BasicStroke) e.getStyle().getStyleProperty(ElementStyleSheet.STROKE);
      float width = stroke.getLineWidth();


      if (shape instanceof Line2D)
      {
        if ((shapeBounds.getWidth() == 0) &&
            (shapeBounds.getHeight() == 0))
        {
          // this shape has no content ...
          return null;
        }
        else if (shapeBounds.getHeight() == 0)
        {
          // horizontal line
          bg = new TableCellBackground(shapeBounds,
                                       null);

          bg.setBorderTop(color, width);
        }
        else if (shapeBounds.getWidth() == 0)
        {
          // vertical line
          bg = new TableCellBackground(shapeBounds,
                                       null);
          bg.setBorderLeft(color, width);
        }
      }
      else if (shape instanceof Rectangle2D)
      {
        if (e.getStyle().getBooleanStyleProperty(ShapeElement.FILL_SHAPE))
        {
          bg = new TableCellBackground(shapeBounds, color);
        }
        else
        {
          bg = new TableCellBackground(shapeBounds, null);
        }
        bg.setBorderLeft(color, width);
        bg.setBorderTop(color, width);
        bg.setBorderBottom(color, width);
        bg.setBorderRight(color, width);
      }
    }
    else if (e.getStyle().getBooleanStyleProperty(ShapeElement.FILL_SHAPE))
    {
      if (shape instanceof Rectangle2D)
      {
        bg = new TableCellBackground(shapeBounds, color);
      }
    }

    return bg;
  }

}
