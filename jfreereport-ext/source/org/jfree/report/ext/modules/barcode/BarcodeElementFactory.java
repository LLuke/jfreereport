package org.jfree.report.ext.modules.barcode;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.ext.modules.barcode.filters.Barcode39Filter;
import org.jfree.report.filter.DataFilter;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DataSource;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: 25 avr. 2004
 * Time: 21:28:48
 * To change this template use File | Settings | File Templates.
 */
public class BarcodeElementFactory extends ElementFactory{

    private static final int CODE39 = 1;
    private static final int USD3 = 1;
    private static final int AKA3of9 = 1;

    private int currentType;

    /** The fieldname of the datarow from where to read the content. */
    private String fieldname;

    /**
     * Font used to print the code
     */
    private FontDefinition font = new FontDefinition("SansSerif", 10);

    /**
     * The font color
     */
    private Color fontColor = Color.BLACK;

    /**
     * Barcode symbols draw color (bars)
     */
    private Color barcodeColor = Color.BLACK;

    /**
     * BackGround color of the whole barcode generated
     */
    private Color backGroundColor = null;

    /**
     * Barcode vertical alignment
     */
    private ElementAlignment barcodeVerticalAlignment = ElementAlignment.TOP;

    /**
     * Barcode horizontal alignment
     */
    private ElementAlignment barcodeHorizontalAlignment = ElementAlignment.LEFT;

    /**
     * Code vertical alignment
     */
    private ElementAlignment codeVerticalAlignment = ElementAlignment.BOTTOM;

    /**
     * Border or the whole barcode
     */
    private Stroke border = null;

    /**
     * Border color
     */
    private Color borderColor = Color.BLACK;

    /**
     * The bar height
     */
    private float minHeight = 0;

    /**
     * The bar wadth
     */
    private float minWidth = 0;

    /**
     * Margins added to the whole barcode
     */
    private Insets margins = new Insets(10, 10, 10, 10);

    /**
     * Code string
     */
    private String code = null;

    /**
     * If the code have to be shown
     */
    private boolean showCode = true;

    /**
     * Blanck margins zones arround the barcode to make it readable by scanners
     */
    private Insets quietZones = new Insets(0, 0, 0, 0);     //10x the minimun size



    public BarcodeElementFactory(int type) {
        this.currentType = type;
    }



    public Element createElement() {
        DataSource dataSource = null;
        DataFilter filter = null;

        if (this.getFieldname() == null)
        {
            throw new IllegalStateException("Fieldname is not set.");
        }

        switch (this.currentType) {
            case CODE39:
                filter = new Barcode39Filter();
                filter.setDataSource(new DataRowDataSource(this.getFieldname()));

                dataSource = filter;
                break;

            default: throw new IllegalArgumentException("Unknown barcode type.");
        }

        final DrawableElement element = new DrawableElement();
        applyElementName(element);
        element.setDataSource(dataSource);
        applyStyle(element.getStyle());

        return element;
    }

    public static Element createBarcodeElement() {
        //have to create an elemant according to parameters
        return null;
    }

    protected void applyStyle(ElementStyleSheet style) {
        super.applyStyle(style);
        style.setStyleProperty(ElementStyleSheet.ALIGNMENT, this.getBarcodeHorizontalAlignment());
        style.setStyleProperty(ElementStyleSheet.VALIGNMENT, this.getBarcodeVerticalAlignment());
    }


    /**
     * Returns the field name from where to read the content of the element.
     *
     * @return the field name.
     */
    public String getFieldname()
    {
        return fieldname;
    }

    /**
     * Defines the field name from where to read the content of the element.
     * The field name is the name of a datarow column.
     *
     * @param fieldname the field name.
     */
    public void setFieldname(final String fieldname)
    {
        this.fieldname = fieldname;
    }

    public FontDefinition getFont() {
        return font;
    }

    public void setFont(FontDefinition font) {
        this.font = font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public Color getBarcodeColor() {
        return barcodeColor;
    }

    public void setBarcodeColor(Color barcodeColor) {
        this.barcodeColor = barcodeColor;
    }

    public Color getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public ElementAlignment getBarcodeVerticalAlignment() {
        return barcodeVerticalAlignment;
    }

    public void setBarcodeVerticalAlignment(ElementAlignment barcodeVerticalAlignment) {
        this.barcodeVerticalAlignment = barcodeVerticalAlignment;
    }

    public ElementAlignment getBarcodeHorizontalAlignment() {
        return barcodeHorizontalAlignment;
    }

    public void setBarcodeHorizontalAlignment(ElementAlignment barcodeHorizontalAlignment) {
        this.barcodeHorizontalAlignment = barcodeHorizontalAlignment;
    }

    public ElementAlignment getCodeVerticalAlignment() {
        return codeVerticalAlignment;
    }

    public void setCodeVerticalAlignment(ElementAlignment codeVerticalAlignment) {
        this.codeVerticalAlignment = codeVerticalAlignment;
    }

    public Stroke getBorder() {
        return border;
    }

    public void setBorder(Stroke border) {
        this.border = border;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    public Insets getMargins() {
        return margins;
    }

    public void setMargins(Insets margins) {
        this.margins = margins;
    }

    public boolean isShowCode() {
        return showCode;
    }

    public void setShowCode(boolean showCode) {
        this.showCode = showCode;
    }

    public Insets getQuietZones() {
        return quietZones;
    }

    public void setQuietZones(Insets quietZones) {
        this.quietZones = quietZones;
    }
}
