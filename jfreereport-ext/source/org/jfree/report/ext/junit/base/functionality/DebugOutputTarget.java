/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: DebugOutputTarget.java,v 1.7 2005/09/19 13:34:24 taqua Exp $
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

import org.jfree.report.PageDefinition;
import org.jfree.report.content.DrawableContent;
import org.jfree.report.content.ImageContent;
import org.jfree.report.layout.DefaultSizeCalculator;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.layout.SizeCalculatorException;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.output.AbstractOutputTarget;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Configuration;

public class DebugOutputTarget extends AbstractOutputTarget
{
  private boolean open;
  private Paint paint;
  private Stroke stroke;
  private FontDefinition font;
  private float hAlign;
  private float vAlign;

  public DebugOutputTarget ()
  {
    this (0, 0);
  }

  /**
   * Creates a new output target.  Both the logical page size and the physical page size will be
   * the same.
   */
  public DebugOutputTarget (final float hAlign, final float vAlign)
  {
    this.hAlign = hAlign;
    this.vAlign = vAlign;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder ()
  {
    return hAlign;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder ()
  {
    return vAlign;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalHorizontalAlignmentBorder ()
  {
    return StrictGeomUtility.toInternalValue(getHorizontalAlignmentBorder());
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalVerticalAlignmentBorder ()
  {
    return StrictGeomUtility.toInternalValue(getVerticalAlignmentBorder());
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
   */
  public void setPaint(final Paint paint)
  {
    this.paint = paint;
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
  public void drawDrawable(final DrawableContent drawable)
  {
  }

  /**
   * Draws a image relative to the specified coordinates.
   *
   * @param image The image to draw (as ImageReference for possible embedding of raw data).
   *
   * @throws OutputTargetException if there is a problem setting the paint.
   */
  public void drawImage(final ImageContent image) throws OutputTargetException
  {
  }

  /**
   * Configures the output target.
   *
   * @param config  the configuration.
   */
  public void configure(final Configuration config)
  {
  }

  public String getExportDescription()
  {
    return "pageable/test";
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
    return new DefaultSizeCalculator(font, false);
  }

  protected void beginPage (final PageDefinition page, final int index)
          throws OutputTargetException
  {
    // todo implement me

  }

  protected boolean isPaintSupported (final Paint p)
  {
    return true;
  }

  protected void printText (final String text)
  {
    // todo implement me

  }
}
