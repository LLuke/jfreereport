/**
 * Date: Mar 9, 2003
 * Time: 3:03:51 PM
 *
 * $Id: WmfGraphics2D.java,v 1.1 2003/03/09 20:38:20 taqua Exp $
 */
package org.jfree.pixie.wmf;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Image;
import java.awt.GraphicsConfiguration;
import java.awt.AlphaComposite;
import java.awt.AWTPermission;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.TexturePaint;
import java.awt.Paint;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Component;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.util.Map;

import org.jfree.pixie.AbstractGraphics2D;

public class WmfGraphics2D extends AbstractGraphics2D
{
  /**
   * Strokes the outline of a <code>Shape</code> using the settings of the
   * current <code>Graphics2D</code> context.  The rendering attributes
   * applied include the <code>Clip</code>, <code>Transform</code>,
   * <code>Paint</code>, <code>Composite</code> and
   * <code>Stroke</code> attributes.
   * @param s the <code>Shape</code> to be rendered
   * @see #setStroke
   * @see #setPaint
   * @see Graphics#setColor
   * @see #transform
   * @see #setTransform
   * @see #clip
   * @see #setClip
   * @see #setComposite
   */
  public void draw(Shape s)
  {
  }

  /**
   * Renders an image, applying a transform from image space into user space
   * before drawing.
   * The transformation from user space into device space is done with
   * the current <code>Transform</code> in the <code>Graphics2D</code>.
   * The specified transformation is applied to the image before the
   * transform attribute in the <code>Graphics2D</code> context is applied.
   * The rendering attributes applied include the <code>Clip</code>,
   * <code>Transform</code>, and <code>Composite</code> attributes.
   * Note that no rendering is done if the specified transform is
   * noninvertible.
   * @param img the <code>Image</code> to be rendered
   * @param xform the transformation from image space into user space
   * @param obs the {@link ImageObserver}
   * to be notified as more of the <code>Image</code>
   * is converted
   * @return <code>true</code> if the <code>Image</code> is
   * fully loaded and completely rendered;
   * <code>false</code> if the <code>Image</code> is still being loaded.
   * @see #transform
   * @see #setTransform
   * @see #setComposite
   * @see #clip
   * @see #setClip
   */
  public boolean drawImage(Image img,
                           AffineTransform xform,
                           ImageObserver obs)
  {
    return false;
  }

  /**
   * Renders a {@link RenderedImage},
   * applying a transform from image
   * space into user space before drawing.
   * The transformation from user space into device space is done with
   * the current <code>Transform</code> in the <code>Graphics2D</code>.
   * The specified transformation is applied to the image before the
   * transform attribute in the <code>Graphics2D</code> context is applied.
   * The rendering attributes applied include the <code>Clip</code>,
   * <code>Transform</code>, and <code>Composite</code> attributes. Note
   * that no rendering is done if the specified transform is
   * noninvertible.
   * @param img the image to be rendered
   * @param xform the transformation from image space into user space
   * @see #transform
   * @see #setTransform
   * @see #setComposite
   * @see #clip
   * @see #setClip
   */
  public void drawRenderedImage(RenderedImage img,
                                AffineTransform xform)
  {
  }

  /**
   * Renders the text specified by the specified <code>String</code>,
   * using the current <code>Font</code> and <code>Paint</code> attributes
   * in the <code>Graphics2D</code> context.
   * The baseline of the first character is at position
   * (<i>x</i>,&nbsp;<i>y</i>) in the User Space.
   * The rendering attributes applied include the <code>Clip</code>,
   * <code>Transform</code>, <code>Paint</code>, <code>Font</code> and
   * <code>Composite</code> attributes. For characters in script systems
   * such as Hebrew and Arabic, the glyphs can be rendered from right to
   * left, in which case the coordinate supplied is the location of the
   * leftmost character on the baseline.
   * @param s the <code>String</code> to be rendered
   * @param x the x-coordinates where the <code>String</code>
   * should be rendered
   * @param y the y-coordinates where the <code>String</code>
   * should be rendered
   * @throws NullPointerException if <code>str</code> is
   *         <code>null</code>
   * @see #setPaint
   * @see Graphics#setColor
   * @see Graphics#setFont
   * @see #setTransform
   * @see #setComposite
   * @see #setClip
   */
  public void drawString(String s, float x, float y)
  {
  }

  /**
   * Fills the interior of a <code>Shape</code> using the settings of the
   * <code>Graphics2D</code> context. The rendering attributes applied
   * include the <code>Clip</code>, <code>Transform</code>,
   * <code>Paint</code>, and <code>Composite</code>.
   * @param s the <code>Shape</code> to be filled
   * @see #setPaint
   * @see Graphics#setColor
   * @see #transform
   * @see #setTransform
   * @see #setComposite
   * @see #clip
   * @see #setClip
   */
  public void fill(Shape s)
  {
  }

  /**
   * Returns the device configuration associated with this
   * <code>Graphics2D</code>.
   */
  public GraphicsConfiguration getDeviceConfiguration()
  {
    return null;
  }

  /**
   * Sets the <code>Composite</code> for the <code>Graphics2D</code> context.
   * The <code>Composite</code> is used in all drawing methods such as
   * <code>drawImage</code>, <code>drawString</code>, <code>draw</code>,
   * and <code>fill</code>.  It specifies how new pixels are to be combined
   * with the existing pixels on the graphics device during the rendering
   * process.
   * <p>If this <code>Graphics2D</code> context is drawing to a
   * <code>Component</code> on the display screen and the
   * <code>Composite</code> is a custom object rather than an
   * instance of the <code>AlphaComposite</code> class, and if
   * there is a security manager, its <code>checkPermission</code>
   * method is called with an <code>AWTPermission("readDisplayPixels")</code>
   * permission.
   * @throws SecurityException
   *         if a custom <code>Composite</code> object is being
   *         used to render to the screen and a security manager
   *         is set and its <code>checkPermission</code> method
   *         does not allow the operation.
   * @param comp the <code>Composite</code> object to be used for rendering
   * @see Graphics#setXORMode
   * @see Graphics#setPaintMode
   * @see AlphaComposite
   * @see SecurityManager#checkPermission
   * @see AWTPermission
   */
  public void setComposite(Composite comp)
  {
  }

  /**
   * Sets the <code>Paint</code> attribute for the
   * <code>Graphics2D</code> context.  Calling this method
   * with a <code>null</code> <code>Paint</code> object does
   * not have any effect on the current <code>Paint</code> attribute
   * of this <code>Graphics2D</code>.
   * @param paint the <code>Paint</code> object to be used to generate
   * color during the rendering process, or <code>null</code>
   * @see Graphics#setColor
   * @see GradientPaint
   * @see TexturePaint
   */
  public void setPaint(Paint paint)
  {
  }

  /**
   * Sets the <code>Stroke</code> for the <code>Graphics2D</code> context.
   * @param s the <code>Stroke</code> object to be used to stroke a
   * <code>Shape</code> during the rendering process
   * @see BasicStroke
   */
  public void setStroke(Stroke s)
  {
  }

  /**
   * Sets the value of a single preference for the rendering algorithms.
   * Hint categories include controls for rendering quality and overall
   * time/quality trade-off in the rendering process.  Refer to the
   * <code>RenderingHints</code> class for definitions of some common
   * keys and values.
   * @param hintKey the key of the hint to be set.
   * @param hintValue the value indicating preferences for the specified
   * hint category.
   * @see RenderingHints
   */
  public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue)
  {
  }

  /**
   * Returns the value of a single preference for the rendering algorithms.
   * Hint categories include controls for rendering quality and overall
   * time/quality trade-off in the rendering process.  Refer to the
   * <code>RenderingHints</code> class for definitions of some common
   * keys and values.
   * @param hintKey the key corresponding to the hint to get.
   * @return an object representing the value for the specified hint key.
   * Some of the keys and their associated values are defined in the
   * <code>RenderingHints</code> class.
   * @see RenderingHints
   */
  public Object getRenderingHint(RenderingHints.Key hintKey)
  {
    return null;
  }

  /**
   * Replaces the values of all preferences for the rendering
   * algorithms with the specified <code>hints</code>.
   * The existing values for all rendering hints are discarded and
   * the new set of known hints and values are initialized from the
   * specified {@link Map} object.
   * Hint categories include controls for rendering quality and
   * overall time/quality trade-off in the rendering process.
   * Refer to the <code>RenderingHints</code> class for definitions of
   * some common keys and values.
   * @param hints the rendering hints to be set
   * @see RenderingHints
   */
  public void setRenderingHints(Map hints)
  {
  }

  /**
   * Sets the values of an arbitrary number of preferences for the
   * rendering algorithms.
   * Only values for the rendering hints that are present in the
   * specified <code>Map</code> object are modified.
   * All other preferences not present in the specified
   * object are left unmodified.
   * Hint categories include controls for rendering quality and
   * overall time/quality trade-off in the rendering process.
   * Refer to the <code>RenderingHints</code> class for definitions of
   * some common keys and values.
   * @param hints the rendering hints to be set
   * @see RenderingHints
   */
  public void addRenderingHints(Map hints)
  {
  }

  /**
   * Gets the preferences for the rendering algorithms.  Hint categories
   * include controls for rendering quality and overall time/quality
   * trade-off in the rendering process.
   * Returns all of the hint key/value pairs that were ever specified in
   * one operation.  Refer to the
   * <code>RenderingHints</code> class for definitions of some common
   * keys and values.
   * @return a reference to an instance of <code>RenderingHints</code>
   * that contains the current preferences.
   * @see RenderingHints
   */
  public RenderingHints getRenderingHints()
  {
    return null;
  }

  /**
   * Sets the <code>Transform</code> in the <code>Graphics2D</code>
   * context.
   * @param Tx the <code>AffineTransform</code> object to be used in the
   * rendering process
   * @see #transform
   * @see AffineTransform
   */
  public void setTransform(AffineTransform Tx)
  {
  }

  /**
   * Returns a copy of the current <code>Transform</code> in the
   * <code>Graphics2D</code> context.
   * @return the current <code>AffineTransform</code> in the
   *             <code>Graphics2D</code> context.
   * @see #transform
   * @see #setTransform
   */
  public AffineTransform getTransform()
  {
    return null;
  }

  /**
   * Returns the current <code>Paint</code> of the
   * <code>Graphics2D</code> context.
   * @return the current <code>Graphics2D</code> <code>Paint</code>,
   * which defines a color or pattern.
   * @see #setPaint
   * @see Graphics#setColor
   */
  public Paint getPaint()
  {
    return null;
  }

  /**
   * Returns the current <code>Composite</code> in the
   * <code>Graphics2D</code> context.
   * @return the current <code>Graphics2D</code> <code>Composite</code>,
   *              which defines a compositing style.
   * @see #setComposite
   */
  public Composite getComposite()
  {
    return null;
  }

  /**
   * Sets the background color for the <code>Graphics2D</code> context.
   * The background color is used for clearing a region.
   * When a <code>Graphics2D</code> is constructed for a
   * <code>Component</code>, the background color is
   * inherited from the <code>Component</code>. Setting the background color
   * in the <code>Graphics2D</code> context only affects the subsequent
   * <code>clearRect</code> calls and not the background color of the
   * <code>Component</code>.  To change the background
   * of the <code>Component</code>, use appropriate methods of
   * the <code>Component</code>.
   * @param color the background color that isused in
   * subsequent calls to <code>clearRect</code>
   * @see #getBackground
   * @see Graphics#clearRect
   */
  public void setBackground(Color color)
  {
  }

  /**
   * Returns the background color used for clearing a region.
   * @return the current <code>Graphics2D</code> <code>Color</code>,
   * which defines the background color.
   * @see #setBackground
   */
  public Color getBackground()
  {
    return null;
  }

  /**
   * Returns the current <code>Stroke</code> in the
   * <code>Graphics2D</code> context.
   * @return the current <code>Graphics2D</code> <code>Stroke</code>,
   *                 which defines the line style.
   * @see #setStroke
   */
  public Stroke getStroke()
  {
    return null;
  }

  /**
   * Intersects the current <code>Clip</code> with the interior of the
   * specified <code>Shape</code> and sets the <code>Clip</code> to the
   * resulting intersection.  The specified <code>Shape</code> is
   * transformed with the current <code>Graphics2D</code>
   * <code>Transform</code> before being intersected with the current
   * <code>Clip</code>.  This method is used to make the current
   * <code>Clip</code> smaller.
   * To make the <code>Clip</code> larger, use <code>setClip</code>.
   * The <i>user clip</i> modified by this method is independent of the
   * clipping associated with device bounds and visibility.  If no clip has
   * previously been set, or if the clip has been cleared using
   * {@link Graphics#setClip(Shape) setClip} with a <code>null</code>
   * argument, the specified <code>Shape</code> becomes the new
   * user clip.
   * @param s the <code>Shape</code> to be intersected with the current
   *          <code>Clip</code>.  If <code>s</code> is <code>null</code>,
   *          this method clears the current <code>Clip</code>.
   */
  public void clip(Shape s)
  {
  }

  /**
   * Get the rendering context of the <code>Font</code> within this
   * <code>Graphics2D</code> context.
   * The {@link FontRenderContext}
   * encapsulates application hints such as anti-aliasing and
   * fractional metrics, as well as target device specific information
   * such as dots-per-inch.  This information should be provided by the
   * application when using objects that perform typographical
   * formatting, such as <code>Font</code> and
   * <code>TextLayout</code>.  This information should also be provided
   * by applications that perform their own layout and need accurate
   * measurements of various characteristics of glyphs such as advance
   * and line height when various rendering hints have been applied to
   * the text rendering.
   *
   * @return a reference to an instance of FontRenderContext.
   * @see FontRenderContext
   * @see Font#createGlyphVector
   * @see TextLayout
   * @since     1.2
   */

  public FontRenderContext getFontRenderContext()
  {
    return null;
  }

  /**
   * Creates a new <code>Graphics</code> object that is
   * a copy of this <code>Graphics</code> object.
   * @return     a new graphics context that is a copy of
   *                       this graphics context.
   */
  public Graphics create()
  {
    return null;
  }

  /**
   * Sets the paint mode of this graphics context to overwrite the
   * destination with this graphics context's current color.
   * This sets the logical pixel operation function to the paint or
   * overwrite mode.  All subsequent rendering operations will
   * overwrite the destination with the current color.
   */
  public void setPaintMode()
  {
  }

  /**
   * Sets the paint mode of this graphics context to alternate between
   * this graphics context's current color and the new specified color.
   * This specifies that logical pixel operations are performed in the
   * XOR mode, which alternates pixels between the current color and
   * a specified XOR color.
   * <p>
   * When drawing operations are performed, pixels which are the
   * current color are changed to the specified color, and vice versa.
   * <p>
   * Pixels that are of colors other than those two colors are changed
   * in an unpredictable but reversible manner; if the same figure is
   * drawn twice, then all pixels are restored to their original values.
   * @param     c1 the XOR alternation color
   */
  public void setXORMode(Color c1)
  {
  }

  /**
   * Gets the current font.
   * @return    this graphics context's current font.
   * @see       Font
   * @see       Graphics#setFont
   */
  public Font getFont()
  {
    return null;
  }

  /**
   * Sets this graphics context's font to the specified font.
   * All subsequent text operations using this graphics context
   * use this font.
   * @param  font   the font.
   * @see     Graphics#getFont
   * @see     Graphics#drawString(String, int, int)
   * @see     Graphics#drawBytes(byte[], int, int, int, int)
   * @see     Graphics#drawChars(char[], int, int, int, int)
   */
  public void setFont(Font font)
  {
  }

  /**
   * Gets the font metrics for the specified font.
   * @return    the font metrics for the specified font.
   * @param     f the specified font
   * @see       Graphics#getFont
   * @see       FontMetrics
   * @see       Graphics#getFontMetrics()
   */
  public FontMetrics getFontMetrics(Font f)
  {
    return null;
  }

  /**
   * Gets the current clipping area.
   * This method returns the user clip, which is independent of the
   * clipping associated with device bounds and window visibility.
   * If no clip has previously been set, or if the clip has been
   * cleared using <code>setClip(null)</code>, this method returns
   * <code>null</code>.
   * @return      a <code>Shape</code> object representing the
   *              current clipping area, or <code>null</code> if
   *              no clip is set.
   * @see         Graphics#getClipBounds
   * @see         Graphics#clipRect
   * @see         Graphics#setClip(int, int, int, int)
   * @see         Graphics#setClip(Shape)
   * @since       JDK1.1
   */
  public Shape getClip()
  {
    return null;
  }

  /**
   * Sets the current clipping area to an arbitrary clip shape.
   * Not all objects that implement the <code>Shape</code>
   * interface can be used to set the clip.  The only
   * <code>Shape</code> objects that are guaranteed to be
   * supported are <code>Shape</code> objects that are
   * obtained via the <code>getClip</code> method and via
   * <code>Rectangle</code> objects.  This method sets the
   * user clip, which is independent of the clipping associated
   * with device bounds and window visibility.
   * @param clip the <code>Shape</code> to use to set the clip
   * @see         Graphics#getClip()
   * @see         Graphics#clipRect
   * @see         Graphics#setClip(int, int, int, int)
   * @since       JDK1.1
   */
  public void setClip(Shape clip)
  {
  }

  /**
   * Copies an area of the component by a distance specified by
   * <code>dx</code> and <code>dy</code>. From the point specified
   * by <code>x</code> and <code>y</code>, this method
   * copies downwards and to the right.  To copy an area of the
   * component to the left or upwards, specify a negative value for
   * <code>dx</code> or <code>dy</code>.
   * If a portion of the source rectangle lies outside the bounds
   * of the component, or is obscured by another window or component,
   * <code>copyArea</code> will be unable to copy the associated
   * pixels. The area that is omitted can be refreshed by calling
   * the component's <code>paint</code> method.
   * @param       x the <i>x</i> coordinate of the source rectangle.
   * @param       y the <i>y</i> coordinate of the source rectangle.
   * @param       width the width of the source rectangle.
   * @param       height the height of the source rectangle.
   * @param       dx the horizontal distance to copy the pixels.
   * @param       dy the vertical distance to copy the pixels.
   */
  public void copyArea(int x, int y, int width, int height,
                       int dx, int dy)
  {
  }

  /**
   * Disposes of this graphics context and releases
   * any system resources that it is using.
   * A <code>Graphics</code> object cannot be used after
   * <code>dispose</code>has been called.
   * <p>
   * When a Java program runs, a large number of <code>Graphics</code>
   * objects can be created within a short time frame.
   * Although the finalization process of the garbage collector
   * also disposes of the same system resources, it is preferable
   * to manually free the associated resources by calling this
   * method rather than to rely on a finalization process which
   * may not run to completion for a long period of time.
   * <p>
   * Graphics objects which are provided as arguments to the
   * <code>paint</code> and <code>update</code> methods
   * of components are automatically released by the system when
   * those methods return. For efficiency, programmers should
   * call <code>dispose</code> when finished using
   * a <code>Graphics</code> object only if it was created
   * directly from a component or another <code>Graphics</code> object.
   * @see         Graphics#finalize
   * @see         Component#paint
   * @see         Component#update
   * @see         Component#getGraphics
   * @see         Graphics#create
   */
  public void dispose()
  {
  }
}
