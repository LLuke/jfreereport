/**
 * Date: Jan 18, 2003
 * Time: 8:01:24 PM
 *
 * $Id: HtmlCellDataFactory.java,v 1.4 2003/01/27 03:17:43 taqua Exp $
 */
package com.jrefinery.report.targets.table.html;

import com.jrefinery.report.Element;
import com.jrefinery.report.ElementAlignment;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.ShapeTransform;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.table.TableCellBackground;
import com.jrefinery.report.targets.table.TableCellData;
import com.jrefinery.report.targets.table.TableCellDataFactory;
import com.jrefinery.report.util.Log;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class HtmlCellDataFactory implements TableCellDataFactory
{
  private HtmlStyleCollection styleCollection;
  private boolean useXHTML;

  public HtmlCellDataFactory(HtmlStyleCollection styleCollection, boolean useXHTML)
  {
    this.styleCollection = styleCollection;
    this.useXHTML = useXHTML;
  }

  public TableCellData createCellData(Element e, Rectangle2D rect)
  {
    Object value = e.getValue();

    if (e.isVisible() == false)
    {
      return null;
    }

    FontDefinition font = e.getStyle().getFontDefinitionProperty();
    Color color = (Color) e.getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    ElementAlignment valign = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.VALIGNMENT);
    ElementAlignment halign = (ElementAlignment) e.getStyle().getStyleProperty(ElementStyleSheet.ALIGNMENT);

    if (value instanceof ImageReference)
    {
      HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
      styleCollection.addStyle(style);
      return new HtmlImageCellData(rect, (ImageReference) value, style, useXHTML);
    }

    if (value instanceof String)
    {
      HtmlCellStyle style = new HtmlCellStyle(font, color, valign, halign);
      styleCollection.addStyle(style);
      return new HtmlTextCellData(rect, (String) value, style, useXHTML);
    }

    if (value instanceof Shape)
    {
      // check backgrounds
      return createBackground(e, (Shape) value, rect);
    }

    Log.debug ("Element " + e + " ignored");
    return null;
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
          Log.debug ("SHAPE for Element has no content");
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

        Log.debug ("Created Line: " + bg);
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
