/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ShapeElementFactory.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ShapeElementFactory.java,v 1.5 2003/10/05 21:52:32 taqua Exp $
 *
 * Changes
 * -------------------------
 * 09-Jun-2003 : Initial version
 *
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.ShapeElement;

/**
 * A base implementation of a factory to define ShapeElements.
 *
 * @author Thomas Morgner
 */
public abstract class ShapeElementFactory extends ElementFactory
{
  /** The shape color. */
  private Color color;
  /** The shape's stroke.*/
  private Stroke stroke;
  /** Defines, whether to scale the shape to fit the element bounds. */
  private Boolean scale;
  /** Defines, whether to keep the aspect ratio when scaling. */
  private Boolean keepAspectRatio;
  /** Defines, whether the shape should be filled. */
  private Boolean shouldFill;
  /** Defines, whether the shape outline should be drawn. */
  private Boolean shouldDraw;

  /**
   * DefaultConstructor.
   */
  public ShapeElementFactory()
  {
  }

  /**
   * Returns the shape color.
   *
   * @return the color of the shape.
   */
  public Color getColor()
  {
    return color;
  }

  /**
   * Defines the color of the shape.
   *
   * @param color the color of the shape.
   */
  public void setColor(final Color color)
  {
    this.color = color;
  }

  /**
   * Returns the shapes stroke. The stroke is used to draw the outline of the shape.
   *
   * @return the stoke.
   */
  public Stroke getStroke()
  {
    return stroke;
  }

  /**
   * Defines the shapes stroke. The stroke is used to draw the outline of the shape.
   *
   * @param stroke the stoke.
   */
  public void setStroke(final Stroke stroke)
  {
    this.stroke = stroke;
  }

  /**
   * Returns, whether the image content should be scaled to fit the complete image
   * element bounds.
   *
   * @return the scale flag of the image element.
   */
  public Boolean getScale()
  {
    return scale;
  }

  /**
   * Defines, whether the image content should be scaled to fit the complete image
   * element bounds.
   *
   * @param scale the scale flag of the image element.
   */
  public void setScale(final Boolean scale)
  {
    this.scale = scale;
  }

  /**
   * Returns whether the generated image element should preserve the original aspect
   * ratio of the image content during scaling. This property has no effect if the image
   * content is not scaled.
   *
   * @return the keep aspect ratio flag.
   */
  public Boolean getKeepAspectRatio()
  {
    return keepAspectRatio;
  }

  /**
   * Defines whether the generated image element should preserve the original aspect
   * ratio of the image content during scaling. This property has no effect if the image
   * content is not scaled.
   *
   * @param keepAspectRatio whether to keep the aspect ratio of the image content during
   * the scaling.
   */
  public void setKeepAspectRatio(final Boolean keepAspectRatio)
  {
    this.keepAspectRatio = keepAspectRatio;
  }

  /**
   * Return whether to fill the shape on report generation.
   *
   * @return the should fill flag.
   */
  public Boolean getShouldFill()
  {
    return shouldFill;
  }

  /**
   * Defines wether to fill the shape on report generation.
   *
   * @param shouldFill the fill flag.
   */
  public void setShouldFill(final Boolean shouldFill)
  {
    this.shouldFill = shouldFill;
  }

  /**
   * Returns whether to draw the shape outline on report generation.
   *
   * @return the draw shape flag.
   */
  public Boolean getShouldDraw()
  {
    return shouldDraw;
  }

  /**
   * Defines whether to draw the shape outline on report generation.
   *
   * @param shouldDraw the draw shape flag.
   */
  public void setShouldDraw(final Boolean shouldDraw)
  {
    this.shouldDraw = shouldDraw;
  }

  /**
   * Applies the style definition to the elements stylesheet.
   *  
   * @param style the element stylesheet which should receive the style definition.
   */
  protected void applyStyle (ElementStyleSheet style)
  {
    super.applyStyle(style);
    style.setStyleProperty(ElementStyleSheet.KEEP_ASPECT_RATIO, getKeepAspectRatio());
    style.setStyleProperty(ElementStyleSheet.PAINT, getColor());
    style.setStyleProperty(ElementStyleSheet.SCALE, getScale());
    style.setStyleProperty(ElementStyleSheet.STROKE, getStroke());
    style.setStyleProperty(ShapeElement.DRAW_SHAPE, getShouldDraw());
    style.setStyleProperty(ShapeElement.FILL_SHAPE, getShouldFill());
  }
}
