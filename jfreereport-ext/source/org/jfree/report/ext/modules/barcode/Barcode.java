/*
* Barcode.java
*
* Created on 25 mars 2004, 01:15
*/

package org.jfree.report.ext.modules.barcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.DefaultSizeCalculator;
import org.jfree.report.style.FontDefinition;
import org.jfree.util.Log;
import org.jfree.ui.Drawable;

/**
 * @author Mimil
 */
public abstract class Barcode implements Drawable, Cloneable
{
  //todo: space for comment??: isbn number...
  //todo: remove the Log.warn and put "... and derivated"
  private FontDefinition font;
  private Color fontColor;
  private Color barcodeColor;
  private Color backGroundColor;
  private ElementAlignment barcodeVerticalAlignment;
  private ElementAlignment barcodeHorizontalAlignment;
  private ElementAlignment codeVerticalAlignment;
  private Stroke border;
  private Color borderColor;
  private float minHeight;
  private float minWidth;
  private Insets margins;
  private String code;
  private boolean showCode;
  private Insets quietZones;;     //10x the minimun size


  /**
   * Creates a new instance of Barcode
   */
  protected Barcode (final String code)
  {
    this();
    setCode(code);
  }

  /**
   * Creates a new instance of Barcode, can only be used by derivated class
   */
  protected Barcode ()
  {
    borderColor = Color.BLACK;
    margins = new Insets(10, 10, 10, 10);
    quietZones = new Insets(0, 0, 0, 0);
    showCode = true;

    barcodeVerticalAlignment = ElementAlignment.TOP;
    barcodeHorizontalAlignment = ElementAlignment.LEFT;
    codeVerticalAlignment = ElementAlignment.BOTTOM;

    font = new FontDefinition("SansSerif", 10);
    fontColor = Color.BLACK;
    barcodeColor = Color.BLACK;
  }

  /**
   * Get the string code
   *
   * @return the code
   */
  public String getCode ()
  {
    return code;
  }

  /**
   * Set the sting code
   *
   * @param code the string to set
   */
  public void setCode (final String code)
  {
    if (code == null)
    {
      throw new NullPointerException("Barcode code must be not null.");
    }
    this.code = code;
  }

  /**
   * Get margins around the barcode
   *
   * @return margins current margins
   */
  public Insets getMargins ()
  {
    return margins;
  }

  /**
   * Set the margins around the barcode
   *
   * @param margin New margins
   */
  public void setMargins (final Insets margin)
  {
    this.margins = margin;
  }

  /**
   * Get the minimum width of a "single bar"
   *
   * @return The minimum width in points
   */
  public float getMinWidth ()
  {
    return minWidth;
  }

  /**
   * Set the minimum width of a "single bar"
   *
   * @param minWidth The minimum width in points
   */
  public void setMinWidth (final float minWidth)
  {
    this.minWidth = minWidth;
  }

  /**
   * Get the height of a bar
   *
   * @return The height in points
   */
  public float getMinHeight ()
  {
    return minHeight;
  }

  /**
   * Set the height of a bar
   *
   * @param minHeight Height in points
   */
  public void setMinHeight (final float minHeight)
  {
    this.minHeight = minHeight;
  }

  /**
   * Get the barcode symbols horizontal alignment
   *
   * @return
   */
  public ElementAlignment getBarcodeHorizontalAlignment ()
  {
    return barcodeHorizontalAlignment;
  }

  /**
   * Set the barcode symbols horizontal alignment on the drawable bounds
   *
   * @param barcodeHorizontalAlignment The new horizontal alignment
   */
  public void setBarcodeHorizontalAlignment (
          final ElementAlignment barcodeHorizontalAlignment)
  {
    this.barcodeHorizontalAlignment = barcodeHorizontalAlignment;
  }

  /**
   * Get the barcode symbols vertical alignment
   *
   * @return The vertical alignment
   */
  public ElementAlignment getBarcodeVerticalAlignment ()
  {
    return barcodeVerticalAlignment;
  }

  /**
   * Set the barcode symbols vertical alignment on the drawable bounds
   *
   * @param barcodeVerticalAlignment The new vertical alignment
   */
  public void setBarcodeVerticalAlignment (
          final ElementAlignment barcodeVerticalAlignment)
  {
    this.barcodeVerticalAlignment = barcodeVerticalAlignment;
  }

  /**
   * Set the verticale aligment of the code
   *
   * @return The alignment
   */
  public ElementAlignment getCodeVerticalAlignment ()
  {
    return codeVerticalAlignment;
  }

  /**
   * Set the vertical aligment of the code according to the bacode symbols area.
   * ElementAlignment.TOP and ElementAlignment.BOTTOM are only ones values accepeted
   * (above and under)
   *
   * @param codeVerticalAlignment The vertical alignment
   */
  public void setCodeVerticalAlignment (final ElementAlignment codeVerticalAlignment)
  {
    this.codeVerticalAlignment = codeVerticalAlignment;
  }

  /**
   * Get the current font
   *
   * @return The font
   */
  public FontDefinition getFont ()
  {
    return font;
  }

  /**
   * Set the current font (Default font is SansSerif 10)
   *
   * @param font The new font
   */
  public void setFont (final FontDefinition font)
  {
    this.font = font;
  }

  /**
   * Get the font color
   *
   * @return The color
   */
  public Color getFontColor ()
  {
    return fontColor;
  }

  /**
   * Set the font color
   *
   * @param fontColor The color
   */
  public void setFontColor (final Color fontColor)
  {
    this.fontColor = fontColor;
  }

  /**
   * Get bar symbols color
   *
   * @return The color
   */
  public Color getBarcodeColor ()
  {
    return barcodeColor;
  }

  /**
   * Set bar symbols color
   *
   * @param barcodeColor The color
   */
  public void setBarcodeColor (final Color barcodeColor)
  {
    this.barcodeColor = barcodeColor;
  }

  /**
   * Get if the code have to be printed
   *
   * @return boolean
   */
  public boolean isShowCode ()
  {
    return showCode;
  }

  /**
   * Set if the code have to be printed or not
   *
   * @param showCode boolean
   */
  public void setShowCode (final boolean showCode)
  {
    this.showCode = showCode;
  }

  /**
   * Get the full barcode background color
   *
   * @return The color
   */
  public Color getBackGroundColor ()
  {
    return backGroundColor;
  }

  /**
   * Set the full barcode background color
   *
   * @param backGroundColor The color
   */
  public void setBackGroundColor (final Color backGroundColor)
  {
    this.backGroundColor = backGroundColor;
  }

  /**
   * Get the current quiet zones
   *
   * @return The Insets
   */
  public Insets getQuietZones ()
  {
    return quietZones;
  }

  /**
   * Set quiet zones around the bacode symbols area
   *
   * @param quietZone The quiet zone insets
   */
  public void setQuietZones (final Insets quietZone)
  {
    if (quietZone == null)
    {
      throw new NullPointerException();
    }
    this.quietZones = quietZone;
  }

  /**
   * Get the current stroke
   *
   * @return The stroke
   */
  public Stroke getBorder ()
  {
    return border;
  }

  /**
   * Set the stroke
   *
   * @param border The stroke
   */
  public void setBorder (final Stroke border)
  {
    this.border = border;
  }

  /**
   * Get the current stroke color
   *
   * @return The color
   */
  public Color getBorderColor ()
  {
    return borderColor;
  }

  /**
   * Set the stroke color
   *
   * @param borderColor The color
   */
  public void setBorderColor (final Color borderColor)
  {
    this.borderColor = borderColor;
  }

  /**
   * Compute the upper left barcode symbols area point
   *
   * @param fullBounds    The full barcode area
   * @param barcodeBounds The bacode symbols area
   * @return The upper left barcode symbols area point
   */
  protected Point2D getBarcodePoint (final Rectangle2D fullBounds,
                                  final Rectangle2D barcodeBounds)
  {
    Rectangle2D barcodeArea = alignCenter(fullBounds, barcodeBounds);

    if (this.codeVerticalAlignment == ElementAlignment.TOP)
    {
      barcodeArea = alignBottom(fullBounds, barcodeArea);

      final double alignedX = barcodeArea.getX() + quietZones.left;
      final double alignedY = barcodeArea.getY() -
              this.quietZones.bottom - this.margins.bottom;
      return new Point2D.Double (alignedX, alignedY);
    }
    else if (this.codeVerticalAlignment == ElementAlignment.BOTTOM)
    {
      barcodeArea = alignTop(fullBounds, barcodeArea);

      final double alignedX = barcodeArea.getX() + quietZones.left;
      final double alignedY = barcodeArea.getY() +
              this.quietZones.top + this.margins.top;
      return new Point2D.Double (alignedX, alignedY);
    }
    else
    {
      throw new IllegalArgumentException
              ("Code vertical alignment is invalid: " + this.getCodeVerticalAlignment());
    }
  }

  protected Rectangle2D alignCenter (final Rectangle2D outer,
                                     final Rectangle2D inner)
  {
    final double y = inner.getY();
    final double w = Math.min (inner.getWidth(), outer.getWidth());
    final double x = outer.getX() +
            ((outer.getWidth() - w) / 2);
    final double h = inner.getHeight();
    return new Rectangle2D.Double(x, y, w, h);
  }

  protected Rectangle2D alignTop (final Rectangle2D outer,
                                  final Rectangle2D inner)
  {
    final double x = inner.getX();
    final double y = outer.getY();
    final double h = Math.min (inner.getHeight(), outer.getHeight());
    final double w = Math.min(inner.getWidth(), outer.getWidth());

    return new Rectangle2D.Double(x, y, w, h);
  }

  protected Rectangle2D alignBottom (final Rectangle2D outer,
                                     final Rectangle2D inner)
  {
    final double h = Math.min (inner.getHeight(), outer.getHeight());
    final double y = (outer.getY() + outer.getHeight() - h);
    final double x = inner.getX();
    final double w = inner.getWidth();

    return new Rectangle2D.Double(x, y, w, h);
  }

  protected Rectangle2D alignLeft (final Rectangle2D outer,
                                   final Rectangle2D inner)
  {

    final double x = outer.getX();
    final double y = inner.getY();
    final double w = Math.min(inner.getWidth(), outer.getWidth());
    final double h = inner.getHeight();

    return new Rectangle2D.Double(x, y, w, h);
  }

  protected Rectangle2D alignMiddle (final Rectangle2D outer,
                                     final Rectangle2D inner)
  {
    final double x = inner.getX();
    final double h = Math.min (inner.getHeight(), outer.getHeight());
    final double y = (outer.getY() + ((outer.getHeight() - h) / 2));
    final double w = inner.getWidth();

    return new Rectangle2D.Double(x, y, w, h);
  }

  protected Rectangle2D alignRight (final Rectangle2D outer,
                                    final Rectangle2D inner)
  {

    final double w = Math.min (inner.getWidth(), outer.getWidth());
    final double x = (outer.getX() + outer.getWidth() - w);
    final double y = inner.getY();
    final double h = inner.getHeight();

    return new Rectangle2D.Double(x, y, w, h);
  }
  /**
   * Print the code
   *
   * @param graphics2D The graphics target
   * @param fullArea   The full barcode area
   * @param codeArea   The printed code area
   */
  protected void printCode (final Graphics2D graphics2D,
                         final Rectangle2D fullArea,
                         final Rectangle2D codeArea)
  {
    graphics2D.setFont(this.font.getFont());
    graphics2D.setColor(this.fontColor);

    final Rectangle2D alignedCodeArea = alignCenter(fullArea, codeArea);

    if (this.codeVerticalAlignment == ElementAlignment.TOP)
    {
      final Rectangle2D printArea = alignTop(fullArea, alignedCodeArea);
      final float x = (float) printArea.getX();
      final float y = (float) (printArea.getY() + printArea.getHeight() + getMargins().top);
      graphics2D.drawString(this.getCode(), x, y);
    }
    else if (this.codeVerticalAlignment == ElementAlignment.BOTTOM)
    {
      final Rectangle2D printArea = alignBottom(fullArea, alignedCodeArea);
      final float x = (float) printArea.getX();
      final float y = (float) (printArea.getMinY() + printArea.getHeight() - getMargins().bottom);
      graphics2D.drawString(this.getCode(), x, y);
    }
    else
    {
      throw new IllegalArgumentException("Code vertical alignment is invalid: " +
              this.getCodeVerticalAlignment());
    }
  }

  /**
   * Create the area representing the full barcode (barcode symbols and printed code)
   *
   * @param drawBounds The full drawable area available
   * @param codeArea    The printed code area
   * @param barcodeArea The barcode symbols area
   * @return The full barcode area (symbols + code)
   */
  protected Rectangle2D createFullArea (final Rectangle2D drawBounds,
                                     final Rectangle2D codeArea,
                                     final Rectangle2D barcodeArea)
  {

    Rectangle2D fullArea = barcodeArea.createUnion(codeArea);

    final Insets margins = getMargins();
    fullArea.setRect(0, 0,
            fullArea.getWidth() + margins.left + margins.right,
            fullArea.getHeight() + margins.top + margins.bottom);

    if (this.barcodeHorizontalAlignment == ElementAlignment.CENTER)
    {
      fullArea = alignCenter(drawBounds, fullArea);
    }
    else if (this.barcodeHorizontalAlignment == ElementAlignment.RIGHT)
    {
      fullArea = alignRight(drawBounds, fullArea);
    }
    else if (this.barcodeHorizontalAlignment == ElementAlignment.LEFT)
    {
      fullArea = alignLeft(drawBounds, fullArea);
    }
    else
    {
      throw new IllegalArgumentException
              ("Barcode horizontal alignment is invalid: " +
              this.barcodeHorizontalAlignment);
    }

    if (this.barcodeVerticalAlignment == ElementAlignment.TOP)
    {
      fullArea = alignTop(drawBounds, fullArea);
    }
    else if (this.barcodeVerticalAlignment == ElementAlignment.MIDDLE)
    {
      fullArea = alignMiddle(drawBounds, fullArea);
    }
    else if (this.barcodeVerticalAlignment == ElementAlignment.BOTTOM)
    {
      fullArea = alignBottom(drawBounds, fullArea);
    }
    else
    {
      throw new IllegalArgumentException
              ("Barcode vertical alignment is invalid: " +
              this.barcodeVerticalAlignment);
    }
    return fullArea;
  }

  //todo: bug stroke
  /**
   * Draw the barcode background.
   *
   * @param graphics2D The full drawable area available
   * @param fullArea   The full barcode drawable area
   */
  protected void printFullArea (final Graphics2D graphics2D, final Rectangle2D fullArea)
  {
    //set the background color
    if (this.backGroundColor != null)
    {
      graphics2D.setBackground(this.backGroundColor);
      graphics2D.setColor(this.backGroundColor);
      graphics2D.fill(fullArea);
    }

    //set borders
    if (this.getBorder() != null)
    {
      final Stroke oldStroke = graphics2D.getStroke();
      graphics2D.setStroke(this.border);
      graphics2D.setColor(this.borderColor);
      graphics2D.draw(fullArea);

      graphics2D.setStroke(oldStroke);
    }

  }

  /**
   * Create the area representing the symboles area according to the barcode symbols width
   * and the minimum height of a symbol.
   *
   * @param rectangle2D  The full drawable area available
   * @param barcodeWidth The width of the barcode barcode symbols
   * @return The barcode symbols area
   */
  protected Rectangle2D computeBarcodeArea (final Rectangle2D rectangle2D,
                                            final float barcodeWidth)
  {

    final Insets quietZones = this.getQuietZones();
    final Insets margins = this.getMargins();
    if (barcodeWidth > (rectangle2D.getWidth() - margins.left - margins.right))
    {
      throw new BarcodeSizeException
              ("The barcode width exceedes the bound width: barcode width=" +
              barcodeWidth, rectangle2D, margins, quietZones, this.getFont());
    }

    if (this.getMinHeight() > (rectangle2D.getHeight() - margins.top - margins.bottom))
    {
      throw new BarcodeSizeException
              ("The barcode height exceedes the bound height: barcode height=" +
              this.minHeight, rectangle2D, margins, quietZones, this.getFont());
    }

    return new Rectangle2D.Float(0, 0,
            barcodeWidth + quietZones.left + quietZones.right,
            this.getMinHeight() + getQuietZones().top + quietZones.bottom);
  }

  /**
   * Create the area representing the printed code according to the font and the code.
   *
   * @param rectangle2D The full drawable area available
   * @param barcodeMaxY The maximum Y value of the barcode symbol area
   * @return The code area
   */
  protected Rectangle2D computeCodeArea (final Rectangle2D rectangle2D,
                                         final float barcodeMaxY)
  {
    if (this.isShowCode())
    {
      final DefaultSizeCalculator calc = new DefaultSizeCalculator(this.getFont());
      final float codeWidth = calc.getStringWidth(this.getCode(), 0, this.getCode()
              .length());
      final float codeHeigh = calc.getLineHeight();

      if (codeWidth > (rectangle2D.getWidth() - this.getMargins().left - this.getMargins()
              .right))
      {
        Log.warn("The code width exceedes the bound width, disabling code string printing (Reduce font size).");
        this.setShowCode(false);
      }
      else if (codeHeigh > (rectangle2D.getHeight() - barcodeMaxY))
      {
        Log.warn("The code height exceedes the bound height, disabling code string printing (Reduce font size).");
        this.setShowCode(false);
      }
      else
      {
        return new Rectangle2D.Float(0, barcodeMaxY, codeWidth, codeHeigh);
      }
    }

    return new Rectangle2D.Float();
  }

  /**
   * Set some minimum values if not set, this methode is called on first line of draw()
   * method.
   *
   * @param graphics2D  graphics component
   * @param rectangle2D The full drawable area available
   * @see Barcode#draw(java.awt.Graphics2D, java.awt.geom.Rectangle2D)
   */
  protected void init (final Graphics2D graphics2D, final Rectangle2D rectangle2D)
  {

    if (this.getMinWidth() == 0)
    {
      this.setMinWidth(0.5f);
    }
    //set minHeight if not set 0.25 inches
    if (this.getMinHeight() == 0)
    {
      this.setMinHeight(18);
    }

  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    final Barcode barcode = (Barcode) super.clone();
    barcode.font = (FontDefinition) this.font.clone();
    barcode.margins = (Insets) this.margins.clone();
    barcode.quietZones = (Insets) this.quietZones.clone();
    return barcode;
  }
}
