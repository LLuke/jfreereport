/*
* Barcode.java
*
* Created on 25 mars 2004, 01:15
*/

package org.jfree.report.ext.modules.barcode;

import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.output.pageable.base.operations.*;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;
import org.jfree.ui.Drawable;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Mimil
 *
 */
public abstract class Barcode implements Drawable, Cloneable {
    //todo: space for comment??: isbn number...
    //todo: new exception: BarcodeIllegalArgument
    //todo: remove the Log.warn and put "... and derivated" 
    private FontDefinition font = new FontDefinition("SansSerif", 10);
    private Color fontColor = Color.BLACK;
    private Color barcodeColor = Color.BLACK;
    private Color backGroundColor = null;
    private ElementAlignment barcodeVerticalAlignment = ElementAlignment.TOP;
    private ElementAlignment barcodeHorizontalAlignment = ElementAlignment.LEFT;
    private ElementAlignment codeVerticalAlignment = ElementAlignment.BOTTOM;
    private Stroke border = null;
    private Color borderColor = Color.BLACK;
    private float minHeight = 0;
    private float minWidth = 0;
    private Insets margins = new Insets(10, 10, 10, 10);
    private String code = null;
    private boolean showCode = true;
    private Insets quietZones = new Insets(0, 0, 0, 0);     //10x the minimun size


    /**
     * Creates a new instance of Barcode
     */
    public Barcode(String code) {
        if(code == null) {
            throw new NullPointerException("Barcode code must be not null.");
        }
    }

    /**
     * Creates a new instance of Barcode, can only be used by derivated class
     */
    protected Barcode() {}

    /**
     * Get the string code
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the sting code
     *
     * @param code the string to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get margins around the barcode
     *
     * @return margins current margins
     */
    public Insets getMargins() {
        return margins;
    }

    /**
     * Set the margins around the barcode
     *
     * @param margin New margins
     */
    public void setMargins(Insets margin) {
        this.margins = margin;
    }

    /**
     * Get the minimum width of a "single bar"
     *
     * @return  The minimum width in points
     */
    public float getMinWidth() {
        return minWidth;
    }

    /**
     * Set the minimum width of a "single bar"
     *
     * @param minWidth  The minimum width in points
     */
    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    /**
     * Get the height of a bar
     *
     * @return The height in points
     */
    public float getMinHeight() {
        return minHeight;
    }

    /**
     * Set the height of a bar
     *
     * @param minHeight Height in points
     */
    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }

    /**
     * Get the barcode symbols horizontal alignment
     * @return
     */
    public ElementAlignment getBarcodeHorizontalAlignment() {
        return barcodeHorizontalAlignment;
    }

    /**
     * Set the barcode symbols horizontal alignment on the drawable bounds
     *
     * @param barcodeHorizontalAlignment    The new horizontal alignment
     */
    public void setBarcodeHorizontalAlignment(ElementAlignment barcodeHorizontalAlignment) {
        this.barcodeHorizontalAlignment = barcodeHorizontalAlignment;
    }

    /**
     * Get the barcode symbols vertical alignment
     *
     * @return The vertical alignment
     */
    public ElementAlignment getBarcodeVerticalAlignment() {
        return barcodeVerticalAlignment;
    }

    /**
     * Set the barcode symbols vertical alignment on the drawable bounds
     *
     * @param barcodeVerticalAlignment The new vertical alignment
     */
    public void setBarcodeVerticalAlignment(ElementAlignment barcodeVerticalAlignment) {
        this.barcodeVerticalAlignment = barcodeVerticalAlignment;
    }

    /**
     * Set the verticale aligment of the code
     *
     * @return  The alignment
     */
    public ElementAlignment getCodeVerticalAlignment() {
        return codeVerticalAlignment;
    }

    /**
     * Set the vertical aligment of the code according to the bacode symbols area.
     * ElementAlignment.TOP and ElementAlignment.BOTTOM are only ones values accepeted (above and under)
     *
     * @param codeVerticalAlignment The vertical alignment
     */
    public void setCodeVerticalAlignment(ElementAlignment codeVerticalAlignment) {
        this.codeVerticalAlignment = codeVerticalAlignment;
    }

    /**
     * Get the current font
     *
     * @return  The font
     */
    public FontDefinition getFont() {
        return font;
    }

    /**
     * Set the current font (Default font is SansSerif 10)
     *
     * @param font The new font
     */
    public void setFont(FontDefinition font) {
        this.font = font;
    }

    /**
     * Get the font color
     *
     * @return  The color
     */
    public Color getFontColor() {
        return fontColor;
    }

    /**
     * Set the font color
     *
     * @param fontColor The color
     */
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * Get bar symbols color
     *
     * @return  The color
     */
    public Color getBarcodeColor() {
        return barcodeColor;
    }

    /**
     * Set bar symbols color
     *
     * @param barcodeColor  The color
     */
    public void setBarcodeColor(Color barcodeColor) {
        this.barcodeColor = barcodeColor;
    }

    /**
     * Get if the code have to be printed
     *
     * @return  boolean
     */
    public boolean isShowCode() {
        return showCode;
    }

    /**
     * Set if the code have to be printed or not
     *
     * @param showCode  boolean
     */
    public void setShowCode(boolean showCode) {
        this.showCode = showCode;
    }

    /**
     * Get the full barcode background color
     *
     * @return  The color
     */
    public Color getBackGroundColor() {
        return backGroundColor;
    }

    /**
     * Set the full barcode background color
     *
     * @param backGroundColor The color
     */
    public void setBackGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    /**
     * Get the current quiet zones
     *
     * @return The Insets
     */
    public Insets getQuietZones() {
        return quietZones;
    }

    /**
     * Set quiet zones around the bacode symbols area
     *
     * @param quietZone The quiet zone insets
     */
    public void setQuietZones(Insets quietZone) {
        this.quietZones = quietZone;
    }

    /**
     * Get the current stroke
     *
     * @return  The stroke
     */
    public Stroke getBorder() {
        return border;
    }

    /**
     * Set the stroke
     *
     * @param border The stroke
     */
    public void setBorder(Stroke border) {
        this.border = border;
    }

    /**
     * Get the current stroke color
     *
     * @return The color
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Set the stroke color
     *
     * @param borderColor The color
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Compute the upper left barcode symbols area point
     *
     * @param fullArea      The full barcode area
     * @param barcodeArea   The bacode symbols area
     * @return  The upper left barcode symbols area point
     */
    public Point2D getBarcodePoint(Rectangle2D fullArea, Rectangle2D barcodeArea) {
        VerticalBoundsAlignment vertical;
        HorizontalBoundsAlignment horizontal = new CenterAlignment(fullArea);

        barcodeArea = horizontal.align(barcodeArea);

        if (this.codeVerticalAlignment == ElementAlignment.TOP) {
            vertical = new BottomAlignment(fullArea);
            barcodeArea = vertical.align(barcodeArea);

            return new Point2D.Float((float) barcodeArea.getMinX() + this.quietZones.left, (float) barcodeArea.getMinY() - this.quietZones.bottom - this.margins.bottom);
        } else if (this.codeVerticalAlignment == ElementAlignment.BOTTOM) {
            vertical = new TopAlignment(fullArea);
            barcodeArea = vertical.align(barcodeArea);

            return new Point2D.Float((float) barcodeArea.getMinX() + this.quietZones.left, (float) barcodeArea.getMinY() + this.quietZones.top + this.margins.top);
        } else {
            Log.error("Code vertical alignment is invalid: " + this.getCodeVerticalAlignment().toString(), new SAXException("Invalid vertical alignment"));
        }

        return new Point2D.Float();
    }

    /**
     * Print the code
     *
     * @param graphics2D    The graphics target
     * @param fullArea      The full barcode area
     * @param codeArea      The printed code area
     */
    public void printCode(Graphics2D graphics2D, Rectangle2D fullArea, Rectangle2D codeArea) {
        VerticalBoundsAlignment vertical;
        HorizontalBoundsAlignment horizontal = new CenterAlignment(fullArea);

        graphics2D.setFont(this.font.getFont());
        graphics2D.setColor(this.fontColor);

        codeArea = horizontal.align(codeArea);

        if (this.codeVerticalAlignment == ElementAlignment.TOP) {
            vertical = new TopAlignment(fullArea);
            codeArea = vertical.align(codeArea);

            graphics2D.drawString(this.getCode(), (float) codeArea.getMinX(), (float) (codeArea.getMinY() + codeArea.getHeight() + this.getMargins().top));
        } else if (this.codeVerticalAlignment == ElementAlignment.BOTTOM) {
            vertical = new BottomAlignment(fullArea);
            codeArea = vertical.align(codeArea);

            graphics2D.drawString(this.getCode(), (float) codeArea.getMinX(), (float) (codeArea.getMinY() + codeArea.getHeight() - this.getMargins().bottom));
        } else {
            Log.error("Code vertical alignment is invalid: " + this.getCodeVerticalAlignment().toString(), new SAXException("Invalid vertical alignment"));
        }
    }

    /**
     * Create the area representing the full barcode (barcode symbols and printed code)
     *
     * @param rectangle2D   The full drawable area available
     * @param codeArea      The printed code area
     * @param barcodeArea   The barcode symbols area
     * @return  The full barcode area (symbols + code)
     */
    public Rectangle2D createFullArea(Rectangle2D rectangle2D, Rectangle2D codeArea, Rectangle2D barcodeArea) {
        BoundsAlignment vertical = null;
        BoundsAlignment horizontal = null;

        Rectangle2D fullArea = barcodeArea.createUnion(codeArea);

        fullArea.setRect(0, 0,
                fullArea.getWidth() + this.getMargins().left + this.getMargins().right,
                fullArea.getHeight() + this.getMargins().top + this.getMargins().bottom);

        if (this.barcodeHorizontalAlignment == ElementAlignment.CENTER) {
            vertical = new CenterAlignment(rectangle2D);
        } else if (this.barcodeHorizontalAlignment == ElementAlignment.RIGHT) {
            vertical = new RightAlignment(rectangle2D);
        } else if (this.barcodeHorizontalAlignment == ElementAlignment.LEFT) {
            vertical = new LeftAlignment(rectangle2D);
        } else {
            Log.error("Barcode horizontal alignment is invalid: " + this.barcodeHorizontalAlignment.toString(), new SAXException("Invalid horizontal alignment"));
        }

        fullArea = vertical.align(fullArea);


        if (this.barcodeVerticalAlignment == ElementAlignment.TOP) {
            horizontal = new TopAlignment(rectangle2D);
        } else if (this.barcodeVerticalAlignment == ElementAlignment.MIDDLE) {
            horizontal = new MiddleAlignment(rectangle2D);
        } else if (this.barcodeVerticalAlignment == ElementAlignment.BOTTOM) {
            horizontal = new BottomAlignment(rectangle2D);
        } else {
            Log.error("Barcode vertical alignment is invalid: " + this.barcodeVerticalAlignment.toString(), new SAXException("Invalid vertical alignment"));
        }

        fullArea = horizontal.align(fullArea);

        return fullArea;
    }

    //todo: bug stroke
    /**
     * Draw the barcode background.
     *
     * @param graphics2D    The full drawable area available
     * @param fullArea      The full barcode drawable area
     */
    public void printFullArea(Graphics2D graphics2D, Rectangle2D fullArea) {
        //set the background color
        if (this.backGroundColor != null) {
            graphics2D.setBackground(this.backGroundColor);
            graphics2D.setColor(this.backGroundColor);
            graphics2D.fill(fullArea);
        }

        //set borders
        if (this.getBorder() != null) {
            Stroke oldStroke = graphics2D.getStroke();
            graphics2D.setStroke(this.border);
            graphics2D.setColor(this.borderColor);
            graphics2D.draw(fullArea);

            graphics2D.setStroke(oldStroke);
        }

    }

    /**
     * Create the area representing the symboles area according to the barcode symbols width and the
     * minimum height of a symbol.
     *
     * @param rectangle2D   The full drawable area available
     * @param barcodeWidth  The width of the barcode barcode symbols
     * @return  The barcode symbols area
     */
    public Rectangle2D computeBarcodeArea(Rectangle2D rectangle2D, float barcodeWidth) {

        if (barcodeWidth > (rectangle2D.getWidth() - this.getMargins().left - this.getMargins().right)) {
            Log.error("The barcode width exceedes the bound width",
                    new BarcodeSizeException("The barcode width exceedes the bound width: barcode width=" + barcodeWidth, rectangle2D, this.getMargins(), this.getQuietZones(), this.getFont()));
        }

        if (this.getMinHeight() > (rectangle2D.getHeight() - this.getMargins().top - this.getMargins().bottom)) {
            Log.error("The barcode height exceedes the bound height",
                    new BarcodeSizeException("The barcode height exceedes the bound height: barcode height=" + this.minHeight, rectangle2D, this.getMargins(), this.getQuietZones(), this.getFont()));
        }

        return new Rectangle2D.Float(0, 0,
                barcodeWidth + this.getQuietZones().left + this.getQuietZones().right,
                this.getMinHeight() + getQuietZones().top + this.getQuietZones().bottom);

    }

    /**
     * Create the area representing the printed code according to the font and the code.
     *
     * @param rectangle2D   The full drawable area available
     * @param barcodeMaxY   The maximum Y value of the barcode symbol area
     * @return  The code area
     */
    public Rectangle2D computeCodeArea(Rectangle2D rectangle2D, float barcodeMaxY) {
        if (this.isShowCode()) {
            BarcodeSizeCalculator calc = new BarcodeSizeCalculator(this.getFont());
            float codeWidth = calc.getStringWidth(this.getCode(), 0, this.getCode().length());
            float codeHeigh = calc.getLineHeight();

            if (codeWidth > (rectangle2D.getWidth() - this.getMargins().left - this.getMargins().right)) {
                Log.warn("The code width exceedes the bound width, disabling code string printing (Reduce font size).");
                this.setShowCode(false);
            } else if (codeHeigh > (rectangle2D.getHeight() - barcodeMaxY)) {
                Log.warn("The code height exceedes the bound height, disabling code string printing (Reduce font size).");
                this.setShowCode(false);
            } else {
                return new Rectangle2D.Float(0, barcodeMaxY, codeWidth, codeHeigh);
            }
        }

        return new Rectangle2D.Float();
    }

    /**
     * Set some minimum values if not set, this methode is called on first line of draw() method.
     * @see Barcode#draw(java.awt.Graphics2D, java.awt.geom.Rectangle2D)
     *
     * @param graphics2D    graphics component
     * @param rectangle2D   The full drawable area available
     */
    public void init(Graphics2D graphics2D, Rectangle2D rectangle2D) {

        if (this.getMinWidth() == 0) {
            this.setMinWidth(0.5f);
        }
        //set minHeight if not set 0.25 inches
        if (this.getMinHeight() == 0) {
            this.setMinHeight(18);
        }

    }

    //todo: do clones
    public Object clone() throws CloneNotSupportedException {
        Barcode barcode = (Barcode) super.clone();
        barcode.font = (FontDefinition) this.font.clone();
        //barcode.barcodeColor = new Color(this.barcodeColor);


        return barcode;
    }
}
