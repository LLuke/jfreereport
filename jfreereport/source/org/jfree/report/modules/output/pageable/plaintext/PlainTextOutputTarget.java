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
 * --------------------------
 * PlainTextOutputTarget.java
 * --------------------------
 * (C)opyright 2003, by Thomas Morgner.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: PlainTextOutputTarget.java,v 1.14 2004/05/07 12:53:10 mungady Exp $
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
import java.io.IOException;

import org.jfree.report.DrawableContainer;
import org.jfree.report.ImageContainer;
import org.jfree.report.PageDefinition;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.content.ImageContent;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.output.AbstractOutputTarget;
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
 * @see org.jfree.report.modules.output.pageable.plaintext.PrinterDriver
 * @see org.jfree.report.modules.output.pageable.plaintext.IBMCompatiblePrinterDriver
 * @see org.jfree.report.modules.output.pageable.plaintext.Epson9PinPrinterDriver
 * @see org.jfree.report.modules.output.pageable.plaintext.Epson24PinPrinterDriver
 * @see org.jfree.report.modules.output.pageable.plaintext.PlainTextPage
 *
 * @author Thomas Morgner
 */
public strictfp class PlainTextOutputTarget extends AbstractOutputTarget
{
  /** The configuration prefix for all properties. */
  public static final String CONFIGURATION_PREFIX =
      "org.jfree.report.modules.output.pageable.plaintext.";

  /** The property to define the encoding of the text. */
  public static final String ENCODING_PROPERTY = "Encoding";
  /** The property to define the lines per inch of the text. */
  public static final String LINES_PER_INCH = "LinesPerInch";
  /** The property to define the characters per inch of the text. */
  public static final String CHARS_PER_INCH = "CharsPerInch";

  /** The 'text encoding' property key. */
  public static final String TEXT_OUTPUT_ENCODING
      = CONFIGURATION_PREFIX + ENCODING_PROPERTY;
  /** A default value of the 'text encoding' property key. */
  public static final String TEXT_OUTPUT_ENCODING_DEFAULT =
      ReportConfiguration.getPlatformDefaultEncoding();

  /**
   * A state of a Graphics2D object. This does not store clipping regions or advanced
   * properties.
   */
  private static final class PlainTextState
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
    private PlainTextState(final PlainTextOutputTarget source)
    {
      save(source);
    }

    /**
     * Saves the state of the OutputTarget.
     *
     * @param source  the OutputTarget.
     */
    protected void save(final PlainTextOutputTarget source)
    {
      mypaint = source.getPaint();
      myfont = source.getFont();
      mystroke = source.getStroke();
    }

    /**
     * Copies the state back to the specified OutputTarget.
     *
     * @param target  the OutputTarget.
     * @throws OutputTargetException if restoring the output target state failed.
     */
    protected void restore(final PlainTextOutputTarget target)
        throws OutputTargetException
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
  private static final class PlainTextSizeCalculator implements SizeCalculator
  {
    /** the current character width. */
    private final float characterWidth;

    /** the current character height. */
    private final float characterHeight;

    /**
     * Creates a new PlainTextSizeCalculator.
     *
     * @param characterWidth the character width in points (1/72 inch).
     * @param characterHeight the character height in points (1/72 inch).
     */
    private PlainTextSizeCalculator(final float characterWidth, final float characterHeight)
    {
      if (characterHeight <= 0)
      {
        throw new IllegalArgumentException("The font size is invalid.");
      }
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

  /** a flag indicating whether this OutputTarget is open. */
  private boolean open;

  /** the current font definition. */
  private FontDefinition font;

  /** the current save state of this output target. */
  private PlainTextState savedState;

  /** the currently generated page. */
  private PlainTextPage pageBuffer;

  /** the current printer command set used to write and format the page. */
  private PrinterDriver driver;
  private String encoding;

  /**
   * Creates a new PlainTextOutputTarget which uses the given command set to write
   * the generated content.
   *
   * @param commandSet the printer commandset used to write the generated content.
   * @throws java.lang.NullPointerException if the printer command set is null
   */
  public PlainTextOutputTarget(final PrinterDriver commandSet)
  {
    if (commandSet == null)
    {
      throw new NullPointerException();
    }
    this.driver = commandSet;
  }

  /**
   * Returns the printer driver. The driver is responsible to perform the output
   * and to add the correct control codes to the output stream.
   *
   * @return the printer driver.
   */
  public PrinterDriver getDriver()
  {
    return driver;
  }

  /**
   * Opens the target.
   *
   * @throws OutputTargetException if there is some problem opening the target.
   */
  public void open() throws OutputTargetException
  {
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
  public void beginPage(final PageDefinition page, final int index)
  {
    // the page must contain the space for the border, or it is invalid
    // the left and top border is always included when performing the layout

    pageBuffer = new PlainTextPage(page.getPageFormat(index), driver, encoding);
    savedState = new PlainTextState(this);
  }

  /**
   * Signals that the current page is ended.  Writes the page buffer to the
   * printer.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void endPage() throws OutputTargetException
  {
    try
    {
      pageBuffer.writePage();
    }
    catch (IOException ioe)
    {
      throw new OutputTargetException("Failed to write the page", ioe);
    }
    pageBuffer = null;
  }

  /**
   * Restores the state of this graphics.
   *
   * @throws OutputTargetException if the argument is not an instance of G2State.
   */
  protected void restoreState() throws OutputTargetException
  {
    savedState.restore(this);
  }

  /**
   * Returns the current font.
   *
   * @return the current font.
   */
  protected FontDefinition getFont()
  {
    return font;
  }

  /**
   * Sets the font. This has no influence on the generated output.
   *
   * @param font  the font.
   *
   * @throws OutputTargetException if there is a problem setting the font.
   */
  protected void setFont(final FontDefinition font) throws OutputTargetException
  {
    this.font = font;
  }

  /**
   * Returns the current stroke.
   *
   * @return the stroke.
   */
  protected Stroke getStroke()
  {
    return null;
  }

  /**
   * Defines the current stroke for the target.  This has no influence on the generated output.
   *
   * @param stroke  the stroke.
   *
   * @throws OutputTargetException if there is a problem setting the stroke.
   */
  protected void setStroke(final Stroke stroke) throws OutputTargetException
  {
  }

  /**
   * Returns the current paint.
   *
   * @return the paint.
   */
  protected Paint getPaint()
  {
    return null;
  }

  /**
   * Sets the paint.  This has no influence on the generated output.
   *
   * @param paint The paint.
   */
  protected void setPaint(final Paint paint)
  {
  }

  protected boolean isPaintSupported(final Paint p)
  {
    return false;
  }

  /**
   * Draws a string at the current cursor position.
   *
   * @param text  the text.
   */
  protected void printText(final String text)
  {
    final Rectangle2D bounds = getOperationBounds();

    final float characterWidthInPoint  = (72f / driver.getCharactersPerInch());
    final float characterHeightInPoint = (72f * driver.getLinesPerInch());

    final int x = correctedDivisionFloor((float) bounds.getX(), characterWidthInPoint);
    final int y = correctedDivisionFloor((float) bounds.getY(), characterHeightInPoint);
    final int w = correctedDivisionFloor((float) bounds.getWidth(), characterWidthInPoint);

    pageBuffer.addTextChunk(x, y, w, text, getFont());
  }

  /**
   * Fixes some floating point errors when calculating positions.
   *
   * @param c the divisor
   * @param d the divident
   * @return the corrected division result.
   */
  public static int correctedDivisionFloor(float c, float d)
  {
    c = Math.round(c * 100f);
    d = Math.round(d * 100f);
    return (int) Math.floor(c / d);
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * shapes.
   *
   * @param shape  the shape to draw.
   */
  protected void drawShape(final Shape shape)
  {
    // this is not supported, does nothing ...
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * shapes.
   *
   * @param shape  the shape to draw.
   */
  protected void fillShape(final Shape shape)
  {
    // this is not supported, does nothing ...
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * images.
   *
   * @param image The image to draw (as ImageReference for possible embedding of raw data).
   *
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  protected void drawImage(final ImageContent image) throws OutputTargetException
  {
  }

  /**
   * Configures the output target.
   *
   * @param config the configuration.
   */
  public void configure (final ReportConfiguration config)
  {
    if (encoding == null)
    {
      encoding = getTextTargetEncoding(config);
    }
  }

  public String getEncoding ()
  {
    return encoding;
  }

  public void setEncoding (final String encoding)
  {
    this.encoding = encoding;
  }

  /**
   * Returns the plain text encoding property value.
   *
   * @param config the report configuration from where to read the encoding property.
   * @return the plain text encoding property value.
   */
  public static String getTextTargetEncoding(ReportConfiguration config)
  {
    if (config == null)
    {
      config = ReportConfiguration.getGlobalConfig();
    }
    return config.getConfigProperty(TEXT_OUTPUT_ENCODING, TEXT_OUTPUT_ENCODING_DEFAULT);
  }


  /**
   * Creates a size calculator for the current state of the output target.  The calculator
   * is used to calculate the string width and line height and later maybe more...
   *
   * @param font  the font.
   *
   * @return the size calculator.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(final FontDefinition font)
      throws OutputTargetException
  {
    final float characterWidthInPoint  = (72f / driver.getCharactersPerInch());
    final float characterHeightInPoint = (72f * driver.getLinesPerInch());

    return new PlainTextSizeCalculator(characterWidthInPoint, characterHeightInPoint);
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
    final float characterWidthInPoint  = (72f / driver.getCharactersPerInch());
    return characterWidthInPoint;
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
    final float characterHeightInPoint = (72f * driver.getLinesPerInch());
    return characterHeightInPoint;
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
  protected void drawDrawable(final DrawableContainer drawable)
  {
  }
}
