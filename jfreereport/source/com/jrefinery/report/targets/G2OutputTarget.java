/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
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
 * G2OutputTarget.java
 * -------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: G2OutputTarget.java,v 1.20 2002/10/15 20:37:30 taqua Exp $
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
 */

package com.jrefinery.report.targets;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.NullOutputStream;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.util.Enumeration;

/**
 * A report output target that uses a Graphics2D object to draw the report.  This allows reports
 * to be printed on the screen and on the printer.
 *
 * @author DG
 */
public class G2OutputTarget extends AbstractOutputTarget
{
  /** The graphics device. */
  private Graphics2D g2;

  /** The saved state of the Graphics2D device. */
  private Object savedState;

  /** A dummy graphics2D. */
  private static Graphics2D dummyGraphics;

  private boolean isOpen;

  /**
   * Creates an empty graphics by using a 1x1 pixel buffered image.
   *
   * @return a Graphics2D instance.
   */
  public static Graphics2D createEmptyGraphics()
  {
    if (dummyGraphics == null)
    {
      BufferedImage image = new BufferedImage(BufferedImage.TYPE_INT_ARGB, 1, 1);
      dummyGraphics = image.createGraphics();
    }
    return dummyGraphics;
  }

  /**
   * A state of a Graphics2D object. This does not store clipping regions or advanced
   * properties.
   */
  private static class G2State
  {
    /** The paint. */
    private Paint mypaint;

    /** The font. */
    private Font myfont;

    /** The stroke. */
    private Stroke mystroke;

    /** The transform. */
    private AffineTransform mytransform;

    /** The background. */
    private Color mybackground;

    /**
     * Create a new state.
     *
     * @param s  the graphics device.
     */
    public G2State(Graphics2D s)
    {
      save(s);
    }

    /**
     * Saves the state of the Graphics2D.
     *
     * @param source  the Graphics2D.
     */
    public void save(Graphics2D source)
    {
      mypaint = source.getPaint();
      myfont = source.getFont();
      mystroke = source.getStroke();
      mytransform = source.getTransform();
      mybackground = source.getBackground();
    }

    /**
     * Copies the state back to the specified Graphics2D.
     *
     * @param target  the Graphics2D.
     */
    public void restore(Graphics2D target)
    {
      target.setStroke(mystroke);
      target.setFont(myfont);
      target.setPaint(mypaint);
      target.setTransform(mytransform);
      target.setBackground(mybackground);
    }
  }

  /**
   * Constructs an output target for drawing to a Java Graphics2D object.
   *
   * @param g2  the graphics device.
   * @param pageFormat  the page format.
   */
  public G2OutputTarget(Graphics2D g2, PageFormat pageFormat)
  {
    super(pageFormat);
    setGraphics2D(g2);
  }

  /**
   * Sets the graphics device for this output target.
   *
   * @param g2 The graphics device (null not permitted).
   */
  public void setGraphics2D(Graphics2D g2)
  {
    if (g2 == null)
    {
      throw new NullPointerException("Graphics must not be null");
    }

    this.g2 = g2;
    g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
  }

  /**
   * Returns the Graphics2D object assigned to the output target.
   *
   * @return the Graphics2D.
   */
  public Graphics2D getGraphics2D()
  {
    return g2;
  }

  /**
   * Opens the target.  No action is required for this particular target.
   */
  public void open()
  {
    // do nothing.
    isOpen = true;
  }

  /**
   * Closes the target.  No action is required for this particular target.
   */
  public void close()
  {
    // do nothing.
    isOpen = false;
  }

  public boolean isOpen()
  {
    return isOpen;
  }

  /**
   * A page is starting.  This target saves the current state of the Graphics2D device.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void beginPage() throws OutputTargetException
  {
    this.savedState = saveState();
  }

  /**
   * A page has ended.  This target restores the state of the Graphics2D device.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void endPage() throws OutputTargetException
  {
    restoreState(savedState);
  }

  /**
   * Sets the font.
   *
   * @param font The font.
   */
  public void setFont(Font font)
  {
    g2.setFont(font);
  }

  /**
   * Returns the current font.
   *
   * @return the font.
   */
  public Font getFont()
  {
    return g2.getFont();
  }

  /**
   * Sets the paint.
   *
   * @param paint The paint.
   */
  public void setPaint(Paint paint)
  {
    g2.setPaint(paint);
  }

  /**
   * Returns the current paint.
   *
   * @return the paint.
   */
  public Paint getPaint()
  {
    return g2.getPaint();
  }

  /**
   * Returns the Stroke for the Graphics2D context.
   *
   * @return the stroke.
   */
  public Stroke getStroke()
  {
    return g2.getStroke();
  }

  /**
   * Sets the Stroke for the Graphics2D context.
   *
   * @param stroke  the stroke.
   *
   * @throws OutputTargetException this exception is not thrown here.
   */
  public void setStroke(Stroke stroke) throws OutputTargetException
  {
    g2.setStroke(stroke);
  }

  /**
   * Draws a shape. The shape is drawn using Graphics2D.draw.  A paint and a stroke have to be
   * set separately.
   * <P>
   *
   * @param shape The shape.
   */
  public void drawShape(Shape shape)
  {
    g2.draw(shape);
  }

  /**
   * Fills a shape. The shape is drawn using Graphics2D.draw. A paint and a stroke have to be
   * set separately.
   * <P>
   *
   * @param shape The shape.
   */
  public void fillShape(Shape shape)
  {
    g2.fill(shape);
  }

  /**
   * Draws the image contained in the given ImageReference.
   *
   * @param image the image reference used to contain the image.
   */
  public void drawImage(ImageReference image)
  {
    Rectangle2D bounds = getCursor().getDrawBounds();
    if (image.getImage() != null)
    {
      try
      {
        g2.drawImage(image.getImage(),
            (int) (bounds.getX()),
            (int) (bounds.getY()),
            (int) bounds.getWidth(),
            (int) bounds.getHeight(),
            null);
      }
      catch (Throwable th)
      {
        th.printStackTrace();
        Log.debug("System.FreeMem: " + Runtime.getRuntime().freeMemory());
        Log.debug("System.TotalMem: " + Runtime.getRuntime().totalMemory());
      }
    }
  }

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text).
   *
   * @param text The text.
   * @param alignment The horizontal alignment.
   */
  public void drawString(String text, int alignment)
  {
    Rectangle2D bounds = getCursor().getDrawBounds();

    float x = (float) bounds.getX();

    FontRenderContext frc = g2.getFontRenderContext();
    Rectangle2D textBounds = g2.getFont().getStringBounds(text, frc);
    float textLength = (float) textBounds.getWidth();
    float elementLength = (float) bounds.getWidth();

    GlyphVector gv = g2.getFont().createGlyphVector(frc, text);
    FontMetrics fm = g2.getFontMetrics();
    float baseline = (float) (bounds.getY() + fm.getMaxAscent());
    if (alignment == Element.LEFT)
    {
      // no adjustment required
    }
    else if (alignment == Element.CENTER)
    {
      x = (float) (bounds.getX() + ((elementLength / 2) - (textLength / 2)));
    }
    else if (alignment == Element.RIGHT)
    {
      x = (float) (bounds.getX() + elementLength - textLength);
    }
    int display = getFont().canDisplayUpTo(text);
    if (display > 0 && display < text.length())
    {
      Log.warn("Unable to display the string completely. Can display up to " + display
             + " chars.");
    }
    g2.drawString(text, x, baseline);
  }

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   *
   * @return the font height.
   */
  protected float getFontHeight()
  {
    FontMetrics fm = g2.getFontMetrics();
    return fm.getAscent() - 1;
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param currentLine the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   *
   * @return the width of the given string in 1/72" dpi.
   */
  public float getStringBounds(String currentLine, int lineStartPos, int endPos)
  {
    Graphics2D g2 = getGraphics2D();
    Font font = g2.getFont();
    FontRenderContext frc = g2.getFontRenderContext();

    Rectangle2D textBounds2 = font.getStringBounds(currentLine, lineStartPos, endPos, frc);
    float x2 = (float) textBounds2.getWidth();
    return x2;
  }


  /**
   * Defines the current clipping are for the band to be drawn. This method is called by
   * the band and should not be called by other entities.
   *
   * @param bounds  the clip bounds.
   */
  public void setClippingArea(Rectangle2D bounds)
  {
    super.setClippingArea(bounds);
    try
    {
      restoreState(savedState);
    }
    catch (OutputTargetException ote)
    {
      Log.warn("Unable to restore state on setClippingArea()", ote);
    }
    g2.transform(AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY()));
  }

  /**
   * Restores the state of this graphics.
   *
   * @param o  the state.
   *
   * @throws OutputTargetException if the argument is not an instance of G2State.
   */
  public void restoreState(Object o) throws OutputTargetException
  {
    if (o instanceof G2State == false)
    {
      throw new OutputTargetException("Need a g2state");
    }
    G2State state = (G2State) o;
    state.restore(this.getGraphics2D());
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @return the state container.
   *
   * @throws OutputTargetException not thrown here.
   */
  public Object saveState() throws OutputTargetException
  {
    return new G2State(this.getGraphics2D());
  }

  /**
   * When the dummyMode is active, everything is done as if the report should be printed,
   * so that any font calculations can be done.But DONT! Write the report , if streaming,
   * write to the NullStream, but NEVER EVER do any real output.
   */
  public OutputTarget createDummyWriter()
  {
    G2OutputTarget dummy = new G2OutputTarget (createEmptyGraphics(), getPageFormat());
    Enumeration enum = getPropertyNames();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      dummy.setProperty(key, getProperty(key));
    }
    return dummy;
  }

  public void configure(ReportConfiguration config)
  {
    // nothing to do here, G2OuputTarget is not configured in any way.
  }

}
