package org.jfree.report.ext.modules.barcode.base;

import org.jfree.report.ext.modules.barcode.Barcode;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Encodes a string into codabar specifications
 *
 * Symbols allowed: 0-9 $ : + - . /
 * Start character: yes, 4 differents   (default A)
 * Stop character: yes, 4 differents (same as above)    (Default B)
 * Check character: not available
 *
 * @author Mimil
 */
public class BarcodeCodabar extends Barcode{

    /**Characters allowed*/
    protected static String CHARTABLE = "0123456789-$:/.+";

    /**First Start-Stop symbol*/
    protected static byte STARTSTOPA[] = {0,0,1,1,0,1,0};

    /**Second Start-Stop symbol*/
    protected static byte STARTSTOPB[] = {0,0,0,1,0,1,1};

    /**Third Start-Stop symbol*/
    protected static byte STARTSTOPC[] = {0,1,0,1,0,0,1};

    /**Fourth Start-Stop symbol*/
    protected static byte STARTSTOPD[] = {0,0,0,1,1,1,0};

    private byte defaultStart[] = STARTSTOPA;

    private byte defaultStop[] = STARTSTOPB;

    private float narrowToWideMultiplier = 2;

    /**Table holding symbols to be drawn*/
    protected ArrayList codeTable;

    /**Symbols allowed*/
    protected static byte TABLE[][] = {
        {0,0,0,0,0,1,1},
        {0,0,0,0,1,1,0},
        {0,0,0,1,0,0,1},
        {1,1,0,0,0,0,0},
        {0,0,1,0,0,1,0},
        {1,0,0,0,0,1,0},
        {0,1,0,0,0,0,1},
        {0,1,0,0,1,0,0},
        {0,1,1,0,0,0,0},
        {1,0,0,1,0,0,0},
        {0,0,0,1,1,0,0},
        {0,0,1,1,0,0,0},
        {1,0,0,0,1,0,1},
        {1,0,1,0,0,0,1},
        {1,0,1,0,1,0,0},
        {0,0,1,1,1,1,1}
    };

    /**
     * Creates a new instance of BarcodeCodabar, can only be used by derivated class
     */
    protected BarcodeCodabar() {}

    /**
     * Creates a new instance of BarcodeCodabar
     */
    public BarcodeCodabar(String code) {
        super(code);
        this.codeTable = new ArrayList();

        this.codeTable.add(defaultStart);   //start character

        for( int i=0; i<code.length(); i++) {

            int index = CHARTABLE.indexOf(code.charAt(i));

            if( (index < 0) || (index == CHARTABLE.length())) {    //not found
                throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in codabar.");
            }else {
                this.codeTable.add(TABLE[index]);

            }
        }

        this.codeTable.add(defaultStop);   //stop character

        super.setCode(code);
    }

    /**
     * Draws the barcode representation of the code on <code>graphics2D</code> target
     * according to <code>rectangle2D</code> window bounds.
     *
     * @param graphics2D    The drawable target
     * @param rectangle2D   The drawable bounds
     */
    public void draw(Graphics2D graphics2D, Rectangle2D rectangle2D) {

        this.init(graphics2D, rectangle2D);

        int narrows = 0;
        int wides = 0;

        for (int i=0; i<this.codeTable.size(); i++) {
            byte tmpChar[] = (byte[])this.codeTable.get(i);

            for(int j=0; j<7; j++) {
                if(tmpChar[j]==0) {
                    narrows++;
                } else {
                    wides++;
                }
            }
        }

        // narrows bars (n narrow bars)
        // wides*narrowtowidemultiplier (n wide bars)
        //nbchar-1 intercharcters
        int nbChars = this.codeTable.size();
        float barcodeWidth = this.getMinWidth() * (narrows +wides*this.narrowToWideMultiplier + 1*(nbChars-1));


        Rectangle2D barcodeArea = this.computeBarcodeArea(rectangle2D, barcodeWidth);
        Rectangle2D codeArea = this.computeCodeArea(rectangle2D, (float)barcodeArea.getMaxY());
        Rectangle2D fullArea = this.createFullArea(rectangle2D, codeArea, barcodeArea);

        this.printFullArea(graphics2D, fullArea);

        if(this.isShowCode()) {
            this.printCode(graphics2D, fullArea, codeArea);
        }

        //now lets draw the barcode
        graphics2D.setColor(this.getBarcodeColor());

        Point2D p = this.getBarcodePoint(fullArea, barcodeArea);
        float xPointer = (float)p.getX();
        float yPointer = (float)p.getY();

        //LETS DRAW NOW I SAID !
        for(int i=0; i<nbChars; i++) {
            byte[] currentByte = (byte[])codeTable.get(i);
            for(int j=0; j< currentByte.length; j++) {
                if (j%2 == 0) { //even == bar
                    if(currentByte[j]==0) { //narrow
                        graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));

                        xPointer += this.getMinWidth();
                    } else {    //wide
                        graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth() * this.getNarrowToWideMultiplier(), this.getMinHeight()));

                        xPointer += this.getMinWidth() * this.getNarrowToWideMultiplier();
                    }
                } else {  //odd == space
                    if(currentByte[j]==0) { //narrow
                        xPointer += this.getMinWidth();
                    } else {    //wide
                        xPointer += this.getMinWidth() * this.getNarrowToWideMultiplier();
                    }
                }
            }

            xPointer += this.getMinWidth(); //inter-character
        }
    }

    /**
     * Return the current Stop symbol of this barcode
     *
     * @return  The current Stop symbol
     */
    public byte[] getDefaultStop() {
        return defaultStop;
    }

    /**
     * Changes the Stop symbol of this barcode
     *
     * @param defaultStop The new Stop symbol to set
     */
    public void setDefaultStop(byte[] defaultStop) {
        this.defaultStop = defaultStop;

        this.codeTable.remove(this.codeTable.size());
        this.codeTable.add(defaultStop);
    }

    /**
     * Return the current Start symbol of this barcode
     *
     * @return  The current Start symbol
     */
    public byte[] getDefaultStart() {
        return defaultStart;
    }

    /**
     * Changes the Start symbol of this barcode
     *
     * @param defaultStart The new Start symbol to set
     */
    public void setDefaultStart(byte[] defaultStart) {
        this.defaultStart = defaultStart;

        this.codeTable.remove(0);
        this.codeTable.add(0, defaultStart);
    }

    /**
     * Returns the coeficient between narrow and wide bars.
     *
     * @return  The coeficient
     */
    public float getNarrowToWideMultiplier() {
        return narrowToWideMultiplier;
    }

    /**
     * Sets the multiplyer coefficient between narrow and wide bars for this barcode.
     *
     * @param narrowToWideMultiplier The new coeficient to set
     */
    public void setNarrowToWideMultiplier(float narrowToWideMultiplier) {
        this.narrowToWideMultiplier = narrowToWideMultiplier;
    }
}
