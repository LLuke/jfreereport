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
 * --------------------
 * PlainTextOutputTarget.java
 * --------------------
 * (C)opyright 2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   -;
 *
 * $Id: PlainTextOutputTarget.java,v 1.8 2003/02/07 22:40:43 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 *
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.base.content.DefaultContentFactory;
import com.jrefinery.report.targets.base.content.TextContentFactoryModule;
import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.targets.pageable.LogicalPage;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.physicals.PhysicalPage;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;

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
 * @see PrinterCommandSet
 * @see IBMPrinterCommandSet
 * @see EpsonPrinterCommandSet
 * @see PlainTextPage
 */
public class PlainTextOutputTarget extends AbstractOutputTarget
{

  /** The characters per inch used for this text  (usually 10, 12, 15, 17 or 20 ) */
  public static final String CPI_PROPERTY =
      "com.jrefinery.report.target.pageable.output.PlainTextOutputTarget.CPI";
  /** The default character spacing is 10 CPI */
  public static final Integer CPI_PROPERTY_DEFAULT = new Integer(10);

  /** The lines per inch used for this text (usually 6 or 8) */
  public static final String LPI_PROPERTY =
      "com.jrefinery.report.target.pageable.output.PlainTextOutputTarget.LPI";
  /** The default linespacing is 6 LPI */
  public static final Integer LPI_PROPERTY_DEFAULT = new Integer(6);

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
    public PlainTextState(OutputTarget source)
    {
      save(source);
    }

    /**
     * Saves the state of the OutputTarget.
     *
     * @param source  the OutputTarget.
     */
    public void save(OutputTarget source)
    {
      mypaint = source.getPaint();
      myfont = source.getFont();
      mystroke = source.getStroke();
    }

    /**
     * Copies the state back to the specified OutputTarget.
     *
     * @param target  the OutputTarget.
     * @
     */
    public void restore(OutputTarget target)
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
  private static class PlainTextSizeCalculator implements SizeCalculator
  {
    /** the current character width */
    private float characterWidth;
    /** the current character height */
    private float characterHeight;

    /**
     * Creates a new PlainTextSizeCalculator.
     *
     * @param characterWidth the character width in points (1/72 inch).
     * @param characterHeight the character height in points (1/72 inch).
     */
    public PlainTextSizeCalculator(float characterWidth, float characterHeight)
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
    public float getStringWidth(String text, int lineStartPos, int endPos)
    {
      if (lineStartPos < 0) throw new IllegalArgumentException("LineStart < 0");
      if (endPos < lineStartPos) throw new IllegalArgumentException("LineEnd < LineStart");
      if (endPos > text.length()) throw new IllegalArgumentException("EndPos > TextLength");

      return characterWidth * (endPos - lineStartPos);
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

  /** a flag indicating whether this OutputTarget is open */
  private boolean open;
  /** the current font definition */
  private FontDefinition font;
  /** the current paint, is not used */
  private Paint paint;
  /** the current stroke, is not used */
  private Stroke stroke;
  /** the current page width in CPI */
  private int currentPageWidth;
  /** the current page height in LPI */
  private int currentPageHeight;
  /** the character width in points */
  private float characterWidth;
  /** the character height in points */
  private float characterHeight;
  /** the current save state of this output target */
  private PlainTextState savedState;
  /** the currently generated page */
  private PlainTextPage pageBuffer;
  /** the current printer command set used to write and format the page */
  private PrinterCommandSet commandSet;

  /**
   * Creates a new PlainTextOutputTarget which uses the given command set to write
   * the generated content.
   *
   * @param format  the page format.
   * @param commandSet the printer commandset used to write the generated content.
   * @throws NullPointerException if the printer command set is null
   */
  public PlainTextOutputTarget(PageFormat format, PrinterCommandSet commandSet)
  {
    super(format);
    if (commandSet == null)
      throw new NullPointerException();

    this.commandSet = commandSet;
  }

  /**
   * Creates a new PlainTextOutputTarget which uses the given command set to write
   * the generated content.
   *
   * @param commandSet the printer commandset used to write the generated content.
   * @param logical  the page format used by this target for layouting.
   * @param physical  the page format used by this target for printing.
   * @throws NullPointerException if the printer command set is null
   */
  public PlainTextOutputTarget(PageFormat logical, PageFormat physical, PrinterCommandSet commandSet)
  {
    super(logical, physical);
    if (commandSet == null)
      throw new NullPointerException();

    this.commandSet = commandSet;
  }

  /**
   * Creates a new PlainTextOutputTarget which uses the given command set to write
   * the generated content.
   *
   * @param commandSet the printer commandset used to write the generated content.
   * @param logicalPage  the page format used by this target for layouting.
   * @throws NullPointerException if the printer command set is null
   */
  public PlainTextOutputTarget(LogicalPage logicalPage, PrinterCommandSet commandSet)
  {
    super(logicalPage);
    if (commandSet == null)
      throw new NullPointerException();

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
   * @throws OutputTargetException if there is some problem opening the target.
   */
  public void open() throws OutputTargetException
  {
    try
    {
      Integer lpi = (Integer) getProperty(LPI_PROPERTY, LPI_PROPERTY_DEFAULT);
      Integer cpi = (Integer) getProperty(CPI_PROPERTY, CPI_PROPERTY_DEFAULT);
      // 1 inch = 72 point
      characterWidth = (72f / cpi.floatValue());
      characterHeight = (72f / lpi.floatValue());
    }
    catch (Exception e)
    {
      throw new OutputTargetException("Failed to parse page format", e);
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
   *
   * @throws OutputTargetException if there is some problem closing the target.
   */
  public void close() throws OutputTargetException
  {
    open = false;
  }

  /**
   * Signals that a page is being started.  Stores the state of the target to
   * make it possible to restore the complete output target.
   *
   * @param page  the physical page.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void beginPage(PhysicalPage page) throws OutputTargetException
  {
    currentPageHeight = (int) (page.getPageFormat().getImageableHeight() / characterHeight);
    currentPageWidth = (int) (page.getPageFormat().getImageableWidth() / characterWidth);

    this.pageBuffer = new PlainTextPage(currentPageWidth, currentPageHeight, getCommandSet());
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
  public void restoreState() throws OutputTargetException
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
   * @throws OutputTargetException if there is a problem setting the font.
   */
  public void setFont(FontDefinition font) throws OutputTargetException
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
   * @throws OutputTargetException if there is a problem setting the stroke.
   */
  public void setStroke(Stroke stroke) throws OutputTargetException
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
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  public void setPaint(Paint paint) throws OutputTargetException
  {
    this.paint = paint;
  }

  /**
   * Draws a string at the current cursor position.
   *
   * @param text  the text.
   */
  public void drawString(String text)
  {
    Rectangle2D bounds = getOperationBounds();

    int x = (int) Math.floor(bounds.getX() / characterWidth);
    int y = (int) Math.floor(bounds.getY() / characterHeight);
    int w = (int) Math.floor(bounds.getWidth() / characterWidth);

    pageBuffer.addTextChunk(x, y, w, text, getFont());
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * shapes.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(Shape shape)
  {
    // this is not supported, does nothing ...
  }

  /**
   * This method is empty, as the PlainTextOutputTarget does not support
   * shapes.
   *
   * @param shape  the shape to draw.
   */
  public void fillShape(Shape shape)
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
  public void drawImage(ImageReference image) throws OutputTargetException
  {
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
    return new DummyOutputTarget(this);
  }

  /**
   * Configures the output target. Valid configuration keys are CPI_PROPERTY and
   * LPI_PROPERTY defining the font of the output.
   *
   * @param config  the configuration.
   */
  public void configure(ReportConfiguration config)
  {
    updateIntProperty(CPI_PROPERTY, config);
    updateIntProperty(LPI_PROPERTY, config);
  }

  /**
   * Updates a property.
   *
   * @param key  the key.
   * @param config  the config.
   */
  private void updateIntProperty(String key, ReportConfiguration config)
  {
    String configString = config.getConfigProperty(key);
    try
    {
      Integer configValue = new Integer(configString);
      String propertyValue = (String) getProperty(key, configValue);
      setProperty(key, propertyValue);
    }
    catch (NumberFormatException nfe)
    {
      Log.warn(new Log.SimpleMessage("ReportConfiguration value ", key, " is no valid integer"));
    }
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
  public SizeCalculator createTextSizeCalculator(FontDefinition font) throws OutputTargetException
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
    DefaultContentFactory contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    return contentFactory;
  }

}
