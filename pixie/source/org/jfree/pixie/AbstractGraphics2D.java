/**
 * Date: Mar 9, 2003
 * Time: 1:33:05 PM
 *
 * $Id: AbstractGraphics2D.java,v 1.2 2003/07/03 16:13:35 taqua Exp $
 */
package org.jfree.pixie;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;

/**
 * Maps all the convinience methods to their root implementations.
 */
public abstract class AbstractGraphics2D extends Graphics2D
{
  public AbstractGraphics2D ()
  {
  }

  /**
   * Renders a <code>BufferedImage</code> that is filtered with a {@link
   * java.awt.image.BufferedImageOp}. The rendering attributes applied include the
   * <code>Clip</code>, <code>Transform</code> and <code>Composite</code> attributes.
   * This is equivalent to:
   * <pre>
   * img1 = op.filter(img, null);
   * drawImage(img1, new AffineTransform(1f,0f,0f,1f,x,y), null);
   * </pre>
   *
   * @param op  the filter to be applied to the image before rendering
   * @param img the <code>BufferedImage</code> to be rendered
   * @see #transform
   * @see #setTransform
   * @see #setComposite
   * @see #clip
   * @see #setClip
   */
  public void drawImage (final BufferedImage img,
                         final BufferedImageOp op,
                         final int x,
                         final int y)
  {
    BufferedImage result = op.createCompatibleDestImage(img, img.getColorModel());
    result = op.filter(img, result);
    drawImage(result, x, y, null);
  }

  /**
   * Renders the text of the specified <code>String</code>, using the current
   * <code>Font</code> and <code>Paint</code> attributes in the <code>Graphics2D</code>
   * context. The baseline of the first character is at position (<i>x</i>,&nbsp;<i>y</i>)
   * in the User Space. The rendering attributes applied include the <code>Clip</code>,
   * <code>Transform</code>, <code>Paint</code>, <code>Font</code> and
   * <code>Composite</code> attributes.  For characters in script systems such as Hebrew
   * and Arabic, the glyphs can be rendered from right to left, in which case the
   * coordinate supplied is the location of the leftmost character on the baseline.
   *
   * @param str the string to be rendered
   * @param x   the coordinates where the iterator's text is to be rendered
   * @param y   the coordinates where the iterator's text is to be rendered
   * @throws NullPointerException if <code>str</code> is <code>null</code>
   * @see java.awt.Graphics#drawBytes
   * @see java.awt.Graphics#drawChars
   * @since JDK1.0
   */
  public void drawString (final String str, final int x, final int y)
  {
    drawString(str, (float) x, (float) y);
  }

  /**
   * Renders the text of the specified iterator, using the <code>Graphics2D</code>
   * context's current <code>Paint</code>. The iterator has to specify a font for each
   * character. The baseline of the first character is at position
   * (<i>x</i>,&nbsp;<i>y</i>) in the User Space. The rendering attributes applied include
   * the <code>Clip</code>, <code>Transform</code>, <code>Paint</code>, and
   * <code>Composite</code> attributes. For characters in script systems such as Hebrew
   * and Arabic, the glyphs can be rendered from right to left, in which case the
   * coordinate supplied is the location of the leftmost character on the baseline.
   *
   * @param iterator the iterator whose text is to be rendered
   * @param x        the coordinates where the iterator's text is to be rendered
   * @param y        the coordinates where the iterator's text is to be rendered
   * @see #setPaint
   * @see java.awt.Graphics#setColor
   * @see #setTransform
   * @see #setComposite
   * @see #setClip
   */
  public void drawString (final AttributedCharacterIterator iterator,
                          final int x, final int y)
  {
    drawString(iterator, (float) x, (float) y);
  }

  /**
   * Renders the text of the specified iterator, using the <code>Graphics2D</code>
   * context's current <code>Paint</code>. The iterator must specify a font for each
   * character. The baseline of the first character is at position
   * (<i>x</i>,&nbsp;<i>y</i>) in the User Space. The rendering attributes applied include
   * the <code>Clip</code>, <code>Transform</code>, <code>Paint</code>, and
   * <code>Composite</code> attributes. For characters in script systems such as Hebrew
   * and Arabic, the glyphs can be rendered from right to left, in which case the
   * coordinate supplied is the location of the leftmost character on the baseline.
   *
   * @param iterator the iterator whose text is to be rendered
   * @param x        the coordinates where the iterator's text is to be rendered
   * @param y        the coordinates where the iterator's text is to be rendered
   * @see #setPaint
   * @see java.awt.Graphics#setColor
   * @see #setTransform
   * @see #setComposite
   * @see #setClip
   */
  public void drawString (final AttributedCharacterIterator iterator,
                          final float x, final float y)
  {
    final StringBuffer sb = new StringBuffer();
    char c = iterator.first();
    while (c != AttributedCharacterIterator.DONE)
    {
      sb.append(c);
      c = iterator.next();
    }
    drawString(sb.toString(), x, y);
  }

  /**
   * Renders the text of the specified {@link java.awt.font.GlyphVector} using the
   * <code>Graphics2D</code> context's rendering attributes. The rendering attributes
   * applied include the <code>Clip</code>, <code>Transform</code>, <code>Paint</code>,
   * and <code>Composite</code> attributes.  The <code>GlyphVector</code> specifies
   * individual glyphs from a {@link java.awt.Font}. The <code>GlyphVector</code> can also
   * contain the glyph positions. This is the fastest way to render a set of characters to
   * the screen.
   *
   * @param g the <code>GlyphVector</code> to be rendered
   * @param x the position in User Space where the glyphs should be rendered
   * @param y the position in User Space where the glyphs should be rendered
   * @see java.awt.font.GlyphVector
   * @see #setPaint
   * @see java.awt.Graphics#setColor
   * @see #setTransform
   * @see #setComposite
   * @see #setClip
   */
  public void drawGlyphVector (final GlyphVector g, final float x, final float y)
  {
    fill(g.getOutline(x, y));
  }

  /**
   * Checks whether or not the specified <code>Shape</code> intersects the specified
   * {@link java.awt.Rectangle}, which is in device space. If <code>onStroke</code> is
   * false, this method checks whether or not the interior of the specified
   * <code>Shape</code> intersects the specified <code>Rectangle</code>.  If
   * <code>onStroke</code> is <code>true</code>, this method checks whether or not the
   * <code>Stroke</code> of the specified <code>Shape</code> outline intersects the
   * specified <code>Rectangle</code>. The rendering attributes taken into account include
   * the <code>Clip</code>, <code>Transform</code>, and <code>Stroke</code> attributes.
   *
   * @param rect     the area in device space to check for a hit
   * @param s        the <code>Shape</code> to check for a hit
   * @param onStroke flag used to choose between testing the stroked or the filled shape.
   *                 If the flag is <code>true</code>, the <code>Stroke</code> oultine is
   *                 tested.  If the flag is <code>false</code>, the filled
   *                 <code>Shape</code> is tested.
   * @return <code>true</code> if there is a hit; <code>false</code> otherwise.
   *
   * @see #setStroke
   * @see #fill
   * @see #draw
   * @see #transform
   * @see #setTransform
   * @see #clip
   * @see #setClip
   */
  public boolean hit (final Rectangle rect,
                      Shape s,
                      final boolean onStroke)
  {
    if (onStroke)
    {
      s = getStroke().createStrokedShape(s);
    }
    s = getTransform().createTransformedShape(s);
    final Area area = new Area(s);
    if (getClip() != null)
    {
      area.intersect(new Area(getClip()));
    }
    return area.intersects(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * Translates the origin of the <code>Graphics2D</code> context to the point
   * (<i>x</i>,&nbsp;<i>y</i>) in the current coordinate system. Modifies the
   * <code>Graphics2D</code> context so that its new origin corresponds to the point
   * (<i>x</i>,&nbsp;<i>y</i>) in the <code>Graphics2D</code> context's former coordinate
   * system.  All coordinates used in subsequent rendering operations on this graphics
   * context are relative to this new origin.
   *
   * @since JDK1.0
   */
  public void translate (final int x, final int y)
  {
    translate((double) x, (double) y);
  }

  /**
   * Concatenates the current <code>Graphics2D</code> <code>Transform</code> with a
   * translation transform. Subsequent rendering is translated by the specified distance
   * relative to the previous position. This is equivalent to calling transform(T), where
   * T is an <code>AffineTransform</code> represented by the following matrix:
   * <pre>
   * 		[   1    0    tx  ]
   * 		[   0    1    ty  ]
   * 		[   0    0    1   ]
   * </pre>
   *
   * @param tx the distance to translate along the x-axis
   * @param ty the distance to translate along the y-axis
   */
  public void translate (final double tx, final double ty)
  {
    transform(AffineTransform.getTranslateInstance(tx, ty));
  }

  /**
   * Concatenates the current <code>Graphics2D</code> <code>Transform</code> with a
   * rotation transform. Subsequent rendering is rotated by the specified radians relative
   * to the previous origin. This is equivalent to calling <code>transform(R)</code>,
   * where R is an <code>AffineTransform</code> represented by the following matrix:
   * <pre>
   * 		[   cos(theta)    -sin(theta)    0   ]
   * 		[   sin(theta)     cos(theta)    0   ]
   * 		[       0              0         1   ]
   * </pre>
   * Rotating with a positive angle theta rotates points on the positive x axis toward the
   * positive y axis.
   *
   * @param theta the angle of rotation in radians
   */
  public void rotate (final double theta)
  {
    transform(AffineTransform.getRotateInstance(theta));
  }

  /**
   * Concatenates the current <code>Graphics2D</code> <code>Transform</code> with a
   * translated rotation transform.  Subsequent rendering is transformed by a transform
   * which is constructed by translating to the specified location, rotating by the
   * specified radians, and translating back by the same amount as the original
   * translation.  This is equivalent to the following sequence of calls:
   * <pre>
   * 		translate(x, y);
   * 		rotate(theta);
   * 		translate(-x, -y);
   * </pre>
   * Rotating with a positive angle theta rotates points on the positive x axis toward the
   * positive y axis.
   *
   * @param theta the angle of rotation in radians
   */
  public void rotate (final double theta, final double x, final double y)
  {
    transform(AffineTransform.getRotateInstance(theta, x, y));
  }

  /**
   * Concatenates the current <code>Graphics2D</code> <code>Transform</code> with a
   * scaling transformation Subsequent rendering is resized according to the specified
   * scaling factors relative to the previous scaling. This is equivalent to calling
   * <code>transform(S)</code>, where S is an <code>AffineTransform</code> represented by
   * the following matrix:
   * <pre>
   * 		[   sx   0    0   ]
   * 		[   0    sy   0   ]
   * 		[   0    0    1   ]
   * </pre>
   *
   * @param sx the amount by which X coordinates in subsequent rendering operations are
   *           multiplied relative to previous rendering operations.
   * @param sy the amount by which Y coordinates in subsequent rendering operations are
   *           multiplied relative to previous rendering operations.
   */
  public void scale (final double sx, final double sy)
  {
    transform(AffineTransform.getScaleInstance(sx, sy));
  }

  /**
   * Concatenates the current <code>Graphics2D</code> <code>Transform</code> with a
   * shearing transform. Subsequent renderings are sheared by the specified multiplier
   * relative to the previous position. This is equivalent to calling
   * <code>transform(SH)</code>, where SH is an <code>AffineTransform</code> represented
   * by the following matrix:
   * <pre>
   * 		[   1   shx   0   ]
   * 		[  shy   1    0   ]
   * 		[   0    0    1   ]
   * </pre>
   *
   * @param shx the multiplier by which coordinates are shifted in the positive X axis
   *            direction as a function of their Y coordinate
   * @param shy the multiplier by which coordinates are shifted in the positive Y axis
   *            direction as a function of their X coordinate
   */
  public void shear (final double shx, final double shy)
  {
    transform(AffineTransform.getShearInstance(shx, shy));
  }

  /**
   * Composes an <code>AffineTransform</code> object with the <code>Transform</code> in
   * this <code>Graphics2D</code> according to the rule last-specified-first-applied.  If
   * the current <code>Transform</code> is Cx, the result of composition with Tx is a new
   * <code>Transform</code> Cx'.  Cx' becomes the current <code>Transform</code> for this
   * <code>Graphics2D</code>. Transforming a point p by the updated <code>Transform</code>
   * Cx' is equivalent to first transforming p by Tx and then transforming the result by
   * the original <code>Transform</code> Cx.  In other words, Cx'(p) = Cx(Tx(p)).  A copy
   * of the Tx is made, if necessary, so further modifications to Tx do not affect
   * rendering.
   *
   * @param Tx the <code>AffineTransform</code> object to be composed with the current
   *           <code>Transform</code>
   * @see #setTransform
   * @see java.awt.geom.AffineTransform
   */
  public void transform (final AffineTransform Tx)
  {
    final AffineTransform transform = new AffineTransform(getTransform());
    transform.concatenate(Tx);
    setTransform(transform);
  }

  /**
   * Gets this graphics context's current color.
   *
   * @return this graphics context's current color.
   *
   * @see java.awt.Color
   * @see java.awt.Graphics#setColor
   */
  public Color getColor ()
  {
    final Paint p = getPaint();
    if (p instanceof Color)
    {
      return (Color) p;
    }

    return null;
  }

  /**
   * Sets this graphics context's current color to the specified color. All subsequent
   * graphics operations using this graphics context use this specified color.
   *
   * @param c the new rendering color.
   * @see java.awt.Color
   * @see java.awt.Graphics#getColor
   */
  public void setColor (final Color c)
  {
    setPaint(c);
  }

  /**
   * Returns the bounding rectangle of the current clipping area. This method refers to
   * the user clip, which is independent of the clipping associated with device bounds and
   * window visibility. If no clip has previously been set, or if the clip has been
   * cleared using <code>setClip(null)</code>, this method returns <code>null</code>. The
   * coordinates in the rectangle are relative to the coordinate system origin of this
   * graphics context.
   *
   * @return the bounding rectangle of the current clipping area, or <code>null</code> if
   *         no clip is set.
   *
   * @see java.awt.Graphics#getClip
   * @see java.awt.Graphics#clipRect
   * @see java.awt.Graphics#setClip(int, int, int, int)
   * @see java.awt.Graphics#setClip(Shape)
   * @since JDK1.1
   */
  public Rectangle getClipBounds ()
  {
    if (getClip() == null)
    {
      return null;
    }
    return getClip().getBounds();
  }

  /**
   * Intersects the current clip with the specified rectangle. The resulting clipping area
   * is the intersection of the current clipping area and the specified rectangle.  If
   * there is no current clipping area, either because the clip has never been set, or the
   * clip has been cleared using <code>setClip(null)</code>, the specified rectangle
   * becomes the new clip. This method sets the user clip, which is independent of the
   * clipping associated with device bounds and window visibility. This method can only be
   * used to make the current clip smaller. To set the current clip larger, use any of the
   * setClip methods. Rendering operations have no effect outside of the clipping area.
   *
   * @param x      the x coordinate of the rectangle to intersect the clip with
   * @param y      the y coordinate of the rectangle to intersect the clip with
   * @param width  the width of the rectangle to intersect the clip with
   * @param height the height of the rectangle to intersect the clip with
   * @see #setClip(int, int, int, int)
   * @see #setClip(Shape)
   */
  public void clipRect (final int x, final int y, final int width, final int height)
  {
    clip(new Rectangle2D.Float(x, y, width, height));
  }

  /**
   * Sets the current clip to the rectangle specified by the given coordinates.  This
   * method sets the user clip, which is independent of the clipping associated with
   * device bounds and window visibility. Rendering operations have no effect outside of
   * the clipping area.
   *
   * @param x      the <i>x</i> coordinate of the new clip rectangle.
   * @param y      the <i>y</i> coordinate of the new clip rectangle.
   * @param width  the width of the new clip rectangle.
   * @param height the height of the new clip rectangle.
   * @see java.awt.Graphics#clipRect
   * @see java.awt.Graphics#setClip(Shape)
   * @since JDK1.1
   */
  public void setClip (final int x, final int y, final int width, final int height)
  {
    setClip(new Rectangle2D.Float(x, y, width, height));
  }

  /**
   * Draws a line, using the current color, between the points <code>(x1,&nbsp;y1)</code>
   * and <code>(x2,&nbsp;y2)</code> in this graphics context's coordinate system.
   *
   * @param x1 the first point's <i>x</i> coordinate.
   * @param y1 the first point's <i>y</i> coordinate.
   * @param x2 the second point's <i>x</i> coordinate.
   * @param y2 the second point's <i>y</i> coordinate.
   */
  public void drawLine (final int x1, final int y1, final int x2, final int y2)
  {
    draw(new Line2D.Float(x1, y1, x2, y2));
  }

  /**
   * Fills the specified rectangle. The left and right edges of the rectangle are at
   * <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>. The top and bottom
   * edges are at <code>y</code> and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>. The
   * resulting rectangle covers an area <code>width</code> pixels wide by
   * <code>height</code> pixels tall. The rectangle is filled using the graphics context's
   * current color.
   *
   * @param x      the <i>x</i> coordinate of the rectangle to be filled.
   * @param y      the <i>y</i> coordinate of the rectangle to be filled.
   * @param width  the width of the rectangle to be filled.
   * @param height the height of the rectangle to be filled.
   * @see java.awt.Graphics#clearRect
   * @see java.awt.Graphics#drawRect
   */
  public void fillRect (final int x, final int y, final int width, final int height)
  {
    fill(new Rectangle2D.Float(x, y, width, height));
  }

  /**
   * Clears the specified rectangle by filling it with the background color of the current
   * drawing surface. This operation does not use the current paint mode.
   * <p/>
   * Beginning with Java&nbsp;1.1, the background color of offscreen images may be system
   * dependent. Applications should use <code>setColor</code> followed by
   * <code>fillRect</code> to ensure that an offscreen image is cleared to a specific
   * color.
   *
   * @param x      the <i>x</i> coordinate of the rectangle to clear.
   * @param y      the <i>y</i> coordinate of the rectangle to clear.
   * @param width  the width of the rectangle to clear.
   * @param height the height of the rectangle to clear.
   * @see java.awt.Graphics#fillRect(int, int, int, int)
   * @see java.awt.Graphics#drawRect
   * @see java.awt.Graphics#setColor(Color)
   * @see java.awt.Graphics#setPaintMode
   * @see java.awt.Graphics#setXORMode(Color)
   */
  public void clearRect (final int x, final int y, final int width, final int height)
  {
    final Paint oldPaint = getPaint();
    setPaint(getBackground());
    fillRect(x, y, width, height);
    setPaint(oldPaint);
  }

  /**
   * Draws an outlined round-cornered rectangle using this graphics context's current
   * color. The left and right edges of the rectangle are at <code>x</code> and
   * <code>x&nbsp;+&nbsp;width</code>, respectively. The top and bottom edges of the
   * rectangle are at <code>y</code> and <code>y&nbsp;+&nbsp;height</code>.
   *
   * @param x         the <i>x</i> coordinate of the rectangle to be drawn.
   * @param y         the <i>y</i> coordinate of the rectangle to be drawn.
   * @param width     the width of the rectangle to be drawn.
   * @param height    the height of the rectangle to be drawn.
   * @param arcWidth  the horizontal diameter of the arc at the four corners.
   * @param arcHeight the vertical diameter of the arc at the four corners.
   * @see java.awt.Graphics#fillRoundRect
   */
  public void drawRoundRect (final int x, final int y, final int width, final int height,
                             final int arcWidth, final int arcHeight)
  {
    draw(new RoundRectangle2D.Float(x, y, width, height, arcWidth, arcHeight));
  }

  /**
   * Fills the specified rounded corner rectangle with the current color. The left and
   * right edges of the rectangle are at <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>,
   * respectively. The top and bottom edges of the rectangle are at <code>y</code> and
   * <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
   *
   * @param x         the <i>x</i> coordinate of the rectangle to be filled.
   * @param y         the <i>y</i> coordinate of the rectangle to be filled.
   * @param width     the width of the rectangle to be filled.
   * @param height    the height of the rectangle to be filled.
   * @param arcWidth  the horizontal diameter of the arc at the four corners.
   * @param arcHeight the vertical diameter of the arc at the four corners.
   * @see java.awt.Graphics#drawRoundRect
   */
  public void fillRoundRect (final int x, final int y, final int width, final int height,
                             final int arcWidth, final int arcHeight)
  {
    fill(new RoundRectangle2D.Float(x, y, width, height, arcWidth, arcHeight));
  }

  /**
   * Draws the outline of an oval. The result is a circle or ellipse that fits within the
   * rectangle specified by the <code>x</code>, <code>y</code>, <code>width</code>, and
   * <code>height</code> arguments.
   * <p/>
   * The oval covers an area that is <code>width&nbsp;+&nbsp;1</code> pixels wide and
   * <code>height&nbsp;+&nbsp;1</code> pixels tall.
   *
   * @param x      the <i>x</i> coordinate of the upper left corner of the oval to be
   *               drawn.
   * @param y      the <i>y</i> coordinate of the upper left corner of the oval to be
   *               drawn.
   * @param width  the width of the oval to be drawn.
   * @param height the height of the oval to be drawn.
   * @see java.awt.Graphics#fillOval
   */
  public void drawOval (final int x, final int y, final int width, final int height)
  {
    draw(new Ellipse2D.Float(x, y, width, height));
  }

  /**
   * Fills an oval bounded by the specified rectangle with the current color.
   *
   * @param x      the <i>x</i> coordinate of the upper left corner of the oval to be
   *               filled.
   * @param y      the <i>y</i> coordinate of the upper left corner of the oval to be
   *               filled.
   * @param width  the width of the oval to be filled.
   * @param height the height of the oval to be filled.
   * @see java.awt.Graphics#drawOval
   */
  public void fillOval (final int x, final int y, final int width, final int height)
  {
    fill(new Ellipse2D.Float(x, y, width, height));
  }

  /**
   * Draws the outline of a circular or elliptical arc covering the specified rectangle.
   * <p/>
   * The resulting arc begins at <code>startAngle</code> and extends for
   * <code>arcAngle</code> degrees, using the current color. Angles are interpreted such
   * that 0&nbsp;degrees is at the 3&nbsp;o'clock position. A positive value indicates a
   * counter-clockwise rotation while a negative value indicates a clockwise rotation.
   * <p/>
   * The center of the arc is the center of the rectangle whose origin is
   * (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the <code>width</code> and
   * <code>height</code> arguments.
   * <p/>
   * The resulting arc covers an area <code>width&nbsp;+&nbsp;1</code> pixels wide by
   * <code>height&nbsp;+&nbsp;1</code> pixels tall.
   * <p/>
   * The angles are specified relative to the non-square extents of the bounding rectangle
   * such that 45 degrees always falls on the line from the center of the ellipse to the
   * upper right corner of the bounding rectangle. As a result, if the bounding rectangle
   * is noticeably longer in one axis than the other, the angles to the start and end of
   * the arc segment will be skewed farther along the longer axis of the bounds.
   *
   * @param x          the <i>x</i> coordinate of the upper-left corner of the arc to be
   *                   drawn.
   * @param y          the <i>y</i>  coordinate of the upper-left corner of the arc to be
   *                   drawn.
   * @param width      the width of the arc to be drawn.
   * @param height     the height of the arc to be drawn.
   * @param startAngle the beginning angle.
   * @param arcAngle   the angular extent of the arc, relative to the start angle.
   * @see java.awt.Graphics#fillArc
   */
  public void drawArc (final int x, final int y, final int width, final int height,
                       final int startAngle, final int arcAngle)
  {
    draw(new Arc2D.Float(x, y, width, height, startAngle, arcAngle, Arc2D.OPEN));
  }

  /**
   * Fills a circular or elliptical arc covering the specified rectangle.
   * <p/>
   * The resulting arc begins at <code>startAngle</code> and extends for
   * <code>arcAngle</code> degrees. Angles are interpreted such that 0&nbsp;degrees is at
   * the 3&nbsp;o'clock position. A positive value indicates a counter-clockwise rotation
   * while a negative value indicates a clockwise rotation.
   * <p/>
   * The center of the arc is the center of the rectangle whose origin is
   * (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the <code>width</code> and
   * <code>height</code> arguments.
   * <p/>
   * The resulting arc covers an area <code>width&nbsp;+&nbsp;1</code> pixels wide by
   * <code>height&nbsp;+&nbsp;1</code> pixels tall.
   * <p/>
   * The angles are specified relative to the non-square extents of the bounding rectangle
   * such that 45 degrees always falls on the line from the center of the ellipse to the
   * upper right corner of the bounding rectangle. As a result, if the bounding rectangle
   * is noticeably longer in one axis than the other, the angles to the start and end of
   * the arc segment will be skewed farther along the longer axis of the bounds.
   *
   * @param x          the <i>x</i> coordinate of the upper-left corner of the arc to be
   *                   filled.
   * @param y          the <i>y</i>  coordinate of the upper-left corner of the arc to be
   *                   filled.
   * @param width      the width of the arc to be filled.
   * @param height     the height of the arc to be filled.
   * @param startAngle the beginning angle.
   * @param arcAngle   the angular extent of the arc, relative to the start angle.
   * @see java.awt.Graphics#drawArc
   */
  public void fillArc (final int x, final int y, final int width, final int height,
                       final int startAngle, final int arcAngle)
  {
    fill(new Arc2D.Float(x, y, width, height, startAngle, arcAngle, Arc2D.OPEN));
  }

  /**
   * Draws a sequence of connected lines defined by arrays of <i>x</i> and <i>y</i>
   * coordinates. Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a point. The
   * figure is not closed if the first point differs from the last point.
   *
   * @param xPoints an array of <i>x</i> points
   * @param yPoints an array of <i>y</i> points
   * @param nPoints the total number of points
   * @see java.awt.Graphics#drawPolygon(int[], int[], int)
   * @since JDK1.1
   */
  public void drawPolyline (final int[] xPoints, final int[] yPoints,
                            final int nPoints)
  {
    final Line2D line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[0], yPoints[0]);
    for (int i = 1; i < nPoints; i++)
    {
      line.setLine(line.getX2(), line.getY2(), xPoints[i], yPoints[i]);
      draw(line);
    }
  }

  /**
   * Draws a closed polygon defined by arrays of <i>x</i> and <i>y</i> coordinates. Each
   * pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a point.
   * <p/>
   * This method draws the polygon defined by <code>nPoint</code> line segments, where the
   * first <code>nPoint&nbsp;-&nbsp;1</code> line segments are line segments from
   * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code> to
   * <code>(xPoints[i],&nbsp;yPoints[i])</code>, for 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;<code>nPoints</code>.
   * The figure is automatically closed by drawing a line connecting the final point to
   * the first point, if those points are different.
   *
   * @param xPoints a an array of <code>x</code> coordinates.
   * @param yPoints a an array of <code>y</code> coordinates.
   * @param nPoints a the total number of points.
   * @see java.awt.Graphics#fillPolygon
   * @see java.awt.Graphics#drawPolyline
   */
  public void drawPolygon (final int[] xPoints, final int[] yPoints,
                           final int nPoints)
  {
    final Polygon poly = new Polygon();
    for (int i = 0; i < nPoints; i++)
    {
      poly.addPoint(xPoints[i], yPoints[i]);
    }
    draw(poly);
  }

  /**
   * Fills a closed polygon defined by arrays of <i>x</i> and <i>y</i> coordinates.
   * <p/>
   * This method draws the polygon defined by <code>nPoint</code> line segments, where the
   * first <code>nPoint&nbsp;-&nbsp;1</code> line segments are line segments from
   * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code> to
   * <code>(xPoints[i],&nbsp;yPoints[i])</code>, for 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;<code>nPoints</code>.
   * The figure is automatically closed by drawing a line connecting the final point to
   * the first point, if those points are different.
   * <p/>
   * The area inside the polygon is defined using an even-odd fill rule, also known as the
   * alternating rule.
   *
   * @param xPoints a an array of <code>x</code> coordinates.
   * @param yPoints a an array of <code>y</code> coordinates.
   * @param nPoints a the total number of points.
   * @see java.awt.Graphics#drawPolygon(int[], int[], int)
   */
  public void fillPolygon (final int[] xPoints, final int[] yPoints,
                           final int nPoints)
  {
    final Polygon poly = new Polygon();
    for (int i = 0; i < nPoints; i++)
    {
      poly.addPoint(xPoints[i], yPoints[i]);
    }
    fill(poly);
  }

  /**
   * Draws as much of the specified image as is currently available. The image is drawn
   * with its top-left corner at (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's
   * coordinate space. Transparent pixels in the image do not affect whatever pixels are
   * already there.
   * <p/>
   * This method returns immediately in all cases, even if the complete image has not yet
   * been loaded, and it has not been dithered and converted for the current output
   * device.
   * <p/>
   * If the image has not yet been completely loaded, then <code>drawImage</code> returns
   * <code>false</code>. As more of the image becomes available, the process that draws
   * the image notifies the specified image observer.
   *
   * @param img      the specified image to be drawn.
   * @param x        the <i>x</i> coordinate.
   * @param y        the <i>y</i> coordinate.
   * @param observer object to be notified as more of the image is converted.
   * @see java.awt.Image
   * @see java.awt.image.ImageObserver
   * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int,
          *      int)
   */
  public boolean drawImage (final Image img, final int x, final int y,
                            final ImageObserver observer)
  {
    return drawImage(img, x, y, null, observer);
  }

  /**
   * Draws as much of the specified image as has already been scaled to fit inside the
   * specified rectangle.
   * <p/>
   * The image is drawn inside the specified rectangle of this graphics context's
   * coordinate space, and is scaled if necessary. Transparent pixels do not affect
   * whatever pixels are already there.
   * <p/>
   * This method returns immediately in all cases, even if the entire image has not yet
   * been scaled, dithered, and converted for the current output device. If the current
   * output representation is not yet complete, then <code>drawImage</code> returns
   * <code>false</code>. As more of the image becomes available, the process that draws
   * the image notifies the image observer by calling its <code>imageUpdate</code>
   * method.
   * <p/>
   * A scaled version of an image will not necessarily be available immediately just
   * because an unscaled version of the image has been constructed for this output device.
   *  Each size of the image may be cached separately and generated from the original data
   * in a separate image production sequence.
   *
   * @param img      the specified image to be drawn.
   * @param x        the <i>x</i> coordinate.
   * @param y        the <i>y</i> coordinate.
   * @param width    the width of the rectangle.
   * @param height   the height of the rectangle.
   * @param observer object to be notified as more of the image is converted.
   * @see java.awt.Image
   * @see java.awt.image.ImageObserver
   * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int,
          *      int)
   */
  public boolean drawImage (final Image img, final int x, final int y,
                            final int width, final int height,
                            final ImageObserver observer)
  {
    return drawImage(img, x, y, width, height, null, observer);
  }

  /**
   * Draws as much of the specified image as is currently available. The image is drawn
   * with its top-left corner at (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's
   * coordinate space.  Transparent pixels are drawn in the specified background color.
   * <p/>
   * This operation is equivalent to filling a rectangle of the width and height of the
   * specified image with the given color and then drawing the image on top of it, but
   * possibly more efficient.
   * <p/>
   * This method returns immediately in all cases, even if the complete image has not yet
   * been loaded, and it has not been dithered and converted for the current output
   * device.
   * <p/>
   * If the image has not yet been completely loaded, then <code>drawImage</code> returns
   * <code>false</code>. As more of the image becomes available, the process that draws
   * the image notifies the specified image observer.
   *
   * @param img      the specified image to be drawn.
   * @param x        the <i>x</i> coordinate.
   * @param y        the <i>y</i> coordinate.
   * @param bgcolor  the background color to paint under the non-opaque portions of the
   *                 image.
   * @param observer object to be notified as more of the image is converted.
   * @see java.awt.Image
   * @see java.awt.image.ImageObserver
   * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int,
          *      int)
   */
  public boolean drawImage (final Image img, final int x, final int y,
                            final Color bgcolor,
                            final ImageObserver observer)
  {
    return drawImage(img, x, y, img.getWidth(observer), img.getHeight(observer), bgcolor, observer);
  }

  /**
   * Draws as much of the specified image as has already been scaled to fit inside the
   * specified rectangle.
   * <p/>
   * The image is drawn inside the specified rectangle of this graphics context's
   * coordinate space, and is scaled if necessary. Transparent pixels are drawn in the
   * specified background color. This operation is equivalent to filling a rectangle of
   * the width and height of the specified image with the given color and then drawing the
   * image on top of it, but possibly more efficient.
   * <p/>
   * This method returns immediately in all cases, even if the entire image has not yet
   * been scaled, dithered, and converted for the current output device. If the current
   * output representation is not yet complete then <code>drawImage</code> returns
   * <code>false</code>. As more of the image becomes available, the process that draws
   * the image notifies the specified image observer.
   * <p/>
   * A scaled version of an image will not necessarily be available immediately just
   * because an unscaled version of the image has been constructed for this output device.
   *  Each size of the image may be cached separately and generated from the original data
   * in a separate image production sequence.
   *
   * @param img      the specified image to be drawn.
   * @param x        the <i>x</i> coordinate.
   * @param y        the <i>y</i> coordinate.
   * @param width    the width of the rectangle.
   * @param height   the height of the rectangle.
   * @param bgcolor  the background color to paint under the non-opaque portions of the
   *                 image.
   * @param observer object to be notified as more of the image is converted.
   * @see java.awt.Image
   * @see java.awt.image.ImageObserver
   * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int,
          *      int)
   */
  public boolean drawImage (final Image img, final int x, final int y,
                            final int width, final int height,
                            final Color bgcolor,
                            final ImageObserver observer)
  {
    final double scalex = width / (double) img.getWidth(observer);
    final double scaley = height / (double) img.getHeight(observer);
    final AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
    tx.scale(scalex, scaley);

    // draw the background ...
    final Paint bgPaint = getPaint();
    setPaint(bgcolor);
    final Rectangle2D bg = new Rectangle2D.Float(x, y, width, height);
    fill(bg);
    // restore the orinal paint ...
    setPaint(bgPaint);
    // finally paint the image ...
    return drawImage(img, tx, observer);
  }

  /**
   * Draws as much of the specified area of the specified image as is currently available,
   * scaling it on the fly to fit inside the specified area of the destination drawable
   * surface. Transparent pixels do not affect whatever pixels are already there.
   * <p/>
   * This method returns immediately in all cases, even if the image area to be drawn has
   * not yet been scaled, dithered, and converted for the current output device. If the
   * current output representation is not yet complete then <code>drawImage</code> returns
   * <code>false</code>. As more of the image becomes available, the process that draws
   * the image notifies the specified image observer.
   * <p/>
   * This method always uses the unscaled version of the image to render the scaled
   * rectangle and performs the required scaling on the fly. It does not use a cached,
   * scaled version of the image for this operation. Scaling of the image from source to
   * destination is performed such that the first coordinate of the source rectangle is
   * mapped to the first coordinate of the destination rectangle, and the second source
   * coordinate is mapped to the second destination coordinate. The subimage is scaled and
   * flipped as needed to preserve those mappings.
   *
   * @param img      the specified image to be drawn
   * @param dx1      the <i>x</i> coordinate of the first corner of the destination
   *                 rectangle.
   * @param dy1      the <i>y</i> coordinate of the first corner of the destination
   *                 rectangle.
   * @param dx2      the <i>x</i> coordinate of the second corner of the destination
   *                 rectangle.
   * @param dy2      the <i>y</i> coordinate of the second corner of the destination
   *                 rectangle.
   * @param sx1      the <i>x</i> coordinate of the first corner of the source rectangle.
   * @param sy1      the <i>y</i> coordinate of the first corner of the source rectangle.
   * @param sx2      the <i>x</i> coordinate of the second corner of the source
   *                 rectangle.
   * @param sy2      the <i>y</i> coordinate of the second corner of the source
   *                 rectangle.
   * @param observer object to be notified as more of the image is scaled and converted.
   * @see java.awt.Image
   * @see java.awt.image.ImageObserver
   * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int,
          *      int)
   * @since JDK1.1
   */
  public boolean drawImage (final Image img,
                            final int dx1, final int dy1, final int dx2, final int dy2,
                            final int sx1, final int sy1, final int sx2, final int sy2,
                            final ImageObserver observer)
  {
    return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, observer);
  }

  /**
   * Draws as much of the specified area of the specified image as is currently available,
   * scaling it on the fly to fit inside the specified area of the destination drawable
   * surface.
   * <p/>
   * Transparent pixels are drawn in the specified background color. This operation is
   * equivalent to filling a rectangle of the width and height of the specified image with
   * the given color and then drawing the image on top of it, but possibly more
   * efficient.
   * <p/>
   * This method returns immediately in all cases, even if the image area to be drawn has
   * not yet been scaled, dithered, and converted for the current output device. If the
   * current output representation is not yet complete then <code>drawImage</code> returns
   * <code>false</code>. As more of the image becomes available, the process that draws
   * the image notifies the specified image observer.
   * <p/>
   * This method always uses the unscaled version of the image to render the scaled
   * rectangle and performs the required scaling on the fly. It does not use a cached,
   * scaled version of the image for this operation. Scaling of the image from source to
   * destination is performed such that the first coordinate of the source rectangle is
   * mapped to the first coordinate of the destination rectangle, and the second source
   * coordinate is mapped to the second destination coordinate. The subimage is scaled and
   * flipped as needed to preserve those mappings.
   *
   * @param img      the specified image to be drawn
   * @param dx1      the <i>x</i> coordinate of the first corner of the destination
   *                 rectangle.
   * @param dy1      the <i>y</i> coordinate of the first corner of the destination
   *                 rectangle.
   * @param dx2      the <i>x</i> coordinate of the second corner of the destination
   *                 rectangle.
   * @param dy2      the <i>y</i> coordinate of the second corner of the destination
   *                 rectangle.
   * @param sx1      the <i>x</i> coordinate of the first corner of the source rectangle.
   * @param sy1      the <i>y</i> coordinate of the first corner of the source rectangle.
   * @param sx2      the <i>x</i> coordinate of the second corner of the source
   *                 rectangle.
   * @param sy2      the <i>y</i> coordinate of the second corner of the source
   *                 rectangle.
   * @param bgcolor  the background color to paint under the non-opaque portions of the
   *                 image.
   * @param observer object to be notified as more of the image is scaled and converted.
   * @see java.awt.Image
   * @see java.awt.image.ImageObserver
   * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int,
          *      int)
   * @since JDK1.1
   */
  public boolean drawImage (final Image img,
                            final int dx1, final int dy1, final int dx2, final int dy2,
                            final int sx1, final int sy1, final int sx2, final int sy2,
                            final Color bgcolor,
                            final ImageObserver observer)
  {
    final double dwidth = (double) dx2 - dx1;
    final double dheight = (double) dy2 - dy1;
    final double swidth = (double) sx2 - sx1;
    final double sheight = (double) sy2 - sy1;

    //if either width or height is 0, then there is nothing to draw
    if (dwidth == 0 || dheight == 0 || swidth == 0 || sheight == 0)
    {
      return true;
    }

    final double scalex = dwidth / swidth;
    final double scaley = dheight / sheight;

    final double transx = sx1 * scalex;
    final double transy = sy1 * scaley;
    final AffineTransform tx = AffineTransform.getTranslateInstance(dx1 - transx, dy1 - transy);
    tx.scale(scalex, scaley);

    // draw the background ...
    final Paint bgPaint = getPaint();
    setPaint(bgcolor);
    final Rectangle2D bg = new Rectangle2D.Double(dx1, dy1, dwidth, dheight);
    fill(bg);
    // restore the orinal paint ...
    setPaint(bgPaint);
    // finally paint the image ...

    drawImage(img, tx, observer);
    return true;
  }

  /**
   * Renders a {@link java.awt.image.renderable.RenderableImage}, applying a transform
   * from image space into user space before drawing. The transformation from user space
   * into device space is done with the current <code>Transform</code> in the
   * <code>Graphics2D</code>. The specified transformation is applied to the image before
   * the transform attribute in the <code>Graphics2D</code> context is applied. The
   * rendering attributes applied include the <code>Clip</code>, <code>Transform</code>,
   * and <code>Composite</code> attributes. Note that no rendering is done if the
   * specified transform is noninvertible.
   * <p/>
   * Rendering hints set on the <code>Graphics2D</code> object might be used in rendering
   * the <code>RenderableImage</code>. If explicit control is required over specific hints
   * recognized by a specific <code>RenderableImage</code>, or if knowledge of which hints
   * are used is required, then a <code>RenderedImage</code> should be obtained directly
   * from the <code>RenderableImage</code> and rendered using {@link
   * #drawRenderedImage(java.awt.image.RenderedImage, AffineTransform)
   * drawRenderedImage}.
   *
   * @param img   the image to be rendered
   * @param xform the transformation from image space into user space
   * @see #transform
   * @see #setTransform
   * @see #setComposite
   * @see #clip
   * @see #setClip
   * @see #drawRenderedImage
   */
  public void drawRenderableImage (final RenderableImage img,
                                   final AffineTransform xform)
  {
    drawRenderedImage(img.createDefaultRendering(), xform);
  }


}
