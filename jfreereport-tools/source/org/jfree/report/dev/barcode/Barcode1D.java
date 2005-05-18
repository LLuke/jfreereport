/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * Barcode1D.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: $
 *
 * Changes (from 2005-04-28)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.Insets;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.style.FontDefinition;
import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.DefaultSizeCalculator;
import org.jfree.ui.ExtendedDrawable;
import org.jfree.ui.Align;

public abstract class Barcode1D implements ExtendedDrawable
{
  public static final double WEIGHT_ALIGNMENT_NOT_EMBEDDED = 1.;
  public static final double WEIGHT_ALIGNMENT_HALF_EMBEDDED = 0.5;
  public static final double WEIGHT_ALIGNMENT_EMBEDDED = 0.;

  /**
   * The base code.
   */
  private String code;
  /**
   * If the code has to be shown.
   */
  private boolean showCode = true;
  /**
   * The font to use for the code.
   */
  private FontDefinition font;
  /**
   * The code color.
   */
  private Color fontColor = Color.BLACK;
  /**
   * The bar color (narrow element).
   */
  private Color barColor = Color.BLACK;
  /**
   * The background color of the code and the badcode.
   */
  private Color backgroundColor = Color.WHITE;
  /**
   * The vertical alignment of the code on the bars.
   */
  private ElementAlignment verticalCodeAlignment = ElementAlignment.BOTTOM;

  private ElementAlignment horizontalCodeAlignment = ElementAlignment.CENTER;
  /**
   * Position of the code on the bars.
   */
  private double codeAlignmentWeight = 1.;
  /**
   * Stroke around the whole barcode.
   */
  private Stroke stroke;
  /**
   * The stroke color.
   */
  private Color strokeColor = Color.BLACK;
  /**
   * margins between the stroke and the barcode.
   */
  private Insets margins;
  /**
   * The gap height between the code and the bars.
   */
  private float barToCodeGap = 3;
  /**
   * The height of bars.
   */
  private float barHeight;
  /**
   * The width of a narrow bar.
   */
  private float barWidth;
  /**
   * The ratio between wide and narrow bars.
   */
  private float narrowToWide = 3;
  /**
   * If values not configured have to be automaticaly computed.
   */
  private boolean autoCompute = false;


  protected Barcode1D (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("The code of a barcode cannot be null.");
    }
    this.code = code;
    font = new FontDefinition("SansSerif", 10);
  }

  /**
   * Returns the margins around the barcode.
   *
   * @return The margins.
   */
  public Insets getMargins ()
  {
    return margins;
  }

  /**
   * Sets the margins around the barcode.
   *
   * @param margins The new margins.
   */
  public void setMargins (final Insets margins)
  {
    this.margins = margins;
  }

  /**
   * Returns the vertical alignment of the code on the bars.
   *
   * @return The alignment.
   */
  public ElementAlignment getVerticalCodeAlignment ()
  {
    return verticalCodeAlignment;
  }

  /**
   * Sets the vertical alignment of the code on the bars.
   *
   * @param codeAlignment The alignment.
   * @throws NullPointerException  If <code>codeAlignment</code> is null.
   * @throws IllegalStateException If <code>codeAlignment</code> is not <code>ElementAlignment.TOP</code>
   *                               or <code>ElementAlignment.BOTTOM</code> values.
   * @see ElementAlignment
   */
  public void setVerticalCodeAlignment (final ElementAlignment codeAlignment)
  {
    if (codeAlignment == null)
    {
      throw new NullPointerException("The vertical code alignment cannot be null.");
    }

    if (codeAlignment == ElementAlignment.BOTTOM || codeAlignment == ElementAlignment.TOP)
    {
      this.verticalCodeAlignment = codeAlignment;
    }
    else
    {
      throw new IllegalStateException("The code alignment must be TOP or BOTTOM only.");
    }
  }

  /**
   * Returns the horizontal alignment of the code on the bars.
   *
   * @return The alignment.
   */
  public ElementAlignment getHorizontalCodeAlignment ()
  {
    return horizontalCodeAlignment;
  }

  /**
   * Sets the horizontal alignment of the code on the bars.
   *
   * @param codeAlignment The alignment.
   * @throws NullPointerException  If <code>codeAlignment</code> is null.
   * @throws IllegalStateException If <code>codeAlignment</code> is not <code>ElementAlignment.LEFT</code>,
   *                               <code>ElementAlignment.CENTER</code> or
   *                               <code>ElementAlignment.RIGHT</code> values.
   * @see ElementAlignment
   */
  public void setHorizontalCodeAlignment (ElementAlignment codeAlignment)
  {
    if (codeAlignment == null)
    {
      throw new NullPointerException("The horizontal code alignment cannot be null.");
    }

    if (codeAlignment == ElementAlignment.CENTER || codeAlignment == ElementAlignment.LEFT
            || codeAlignment == ElementAlignment.RIGHT)
    {
      this.verticalCodeAlignment = codeAlignment;
    }
    else
    {
      throw new IllegalStateException("The code alignment must be LEFT, CENTER or RIGHT only.");
    }
  }

  /**
   * Returns the font color.
   *
   * @return The color.
   */
  public Color getFontColor ()
  {
    return fontColor;
  }

  /**
   * Sets the font color.
   *
   * @param fontColor The new color.
   * @throws NullPointerException If the new color is null.
   */
  public void setFontColor (final Color fontColor)
  {
    if (fontColor == null)
    {
      throw new NullPointerException("The font color cannot be null.");
    }
    this.fontColor = fontColor;
  }

  /**
   * Returns the bar color.
   *
   * @return The color.
   */
  public Color getBarColor ()
  {
    return barColor;
  }

  /**
   * Sets the bar color.
   *
   * @param barColor The new color.
   * @throws NullPointerException If the new color is null.
   */
  public void setBarColor (final Color barColor)
  {
    if (barColor == null)
    {
      throw new NullPointerException("The bar color cannot be null.");
    }
    this.barColor = barColor;
  }

  /**
   * Returns the background color.
   *
   * @return The color.
   */
  public Color getBackgroundColor ()
  {
    return backgroundColor;
  }

  /**
   * Sets the background color.
   *
   * @param backgroundColor The new color.
   * @throws NullPointerException If the new color is null.
   */
  public void setBackgroundColor (final Color backgroundColor)
  {
    if (backgroundColor == null)
    {
      throw new NullPointerException("The background color cannot be null.");
    }
    this.backgroundColor = backgroundColor;
  }

  /**
   * Returns the stroke color.
   *
   * @return The color.
   */
  public Color getStrokeColor ()
  {
    return strokeColor;
  }

  /**
   * Sets the stroke color.
   *
   * @param strokeColor The new color.
   * @throws NullPointerException If the new color is null.
   */
  public void setStrokeColor (final Color strokeColor)
  {
    if (strokeColor == null)
    {
      throw new NullPointerException("The stroke color cannot be null.");
    }
    this.strokeColor = strokeColor;
  }

  /**
   * Returns if the code have to be displayed.
   *
   * @return Boolean
   */
  public boolean isShowCode ()
  {
    return showCode;
  }

  /**
   * Sets if the code have to be displayed.
   *
   * @param showCode Boolean.
   */
  public void setShowCode (boolean showCode)
  {
    this.showCode = showCode;
  }

  /**
   * Returns the font used to print the code.
   *
   * @return The font.
   */
  public FontDefinition getFont ()
  {
    return font;
  }

  /**
   * Sets the font used to print the code.
   *
   * @param font The new font.
   * @throws NullPointerException If the font is null.
   */
  public void setFont (final FontDefinition font)
  {
    if (font == null)
    {
      throw new NullPointerException("The font cannot be null");
    }
    this.font = font;
  }

  /**
   * Returns the stroke.
   *
   * @return The stroke.
   */
  public Stroke getStroke ()
  {
    return stroke;
  }

  /**
   * Sets the stroke.
   *
   * @param stroke The new stroke.
   * @throws NullPointerException If the new stroke is null.
   */
  public void setStroke (final Stroke stroke)
  {
    if (stroke == null)
    {
      throw new NullPointerException("The stroke cannot be null");
    }
    this.stroke = stroke;
  }

  /**
   * Returns the gap height between the code and the bar area.
   *
   * @return The gap height.
   */
  public float getBarToCodeGap ()
  {
    return barToCodeGap;
  }

  /**
   * Sets the gap height between the code and the bar area.
   *
   * @param barToCodeGap The new height.
   */
  public void setBarToCodeGap (float barToCodeGap)
  {
    this.barToCodeGap = barToCodeGap;
  }

  /**
   * Returns the bar height.
   *
   * @return The height of bars.
   */
  public float getBarHeight ()
  {
    return barHeight;
  }

  /**
   * Sets the bar height.
   *
   * @param barHeight The new height of bars.
   * @throws IllegalStateException If the height is inferior or equal to zero.
   */
  public void setBarHeight (float barHeight)
  {
    if (barHeight <= 0)
    {
      throw new IllegalStateException("The height of bars cannot be inferior or equal to 0.");
    }
    this.barHeight = barHeight;
  }

  /**
   * Returns the narrow bar width.
   *
   * @return The width of narrow bars.
   */
  public float getBarWidth ()
  {
    return barWidth;
  }

  /**
   * Sets the narrow bar width.
   *
   * @param barWidth The new width od narrow bars.
   * @throws IllegalStateException If the width uis inferior or equal to zero.
   */
  public void setBarWidth (float barWidth)
  {
    if (barWidth <= 0)
    {
      throw new IllegalStateException("The width of narrow bars cannot be inferior or equal to 0.");
    }
    this.barWidth = barWidth;
  }

  /**
   * Tells if values of this barcode have to be computed according to the standard.
   *
   * @return Boolean.
   */
  public boolean isAutoCompute ()
  {
    return autoCompute;
  }

  /**
   * Sets if the values of this barcode have to be computed according to the standard.
   *
   * @param autoCompute Boolean.
   */
  public void setAutoCompute (boolean autoCompute)
  {
    this.autoCompute = autoCompute;
  }

  /**
   * Computes drawing parameters of this barcode according to the standards.
   */
  public void computeAutoValues() {
    //the X-dimention
  }

  /**
   * Returns the weight alignment of the code on the bars.
   *
   * @return The alignment weight.
   */
  public double getCodeAlignmentWeight ()
  {
    return codeAlignmentWeight;
  }

  /**
   * Sets the weight alignment of the code on the bars.
   *
   * @param codeAlignmentWeight The new weight alignment.
   */
  public void setCodeAlignmentWeight (double codeAlignmentWeight)
  {
    this.codeAlignmentWeight = codeAlignmentWeight;
  }

  /**
   * Returns the narrow bar to wide bar ratio.
   *
   * @return The ratio.
   */
  public float getNarrowToWide ()
  {
    return narrowToWide;
  }

  /**
   * Sets the wide bar to narrow bar ratio.
   *
   * @param narrowToWide The new ratio.
   */
  public void setNarrowToWide (float narrowToWide)
  {
    this.narrowToWide = narrowToWide;
  }

  /**
   * Returns the code that sould be displayed, it can differ from the base code passed as
   * argument to the constructor.
   *
   * @return The code to display.
   */
  public String getDisplayedCode ()
  {
    return code;
  }

  /**
   * Computes the displayed code bounds.
   *
   * @return The code bounds initialized on <code>x=0</code> and <code>y=0</code>.
   */
  public Rectangle2D getCodeBounds ()
  {
    final DefaultSizeCalculator calculator = new DefaultSizeCalculator(getFont());
    float width = calculator.getStringWidth(getDisplayedCode(), 0, getDisplayedCode()
            .length());
    float height = calculator.getLineHeight();

    return new Rectangle2D.Double(0, 0, width, height);
  }

  /**
   * Computes the bars bounds.
   *
   * @return The bars bounds initialized on <code>x=0</code> and <code>y=0</code>.
   */
  //todo: use autoCompute
  abstract public Rectangle2D getBarBounds ();

  /**
   * Computes the bounds of the barcode (bars and displayed code).<br>
   * Coordinates of either <code>codeBounds</code> and <code>barBounds</code> are computed
   * and modified.
   *
   * @param codeBounds The code bounds.
   * @param barBounds  The bars bounds.
   * @return The barcode bounds initialized on <code>x=0</code> and <code>y=0</code>.
   * @throws IllegalStateException If alignments alignments fields are not correct.
   */
  public Rectangle2D getBarcodeBounds (final Rectangle2D codeBounds,
                                       final Rectangle2D barBounds)
  {
    double width = Math.max(codeBounds.getWidth(), barBounds.getWidth());
    double height = 0;

    if (codeAlignmentWeight == WEIGHT_ALIGNMENT_EMBEDDED)
    {
      height = Math.max(codeBounds.getHeight(), barBounds.getHeight()) + barToCodeGap;
    }
    else if (codeAlignmentWeight == WEIGHT_ALIGNMENT_HALF_EMBEDDED)
    {
      height = codeBounds.getHeight() / 2 + barBounds.getHeight() + barToCodeGap;
    }
    else if (codeAlignmentWeight == WEIGHT_ALIGNMENT_NOT_EMBEDDED)
    {
      height = codeBounds.getHeight() + barBounds.getHeight() + barToCodeGap;
    }


    Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, (float) width, (float) height);

    int align = 0;
    if (horizontalCodeAlignment == ElementAlignment.RIGHT)
    {
      align = Align.RIGHT;
    }
    else if (horizontalCodeAlignment == ElementAlignment.CENTER)
    {
      align = Align.CENTER;
    }
    else if (horizontalCodeAlignment == ElementAlignment.LEFT)
    {
      align = Align.LEFT;
    }
    else
    {
      throw new IllegalStateException("horizontal alignment not supported.");
    }


    if (verticalCodeAlignment == ElementAlignment.TOP)
    {
      Align.align(codeBounds, rect, align | Align.TOP);
      Align.align(barBounds, rect, align | Align.BOTTOM);
    }
    else if (verticalCodeAlignment == ElementAlignment.BOTTOM)
    {
      Align.align(codeBounds, rect, align | Align.BOTTOM);
      Align.align(barBounds, rect, align | Align.TOP);
    }
    else
    {
      throw new IllegalStateException("vertical alignment not supported");
    }


    return rect;
  }

  /**
   * Draws the barcode.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   * @see org.jfree.ui.Drawable
   */
  //todo: use autoCompute
  abstract public void draw (Graphics2D g2, Rectangle2D area);


}
