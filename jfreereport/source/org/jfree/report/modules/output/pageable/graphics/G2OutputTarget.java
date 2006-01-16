/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * G2OutputTarget.java
 * -------------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: G2OutputTarget.java,v 1.29 2005/11/17 17:03:48 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 18-Apr-2002 : MultilineText is working again, ImageElement support
 * 16-May-2002 : Interface of drawShape changed so we can draw different line width (JS)
 * 08-Jun-2002 : Documentation
 * 17-Jul-2002 : Fixed a nullpointer when an ImageReference did not contain a graphics
 * 26-Aug-2002 : Fixed drawString: Text was placed too deep, Fontheight is defined MaxAscent,
 *               not font.getFontheight()!
 * 10-Dec-2002 : Updated Javadocs (DG);
 *
 */

package org.jfree.report.modules.output.pageable.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.net.URL;

import org.jfree.base.log.MemoryUsageMessage;
import org.jfree.report.ImageContainer;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.PageDefinition;
import org.jfree.report.URLImageContainer;
import org.jfree.report.content.DrawableContent;
import org.jfree.report.content.ImageContent;
import org.jfree.report.layout.DefaultSizeCalculator;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.output.AbstractOutputTarget;
import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.WaitingImageObserver;

/**
 * A report output target that uses a Graphics2D object to draw the report.  This allows
 * reports to be printed on the screen and on the printer.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public strictfp class G2OutputTarget extends AbstractOutputTarget
{
  /**
   * The graphics device.
   */
  private Graphics2D g2;

  /**
   * The saved state of the Graphics2D device.
   */
  private G2State savedState;

  /**
   * Temporary storage for the Graphics2D clip region.
   */
  private Shape originalClip;

  /**
   * A dummy graphics2D.
   */
  private static Graphics2D dummyGraphics;

  /**
   * The open flag.
   */
  private boolean isOpen;

  /**
   * The current font definition for this outputtarget.
   */
  private FontDefinition fontDefinition;

  /**
   * Creates an empty graphics by using a 1x1 pixel buffered image.
   *
   * @return a Graphics2D instance.
   */
  public static Graphics2D createEmptyGraphics ()
  {
    if (dummyGraphics == null)
    {
      final BufferedImage image = new BufferedImage(BufferedImage.TYPE_INT_ARGB, 1, 1);
      dummyGraphics = image.createGraphics();
      applyStandardRenderingHints(dummyGraphics);
    }
    return dummyGraphics;
  }

  /**
   * Configures a Graphics2D object to use a predefined set of RenderingHints. This
   * enables KEY_FRACTIONALMETRICS to value VALUE_FRACTIONALMETRICS_ON, KEY_ANTIALIASING
   * to value VALUE_ANTIALIAS_OFF and sets the TextAliasing as defined by the
   * DefaultFontRenderContext.
   *
   * @param g2 the graphics2D object that should be configured
   */
  private static void applyStandardRenderingHints (final Graphics2D g2)
  {
    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    if (DefaultSizeCalculator.getFrcDetector().isAliased())
    {
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    else
    {
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF);
  }

  /**
   * A state of a Graphics2D object. This does not store clipping regions or advanced
   * properties.
   */
  private static final class G2State
  {
    /**
     * The paint.
     */
    private Paint mypaint;

    /**
     * The font.
     */
    private FontDefinition myfont;

    /**
     * The stroke.
     */
    private Stroke mystroke;

    /**
     * The transform.
     */
    private AffineTransform mytransform;

    /**
     * The background.
     */
    private Color mybackground;

    /**
     * The clip area.
     */
    private Shape myclip;

    /**
     * Create a new state.
     *
     * @param s the graphics device.
     */
    private G2State (final G2OutputTarget s)
    {
      save(s);
    }

    /**
     * Saves the state of the Graphics2D.
     *
     * @param source the Graphics2D.
     */
    protected void save (final G2OutputTarget source)
    {
      mypaint = source.getPaint();
      myfont = source.getFont();
      mystroke = source.getStroke();

      final Graphics2D g2 = source.getGraphics2D();
      mytransform = g2.getTransform();
      mybackground = g2.getBackground();
      myclip = g2.getClip();
    }

    /**
     * Copies the state back to the specified Graphics2D.
     *
     * @param target the Graphics2D.
     */
    protected void restore (final G2OutputTarget target)
    {
      target.setStroke(mystroke);
      target.setFont(myfont);
      target.setPaint(mypaint);

      final Graphics2D g2 = target.getGraphics2D();
      g2.setTransform(mytransform);
      g2.setBackground(mybackground);
      g2.setClip(myclip);
    }
  }

  /** The PageFormat for the current output. */
  private PageFormat currentPageFormat;

  /**
   * Creates a new output target.
   *
   * @param graphics the graphics device.
   */
  public G2OutputTarget (final Graphics2D graphics)
  {
    setGraphics2D(graphics);
  }

  /**
   * Sets the graphics device for this output target.
   *
   * @param g2 the graphics device (null not permitted).
   */
  private void setGraphics2D (final Graphics2D g2)
  {
    if (g2 == null)
    {
      throw new NullPointerException("Graphics must not be null");
    }

    this.g2 = (Graphics2D) g2.create();
    applyStandardRenderingHints(this.g2);
  }

  /**
   * Opens the target.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public void open ()
          throws OutputTargetException
  {
    originalClip = g2.getClip();
    setFont(new FontDefinition(g2.getFont().getName(), g2.getFont().getSize()));
    isOpen = true;
  }

  /**
   * Closes the target.
   */
  public void close ()
  {
    originalClip = null;
    savedState = null;
    isOpen = false;
  }

  /**
   * Returns <code>true</code> if the output target is open, and <code>false</code>
   * otherwise.
   *
   * @return the open state of this target.
   */
  public boolean isOpen ()
  {
    return isOpen;
  }

  /**
   * A page is starting. This method saves the current state of the Graphics2D device.
   *
   * @param page the physical page.
   * @param index the index of the current page within the page definition.
   */
  protected void beginPage (final PageDefinition page, final int index)
  {
    currentPageFormat = page.getPageFormat(index);
    final Rectangle2D bounds = new Rectangle2D.Double
            (currentPageFormat.getImageableX(),
             currentPageFormat.getImageableY(),
             currentPageFormat.getImageableWidth() + 1d,
             currentPageFormat.getImageableHeight() + 1d);
    g2.clip(bounds);

    final Rectangle2D pbounds = page.getPagePosition(index);
    g2.translate(-pbounds.getX(), -pbounds.getY());

    final double ty = currentPageFormat.getImageableY();
    final double tx = currentPageFormat.getImageableX();
    g2.transform(AffineTransform.getTranslateInstance(tx, ty));

    savedState = saveState();
  }

  /**
   * A page has ended.  This target restores the state of the Graphics2D device.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  protected void endPage ()
          throws OutputTargetException
  {
    final Rectangle2D pageBounds = getPageBounds();
    g2.transform(AffineTransform.getTranslateInstance(0 - currentPageFormat.getImageableX()
            + pageBounds.getX(),
            0 - currentPageFormat.getImageableY()
            + pageBounds.getY()));
    g2.setClip(originalClip);
    restoreState();
  }

  /**
   * Sets the font.
   *
   * @param font the font.
   */
  protected void setFont (final FontDefinition font)
  {
    if (font == null)
    {
      throw new NullPointerException();
    }
    this.fontDefinition = font;
    g2.setFont(font.getFont());
  }

  /**
   * Returns the current font.
   *
   * @return the font.
   */
  protected FontDefinition getFont ()
  {
    return fontDefinition;
  }

  /**
   * Sets the paint.
   *
   * @param paint The paint.
   */
  protected void setPaint (final Paint paint)
  {
    g2.setPaint(paint);
  }

  /**
   * Returns the current paint.
   *
   * @return the paint.
   */
  protected Paint getPaint ()
  {
    return g2.getPaint();
  }

  /**
   * Returns the Stroke for the Graphics2D context.
   *
   * @return the stroke.
   */
  protected Stroke getStroke ()
  {
    return g2.getStroke();
  }

  /**
   * Sets the Stroke for the Graphics2D context.
   *
   * @param stroke the stroke.
   */
  protected void setStroke (final Stroke stroke)
  {
    g2.setStroke(stroke);
  }

  /**
   * Draws a shape. The shape is drawn using Graphics2D.draw.  A paint and a stroke have
   * to be set separately. <P>
   *
   * @param shape The shape.
   */
  protected void drawShape (final Shape shape)
  {
    g2.draw(shape);
  }

  /**
   * Fills a shape. The shape is drawn using Graphics2D.draw. A paint and a stroke have to
   * be set separately. <P>
   *
   * @param shape The shape.
   */
  protected void fillShape (final Shape shape)
  {
    g2.fill(shape);
  }

  /**
   * Draws the image contained in the given ImageReference.
   *
   * @param content the image content.
   */
  protected void drawImage (final ImageContent content)
  {
    Image image = null;
    final ImageContainer imageContainer = content.getContent();

    if (imageContainer instanceof LocalImageContainer)
    {
      final LocalImageContainer loImage = (LocalImageContainer) imageContainer;
      image = loImage.getImage();
    }
    if (image == null && imageContainer instanceof URLImageContainer)
    {
      final URLImageContainer urlImageContainer = (URLImageContainer) imageContainer;
      if (urlImageContainer.isLoadable() == false)
      {
        return;
      }
      final URL url = urlImageContainer.getSourceURL();
      if (url == null)
      {
        return;
      }
      try
      {
        image = ImageFactory.getInstance().createImage(url);
      }
      catch (IOException e)
      {
        Log.warn("Unable to load image from " + url);
        return;
      }
    }
    if (image == null)
    {
      return;
    }

    //final ImageContainer image = content.getContent();
    final StrictBounds imageArea = content.getImageArea();
    final StrictBounds bounds = getInternalOperationBounds();

    final Shape s = g2.getClip();
    final AffineTransform transform = g2.getTransform();
    try
    {
      final double imageWidth = StrictGeomUtility.toExternalValue(bounds.getWidth());
      final double imageHeight = StrictGeomUtility.toExternalValue(bounds.getHeight());
      final Rectangle2D newClipArea =
              new Rectangle2D.Double(0, 0, imageWidth, imageHeight);
      g2.clip(newClipArea);
      // normalize the image to 72 DPI
      // This scale must not be done, it is already included in the bounds/imageArea ratio.
//      g2.scale(imageContainer.getScaleX(), imageContainer.getScaleY());

      final double scaleX = bounds.getWidth() / (double) imageArea.getWidth();
      final double scaleY = bounds.getHeight() / (double) imageArea.getHeight();
      // and apply the layouters scaling ..
      g2.scale(scaleX, scaleY);

      final int imageX = (int) StrictGeomUtility.toExternalValue(imageArea.getX());
      final int imageY = (int) StrictGeomUtility.toExternalValue(imageArea.getY());
      while (g2.drawImage(image, -imageX, -imageY, null) == false)
      {
        final WaitingImageObserver obs = new WaitingImageObserver(image);
        obs.waitImageLoaded();
        if (obs.isError())
        {
          Log.warn("The image observer detected an error while loading the Image");
          break;
        }
      }
    }
    catch (Throwable th)
    {
      // just in case the image drawing caused trouble ..
      Log.warn(new MemoryUsageMessage("Failure at drawImage"));
      Log.warn(th);
    }
    g2.setTransform(transform);
    g2.setClip(s);
  }

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline
   * of the text).
   *
   * @param text The text.
   */
  protected void printText (final String text)
  {
    // Draw the string on the given location. The fontmetrics is not correct for
    // the fonts I tested ("Arial 10 Plain -> FM.height = 13, Ascent = 10, Descent = 2"
    // while iText found out that the correct Ascent must be 7.2802734 and the descent
    // for that font is 2.104492.
    //
    // I don't trust java, and iText reads the font descriptor from the files, so I
    // correct the AWT-fontMetrics. This correction is not 100% perfect, but it is
    // perfect enough for printing ...
    final FontMetrics fm = g2.getFontMetrics();
    final Rectangle2D rect = fm.getMaxCharBounds(g2);
    final float baseline = (float) (-rect.getY());

    final Stroke stroke = getStroke();
    g2.setStroke(new BasicStroke(1));
    g2.drawString(text, 0.0f, baseline);
    g2.setStroke(stroke);

    if (getFont().isUnderline())
    {
      final float l = (getFont().getFont().getSize2D() + baseline) / 2.0f;
      final Line2D line = new Line2D.Float(0, l, (float) getOperationBounds().getWidth(), l);
      g2.draw(line);
    }
    if (getFont().isStrikeThrough())
    {
      final float fontHeight = getFont().getFont().getSize2D();
      final Line2D line = new Line2D.Float(0, fontHeight / 2f,
              (float) getOperationBounds().getWidth(), fontHeight / 2f);
      g2.draw(line);
    }
  }

  /**
   * The Graphics2D target supports all paint types.
   *
   * @param p the paint, that should be checked.
   * @return true, if the given paint is supported, false otherwise.
   */
  protected boolean isPaintSupported (final Paint p)
  {
    return p != null;
  }

  /**
   * Restores the state of this graphics.
   */
  private void restoreState ()
  {
    savedState.restore(this);
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously
   * saved state.
   *
   * @return the state container.
   */
  private G2State saveState ()
  {
    return new G2State(this);
  }

  /**
   * Returns a reference to the used graphics2D instance.
   *
   * @return the graphics2D instance used in this target.
   */
  protected Graphics2D getGraphics2D ()
  {
    return g2;
  }

  /**
   * Configures the output target.
   * <p/>
   * There are no configuration parameters that affect this output target, so this method
   * does nothing.
   *
   * @param config the configuration.
   */
  public void configure (final Configuration config)
  {
    // nothing to do here, G2OuputTarget is not configured in any way.
  }

  /**
   * Creates a size calculator for the current state of the output target. The calculator
   * is used to calculate the string width and line height and later maybe more.
   *
   * @param font the font.
   * @return the size calculator.
   */
  public SizeCalculator createTextSizeCalculator (final FontDefinition font)
  {
    return DefaultSizeCalculator.getDefaultSizeCalculator(font);
  }

  /**
   * Sets the operation bounds.
   *
   * @param bounds the bounds.
   */
  protected void setInternalOperationBounds (final StrictBounds bounds)
  {
    final StrictBounds oldBounds = super.getInternalOperationBounds();
    // undo the last bounds operation

    g2.transform(AffineTransform.getTranslateInstance
            (0f - StrictGeomUtility.toExternalValue(oldBounds.getX()),
                    0f - StrictGeomUtility.toExternalValue(oldBounds.getY())));
    super.setInternalOperationBounds(bounds);
    // then apply the new bounds operation
    g2.transform(AffineTransform.getTranslateInstance
            (StrictGeomUtility.toExternalValue(bounds.getX()),
                    StrictGeomUtility.toExternalValue(bounds.getY())));
  }

  /**
   * Draws a drawable relative to the current position.
   *
   * @param content the drawable to draw.
   */
  protected void drawDrawable (final DrawableContent content)
  {
    // only the drawable clippingbounds region will be drawn.
    // the clipping is set to the clipping bounds of the drawable

    // the clipping bounds are relative to the drawable dimension,
    // they are not influenced by the drawables position on the page
    final Drawable drawable = content.getContent();
    final StrictBounds imageArea = content.getImageArea();
    final StrictBounds bounds = getInternalOperationBounds();

    final Graphics2D g2 = (Graphics2D) this.g2.create();

    final double imageWidth = StrictGeomUtility.toExternalValue(bounds.getWidth());
    final double imageHeight = StrictGeomUtility.toExternalValue(bounds.getHeight());
    final Rectangle2D newClipArea = new Rectangle2D.Double(0, 0, imageWidth, imageHeight);
    g2.clip(newClipArea);

    final int imageX = (int) StrictGeomUtility.toExternalValue(imageArea.getX());
    final int imageY = (int) StrictGeomUtility.toExternalValue(imageArea.getY());
    g2.translate(-imageX, -imageY);

    final StrictBounds drawableBounds = content.getBounds();
    final double drawableWidth = StrictGeomUtility.toExternalValue(drawableBounds.getWidth());
    final double drawableHeight = StrictGeomUtility.toExternalValue(drawableBounds.getHeight());
    drawable.draw(g2, new Rectangle2D.Double(0, 0, drawableWidth, drawableHeight));
    g2.dispose();
  }
}
