/**
 * Date: Jan 29, 2003
 * Time: 1:49:26 PM
 *
 * $Id: PlainTextOutputTarget.java,v 1.7 2003/02/04 17:56:29 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.base.content.DefaultContentFactory;
import com.jrefinery.report.targets.base.content.TextContentFactoryModule;
import com.jrefinery.report.targets.base.content.ImageContentFactoryModule;
import com.jrefinery.report.targets.base.content.ShapeContentFactoryModule;
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

public class PlainTextOutputTarget extends AbstractOutputTarget
{

  /** The characters per inch used for this text  (usually 10, 12, 15 ) */
  public static final String CPI_PROPERTY =
      "com.jrefinery.report.target.pageable.output.PlainTextOutputTarget.CPI";
  public static final Integer CPI_PROPERTY_DEFAULT = new Integer(10);

  /** The lines per inch used for this text (usually 6 or 8) */
  public static final String LPI_PROPERTY =
      "com.jrefinery.report.target.pageable.output.PlainTextOutputTarget.LPI";
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

    public PlainTextState(PlainTextOutputTarget source)
    {
      save(source);
    }

    /**
     * Saves the state of the Graphics2D.
     *
     * @param source  the Graphics2D.
     */
    public void save(PlainTextOutputTarget source)
    {
      mypaint = source.getPaint();
      myfont = source.getFont();
      mystroke = source.getStroke();
    }

    /**
     * Copies the state back to the specified Graphics2D.
     *
     * @param target  the Graphics2D.
     */
    public void restore(PlainTextOutputTarget target)
      throws OutputTargetException
    {
      target.setStroke(mystroke);
      target.setFont(myfont);
      target.setPaint(mypaint);
    }
  }

  private static class PlainTextSizeCalculator implements SizeCalculator
  {
    private float characterWidth;
    private float characterHeight;

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

  private boolean open;
  private FontDefinition font;
  private Paint paint;
  private Stroke stroke;
  private int currentPageWidth;
  private int currentPageHeight;
  private float characterWidth;
  private float characterHeight;
  private PlainTextState savedState;
  private PlainTextPage pageBuffer;
  private PrinterCommandSet commandSet;

  /**
   * Creates a new output target.  Both the logical page size and the physical page size will be
   * the same.
   *
   * @param format  the page format.
   */
  public PlainTextOutputTarget(PageFormat format, PrinterCommandSet commandSet)
  {
    super(format);
    this.commandSet = commandSet;
  }

  /**
   * Creates a new output target with the specified logical and physical page sizes.
   *
   * @param logical  the page format used by this target for layouting.
   * @param physical  the page format used by this target for printing.
   */
  public PlainTextOutputTarget(PageFormat logical, PageFormat physical, PrinterCommandSet commandSet)
  {
    super(logical, physical);
    this.commandSet = commandSet;
  }

  /**
   * Creates a new output target.
   *
   * @param logicalPage  the logical page.
   */
  public PlainTextOutputTarget(LogicalPage logicalPage, PrinterCommandSet commandSet)
  {
    super(logicalPage);
    this.commandSet = commandSet;
  }

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
    savedState = saveState();
  }

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
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
   * Saves the state of this graphics object. Use restoreState to restore a previously saved
   * state.
   *
   * @return the state container.
   */
  protected PlainTextState saveState()
  {
    return new PlainTextState(this);
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
   * Sets the font.
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
   * Defines the current stroke for the target.
   * <P>
   * The stroke is used to draw the outlines of shapes.
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
   * Sets the paint.
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
   * Draws a shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(Shape shape)
  {
    // this is not supported, does nothing ...
  }

  /**
   * Fills the shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void fillShape(Shape shape)
  {
    // this is not supported, does nothing ...
  }

  /**
   * Draws a image relative to the specified coordinates.
   *
   * @param image The image to draw (as ImageReference for possible embedding of raw data).
   *
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  public void drawImage(ImageReference image) throws OutputTargetException
  {
    // this is not supported, does nothing ...
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
   * Configures the output target.
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
      Log.warn (new Log.SimpleMessage ("ReportConfiguration value ", key, " is no valid integer"));
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
   * PlainTextOutputTarget does not support ImageContent ...
   * @return
   */
  protected ContentFactory createContentFactory ()
  {
    DefaultContentFactory contentFactory = new DefaultContentFactory ();
    contentFactory.addModule(new TextContentFactoryModule());
    contentFactory.addModule(new ShapeContentFactoryModule());
    return contentFactory;
  }

}
