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
 * -------------------------
 * DummyOutputTarget.java
 * -------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: DummyOutputTarget.java,v 1.4 2003/02/10 19:33:50 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 10-Feb-2003 : Save state implemented; Documentation
 */
package com.jrefinery.report.targets.pageable.output;

import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.bandlayout.BandLayoutManager;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.targets.base.layout.SizeCalculatorException;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.physicals.PhysicalPage;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * The dummy output target wraps an output target for the layouting process,
 * so that no real output is done.
 * <p>
 * This implementation forwards all requests belonging to the LayoutSupport
 * functionality to the wrapped OutputTarget. All other method calls are ignored.
 *
 * @see com.jrefinery.report.targets.base.layout.LayoutSupport
 */
public class DummyOutputTarget extends AbstractOutputTarget
{
  /**
   * A state of a Graphics2D object. This does not store clipping regions or advanced
   * properties.
   */
  private static class OutputTargetState
  {
    /** The paint. */
    private Paint mypaint;

    /** The font. */
    private FontDefinition myfont;

    /** The stroke. */
    private Stroke mystroke;

    /**
     * Create a new state.
     *
     * @param s  the graphics device.
     */
    public OutputTargetState(OutputTarget s)
    {
      save(s);
    }

    /**
     * Saves the state of the OutputTarget.
     *
     * @param source  the OutputTarget, that should save its state.
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
     * @param target  the output target, that receives the restored state.
     * @throws OutputTargetException if the state restoration failed. 
     */
    public void restore(OutputTarget target)
      throws OutputTargetException
    {
      target.setStroke(mystroke);
      target.setFont(myfont);
      target.setPaint(mypaint);
    }
  }


  /** The wrapped outputtarget. */
  private OutputTarget backend;
  /** A flag to maintain the open state of this output target. */
  private boolean isOpen;
  /** The current font definition. */
  private FontDefinition font;
  /** the current color. */
  private Paint paint;
  /** the current stroke. */
  private Stroke stroke;
  /** the current save state. */
  private OutputTargetState state;

  /**
   * Creates a new output target based on the given backend.
   *
   * @param backend the original outputtarget, that should be used in this proxy.
   */
  public DummyOutputTarget(OutputTarget backend)
  {
    super(backend.getLogicalPage());
    this.backend = backend;
  }

  /**
   * Opens the target. The request is not forwarded to the backend.
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
   * Closes the target. The request is not forwarded to the backend.
   *
   * @throws OutputTargetException if there is some problem closing the target.
   */
  public void close() throws OutputTargetException
  {
    isOpen = false;
  }

  /**
   * This method does nothing.
   *
   * @param page  the physical page.
   *
   * @throws OutputTargetException if there is some problem with the target.
   */
  public void beginPage(PhysicalPage page) throws OutputTargetException
  {
    state = new OutputTargetState(this);
  }

  /**
   * This method does nothing.
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
    if (state == null)
      throw new IllegalStateException("No page started, unable to restore state");

    state.restore(this);
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
   * This method does nothing.
   *
   * @param text  the text.
   */
  public void drawString(String text)
  {
  }

  /**
   * This method does nothing.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(Shape shape)
  {
  }

  /**
   * This method does nothing.
   *
   * @param shape  the shape to draw.
   */
  public void fillShape(Shape shape)
  {
  }

  /**
   * This method does nothing.
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
   * This method does nothing.
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
