/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * --------------------------
 * PlainTextOutputTarget.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PlainTextOutputTarget.java,v 1.25 2003/07/03 15:59:29 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 21-Feb-2003 : Fixed a bug with unclean calculations when writing strings.
 */
package org.jfree.report.modules.output.pageable.plaintext;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;

import org.jfree.report.DrawableContainer;
import org.jfree.report.ImageReference;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.modules.output.pageable.base.output.AbstractOutputTarget;
import org.jfree.report.modules.output.pageable.base.output.DummyOutputTarget;
import org.jfree.report.modules.output.pageable.base.physicals.PhysicalPage;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.ReportConfiguration;

/**
 * An outputtarget, that generates plaintext. The text can be enriched with
 * escape sequences for Epson- or IBM-Compatible printers.
 * <p>
 * This target does not support images, shapes or different fonts.
 * The output generation is needle-printer oriented, the pageformat is translated
 * into a text page, graphics coordinates are aligned along the character grid
 * of the text mode.
 * <p>
 * It is assumed that all characters have the same width, proportional printing
 * is not supported.
 * <p>
 * The output mode is defined by supplying a suitable PrinterCommandSet. Depending
 * on the target printer, you can supply several PrinterCommandSets:
 * <ul>
 * <li>EpsonPrinterCommandSet
 * <p>Suitable for all Epson ESC/P compatible printers. The content will be formated
 * with ESC/P escape sequences.
 * <li>IBMPrinterCommandSet
 * <p>Suitable for all IBM Compatible needle printers. The content will be formated using
 * IBM escape sequences.
 * <li>PrinterCommandSet
 * <p>Suitable for unsupported printers and for text file output. The content will be
 * written without any printer control sequences.
 * </ul>
 *
 * @see org.jfree.report.modules.output.pageable.plaintext.PrinterCommandSet
 * @see org.jfree.report.modules.output.pageable.plaintext.IBMPrinterCommandSet
 * @see org.jfree.report.modules.output.pageable.plaintext.EpsonPrinterCommandSet
 * @see org.jfree.report.modules.output.pageable.plaintext.PlainTextPage
 *
 * @author Thomas Morgner
 */
public class PlainTextOutputTarget extends AbstractOutputTarget
{
  /** The 'XML encoding' property key. */
  public static final String TEXT_OUTPUT_ENCODING
      = "org.jfree.report.modules.output.pageable.plaintext.Encoding";
  /** A default value of the 'XML encoding' property key. */
  public static final String TEXT_OUTPUT_ENCODING_DEFAULT =
      ReportConfiguration.getPlatformDefaultEncoding();


  /**
   * A state of a Graphics2D object. This does not store clipping regions or advanced
   * properties.
   */
  private static class PlainTextState
  {
    /** The paint. */
    private Paint mypaint;

    /** The font. */
    private FontDefinition myfont;

    /** The stroke. */
    private Stroke mystroke;

    /**
     * creates a new PlainTextState.
     *
     * @param source the source outputtarget.
     */
    private PlainTextState(final org.jfree.report.modules.output.pageable.base.OutputTarget source)
    {
      save(source);
    }

    /**
     * Saves the state of the OutputTarget.
     *
     * @param source  the OutputTarget.
     */
    protected void save(final org.jfree.report.modules.output.pageable.base.OutputTarget source)
    {
      mypaint = source.getPaint();
      myfont = source.getFont();
      mystroke = source.getStroke();
    }

    /**
     * Copies the state back to the specified OutputTarget.
     *
     * @param target  the OutputTarget.
     * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if restoring the output target state failed.
     */
    protected void restore(final org.jfree.report.modules.output.pageable.base.OutputTarget target)
        throws org.jfree.report.modules.output.pageable.base.OutputTargetException
    {
      target.setStroke(mystroke);
      target.setFont(myfont);
      target.setPaint(mypaint);
    }
  }

  /**
   * The PlainTextSizeCalculator is used to calculate the dimensions
   * of an PlainText string.
   */
  private static class PlainTextSizeCalculator implements SizeCalculator
  {
    /** the current character width. */
    private float characterWidth;

    /** the current character height. */
    private float characterHeight;

    /**
     * Creates a new PlainTextSizeCalculator.
     *
     * @param characterWidth the character width in points (1/72 inch).
     * @param characterHeight the character height in points (1/72 inch).
     */
    private PlainTextSizeCalculator(final float characterWidth, final float characterHeight)
    {
      this.characterWidth = characterWidth;
      this.characterHeight = characterHeight;
    }

    /**
     * Calculates the width of a <code>String<code> in the current <code>Graphics</code> context.
     *
     * @param text the text.
     * @param lineStartPos the start position of the substring to be measured.
     * @param endPos the position of the last character to be measured.
     *
     * @return the width of the string in Java2D units.
     */
    public float getStringWidth(final String text, final int lineStartPos, final int endPos)
    {
      if (lineStartPos < 0)
      {
        throw new IllegalArgumentException("LineStart < 0");
      }
      if (endPos < lineStartPos)
      {
        throw new IllegalArgumentException("LineEnd < LineStart");
      }
      if (endPos > text.length())
      {
        throw new IllegalArgumentException("EndPos > TextLength");
      }

      return characterWidth * ((float) (endPos - lineStartPos));
    }

    /**
     * Returns the line height.  This includes the font's ascent, descent and leading.
     *
     * @return the line height.
     */
    public float getLineHeight()
    {
      return characterHeight;
    }
  }

  /** The property to define the encoding of the text. */
  public static final String ENCODING_PROPERTY = "Encoding";

  /** a flag indicating whether this OutputTarget is open. */
  private boolean open;

  /** the current font definition. */
  private FontDefinition font;

  /** the current paint, is not used. */
  private Paint paint;

  /** the current stroke, is not used. */
  private Stroke stroke;

  /** the current page width in CPI. */
  private int currentPageWidth;

  /** the current page height in LPI. */
  private int currentPageHeight;

  /** the character width in points. */
  private float characterWidth;

  /** the character height in points. */
  private float characterHeight;

  /** the current save state of this output target. */
  private PlainTextState savedState;

  /** the currently generated page. */
  private PlainTextPage pageBuffer;

  /** the current printer command set used to write and format the page. */
  private PrinterCommandSet commandSet;

  /**
   * Creates a new PlainTextOutputTarget which uses the given command set to write
   * the generated content.
   *
   * @param format  the page format.
   * @param commandSet the printer commandset used to write the generated content.
   * @throws java.lang.NullPointerException if the printer command set is null
   */
  public PlainTextOutputTarget(final PageFormat format, final PrinterCommandSet commandSet)
  {
    super(format);
    if (commandSet == null)
    {
      throw new NullPointerException();
    }
    this.commandSet = commandSet;
  }

  /**
   * Creates a new PlainTextOutputTarget which uses the given command set to write
   * the generated content.
   *
   * @param commandSet the printer commandset used to write the generated content.
   * @param logical  the page format used by this target for layouting.
   * @param physical  the page format used by this target for printing.
   * @throws java.lang.NullPointerException if the printer command set is null
   */
  public PlainTextOutputTarget(final PageFormat logical, final PageFormat physical,
                               final PrinterCommandSet commandSet)
  {
    super(logical, physical);
    if (commandSet == null)
    {
      throw new NullPointerException();
    }
    this.commandSet = commandSet;
  }

  /**
   * Creates a new PlainTextOutputTarget which uses the given command set to write
   * the generated content.
   *
   * @param commandSet the printer commandset used to write the generated content.
   * @param logicalPage  the page format used by this target for layouting.
   * @throws java.lang.NullPointerException if the printer command set is null
   */
  public PlainTextOutputTarget(final org.jfree.report.modules.output.pageable.base.LogicalPage logicalPage, final PrinterCommandSet commandSet)
  {
    super(logicalPage);
    if (commandSet == null)
    {
      throw new NullPointerException();
    }
    this.commandSet = commandSet;
  }

  /**
   * Gets the printercommandset used to format the text.
   * @return the printer command set.
   */
  public PrinterCommandSet getCommandSet()
  {
    return commandSet;
  }

  /**
   * Opens the target.
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is some problem opening the target.
   */
  public void open() throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
    try
    {
      // 1 inch = 72 point
      characterWidth = (72f / (float) commandSet.getDefaultCPI());
      characterHeight = (72f / (float) commandSet.getDefaultLPI());
    }
    catch (Exception e)
    {
      throw new org.jfree.report.modules.output.pageable.base.OutputTargetException("Failed to parse page format", e);
    }
    open = true;
  }

  /**
   * Returns true if the target is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen()
  {
    return open;
  }

  /**
   * Closes the target.
   */
  public void close()
  {
    open = false;
  }

  /**
   * Signals that a page is being started.  Stores the state of the target to
   * make it possible to restore the complete output target.
   *
   * @param page  the physical page.
   */
  public void beginPage(final PhysicalPage page)
  {
    currentPageHeight = correctedDivisionFloor
        ((float) page.getPageFormat().getImageableHeight(), characterHeight);
    currentPageWidth = correctedDivisionFloor
        ((float) page.getPageFormat().getImageableWidth(), characterWidth);

    this.pageBuffer = new PlainTextPage(currentPageWidth, currentPageHeight,
        getCommandSet(), getDocumentEncoding());
    savedState = new PlainTextState(this);
  }

  /**
   * Signals that the current page is ended.  Writes the page buffer to the
   * printer.
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is some problem with the target.
   */
  public void endPage() throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
    try
    {
      pageBuffer.writePage();
    }
    catch (IOException ioe)
    {
      throw new org.jfree.report.modules.output.pageable.base.OutputTargetException("Failed to write the page", ioe);
    }
    pageBuffer = null;
  }

  /**
   * Restores the state of this graphics.
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if the argument is not an instance of G2State.
   */
  public void restoreState() throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
    savedState.restore(this);
  }

  /**
   * Returns the current font.
   *
   * @return the current font.
   */
  public FontDefinition getFont()
  {
    return font;
  }

  /**
   * Sets the font. This has no influence on the generated output.
   *
   * @param font  the font.
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is a problem setting the font.
   */
  public void setFont(final FontDefinition font) throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
    this.font = font;
  }

  /**
   * Returns the current stroke.
   *
   * @return the stroke.
   */
  public Stroke getStroke()
  {
    return stroke;
  }

  /**
   * Defines the current stroke for the target.  This has no influence on the generated output.
   *
   * @param stroke  the stroke.
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is a problem setting the stroke.
   */
  public void setStroke(final Stroke stroke) throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
    this.stroke = stroke;
  }

  /**
   * Returns the current paint.
   *
   * @return the paint.
   */
  public Paint getPaint()
  {
    return paint;
  }

  /**
   * Sets the paint.  This has no influence on the generated output.
   *
   * @param paint The paint.
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is a problem setting the paint.
   */
  public void setPaint(final Paint paint) throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
    this.paint = paint;
  }

  /**
   * Draws a string at the current cursor position.
   *
   * @param text  the text.
   */
  public void drawString(final String text)
  {
    final Rectangle2D bounds = getOperationBounds();

    final int x = correctedDivisionFloor((float) bounds.getX(), characterWidth);
    final int y = correctedDivisionFloor((float) bounds.getY(), characterHeight);
    final int w = correctedDivisionFloor((float) bounds.getWidth(), characterWidth);
/*
    Log.debug ("Bounds: " + bounds);
    Log.debug ("CW: " + characterWidth);
    Log.debug ("CH: " + characterHeight);
*/
    pageBuffer.addTextChunk(x, y, w, text, getFont());
  }

  /**
   * Fixes some floating point errors when calculating positions.
   *
   * @param c the divisor
   * @param d the divident
   * @return the corrected division result.
   */
  private int correctedDivisionFloor(float c, float d)
  {
    c = Math.round(c * 100);
    d = Math.round(d * 100);
    return (int) Math.floor(c / d);
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * shapes.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(final Shape shape)
  {
    // this is not supported, does nothing ...
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * shapes.
   *
   * @param shape  the shape to draw.
   */
  public void fillShape(final Shape shape)
  {
    // this is not supported, does nothing ...
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * images.
   *
   * @param image The image to draw (as ImageReference for possible embedding of raw data).
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is a problem setting the paint.
   */
  public void drawImage(final ImageReference image) throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
  }

  /**
   * Creates an output target that mimics a real output target, but produces no output.
   * This is used by the reporting engine when it makes its first pass through the report,
   * calculating page boundaries etc.  The second pass will use a real output target.
   *
   * @return a dummy output target.
   */
  public org.jfree.report.modules.output.pageable.base.OutputTarget createDummyWriter()
  {
    return new DummyOutputTarget(this);
  }

  /**
   * Configures the encoding of the plain text output, if not already set.
   * The OutputTarget is also configured by supplying a valid
   * PrinterCommand set.
   *
   * @param config  the configuration.
   */
  public void configure(final ReportConfiguration config)
  {
    if (getDocumentEncoding() == null)
    {
      setDocumentEncoding(getTextTargetEncoding(config));
    }
  }

  /**
   * Returns the plain text encoding property value.
   *
   * @return the plain text encoding property value.
   */
  public static String getTextTargetEncoding(ReportConfiguration config)
  {
    if (config == null)
    {
      config = ReportConfiguration.getGlobalConfig();
    }
    return config.getConfigProperty (TEXT_OUTPUT_ENCODING, TEXT_OUTPUT_ENCODING_DEFAULT);
  }


  /**
   * Creates a size calculator for the current state of the output target.  The calculator
   * is used to calculate the string width and line height and later maybe more...
   *
   * @param font  the font.
   *
   * @return the size calculator.
   *
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(final FontDefinition font) throws org.jfree.report.modules.output.pageable.base.OutputTargetException
  {
    return new PlainTextSizeCalculator(characterWidth, characterHeight);
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder()
  {
    return characterWidth;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder()
  {
    return characterHeight;
  }

  /**
   * Creates a content factory. The factory does only support TextContent.
   *
   * @return the created content factory.
   */
  protected ContentFactory createContentFactory()
  {
    final DefaultContentFactory contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    return contentFactory;
  }

  /**
   * Draws a drawable relative to the current position. Drawables are not
   * supported, they do not provide text information...
   *
   * @param drawable the drawable to draw.
   */
  public void drawDrawable(final DrawableContainer drawable)
  {
  }

  /**
   * Returns the current document encoding.
   *
   * @return the document encoding.
   */
  public String getDocumentEncoding()
  {
    return getProperty(ENCODING_PROPERTY);
  }

  /**
   * Defines the document encoding for the plain text output.
   * The specified encoding must be supported by the assigned PrinterCommandSet.
   *
   * @param documentEncoding
   */
  public void setDocumentEncoding(final String documentEncoding)
  {
    if (documentEncoding == null)
    {
      throw new NullPointerException("DocumentEncoding must not be null.");
    }
    if (getCommandSet().isEncodingSupported(documentEncoding))
    {
      setProperty(ENCODING_PROPERTY, documentEncoding);
    }
    else
    {
      throw new IllegalArgumentException("This encoding is not supported by the printer. : " + documentEncoding);
    }
  }
}
