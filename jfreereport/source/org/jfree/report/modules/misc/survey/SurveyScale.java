/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
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
 * ----------------
 * SurveyScale.java
 * ----------------
 * (C)opyright 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SurveyScale.java,v 1.1.2.3 2004/10/13 18:42:20 taqua Exp $
 *
 * Changes
 * -------
 * 19-Mar-2004 : Version 1 (DG);
 * 
 */

package org.jfree.report.modules.misc.survey;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jfree.text.TextUtilities;
import org.jfree.ui.Drawable;
import org.jfree.ui.TextAnchor;
import org.jfree.util.BooleanList;
import org.jfree.util.BooleanUtilities;
import org.jfree.util.ShapeList;

/**
 * Draws a survey scale.  By implementing the {@link Drawable} interface, instances can be
 * displayed within a report using the {@link org.jfree.report.DrawableElement} class.
 *
 * @author David Gilbert
 */
public class SurveyScale implements Drawable
{
  private static final float SQRT2 = (float) Math.pow(2.0, 0.5);

  /**
   * The lowest response value on the scale.
   */
  private int lowest;

  /**
   * The highest response value on the scale.
   */
  private int highest;

  /**
   * The lower margin.
   */
  private double lowerMargin = 0.10;

  /**
   * The upper margin.
   */
  private double upperMargin = 0.10;

  /**
   * The shapes to display.
   */
  private ShapeList shapes;

  /**
   * The fill paint.
   */
  private Paint fillPaint;

  /**
   * The outline stroke for the shapes.
   */
  private Stroke outlineStroke;

  /**
   * A list of flags that control whether or not the shapes are filled.
   */
  private BooleanList fillShapes;

  /**
   * The values to display.
   */
  private Number[] values;

  /**
   * The lower bound of the highlighted range.
   */
  private Number rangeLowerBound;

  /**
   * The upper bound of the highlighted range.
   */
  private Number rangeUpperBound;

  /**
   * Draw a border?
   */
  private boolean drawBorder = false;

  /**
   * Draw the tick marks?
   */
  private boolean drawTickMarks;

  /**
   * The tick mark paint.
   */
  private Paint tickMarkPaint;

  /**
   * Draw the scale values.
   */
  private boolean drawScaleValues = false;

  /**
   * The font used to display the scale values.
   */
  private Font scaleValueFont;

  /**
   * The paint used to draw the scale values.
   */
  private Paint scaleValuePaint;

  /**
   * The range paint.
   */
  private Paint rangePaint;

  /**
   * The default shape, if no shape is defined in the shapeList for the given value.
   */
  private Shape defaultShape;

  /**
   * Creates a new default instance.
   */
  public SurveyScale ()
  {
    this(1, 5, null);
  }

  /**
   * Creates a new instance.
   *
   * @param lowest  the lowest response value on the scale.
   * @param highest the highest response value on the scale.
   * @param values  the values to display.
   */
  public SurveyScale (final int lowest, final int highest,
                      final Number[] values)
  {

    this.lowest = lowest;
    this.highest = highest;
    this.values = values;

    this.drawTickMarks = true;
    this.tickMarkPaint = Color.gray;

    this.scaleValueFont = new Font("Serif", Font.PLAIN + Font.ITALIC, 10);
    this.scaleValuePaint = Color.black;
    this.defaultShape = new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0);

    this.rangeLowerBound = null;
    this.rangeUpperBound = null;
    this.rangePaint = Color.lightGray;

    this.shapes = createShapeList();
    this.fillShapes = new BooleanList();
    this.fillShapes.setBoolean(0, Boolean.TRUE);
    //this.fillShapes.setBoolean(5, Boolean.TRUE);
    this.fillPaint = Color.black;
    this.outlineStroke = new BasicStroke(0.5f);

  }

  /**
   * Creates the shape list used when drawing the scale.
   * The list returned must contain exactly 6 elements.
   *
   * @return
   */
  protected ShapeList createShapeList ()
  {
    final ShapeList shapes = new ShapeList();
    //this.shapes.setShape(0, createDiagonalCross(3.0f, 0.5f));
    shapes.setShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
    shapes.setShape(1, createDownTriangle(4.0f));
    shapes.setShape(2, createUpTriangle(4.0f));
    shapes.setShape(3, createDiamond(4.0f));
    shapes.setShape(4, new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
    shapes.setShape(5, new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0));
    //this.shapes.setShape(5, createDiagonalCross(3.0f, 0.5f));
    return shapes;
  }

  /**
   * Returns the lower bound of the highlighted range.  A <code>null</code> value
   * indicates that no range is set for highlighting.
   *
   * @return The lower bound (possibly <code>null</code>).
   */
  public Number getRangeLowerBound ()
  {
    return this.rangeLowerBound;
  }

  /**
   * Sets the lower bound for the range that is highlighted on the scale.
   *
   * @param bound the lower bound (<code>null</code> permitted).
   */
  public void setRangeLowerBound (final Number bound)
  {
    this.rangeLowerBound = bound;
  }

  /**
   * Returns the upper bound of the highlighted range.  A <code>null</code> value
   * indicates that no range is set for highlighting.
   *
   * @return The upper bound (possibly <code>null</code>).
   */
  public Number getRangeUpperBound ()
  {
    return this.rangeUpperBound;
  }

  /**
   * Sets the upper bound for the range that is highlighted on the scale.
   *
   * @param bound the upper bound (<code>null</code> permitted).
   */
  public void setRangeUpperBound (final Number bound)
  {
    this.rangeUpperBound = bound;
  }

  /**
   * Returns a flag that controls whether or not a border is drawn around the scale.
   *
   * @return A boolean.
   */
  public boolean getDrawBorder ()
  {
    return this.drawBorder;
  }

  /**
   * Sets a flag that controls whether or not a border is drawn around the scale.
   *
   * @param flag the flag.
   */
  public void setDrawBorder (final boolean flag)
  {
    this.drawBorder = flag;
  }

  /**
   * Returns the flag that controls whether the tick marks are drawn.
   *
   * @return A boolean.
   */
  public boolean getDrawTickMarks ()
  {
    return this.drawTickMarks;
  }

  /**
   * Sets the flag that controls whether the tick marks are drawn.
   *
   * @param flag a boolean.
   */
  public void setDrawTickMarks (final boolean flag)
  {
    this.drawTickMarks = flag;
  }

  /**
   * Returns a flag that controls whether or not scale values are drawn.
   *
   * @return a boolean.
   */
  public boolean getDrawScaleValues ()
  {
    return this.drawScaleValues;
  }

  /**
   * Sets a flag that controls whether or not scale values are drawn.
   *
   * @param flag the flag.
   */
  public void setDrawScaleValues (final boolean flag)
  {
    this.drawScaleValues = flag;
  }

  /**
   * Returns the font used to display the scale values.
   *
   * @return A font (never <code>null</code>).
   */
  public Font getScaleValueFont ()
  {
    return this.scaleValueFont;
  }

  /**
   * Sets the font used to display the scale values.
   *
   * @param font the font (<code>null</code> not permitted).
   */
  public void setScaleValueFont (final Font font)
  {
    if (font == null)
    {
      throw new IllegalArgumentException("Null 'font' argument.");
    }
    this.scaleValueFont = font;
  }

  /**
   * Returns the color used to draw the scale values (if they are visible).
   *
   * @return A paint (never <code>null</code>).
   */
  public Paint getScaleValuePaint ()
  {
    return this.scaleValuePaint;
  }

  /**
   * Sets the color used to draw the scale values.
   *
   * @param paint the paint (<code>null</code> not permitted).
   */
  public void setScaleValuePaint (final Paint paint)
  {
    if (paint == null)
    {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
  }

  /**
   * Returns the shape used to indicate the value of a response.
   *
   * @param index the value index (zero-based).
   * @return The shape.
   */
  public Shape getShape (final int index)
  {
    return this.shapes.getShape(index);
  }

  /**
   * Sets the shape used to mark a particular value in the dataset.
   *
   * @param index the value index (zero-based).
   * @param shape the shape (<code>null</code> not permitted).
   */
  public void setShape (final int index, final Shape shape)
  {
    this.shapes.setShape(index, shape);
  }

  /**
   * Returns a flag that controls whether the shape for a particular value should be
   * filled.
   *
   * @param index the value index (zero-based).
   * @return A boolean.
   */
  public boolean isShapeFilled (final int index)
  {
    boolean result = false;
    final Boolean b = this.fillShapes.getBoolean(index);
    if (b != null)
    {
      result = b.booleanValue();
    }
    return result;
  }

  /**
   * Sets the flag that controls whether the shape for a particular value should be
   * filled.
   *
   * @param index the value index (zero-based).
   * @param fill  the flag.
   */
  public void setShapeFilled (final int index, final boolean fill)
  {
    this.fillShapes.setBoolean(index, BooleanUtilities.valueOf(fill));
  }

  /**
   * Returns the paint used to highlight the range.
   *
   * @return A {@link Paint} object (never <code>null</code>).
   */
  public Paint getRangePaint ()
  {
    return this.rangePaint;
  }

  /**
   * Sets the paint used to highlight the range (if one is specified).
   *
   * @param paint the paint (<code>null</code> not permitted).
   */
  public void setRangePaint (final Paint paint)
  {
    if (paint == null)
    {
      throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.rangePaint = paint;
  }

  /**
   * Returns the default shape, which is used, if a shape for a
   * certain value is not defined.
   *
   * @return the default shape, never null.
   */
  public Shape getDefaultShape ()
  {
    return defaultShape;
  }

  /**
   * Redefines the default shape.
   *
   * @param defaultShape the default shape
   * @throws NullPointerException if the given shape is null.
   */
  public void setDefaultShape (final Shape defaultShape)
  {
    if (defaultShape == null)
    {
      throw new NullPointerException("The default shape must not be null.");
    }
    this.defaultShape = defaultShape;
  }

  /**
   * Draws the survey scale.
   *
   * @param g2   the graphics device.
   * @param area the area.
   */
  public void draw (final Graphics2D g2, final Rectangle2D area)
  {

    final double range = this.highest - this.lowest;
    final double lowerBound = this.lowest - (range * this.lowerMargin);
    final double upperBound = this.highest + (range * this.upperMargin);

    if (this.drawBorder)
    {
      g2.setStroke(new BasicStroke(0.5f));
      g2.setPaint(Color.black);
      g2.draw(area);
    }

    if (this.rangeLowerBound != null && this.rangeUpperBound != null)
    {
      final double x0 = valueToJava2D(this.rangeLowerBound.doubleValue(), area, lowerBound, upperBound);
      final double x1 = valueToJava2D(this.rangeUpperBound.doubleValue(), area, lowerBound, upperBound);
      final Rectangle2D rangeArea = new Rectangle2D.Double(x0, area.getY(), (x1 - x0), area.getHeight());
      g2.setPaint(this.rangePaint);
      g2.fill(rangeArea);
    }

    // draw tick marks...
    if (this.drawTickMarks)
    {
      g2.setPaint(this.tickMarkPaint);
      g2.setStroke(new BasicStroke(0.1f));
      for (int i = this.lowest; i < this.highest; i++)
      {
        for (int j = 0; j < 10; j++)
        {
          final double xx = valueToJava2D(i + j / 10.0, area, lowerBound, upperBound);
          final Line2D mark = new Line2D.Double(xx, area.getCenterY() - 2.0, xx, area.getCenterY() + 2.0);
          g2.draw(mark);
        }
      }
      final double xx = valueToJava2D(this.highest, area, lowerBound, upperBound);
      final Line2D mark = new Line2D.Double(xx, area.getCenterY() - 2.0, xx, area.getCenterY() + 2.0);
      g2.draw(mark);
    }


    // draw scale values...
    if (this.drawScaleValues)
    {
      g2.setPaint(this.scaleValuePaint);
      g2.setFont(this.scaleValueFont);
      for (int i = this.lowest; i <= this.highest; i++)
      {
        final double x = valueToJava2D(i, area, lowerBound, upperBound);
        final double y = area.getCenterY();
        TextUtilities.drawAlignedString(String.valueOf(i), g2, (float) x, (float) y, TextAnchor.CENTER);
      }
    }

    // draw data values...
    if (this.values != null)
    {
      g2.setPaint(this.fillPaint);
      for (int i = 0; i < this.values.length; i++)
      {
        final Number n = this.values[i];
        if (n != null)
        {
          final double v = n.doubleValue();
          final double x = valueToJava2D(v, area, lowerBound, upperBound);
          final double y = area.getCenterY();
          Shape valueShape = this.shapes.getShape(i);
          if (valueShape == null)
          {
            valueShape = defaultShape;
          }
          final Shape s = translateShape(valueShape, x, y);
          final Boolean b = this.fillShapes.getBoolean(i);
          if (Boolean.TRUE.equals(b))
          {
            g2.fill(s);
          }
          else
          {
            g2.setStroke(this.outlineStroke);
            g2.draw(s);
          }
        }
      }
    }
  }

  /**
   * Translates a data value to Java2D coordinates.
   *
   * @param value      the value.
   * @param area       the area.
   * @param lowerBound the lower bound.
   * @param upperBound the upper bound.
   * @return The Java2D coordinate.
   */
  private double valueToJava2D (final double value,
                                final Rectangle2D area,
                                final double lowerBound,
                                final double upperBound)
  {

    return area.getMinX()
            + ((value - lowerBound) / (upperBound - lowerBound) * area.getWidth());

  }

  /**
   * Returns a translated shape.
   *
   * @param shape  the shape (<code>null</code> not permitted).
   * @param transX the x translation.
   * @param transY the y translation.
   * @return the translated shape.
   *         <p/>
   *         TODO: move this to ShapeUtilities.
   */
  public static Shape translateShape (final Shape shape, final double transX, final double transY)
  {
    if (shape == null)
    {
      throw new IllegalArgumentException("Null 'shape' argument.");
    }
    final AffineTransform transform = AffineTransform.getTranslateInstance(transX, transY);
    return transform.createTransformedShape(shape);
  }


  /**
   * Creates a diagonal cross shape.
   *
   * @param l the length of each 'arm'.
   * @param t the thickness.
   * @return A diagonal cross shape.
   *         <p/>
   *         TODO: move this to ShapeUtilities.
   */
  public static Shape createDiagonalCross (final float l, final float t)
  {
    final GeneralPath p0 = new GeneralPath();
    p0.moveTo(-l - t, -l + t);
    p0.lineTo(-l + t, -l - t);
    p0.lineTo(0.0f, -t * SQRT2);
    p0.lineTo(l - t, -l - t);
    p0.lineTo(l + t, -l + t);
    p0.lineTo(t * SQRT2, 0.0f);
    p0.lineTo(l + t, l - t);
    p0.lineTo(l - t, l + t);
    p0.lineTo(0.0f, t * SQRT2);
    p0.lineTo(-l + t, l + t);
    p0.lineTo(-l - t, l - t);
    p0.lineTo(-t * SQRT2, 0.0f);
    p0.closePath();
    return p0;
  }

  /**
   * Creates a diagonal cross shape.
   *
   * @param l the length of each 'arm'.
   * @param t the thickness.
   * @return A diagonal cross shape.
   *         <p/>
   *         TODO: move this to ShapeUtilities.
   */
  public static Shape createRegularCross (final float l, final float t)
  {
    final GeneralPath p0 = new GeneralPath();
    p0.moveTo(-l, t);
    p0.lineTo(-t, t);
    p0.lineTo(-t, l);
    p0.lineTo(t, l);
    p0.lineTo(t, t);
    p0.lineTo(l, t);
    p0.lineTo(l, -t);
    p0.lineTo(t, -t);
    p0.lineTo(t, -l);
    p0.lineTo(-t, -l);
    p0.lineTo(-t, -t);
    p0.lineTo(-l, -t);
    p0.closePath();
    return p0;
  }

  /**
   * Creates a diamond shape.
   *
   * @param s the size factor (equal to half the height of the diamond).
   * @return A diamond shape.
   *         <p/>
   *         TODO: move this to ShapeUtilities.
   */
  public static Shape createDiamond (final float s)
  {
    final GeneralPath p0 = new GeneralPath();
    p0.moveTo(0.0f, -s);
    p0.lineTo(s, 0.0f);
    p0.lineTo(0.0f, s);
    p0.lineTo(-s, 0.0f);
    p0.closePath();
    return p0;
  }

  /**
   * Creates a triangle shape that points upwards.
   *
   * @param s the size factor (equal to half the height of the triangle).
   * @return A triangle shape.
   */
  public static Shape createUpTriangle (final float s)
  {
    final GeneralPath p0 = new GeneralPath();
    p0.moveTo(0.0f, -s);
    p0.lineTo(s, s);
    p0.lineTo(-s, s);
    p0.closePath();
    return p0;
  }

  /**
   * Creates a triangle shape that points downwards.
   *
   * @param s the size factor (equal to half the height of the triangle).
   * @return A triangle shape.
   */
  public static Shape createDownTriangle (final float s)
  {
    final GeneralPath p0 = new GeneralPath();
    p0.moveTo(0.0f, s);
    p0.lineTo(s, -s);
    p0.lineTo(-s, -s);
    p0.closePath();
    return p0;
  }

}