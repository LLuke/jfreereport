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
 * $Id: G2OutputTarget.java,v 1.10 2003/01/14 23:48:12 taqua Exp $
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
import com.jrefinery.report.targets.pageable.SizeCalculator;
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
  private static class BuggyFontRendererDetector
  {
    private boolean isBuggyVersion;
    private boolean isAliased;

    public BuggyFontRendererDetector ()
    {
      isAliased = ReportConfiguration.getGlobalConfig().isG2TargetUseAliasing();
      
      // Another funny thing for the docs: On JDK 1.4 the font renderer changed.
      // in previous versions, the font renderer was sensitive to fractional metrics,
      // so that fonts were always rendered without FractionalMetrics enabled.
      // Since 1.4, fonts are always rendered with FractionalMetrics disabled.

      // On a 1.4 version, the aliasing has no influence on non-fractional metrics
      // aliasing has no influence on any version if fractional metrics are enabled.
      FontRenderContext frc_alias = new FontRenderContext(null, true, false);
      FontRenderContext frc_noAlias = new FontRenderContext(null, false, false);
      Font font = new Font ("Serif", Font.PLAIN, 10);
      String myText = "A simple text with some characters to calculate the length.";

      double wAlias =  font.getStringBounds(myText, 0, myText.length(), frc_alias).getWidth();
      double wNoAlias =  font.getStringBounds(myText, 0, myText.length(), frc_noAlias).getWidth();
      isBuggyVersion = (wAlias != wNoAlias);
      boolean buggyOverride = ReportConfiguration.getGlobalConfig().isG2BuggyFRC();
      Log.debug ("This is a buggy version of the font-renderer context: " + isBuggyVersion);
      Log.debug ("The buggy-value is defined in the configuration     : " + buggyOverride);
      if (isAliased())
      {
        Log.debug ("The G2OutputTarget uses Antialiasing. \n" +
                   "The FontRendererBugs should not be visible in TextAntiAliasing-Mode." +
                   "If there are problems with the string-placement, please report your " +
                   "Operating System version and your JDK Version to www.object-refinery.com/jfreereport.");
      }
      else
      {
        if (isBuggyVersion)
        {
          Log.debug ("The G2OutputTarget does not use Antialiasing. \n" +
                     "If your FontRenderer is buggy (text is not displayed correctly by default). \n" +
                     "The system was able to detect this and will try to correct the bug. \n" +
                     "If your strings are not displayed correctly, report your OperationSystem version and your " +
                     "JDK Version to www.object-refinery.com/jfreereport");
        }
        else
        {
          Log.debug ("The G2OutputTarget does not use Antialiasing. \n" +
                     "If your FontRenderer seems to be ok. \n" +
                     "If your strings are not displayed correctly, try to enable the configuration key " +
                     "\"com.jrefinery.report.targets.G2OutputTarget.isBuggyFRC=true\"" +
                     "in the file 'jfreereport.properties' or set this property as System-property. " +
                     "If the bug remains alive, please report your Operating System version and your " +
                     "JDK Version to www.object-refinery.com/jfreereport.");
        }
      }

      if (buggyOverride == true)
      {
        isBuggyVersion = true;
      }
    }

    public FontRenderContext createFontRenderContext ()
    {
      if (isAliased())
      {
        return new FontRenderContext(null, isAliased(), true);
      }
      // buggy is only important on non-aliased environments ...
      // dont use fractional metrics on buggy versions

      // use int_metrics wenn buggy ...
      return new FontRenderContext(null, isAliased(), isBuggyVersion() == false);
    }

    public boolean isAliased ()
    {
      return isAliased;
    }

    public boolean isBuggyVersion ()
    {
      return isBuggyVersion;
    }
  }

  private static BuggyFontRendererDetector frcDetector;

  public static BuggyFontRendererDetector getFrcDetector ()
  {
    if (frcDetector == null)
    {
      frcDetector = new BuggyFontRendererDetector();
    }
    return frcDetector;
  }

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
    if (getFrcDetector().isAliased())
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
   * A size calculator used internally by the <code>G2OutputTarget</code> class.
   */
  private static class G2SizeCalculator implements SizeCalculator
  {
    /**
     * The font.
     */
    private FontDefinition font;

    /**
     * Creates a new size calculator.
     *
     * @param font  the font.
     */
    public G2SizeCalculator(FontDefinition font)
    {
      this.font = font;
    }

    /**
     * Returns the height of the current font. The font height specifies the distance between
     * 2 base lines.
     *
     * @return the font height.
     */
    public float getLineHeight()
    {
      return font.getFont().getSize2D();
    }

    /**
     * Calculates the width of the specified String in the current Graphics context.
     *
     * @param text the text to be weighted.
     * @param lineStartPos the start position of the substring to be weighted.
     * @param endPos the position of the last characterto be included in the weightening process.
     *
     * @return the width of the given string in 1/72" dpi.
     */
    public float getStringWidth(String text, int lineStartPos, int endPos)
    {
      if (lineStartPos < 0)
      {
        throw new IllegalArgumentException();
      }
      if (lineStartPos > endPos)
      {
        throw new IllegalArgumentException("LineStart on: " + lineStartPos + " End on " + endPos);
      }

      if (lineStartPos == endPos)
        return 0;

      FontRenderContext frc = getFrcDetector().createFontRenderContext();
      Rectangle2D textBounds2 = font.getFont().getStringBounds(text, lineStartPos, endPos, frc);
      float x2 = (float) textBounds2.getWidth();
//      Log.debug ("Text: " + text.substring(lineStartPos, endPos) + " : Bounds: " + x2 + " : " + frc.usesFractionalMetrics());
//      if (text.substring(lineStartPos, endPos).equals ("invoic")) new Exception().printStackTrace();
      return x2;
    }

    /**
     * Converts this object to a string.
     *
     * @return a string.
     */
    public String toString ()
    {
      return "OT: " + font;
    }
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
    G2SizeCalculator cal = new G2SizeCalculator(font);
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
