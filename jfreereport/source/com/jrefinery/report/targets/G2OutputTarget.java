/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * $Id: G2OutputTarget.java,v 1.5 2002/05/28 19:38:10 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 18-Apr-2002 : MultilineText is working again, ImageElement support
 * 16-May-2002 : Interface of drawShape changhed so we can draw different line width (JS)
 *
 */

package com.jrefinery.report.targets;

import com.jrefinery.report.Element;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.util.Log;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;

/**
 * A report output target that uses a Graphics2D object to draw the report.  This allows reports
 * to be printed on the screen and on the printer.
 */
public class G2OutputTarget extends AbstractOutputTarget
{
  /** The graphics device. */
  private Graphics2D g2;
  private Object savedState;

  private static Graphics2D dummyGraphics;

  /**
   * Creates an empty graphics by using a 1x1 pixel buffered image.
   */
  public static Graphics2D createEmptyGraphics ()
  {
    if (dummyGraphics == null)
    {
      BufferedImage image = new BufferedImage (BufferedImage.TYPE_INT_ARGB, 1, 1);
      dummyGraphics = image.createGraphics ();
    }
    return dummyGraphics;
  }

  /**
   * a state of an Graphics2D object. This does not store clipping regions or advanced
   * properties.
   */
  private static class G2State
  {
    private Paint mypaint;
    private Font myfont;
    private Stroke mystroke;
    private AffineTransform mytransform;
    private Color mybackground;

    public G2State (Graphics2D s)
    {
      save (s);
    }

    public void save (Graphics2D source)
    {
      mypaint = source.getPaint ();
      myfont = source.getFont ();
      mystroke = source.getStroke ();
      mytransform = source.getTransform ();
      mybackground = source.getBackground ();
    }

    public void restore (Graphics2D dest)
    {
      dest.setStroke (mystroke);
      dest.setFont (myfont);
      dest.setPaint (mypaint);
      dest.setTransform (mytransform);
      dest.setBackground (mybackground);
    }
  }

  /**
   * Constructs an OutputTarget for drawing to a Java Graphics2D object.
   *
   * @param g2 The graphics device.
   * @param pageFormat The page format.
   */
  public G2OutputTarget (Graphics2D g2, PageFormat pageFormat)
  {
    super (pageFormat);
    setGraphics2D (g2);
  }

  /**
   * Sets the graphics device for this output target.
   *
   * @param g2 The graphics device.
   */
  public void setGraphics2D (Graphics2D g2)
  {
    if (g2 == null)
      throw new NullPointerException ("Graphics must not be null");

    this.g2 = g2;
  }

  /**
   * Returns the currently assigned Graphics2D object.
   */
  public Graphics2D getGraphics2D ()
  {
    return g2;
  }

  /**
   * Opens the target.
   *
   * @param title The report title.
   * @param author The report author.
   */
  public void open (String title, String author)
  {
    // do nothing.
  }

  /**
   * Closes the target.
   */
  public void close ()
  {
    // do nothing.
  }

  /**
   * Sets the font.
   *
   * @param font The font.
   */
  public void setFont (Font font)
  {
    g2.setFont (font);
  }

  /**
   * Returns the currently set font of this Graphics2D object.
   */
  public Font getFont ()
  {
    return g2.getFont ();
  }

  /**
   * Sets the paint.
   *
   * @param paint The paint.
   */
  public void setPaint (Paint paint)
  {
    g2.setPaint (paint);
  }

  /**
   * Returns the currently defined paint
   */
  public Paint getPaint ()
  {
    return g2.getPaint ();
  }

  /**
   * Draws a shape. The shape is drawn using Graphics2D.draw. A paint and a stroke have to be
   * set separatly.
   * <P>
   *
   * @param shape The shape.
   */
  public void drawShape (Shape shape)
  {
    g2.draw (shape);
  }

  /**
   * Fills a shape. The shape is drawn using Graphics2D.draw. A paint and a stroke have to be
   * set separatly.
   * <P>
   *
   * @param shape The shape.
   */
  public void fillShape (Shape shape)
  {
    g2.fill (shape);
  }

  /**
   * This method is called when the page is ended. This returns the initial transformation state
   * of the graphics object.
   */
  public void endPage () throws OutputTargetException
  {
    restoreState (savedState);
  }

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is being started,
   * others can simply ignore this message.
   */
  public void beginPage () throws OutputTargetException
  {
    savedState = saveState ();
  }

  /**
   * returns the Stroke for the Graphics2D context.
   */
  public Stroke getStroke ()
  {
    return g2.getStroke ();
  }

  /**
   * sets the Stroke for the Graphics2D context.
   *
   * @throws OutputTargetException this exception is not thrown here.
   */
  public void setStroke (Stroke stroke) throws OutputTargetException
  {
    g2.setStroke (stroke);
  }

  /**
   * Draws the image contained in the given ImageReference.
   *
   * @param the imagereference used to contain the image.
   */
  public void drawImage (ImageReference image)
  {
    Rectangle2D bounds = getCursor ().getDrawBounds ();
    g2.drawImage (image.getImage (),
            (int) (bounds.getX ()),
            (int) (bounds.getY ()),
            (int) bounds.getWidth (),
            (int) bounds.getHeight (),
            null);
  }

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text).
   *
   * @param text The text.
   * @param alignment The horizontal alignment.
   */
  public void drawString (String text, int alignment)
  {
    Rectangle2D bounds = getCursor ().getDrawBounds ();

    float x = (float) bounds.getX ();
    FontMetrics fm = g2.getFontMetrics ();
    float baseline = (float) (bounds.getY () + bounds.getHeight () - fm.getDescent ());
    if (alignment == Element.LEFT)
    {
      // no adjustment required
    }
    else if (alignment == Element.CENTER)
    {
      FontRenderContext frc = g2.getFontRenderContext ();
      Rectangle2D textBounds = g2.getFont ().getStringBounds (text, frc);
      x = (float) ((bounds.getX () + (bounds.getWidth () / 2)) - (textBounds.getWidth () / 2));
    }
    else if (alignment == Element.RIGHT)
    {
      FontRenderContext frc = g2.getFontRenderContext ();
      Rectangle2D textBounds = g2.getFont ().getStringBounds (text, frc);
      x = (float) ((bounds.getX () + bounds.getWidth ()) - textBounds.getWidth ());
    }
    int display = getFont().canDisplayUpTo(text);
    if (display != text.length())
    {
      Log.warn("Unable to display the string completely. Can display up to " + display + " chars.");
    }
    g2.drawString (text, x, baseline);
  }

  /**
   * Returns the height of the current font. The font height specifies the distance between
   * 2 base lines.
   */
  protected float getFontHeight ()
  {
    FontMetrics fm = g2.getFontMetrics ();
    return fm.getHeight ();
  }

  /**
   * Calculates the width of the specified String in the current Graphics context.
   *
   * @param text the text to be weighted.
   * @param lineStartPos the start position of the substring to be weighted.
   * @param endPos the position of the last characterto be included in the weightening process.
   * @returns the width of the given string in 1/72" dpi.
   */
  public float getStringBounds (String currentLine, int lineStartPos, int endPos)
  {
    Graphics2D g2 = getGraphics2D ();
    Font font = g2.getFont ();
    FontRenderContext frc = g2.getFontRenderContext ();

    Rectangle2D textBounds2 = font.getStringBounds (currentLine, lineStartPos, endPos, frc);
    float x2 = (float) textBounds2.getWidth ();
    return x2;
  }


  /**
   * Defines the current clipping are for the band to be drawn. This method is called by
   * the band and should not be called by other entities.
   */
  public void setClippingArea (Rectangle2D bounds)
  {
    super.setClippingArea (bounds);
    try
    {
      restoreState (savedState);
    }
    catch (OutputTargetException ote)
    {
      Log.warn ("Unable to restore state on setClippingArea()", ote);
    }
    g2.transform (AffineTransform.getTranslateInstance (bounds.getX (), bounds.getY ()));
  }

  /**
   * Restores the state of this graphics.
   */
  public void restoreState (Object o) throws OutputTargetException
  {
    if (o instanceof G2State == false) throw new OutputTargetException ("Need a g2state");
    G2State state = (G2State) o;
    state.restore (this.getGraphics2D ());
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @returns the state container.
   */
  public Object saveState () throws OutputTargetException
  {
    return new G2State (this.getGraphics2D ());
  }

}
