/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 */
package org.jfree.report.ext.modules.barcode.base.content;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.geom.Dimension2D;

import org.jfree.report.ElementAlignment;
import org.jfree.report.style.FontDefinition;

/**
 * Base class containing properties and methods commom to all
 * barcode types.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public abstract class Barcode implements Cloneable
{

  public static final FontDefinition DEFAULT_FONT = new FontDefinition("SansSerif", 10);

  /** The minimum bar width. (was X)
   */
  private float minWidth;

  /** The text font. <CODE>null</CODE> if no text.
   */
  private FontDefinition font;

  /**
   * If positive, the text distance under the bars. If zero or negative,
   * the text distance above the bars.
   */
  private float baseline;

  /**
   * The height of the bars.
   */
  private float barHeight;

  /**
   * The text alignment. Can be <CODE>Element.ALIGN_LEFT</CODE>,
   * <CODE>Element.ALIGN_CENTER</CODE> or <CODE>Element.ALIGN_RIGHT</CODE>.
   */
  private ElementAlignment textAlignment;

  /**
   * The optional checksum generation.
   */
  private boolean generateChecksum;

  /**
   * Shows the generated checksum in the the text.
   */
  private boolean displayChecksumInText;

  /**
   * The code to generate.
   */
  private String code;

  public Barcode()
  {
    setMinWidth(0.8f);
    setFont(DEFAULT_FONT);
    final int size = DEFAULT_FONT.getFontSize();
    setBaseline(size);
    setBarHeight(size * 3);
    setTextAlignment(ElementAlignment.CENTER);
    generateChecksum = true;
  }

  /**
   * Gets the minimum bar width.
   * @return the minimum bar width
   */
  public float getMinWidth()
  {
    return minWidth;
  }

  /** Sets the minimum bar width.
   * @param minWidth the minimum bar width
   */
  public void setMinWidth(final float minWidth)
  {
    this.minWidth = minWidth;
  }

  /** Gets the text font. <CODE>null</CODE> if no text.
   * @return the text font. <CODE>null</CODE> if no text
   */
  public FontDefinition getFont()
  {
    return font;
  }

  /** Sets the text font. <CODE>null</CODE> if no text.
   * @param font the text font. <CODE>null</CODE> if no text
   */
  public void setFont(final FontDefinition font)
  {
    this.font = font;
  }

  /** Gets the size of the text.
   * @return the size of the text
   *
  public float getTextSize()
  {
    return textSize;
  }
*/
  /** Sets the size of the text.
   * @param size the size of the text
  public void setTextSize(float size)
  {
    this.textSize = size;
  }
   */

  /** Gets the text baseline.
   * If positive, the text distance under the bars. If zero or negative,
   * the text distance above the bars.
   * @return the baseline.
   */
  public float getBaseline()
  {
    return baseline;
  }

  /** Sets the text baseline.
   * If positive, the text distance under the bars. If zero or negative,
   * the text distance above the bars.
   * @param baseline the baseline.
   */
  public void setBaseline(final float baseline)
  {
    this.baseline = baseline;
  }

  /** Gets the height of the bars.
   * @return the height of the bars
   */
  public float getBarHeight()
  {
    return barHeight;
  }

  /** Sets the height of the bars.
   * @param barHeight the height of the bars
   */
  public void setBarHeight(final float barHeight)
  {
    this.barHeight = barHeight;
  }

  /** Gets the text alignment. Can be <CODE>Element.ALIGN_LEFT</CODE>,
   * <CODE>Element.ALIGN_CENTER</CODE> or <CODE>Element.ALIGN_RIGHT</CODE>.
   * @return the text alignment
   */
  public ElementAlignment getTextAlignment()
  {
    return textAlignment;
  }

  /** Sets the text alignment. Can be <CODE>Element.ALIGN_LEFT</CODE>,
   * <CODE>Element.ALIGN_CENTER</CODE> or <CODE>Element.ALIGN_RIGHT</CODE>.
   * @param textAlignment the text alignment
   */
  public void setTextAlignment(final ElementAlignment textAlignment)
  {
    this.textAlignment = textAlignment;
  }

  /** Gets the optional checksum generation.
   * @return the optional checksum generation
   */
  public boolean isGenerateChecksum()
  {
    return generateChecksum;
  }

  /** Setter for property generateChecksum.
   * @param generateChecksum New value of property generateChecksum.
   */
  public void setGenerateChecksum(final boolean generateChecksum)
  {
    this.generateChecksum = generateChecksum;
  }

  /** Gets the property to show the generated checksum in the the text.
   * @return value of property checksumText
   */
  public boolean isDisplayChecksumText()
  {
    return displayChecksumInText;
  }

  /** Sets the property to show the generated checksum in the the text.
   * @param checksumText new value of property checksumText
   */
  public void setDisplayChecksumText(final boolean checksumText)
  {
    this.displayChecksumInText = checksumText;
  }

  /** Gets the code to generate.
   * @return the code to generate
   */
  public String getCode()
  {
    return code;
  }

  /** Sets the code to generate.
   * @param code the code to generate
   */
  public void setCode(final String code)
  {
    this.code = code;
  }

  /**
   * Gets the maximum area that the barcode and the text, if
   * any, will occupy.
   * @return the size the barcode occupies.
   */
  public abstract Dimension2D getBarcodeSize();

  /** Creates an <CODE>Image</CODE> with the barcode.
   * @param barColor the color of the bars. It can be <CODE>null</CODE>
   * @param textColor the color of the text. It can be <CODE>null</CODE>
   * @return the <CODE>Image</CODE>
   */
  public abstract Image createImageWithBarcode(Color barColor, Color textColor);

  protected float getFontDescent(final Font font)
  {
    final FontMetrics fm = null;

    // correction factor..
    final double cFact = font.getSize2D() / fm.getHeight();
    final float baseline = (float) fm.getAscent();
    final float correctedBaseline = (float) (baseline * cFact);

    final float l = (font.getSize2D() + correctedBaseline) / 2.0f;
    return l;
  }

  protected float getFontAscent (final Font font)
  {
    final FontMetrics fm = null;

    // correction factor..
    final double cFact = font.getSize2D() / fm.getHeight();
    final float baseline = (float) fm.getAscent();
    final float correctedBaseline = (float) (baseline * cFact);
    return correctedBaseline;
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone();
  }
}
