/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ------------------------------
 * DebugOutputTarget.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DebugOutputTarget.java,v 1.1 2003/06/11 20:44:34 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.06.2003 : Initial version
 *
 */

package com.jrefinery.report.ext.junit.base.functionality;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.print.PageFormat;

import com.jrefinery.report.DrawableContainer;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.layout.DefaultSizeCalculator;
import com.jrefinery.report.targets.base.layout.SizeCalculator;
import com.jrefinery.report.targets.base.layout.SizeCalculatorException;
import com.jrefinery.report.targets.pageable.OutputTarget;
import com.jrefinery.report.targets.pageable.OutputTargetException;
import com.jrefinery.report.targets.pageable.output.AbstractOutputTarget;
import com.jrefinery.report.targets.pageable.output.DummyOutputTarget;
import com.jrefinery.report.targets.pageable.physicals.PhysicalPage;
import com.jrefinery.report.util.ReportConfiguration;

public class DebugOutputTarget extends AbstractOutputTarget
{
  private boolean open;
  private Paint paint;
  private Stroke stroke;
  private FontDefinition font;

  /**
   * Creates a new output target.  Both the logical page size and the physical page size will be
   * the same.
   *
   * @param format  the page format.
   */
  public DebugOutputTarget(final PageFormat format)
  {
    super(format);
    open = false;
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
  public void beginPage(final PhysicalPage page)
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
  public void setFont(final FontDefinition font) throws OutputTargetException
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
  public void setStroke(final Stroke stroke) throws OutputTargetException
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
  public void setPaint(final Paint paint) throws OutputTargetException
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
  }

  /**
   * Draws a shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void drawShape(final Shape shape)
  {
  }

  /**
   * Fills the shape relative to the current position.
   *
   * @param shape  the shape to draw.
   */
  public void fillShape(final Shape shape)
  {
  }

  /**
   * Draws a drawable relative to the current position.
   *
   * @param drawable the drawable to draw.
   */
  public void drawDrawable(final DrawableContainer drawable)
  {
  }

  /**
   * Draws a image relative to the specified coordinates.
   *
   * @param image The image to draw (as ImageReference for possible embedding of raw data).
   *
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  public void drawImage(final ImageReference image) throws OutputTargetException
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
  public void configure(final ReportConfiguration config)
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
  public SizeCalculator createTextSizeCalculator(final FontDefinition font)
      throws SizeCalculatorException
  {
    return new DefaultSizeCalculator(font);
  }
}
