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
 * $Id: DebugOutputTarget.java,v 1.2 2003/07/03 16:06:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.functionality;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.print.PageFormat;

import org.jfree.report.DrawableContainer;
import org.jfree.report.ImageReference;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.layout.DefaultSizeCalculator;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.layout.SizeCalculatorException;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.output.AbstractOutputTarget;
import org.jfree.report.modules.output.pageable.base.output.DummyOutputTarget;
import org.jfree.report.modules.output.pageable.base.physicals.PhysicalPage;
import org.jfree.report.util.ReportConfiguration;

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
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is some problem opening the target.
   */
  public void open() throws org.jfree.report.modules.output.pageable.base.OutputTargetException
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
   * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException if there is some problem with the target.
   */
  public void endPage() throws org.jfree.report.modules.output.pageable.base.OutputTargetException
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
   * Defines the current stroke for the target.
   * <P>
   * The stroke is used to draw the outlines of shapes.
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
   * Sets the paint.
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
   * @throws org.jfree.report.layout.SizeCalculatorException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(final FontDefinition font)
      throws SizeCalculatorException
  {
    return new DefaultSizeCalculator(font);
  }
}
