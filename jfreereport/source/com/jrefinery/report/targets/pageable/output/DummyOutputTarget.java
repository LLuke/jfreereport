/**
 * Date: Jan 29, 2003
 * Time: 8:56:27 PM
 *
 * $Id: DummyOutputTarget.java,v 1.2 2003/01/29 23:05:21 taqua Exp $
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.targets.base.layout.SizeCalculatorException;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.physicals.PhysicalPage;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

public class DummyOutputTarget extends AbstractOutputTarget
{
  private OutputTarget backend;
  private boolean isOpen;
  private FontDefinition font;
  private Paint paint;
  private Stroke stroke;

  /**
   * Creates a new output target based on the given backend.
   */
  public DummyOutputTarget(OutputTarget backend)
  {
    super(backend.getLogicalPage());
    this.backend = backend;
  }

  /**
   * Opens the target.
   *
   * @throws OutputTargetException if there is some problem opening the target.
   */
  public void open() throws OutputTargetException
  {
    isOpen = true;
  }

  /**
   * Returns true if the target is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen()
  {
    return isOpen;
  }

  /**
   * Closes the target.
   *
   * @throws OutputTargetException if there is some problem closing the target.
   */
  public void close() throws OutputTargetException
  {
    isOpen = false;
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
  }

  /**
   * Signals that the current page is ended.  Some targets need to know when a page is finished,
   * others can simply ignore this message.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void endPage() throws OutputTargetException
  {
  }

  /**
   * Restores the state from the beginning of the page.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void restoreState() throws OutputTargetException
  {
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
  }

  /**
   * Draws a shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(Shape shape)
  {
  }

  /**
   * Fills the shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void fillShape(Shape shape)
  {
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
  }

  /**
   * Creates a size calculator for the current state of the output target.  The calculator
   * is used to calculate the string width and line height and later maybe more...
   *
   * @param font  the font.
   *
   * @return the size calculator.
   *
   * @throws SizeCalculatorException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(FontDefinition font) throws SizeCalculatorException
  {
    return backend.createTextSizeCalculator(font);
  }

  /**
   * Creates and returns a default layout manager for this output target.
   * <p>
   * Note that a new layout manager is created every time this method is called.
   *
   * @return a default layout manager.
   */
  public BandLayoutManager getDefaultLayoutManager()
  {
    return backend.getDefaultLayoutManager();
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
    return backend.getHorizontalAlignmentBorder();
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
    return backend.getVerticalAlignmentBorder();
  }

  /**
   * Returns the assigned content factory for the target.
   *
   * @return the content factory.
   */
  public ContentFactory getContentFactory()
  {
    return backend.getContentFactory();
  }
}
