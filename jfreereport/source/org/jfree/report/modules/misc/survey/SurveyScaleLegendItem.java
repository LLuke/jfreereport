/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 /**
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
 * SurveyScaleLegendItem.java
 * --------------------------
 * (C)opyright 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SurveyScaleLegendItem.java,v 1.3 2005/02/04 19:22:55 taqua Exp $
 *
 * Changes
 * -------
 * 19-Mar-2004 : Version 1 (DG);
 * 
 */

package org.jfree.report.modules.misc.survey;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.jfree.text.TextUtilities;
import org.jfree.ui.Drawable;
import org.jfree.ui.TextAnchor;

/**
 * A {@link Drawable} object that represents a legend item for a {@link SurveyScale}.
 */
public class SurveyScaleLegendItem implements Drawable
{

  /**
   * The shape.
   */
  private Shape shape;

  /**
   * The label.
   */
  private String label;

  /**
   * Draw the shape?
   */
  private boolean draw;

  /**
   * Fill the shape?
   */
  private boolean fill;

  /**
   * The label font.
   */
  private Font font;

  public SurveyScaleLegendItem ()
  {
    font = new Font("Serif", Font.ITALIC, 10);
  }

  /**
   * Creates a new legend item.
   *
   * @param shape the shape.
   * @param label the label.
   * @param draw  draw the shape?
   * @param fill  fill the shape?
   */
  public SurveyScaleLegendItem (final Shape shape,
                                final String label,
                                final boolean draw,
                                final boolean fill)
  {
    this.shape = shape;
    this.label = label;
    this.draw = draw;
    this.fill = fill;
  }

  /**
   * Draws the legend item.
   *
   * @param g2   the graphic device.
   * @param area the area.
   */
  public void draw (final Graphics2D g2, final Rectangle2D area)
  {
    if (shape == null || font == null || label == null)
    {
      return;
    }
    if (draw == false && fill == false)
    {
      return;
    }

    final Rectangle2D b = this.shape.getBounds2D();
    double x = area.getMinX() + b.getWidth() / 2.0 + 1.0;
    final double y = area.getCenterY();
    final Shape s = getShape();
    g2.translate(x,y);
    g2.setPaint(Color.black);
    if (this.draw)
    {
      g2.setStroke(new BasicStroke(0.5f));
      g2.draw(s);
    }
    if (this.fill)
    {
      g2.fill(s);
    }
    g2.translate(-x, -y);
    x += b.getWidth() / 2.0 + 3.0;
    g2.setFont(this.font);
    TextUtilities.drawAlignedString
            (this.label, g2, (float) x, (float) y, TextAnchor.HALF_ASCENT_LEFT);
  }

  public boolean isDraw ()
  {
    return draw;
  }

  public void setDraw (final boolean draw)
  {
    this.draw = draw;
  }

  public boolean isFill ()
  {
    return fill;
  }

  public void setFill (final boolean fill)
  {
    this.fill = fill;
  }

  public Font getFont ()
  {
    return font;
  }

  public void setFont (final Font font)
  {
    this.font = font;
  }

  public String getLabel ()
  {
    return label;
  }

  public void setLabel (final String label)
  {
    this.label = label;
  }

  public Shape getShape ()
  {
    return shape;
  }

  public void setShape (final Shape shape)
  {
    this.shape = shape;
  }

}
