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
 * ---------------------------------
 * AbstractTableCellDataFactory.java
 * ---------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractTableCellDataFactory.java,v 1.1 2003/07/07 22:44:07 taqua Exp $
 *
 * Changes
 * -------
 * 28-Jan-2003 : Initial version
 * 24-Feb-2003 : Fixed Checkstyle issues (DG);
 *
 */

package org.jfree.report.modules.output.table.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ShapeElement;
import org.jfree.report.content.ShapeTransform;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.ui.FloatDimension;

/**
 * A base implementation of the {@link TableCellDataFactory} interface, which is able to handle
 * background cell and band cells.
 *
 * @see TableBandArea
 * @see TableCellData
 *
 * @author Thomas Morgner
 */
public abstract class AbstractTableCellDataFactory implements TableCellDataFactory
{
  /**
   * Create a band cell for the given element. A band cell is used to create
   * borders for the band bounds, and contains no other data or formats.
   *
   * @param rect  the bounds of the element.
   *
   * @return The band area or <code>null<code>, if the band has a height or width of 0.
   */
  public TableCellData createBandCell(final Rectangle2D rect)
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
   * @param e  the element that defines the background, usually a {@link ShapeElement}.
   * @param shape  the shape that should be used as background.
   * @param bounds  the element's bounds within the table.
   *
   * @return the generated {@link TableCellBackground} or <code>null</code> if the background
   *         shape is not supported.
   */
  public TableCellBackground createBackground
    (final Element e, final Shape shape, final Rectangle2D bounds)
  {
    TableCellBackground bg = null;

    final Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    final Point2D point = new Point2D.Float((float) bounds.getX(), (float) bounds.getY());
    final Dimension2D dim = new FloatDimension
      ((float) bounds.getWidth(), (float) bounds.getHeight());

    final Shape s = ShapeTransform.transformShape(
        shape,
        e.getStyle().getBooleanStyleProperty(ElementStyleSheet.SCALE),
        e.getStyle().getBooleanStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO),
        dim);
    final Rectangle2D shapeBounds = s.getBounds2D();
    shapeBounds.setRect(point.getX(),
        point.getY(),
        shapeBounds.getWidth(),
        shapeBounds.getHeight());

    if (e.getStyle().getBooleanStyleProperty(ShapeElement.DRAW_SHAPE))
    {
      final BasicStroke stroke = (BasicStroke) 
        e.getStyle().getStyleProperty(ElementStyleSheet.STROKE);
      final float width = stroke.getLineWidth();


      if (shape instanceof Line2D)
      {
        if ((shapeBounds.getWidth() == 0) && (shapeBounds.getHeight() == 0))
        {
          // this shape has no content ...
          return null;
        }
        else if (shapeBounds.getHeight() == 0)
        {
          // horizontal line
          bg = new TableCellBackground(shapeBounds, null);

          bg.setBorderTop(color, width);
        }
        else if (shapeBounds.getWidth() == 0)
        {
          // vertical line
          bg = new TableCellBackground(shapeBounds, null);
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
