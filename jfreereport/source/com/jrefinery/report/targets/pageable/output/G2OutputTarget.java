/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
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
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: G2OutputTarget.java,v 1.11 2003/01/24 16:39:07 taqua Exp $
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

package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.pageable.physicals.PhysicalPage;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.SizeCalculator;
import com.jrefinery.report.targets.DefaultSizeCalculator;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.util.Log;
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
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.util.Enumeration;

/**
 * A report output target that uses a Graphics2D object to draw the report.  This allows reports
 * to be printed on the screen and on the printer.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class G2OutputTarget extends AbstractOutputTarget
{
  /** The graphics device. */
  private Graphics2D g2;

  /** The saved state of the Graphics2D device. */
  private G2State savedState;

  /** The current age. */
  private PhysicalPage currentPage;

  /** Temporary storage for the Graphics2D clip region. */
  private Shape originalClip;

  /** A dummy graphics2D. */
  private static Graphics2D dummyGraphics;

  /** The open flag. */
  private boolean isOpen;

  private FontDefinition fontDefinition;

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
      applyStandardRenderingHints(dummyGraphics);
    }
    return dummyGraphics;
  }

  private static void applyStandardRenderingHints (Graphics2D g2)
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

    /** The clip area. */
    private Shape myclip;

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
      myclip = source.getClip();
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
      target.setClip(myclip);
    }
  }

  /**
   * Creates a new output target.
   *
   * @param page  the logical page.
   * @param graphics  the graphics device.
   */
  public G2OutputTarget (LogicalPage page, Graphics2D graphics)
  {
    super(page);
    setGraphics2D(graphics);
  }

  /**
   * Constructs an output target for drawing to a Java Graphics2D object.
   *
   * @param g2  the graphics device.
   * @param physPageFormat  the page format for the physical page used to print the report.
   * @param logPageFormat  the page format for the logical page used to layout the report.
   */
  public G2OutputTarget(Graphics2D g2, PageFormat physPageFormat, PageFormat logPageFormat)
  {
    super(physPageFormat, logPageFormat);
    setGraphics2D(g2);
  }

  /**
   * Constructs an output target for drawing to a Java Graphics2D object.
   *
   * @param g2  the graphics device.
   * @param pageFormat  the page format.
   */
  public G2OutputTarget(Graphics2D g2, PageFormat pageFormat)
  {
    this(g2, pageFormat, pageFormat);
  }

  /**
   * Sets the graphics device for this output target.
   *
   * @param g2  the graphics device (null not permitted).
   */
  public void setGraphics2D(Graphics2D g2)
  {
    if (g2 == null)
    {
      throw new NullPointerException("Graphics must not be null");
    }

    this.g2 = (Graphics2D) g2.create();
    applyStandardRenderingHints(this.g2);
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
   * Opens the target.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public void open() throws OutputTargetException
  {
    originalClip = g2.getClip();
    isOpen = true;
  }

  /**
   * Closes the target.
   */
  public void close()
  {
    originalClip = null;
    savedState = null;
    isOpen = false;
  }

  /**
   * Returns <code>true</code> if the output target is open, and <code>false</code> otherwise.
   *
   * @return boolean.
   */
  public boolean isOpen()
  {
    return isOpen;
  }

  /**
   * A page is starting.  This target saves the current state of the Graphics2D device.
   *
   * @param page  the physical page.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void beginPage(PhysicalPage page) throws OutputTargetException
  {
    this.currentPage = page;
    Rectangle2D pageBounds = currentPage.getBounds();
    PageFormat currentPageFormat = page.getPageFormat();
    Rectangle2D bounds = new Rectangle2D.Double (currentPageFormat.getImageableX(),
                                                 currentPageFormat.getImageableY(),
                                                 currentPageFormat.getImageableWidth() + 1,
                                                 currentPageFormat.getImageableHeight() + 1);
    g2.clip(bounds);
    g2.transform(AffineTransform.getTranslateInstance(currentPageFormat.getImageableX()
                                                      + pageBounds.getX(),
                                                      currentPageFormat.getImageableY()
                                                      + pageBounds.getY()));
    savedState = saveState();
  }

  /**
   * A page has ended.  This target restores the state of the Graphics2D device.
   *
   * @throws OutputTargetException if there is a problem with the target.
   */
  public void endPage() throws OutputTargetException
  {
    PageFormat currentPageFormat = currentPage.getPageFormat();
    Rectangle2D pageBounds = currentPage.getBounds();
    g2.setClip(originalClip);
    g2.transform(AffineTransform.getTranslateInstance(0 - currentPageFormat.getImageableX()
                                                      - pageBounds.getX(),
                                                      0 - currentPageFormat.getImageableY()
                                                      - pageBounds.getY()));
    restoreState();
  }

  /**
   * Sets the font.
   *
   * @param font The font.
   */
  public void setFont(FontDefinition font)
  {
    this.fontDefinition = font;
    g2.setFont(font.getFont());
  }

  /**
   * Returns the current font.
   *
   * @return the font.
   */
  public FontDefinition getFont()
  {
    return fontDefinition;
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
   * Draws the image contained in the given ImageReference. The image is drawn
   * at
   *
   * @param image the image reference used to contain the image.
   */
  public void drawImage(ImageReference image)
  {
    Rectangle2D myBounds = image.getBoundsScaled();
    Rectangle2D bounds = getOperationBounds();

    if (image.getImage() != null)
    {
      Shape s = g2.getClip();
      AffineTransform transform = g2.getTransform();
      try
      {
        g2.clip(new Rectangle2D.Double (0, 0,
                    (Math.min (bounds.getWidth(), myBounds.getWidth())),
                    (Math.min (bounds.getHeight(), myBounds.getHeight()))));
        g2.transform(AffineTransform.getScaleInstance(image.getScaleX(), image.getScaleY()));
        g2.drawImage(image.getImage(), (int) -myBounds.getX(), (int) -myBounds.getY(), null);
      }
      catch (Throwable th)
      {
        // just in case the image drawing caused trouble ..
        th.printStackTrace();
        Log.debug (new Log.MemoryUsageMessage ("Failure at drawImage"));
      }
      g2.setTransform(transform);
      g2.setClip(s);
    }
  }

  /**
   * Draws a string inside a rectangular area (the lower edge is aligned with the baseline of
   * the text).
   *
   * @param text The text.
   */
  public void drawString(String text)
  {
    // Draw the string on the given location. The fontmetrics is not correct for
    // the fonts I tested ("Arial 10 Plain -> FM.height = 13, Ascent = 10, Descent = 2"
    // while iText found out that the correct Ascent must be 7.2802734 and the descent
    // for that font is 2.104492.
    //
    // I don't trust java, and iText reads the font descriptor from the files, so I
    // correct the AWT-fontMetrics. This correction is not 100% perfect, but it is
    // perfect enough for printing ...
    FontMetrics fm = g2.getFontMetrics();
    float baseline = (float) fm.getAscent();
    double cFact = getFont().getFont().getSize2D() / fm.getHeight();

    /*
    Rectangle2D ob = getOperationBounds();
    Log.debug ("OperationBounds: " + ob);
    g2.setColor(Color.lightGray);
    g2.fill(new Rectangle2D.Double(0,0,ob.getWidth(), ob.getHeight()));
    g2.setColor(Color.black);
    */

    float correctedBaseline = (float) (baseline * cFact);
    g2.drawString(text, 0.0f, correctedBaseline);

    if (getFont().isUnderline())
    {
      float l = (getFont().getFont().getSize2D() + correctedBaseline) / 2.0f;
      Line2D line = new Line2D.Double(0, l, getOperationBounds().getWidth(), l);
      g2.draw(line);
    }
    if (getFont().isStrikeThrough())
    {
      float l = getFont().getFont().getSize2D();
      Line2D line = new Line2D.Double(0, l/2, getOperationBounds().getWidth(), l/2);
      g2.draw(line);
    }
  }

  /**
   * Restores the state of this graphics.
   *
   * @throws OutputTargetException if the argument is not an instance of G2State.
   */
  public void restoreState() throws OutputTargetException
  {
    savedState.restore(this.getGraphics2D());
  }

  /**
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @return the state container.
   *
   * @throws OutputTargetException not thrown here.
   */
  protected G2State saveState() throws OutputTargetException
  {
    return new G2State(this.getGraphics2D());
  }

  /**
   * Creates an output target that mimics a real output target, but produces no output.
   * This is used by the reporting engine when it makes its first pass through the report,
   * calculating page boundaries etc.  The second pass will use a real output target.
   *
   * @return a dummy output target.
   */
  public OutputTarget createDummyWriter()
  {
    G2OutputTarget dummy = new G2OutputTarget(getLogicalPage().newInstance(),
                                              createEmptyGraphics());
    Enumeration enum = getPropertyNames();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      dummy.setProperty(key, getProperty(key));
    }
    return dummy;
  }

  /**
   * Configures the output target.
   * <p>
   * There are no configuration parameters that affect this output target, so this method
   * does nothing.
   *
   * @param config  the configuration.
   */
  public void configure(ReportConfiguration config)
  {
    // nothing to do here, G2OuputTarget is not configured in any way.
  }

  /**
   * Creates a size calculator for the current state of the output target. The calculator
   * is used to calculate the string width and line height and later maybe more.
   *
   * @param font  the font.
   *
   * @return the size calculator.
   */
  public SizeCalculator createTextSizeCalculator(FontDefinition font)
  {
    DefaultSizeCalculator cal = new DefaultSizeCalculator(font);
    return cal;
  }

  /**
   * Sets the operation bounds.
   *
   * @param bounds  the bounds.
   */
  public void setOperationBounds(Rectangle2D bounds)
  {
    Rectangle2D oldBounds = super.getOperationBounds();
    // undo the last bounds operation
    g2.transform(AffineTransform.getTranslateInstance(0 - oldBounds.getX(), 0 - oldBounds.getY()));
    super.setOperationBounds(bounds);
    // then apply the new bounds operation
    g2.transform(AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY()));
  }

  public static void main (String [] args)
  {
    //G2OutputTarget ot = new G2OutputTarget(G2OutputTarget.createEmptyGraphics(), new PageFormat());


    printMe(false);
    printMe(true);
  }

  private static void printMe (boolean alias)
  {
    String myText = "A simple text with not tricks and traps";
    FontRenderContext frc_fract = new FontRenderContext(null, alias, true);
    FontRenderContext frc_int = new FontRenderContext(null, alias, false);

    Font font = new Font ("Serif", Font.PLAIN, 10);
    TextLayout lay = new TextLayout(myText, font, frc_fract);
    Log.debug ("\nText: 10: Lay:   " + lay.getBounds());
    Log.debug ("Text: 10: Fract: " + font.getStringBounds(myText, 0, myText.length(), frc_fract));
    Log.debug ("Text: 10: Int  : " + font.getStringBounds(myText, 0, myText.length(), frc_int));
    Log.debug ("Text: 10: GVi  : " + font.createGlyphVector(frc_int, myText).getOutline().getBounds2D());
    Log.debug ("Text: 10: GViv : " + font.createGlyphVector(frc_int, myText).getOutline().getBounds2D());
    Log.debug ("Text: 10: GVf  : " + font.createGlyphVector(frc_fract, myText).getOutline().getBounds2D());
    Log.debug ("Text: 10: GVfv : " + font.createGlyphVector(frc_fract, myText).getOutline().getBounds2D());
  }
}
