/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * BorderShapeFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: BorderShapeFactory.java,v 1.1 2006/07/11 13:54:24 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.pageable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.BorderCorner;
import org.jfree.layouting.renderer.border.BorderEdge;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.layouting.util.geom.StrictInsets;
import org.jfree.layouting.input.style.values.CSSColorValue;

/**
 * Creation-Date: 11.07.2006, 13:32:31
 *
 * @author Thomas Morgner
 */
public class BorderShapeFactory
{
  private static final int CORNER_RIGHT_TOP = 0;
  private static final int CORNER_TOP_RIGHT = 1;
  private static final int CORNER_TOP_LEFT = 2;
  private static final int CORNER_LEFT_TOP = 3;
  private static final int CORNER_LEFT_BOTTOM = 4;
  private static final int CORNER_BOTTOM_LEFT = 5;
  private static final int CORNER_BOTTOM_RIGHT = 6;
  private static final int CORNER_RIGHT_BOTTOM = 7;

  private static final byte[][] CORNER_FACTORS = new byte[][] {
          { +1, +1}, // RIGHT_TOP
          { +1, +1}, // TOP_RIGHT
          { -1, +1}, // TOP_LEFT
          { -1, +1}, // LEFT_TOP
          { +1, -1}, // LEFT_BOTTOM
          { +1, -1}, // BOTTOM_LEFT
          { -1, -1}, // BOTTOM_RIGHT
          { -1, -1}, // RIGHT_BOTTOM
  };


  public static class BorderDrawOperation
  {
    private Color color;
    private BasicStroke stroke;
    private Shape shape;

    public BorderDrawOperation(final Shape shape,
                               final Color color,
                               final BasicStroke stroke)
    {
      this.shape = shape;
      this.color = color;
      this.stroke = stroke;
    }

    public void draw(Graphics2D g2)
    {
      if (shape == null) return;
      if (stroke.getLineWidth() == 0) return;
      if (color.getAlpha() == 0) return;

      g2.setStroke(stroke);
      g2.setColor(color);
      g2.draw(shape);
    }

    public void fill(final Graphics2D g2)
    {
      if (shape == null) return;
      if (stroke.getLineWidth() == 0) return;
      if (color.getAlpha() == 0) return;

      g2.setStroke(stroke);
      g2.setColor(color);
      g2.fill(shape);
    }
  }

  private Color color;
  private BasicStroke stroke;
  private double height;
  private double width;
  private double x;
  private double y;
  private Border border;
  private StrictInsets borderSizes;
  private ArrayList drawOps;
  private ArrayList fillOps;
  private CSSColorValue backgroundColor;

  public BorderShapeFactory(final RenderBox box)
  {
    drawOps = new ArrayList();
    fillOps = new ArrayList();

    x = StrictGeomUtility.toExternalValue(box.getX());
    y = StrictGeomUtility.toExternalValue(box.getY());
    width = StrictGeomUtility.toExternalValue(box.getWidth());
    height = StrictGeomUtility.toExternalValue(box.getHeight());

    border = box.getBorder();
    borderSizes = box.getBorderWidths();
    backgroundColor = box.getBoxDefinition().getBackgroundColor();
  }

  private Arc2D generateCorner (int type,
                                double cornerX,
                                double cornerY,
                                BorderCorner corner,
                                boolean fillShape)
  {
    if (corner.getHeight().getValue() > 0 && corner.getWidth().getValue() > 0)
    {
      byte[] cornerFactors = CORNER_FACTORS[type];

      final double widthTopLeft =
              StrictGeomUtility.toExternalValue(corner.getWidth().getValue());
      final double heightTopLeft =
              StrictGeomUtility.toExternalValue(corner.getHeight().getValue());

      final int open;
      if (fillShape)
      {
        open = Arc2D.PIE;
      }
      else
      {
        open = Arc2D.OPEN;
      }
      return new Arc2D.Double
              (cornerX + widthTopLeft * cornerFactors[0],
                      cornerY + heightTopLeft * cornerFactors[1],
                      widthTopLeft, heightTopLeft,
                      Math.PI * type / 4.0, Math.PI / 4.0, open);
    }
    return null;
  }

  private BasicStroke createStroke (BorderEdge edge, long width)
  {
    return new BasicStroke(width / 1000f);
  }

  public void generateBorder (Graphics2D g2)
  {
    generateTopEdge();
    generateLeftEdge();
    generateBottomEdge();
    geenrateRightEdge();

    for (int i = 0; i < drawOps.size(); i++)
    {
      BorderDrawOperation operation = (BorderDrawOperation) drawOps.get(i);
      operation.draw(g2);
    }

    for (int i = 0; i < fillOps.size(); i++)
    {
      BorderDrawOperation operation = (BorderDrawOperation) fillOps.get(i);
      operation.fill(g2);
    }

    if (backgroundColor == null || backgroundColor.getAlpha() == 0)
    {
      return;
    }
    // now we need some geometry stuff (yeah, I'm lazy!)
    Area globalArea = new Area(new Rectangle2D.Double(x, y, width, height));

    // for each corner:
    //
    // create the inverse area and substract that from
    // the global area.
    for (int i = 0; i < fillOps.size(); i++)
    {
      BorderDrawOperation operation = (BorderDrawOperation) fillOps.get(i);
      final Shape shape = operation.shape;
      if (shape == null)
      {
        continue;
      }

      Area cornerArea = new Area(shape.getBounds2D());
      cornerArea.subtract(new Area (shape));
      globalArea.subtract(cornerArea);
    }
    // oh, yeah, I know, there are faster ways than that.

    g2.setColor(backgroundColor);
    g2.fill(globalArea);
  }

  private void draw (Shape s)
  {
    drawOps.add(new BorderDrawOperation(s, color, stroke));
  }

  private void fill (Shape s)
  {
    fillOps.add(new BorderDrawOperation(s, color, stroke));
  }

  private void geenrateRightEdge ()
  {
    final BorderEdge rightEdge = border.getRight();
    color = rightEdge.getColor();
    stroke = createStroke(rightEdge, borderSizes.getRight());

    final BorderCorner firstCorner = border.getBottomRight();
    final BorderCorner secondCorner = border.getTopRight();

    draw(generateCorner(CORNER_RIGHT_BOTTOM, x + width, y + height, firstCorner, false));
    draw(generateCorner(CORNER_RIGHT_TOP, x + width, y, secondCorner, false));
    fill(generateCorner(CORNER_RIGHT_BOTTOM, x + width, y + height, firstCorner, true));
    fill(generateCorner(CORNER_RIGHT_TOP, x + width, y, secondCorner, true));

    draw (new Line2D.Double
            (x + width - firstCorner.getWidth().getValue(),
             y + height - firstCorner.getHeight().getValue(),
             x + width - secondCorner.getWidth().getValue(),
             y + secondCorner.getHeight().getValue()));

  }

  private void generateBottomEdge ()
  {
    final BorderEdge bottomEdge = border.getBottom();
    final BorderCorner firstCorner = border.getBottomLeft();
    final BorderCorner secondCorner = border.getBottomRight();

    color = bottomEdge.getColor();
    stroke = createStroke(bottomEdge, borderSizes.getBottom());

    draw(generateCorner(CORNER_BOTTOM_LEFT, x, y + height, firstCorner, false));
    draw(generateCorner(CORNER_BOTTOM_RIGHT, x + width, y + height, secondCorner, false));
    fill(generateCorner(CORNER_BOTTOM_LEFT, x, y + height, firstCorner, true));
    fill(generateCorner(CORNER_BOTTOM_RIGHT, x + width, y + height, secondCorner, true));

    draw (new Line2D.Double
            (x + firstCorner.getWidth().getValue(),
             y + height - firstCorner.getHeight().getValue(),
             x + width - secondCorner.getWidth().getValue(),
             y + height - secondCorner.getHeight().getValue()));

  }

  private void generateLeftEdge ()
  {
    final BorderEdge leftEdge = border.getLeft();

    final BorderCorner firstCorner = border.getTopLeft();
    final BorderCorner secondCorner = border.getBottomLeft();

    stroke = createStroke(leftEdge, borderSizes.getTop());
    color = leftEdge.getColor();

    draw(generateCorner
      (CORNER_LEFT_TOP, x, y, firstCorner, false));
    draw(generateCorner
      (CORNER_LEFT_BOTTOM, x, y + height, secondCorner, false));
    fill(generateCorner
      (CORNER_LEFT_TOP, x, y, firstCorner, true));
    fill(generateCorner
      (CORNER_LEFT_BOTTOM, x, y + height, secondCorner, true));

    final double firstWidth =
            StrictGeomUtility.toInternalValue(firstCorner.getWidth().getValue());
    final double firstHeight =
            StrictGeomUtility.toInternalValue(firstCorner.getHeight().getValue());
    final double secondWidth =
            StrictGeomUtility.toInternalValue(secondCorner.getWidth().getValue());
    final double secondHeight =
            StrictGeomUtility.toInternalValue(secondCorner.getHeight().getValue());
    draw(new Line2D.Double
            (x + firstWidth,
             y + firstHeight,
             x + secondWidth,
             y + height - secondHeight));
  }

  private void generateTopEdge ()
  {
    final BorderEdge topEdge = border.getTop();
    stroke = createStroke(topEdge, borderSizes.getTop());
    color = topEdge.getColor();

    final BorderCorner firstCorner = border.getTopLeft();
    final BorderCorner secondCorner = border.getBottomLeft();

    draw(generateCorner
            (CORNER_TOP_RIGHT, x + width, y, border.getTopRight(), false));
    draw(generateCorner
            (CORNER_TOP_LEFT, x, y, border.getTopLeft(), false));

    fill(generateCorner
            (CORNER_TOP_RIGHT, x + width, y, border.getTopRight(), true));
    fill(generateCorner
            (CORNER_TOP_LEFT, x, y, border.getTopLeft(), true));

    final double firstWidth =
            StrictGeomUtility.toInternalValue(firstCorner.getWidth().getValue());
    final double firstHeight =
            StrictGeomUtility.toInternalValue(firstCorner.getHeight().getValue());
    final double secondWidth =
            StrictGeomUtility.toInternalValue(secondCorner.getWidth().getValue());
    final double secondHeight =
            StrictGeomUtility.toInternalValue(secondCorner.getHeight().getValue());
    draw(new Line2D.Double
            (x + firstWidth, y + firstHeight,
             x + width - secondWidth, y + secondHeight));
  }


}
