/**
 * Date: Jan 28, 2003
 * Time: 3:14:12 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.table;

import com.jrefinery.report.Element;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.ShapeTransform;

import java.awt.Shape;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;

public abstract class AbstractTableCellDataFactory implements TableCellDataFactory
{
  public TableCellData createBandCell(Element e, Rectangle2D rect)
  {
    if (rect.getHeight() == 0)
    {
      Log.debug ("Band " + e + " has Rect Height of 0" + e.getStyle().getStyleProperty(ElementStyleSheet.BOUNDS));
      Log.debug ("--------> 0" + rect);
      return null;
    }
    return new TableBandArea(rect);
  }

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
      bg = new TableCellBackground(shapeBounds, color);
    }

    return bg;
  }

}
