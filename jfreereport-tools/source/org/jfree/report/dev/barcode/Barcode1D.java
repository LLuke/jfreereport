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
 * $Id: Barcode1D.java,v 1.7 2005/05/25 19:52:51 mimil Exp $
 *
 * Changes (from 2005-04-28) (CP)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.DefaultSizeCalculator;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.Align;
import org.jfree.ui.ExtendedDrawable;

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
  private double codeAlignmentWeight = WEIGHT_ALIGNMENT_NOT_EMBEDDED;
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
  private float barHeight = 60;
  /**
   * The width of a narrow bar.
   */
  private float barWidth = 2;
  /**
   * The ratio between wide and narrow bars.
   */
  private float narrowToWide = 3;
  /**
   * If values not configured have to be automaticaly computed.
   */
  private boolean autoCompute = false;
  /**
   * If the symbol table has be filled.
   */
  private boolean encoded = false;
  /**
   * Table holding symbols to be drawn.
   */
  private List codeTable;


  protected Barcode1D (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("The code of a barcode cannot be null.");
    }
    this.code = code;
    font = new FontDefinition("SansSerif", 10);
    margins = new Insets(10, 10, 10, 10);
    codeTable = new ArrayList();
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
  public void computeAutoValues ()
  {
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
   * Returns the code that should be displayed, it can differ from the base code passed as
   * argument to the constructor.
   *
   * @return The code to display.
   *
   * @throws IllegalStateException If there is a conflict for displaying the code.
   */
  public String getDisplayedCode ()
  {
    return code;
  }

  /**
   * Returns the base input code.
   *
   * @return The code.
   */
  public String getCode ()
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
  abstract public Rectangle2D getBarBounds ();

  /**
   * Draws the bars symbols. The graphics target is already clipped.
   *
   * @param g2 The graphics target.
   */
  abstract public void drawBars (Graphics2D g2);

  /**
   * Draws the code. The graphics target is already clipped.
   *
   * @param g2 The graphics target.
   */
  public void drawCode (Graphics2D g2)
  {
    final String displayedCode = getDisplayedCode();
    if (displayedCode != null)
    {
      final FontMetrics fm = g2.getFontMetrics();
      final float baseline = fm.getAscent();
      final float cFact = getFont().getFont().getSize2D() / fm.getHeight();

      final float correctedBaseline = baseline * cFact;
      g2.drawString(displayedCode, 0, correctedBaseline);
      System.err.println("drawing code: " + displayedCode);
    }
    else
    {
      System.err.println("no code to display.");
    }
  }

  /**
   * Computes the bounds of the barcode (bars and displayed code).<br> Coordinates of
   * either <code>codeBounds</code> and <code>barBounds</code> are computed and modified.
   *
   * @param codeBounds The code bounds.
   * @param barBounds  The bars bounds.
   * @return The barcode bounds initialized on <code>x=0</code> and <code>y=0</code>.
   *
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
  public void draw (Graphics2D g2, Rectangle2D area)
  {
    if (!isEncoded())
    {
      encode();
    }

    final Rectangle2D codeBounds = getCodeBounds();
    final Rectangle2D barBounds = getBarBounds();
    final Rectangle2D barcodeBounds = getBarcodeBounds(codeBounds, barBounds);

    //setting the background
    g2.setBackground(getBackgroundColor());
    g2.setColor(backgroundColor);
    g2.fill(area);

    //drawing the stroke
    if (stroke != null)
    {
      final Stroke oldStroke = g2.getStroke();
      final Shape strokedShape = stroke.createStrokedShape(area);
      final double x = strokedShape.getBounds2D().getWidth() - area.getWidth();
      final double y = strokedShape.getBounds2D().getHeight() - area.getHeight();
      //todo: how could I know that it is a perfect rectangle (coordinates/2)
      final Rectangle2D re = new Rectangle2D.Double(x / 2., x / 2., area.getWidth() + x / 2, area.getHeight() + y / 2);
      g2.setColor(strokeColor);
      g2.setStroke(stroke);
      g2.draw(re);
      g2.setStroke(oldStroke);
    }

    final Shape oldclip = g2.getClip();

    //removing the margings
    final Rectangle2D rect = new Rectangle2D.Double(margins.left,
            margins.top,
            barcodeBounds.getWidth(),
            barcodeBounds.getHeight());
    g2.setClip(rect);

    //drawing the code
    g2.setColor(fontColor);
    g2.setClip(codeBounds);
    g2.setFont(font.getFont());
    drawCode(g2);
    //restore
    g2.setClip(oldclip);

    //drawing the bars
    g2.setColor(barColor);
    g2.setClip(barBounds);
    //drawBars(g2);
    //restore
    g2.setClip(oldclip);

  }

  /**
   * Returns the preferred size of the barcode. If the drawable is aspect ratio aware,
   * these bounds should be used to compute the preferred aspect ratio for this drawable.
   *
   * @return the preferred size.
   *
   * @see ExtendedDrawable
   */
  public Dimension getPreferredSize ()
  {
    if (!isEncoded())
    {
      encode();
    }

    final Rectangle2D codeBounds = getCodeBounds();
    final Rectangle2D barBounds = getBarBounds();

    System.err.println("codeBounds: " + codeBounds);
    System.err.println("barBounds: " + barBounds);

    final Rectangle2D barcodeBounds = getBarcodeBounds(codeBounds, barBounds);

    System.err.println("barcodeBounds: " + barcodeBounds);
    System.err.println("codeBounds after: " + codeBounds);
    System.err.println("barBounds after: " + barBounds);

    return new Dimension((int) (barcodeBounds.getWidth() + margins.left + margins.right),
            (int) (barcodeBounds.getHeight() + margins.top + margins.bottom));
  }

  /**
   * Encodes the characters code in the symbols code.
   */
  abstract public void encode ();

  /**
   * Tells if the string code has been encoded to symbols.
   *
   * @return Boolean.
   */
  public boolean isEncoded ()
  {
    return encoded;
  }

  /**
   * Sets if the string code has been encoded to symbols.
   *
   * @param encoded Boolean.
   */
  public void setEncoded (boolean encoded)
  {
    this.encoded = encoded;
  }

  /**
   * Returns the symbols table.
   *
   * @return The symbols table.
   */
  public List getCodeTable ()
  {
    return codeTable;
  }

  /**
   * Sets the symbols table.
   *
   * @param codeTable The symbols table.
   */
  public void setCodeTable (List codeTable)
  {
    this.codeTable = codeTable;
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  //todo: me
  public boolean isPreserveAspectRatio ()
  {
    return false;
  }
}
