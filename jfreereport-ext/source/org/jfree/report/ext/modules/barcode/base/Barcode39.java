/*
* Barcode39.java
*
* Created on 27 mars 2004, 22:03
*/

package org.jfree.report.ext.modules.barcode.base;

import org.jfree.report.ext.modules.barcode.Barcode;
import org.jfree.report.util.Log;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


/**
 * Encodes a string into code39 specifications
 *
 * Symbols allowed: 0-9 A-Z $ % * + - . / space
 * Start character: yes, in the symbols table (*)
 * Stop character: yes, in the symbols table (*)
 * Check character: available
 *
 * Also know as:
 * - USD3
 * - 3of9
 * - HIBC
 * - LOGMARS
 *
 * @author Mimil
 */
public class Barcode39 extends Barcode {
    /**Multiplier coeficient between the width a narrow bar and a wide one*/
    private float narrowToWideMultiplier = 3;
    /**Table holding symbols to be drawn*/
    protected ArrayList codeTable;
    /**Characters allowed*/
    protected static String CHARTABLE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*";
    /**Checksum character*/
    private char checkSum;

    private boolean showStartStop = false;

    private boolean checkSumNeeded = false;

    private boolean showCheckSum = false;

    /**Symbols allowed*/
    protected static byte TABLE[][] = {
        {0, 0, 0, 1, 1, 0, 1, 0, 0},
        {1, 0, 0, 1, 0, 0, 0, 0, 1},
        {0, 0, 1, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 1, 0, 0, 0, 1},
        {1, 0, 0, 1, 1, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 1, 0, 1},
        {1, 0, 0, 1, 0, 0, 1, 0, 0},
        {0, 0, 1, 1, 0, 0, 1, 0, 0},
        {1, 0, 0, 0, 0, 1, 0, 0, 1},
        {0, 0, 1, 0, 0, 1, 0, 0, 1},
        {1, 0, 1, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 0, 0, 1},
        {1, 0, 0, 0, 1, 1, 0, 0, 0},
        {0, 0, 1, 0, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 0, 0},
        {0, 0, 1, 0, 0, 1, 1, 0, 0},
        {0, 0, 0, 0, 1, 1, 1, 0, 0},
        {1, 0, 0, 0, 0, 0, 0, 1, 1},
        {0, 0, 1, 0, 0, 0, 0, 1, 1},
        {1, 0, 1, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 1, 0, 0, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 1, 0},
        {0, 0, 1, 0, 1, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 1, 1, 0},
        {0, 0, 1, 0, 0, 0, 1, 1, 0},
        {0, 0, 0, 0, 1, 0, 1, 1, 0},
        {1, 1, 0, 0, 0, 0, 0, 0, 1},
        {0, 1, 1, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 1, 0, 0, 0, 1},
        {1, 1, 0, 0, 1, 0, 0, 0, 0},
        {0, 1, 1, 0, 1, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 1, 0, 1},
        {1, 1, 0, 0, 0, 0, 1, 0, 0},
        {0, 1, 1, 0, 0, 0, 1, 0, 0},
        {0, 1, 0, 1, 0, 1, 0, 0, 0},
        {0, 1, 0, 1, 0, 0, 0, 1, 0},
        {0, 1, 0, 0, 0, 1, 0, 1, 0},
        {0, 0, 0, 1, 0, 1, 0, 1, 0},
        {0, 1, 0, 0, 1, 0, 1, 0, 0}
    };

    /**
     * Creates a new instance of Barcode39, can only be used by derivated class
     */
    protected Barcode39() {}

    /**
     * Creates a new instance of Barcode39
     */
    public Barcode39(String code) {
        super(code);

        this.codeTable = new ArrayList();
        int check = 0;

        this.codeTable.add(TABLE[CHARTABLE.indexOf('*')]);   //add the start character

        for( int i=0; i<code.length(); i++) {

            int index = CHARTABLE.indexOf(code.charAt(i));

            if( (index < 0) || (index == CHARTABLE.length()-1)) {    //not found or '*' found
                throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 39.");

            }else {
                this.codeTable.add(TABLE[index]);
                check += index;
            }
        }


        this.checkSum = CHARTABLE.charAt(check%43);     //compute the checksum

        this.codeTable.add(TABLE[CHARTABLE.indexOf('*')]);      //add the stop character

        super.setCode(code);
    }

    /**
     * Draws the barcode representation of the code on <code>graphics2D</code> target
     * according to <code>rectangle2D</code> window bounds.
     *
     * @param graphics2D    The drawable target
     * @param rectangle2D   The drawable bounds
     */
    public void draw(java.awt.Graphics2D graphics2D, java.awt.geom.Rectangle2D rectangle2D) {
        this.init(graphics2D, rectangle2D);

        //easy to know the final width of the barcode before drawing it
        //(3*this.getWideToNarowMultiplier()*nbChars*this.getMinWidth() 3 wides bars by char
        //(6*nbChars*this.getMinWidth())    6 narrows bars
        //((nbChars-1)*this.getMinWidth()   inter-characters
        int nbChars = this.codeTable.size();
        float barcodeWidth = (((3*this.getNarrowToWideMultiplier()+6)*nbChars)+(nbChars-1))*this.getMinWidth();

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

    /*public void setMinWidth(float minWidth) {
    if(minWidth < 0.5f) {//in reality 0.54, but it is impossible to draw the barcode correctly it is not a mutiple of 0.25 (a graphical point)
    Log.warn("Smallest element height must be greater than 0.5 points, anything smaller you use at your own risk. (Please use 0.25 multiple)");
    }

    super.setMinWidth(minWidth);
    }*/

    //overriding getcode methode
    /**
     * Returns the code of this barcode according to its options.
     *
     * @return  The corresponding code
     */
    public String getCode() {
        if(this.showStartStop) {
            if (this.showCheckSum) {
                return ("*"+super.getCode()+this.getCheckSum()+"*");
            }else {
                return ("*"+super.getCode()+"*");
            }
        } else {
            if(this.showCheckSum) {
                return super.getCode()+this.getCheckSum();
            }else {
                return super.getCode();
            }
        }
    }

    /**
     * Tells if the checksum have to be used.
     *
     * @return Boolean
     */
    public boolean isCheckSumNeeded() {
        return checkSumNeeded;
    }

    /**
     * Activates, or not, the use of the checksum character.
     *
     * @param needCheckSum  Boolean
     */
    public void setCheckSumNeeded(boolean needCheckSum) {
        String oldCode = super.getCode();

        if(this.checkSumNeeded == false && needCheckSum == true) {
            this.setCode(oldCode + this.getCheckSum());
            this.codeTable.add( this.codeTable.size()-1, TABLE[CHARTABLE.indexOf(this.getCheckSum())]);
            this.checkSumNeeded = true;
        } else if(this.checkSumNeeded == true && needCheckSum == false) {
            Log.warn("Removing the CheckSum.");
            this.codeTable.remove(this.codeTable.size()-1);
            this.setCode(oldCode.substring(0, oldCode.length()-1));
            this.checkSumNeeded = false;
        }
    }

    /**
     * Returns the checksum character for this code.
     *
     * @return  The check character
     */
    public char getCheckSum() {
        return checkSum;
    }

    /**
     * Tells if start and stop characters have to shown in <code>getCode()</code>.
     * <p>
     * In code39 they are '*'.
     *
     * @return Boolean
     */
    public boolean isShowStartStop() {
        return showStartStop;
    }

    /**
     * Activates, or not, the print of characters start and stop.
     * @param showStartStop Boolean
     */
    public void setShowStartStop(boolean showStartStop) {
        this.showStartStop = showStartStop;
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
     * @param WideToNarowMultiplier The new coeficient to set
     */
    public void setNarrowToWideMultiplier(float WideToNarowMultiplier) {
        /*if((WideToNarowMultiplier <= 1.8f) || (WideToNarowMultiplier >= 3.4f)) {
        Log.warn("Wide to narrow coeffecient must be between 1.8 and 3.4, anything else you use at your own risk.");
        }

        if((this.getMinWidth() <= 1.44f) && (WideToNarowMultiplier < 2.5f)) {
        Log.warn("Wide to narrow coeffecient must be at least 2.5 if the smallest element width is at most 1.44 points, anything else you use at your own risk.");
        }*/

        this.narrowToWideMultiplier = WideToNarowMultiplier;
    }

    /**
     * Tells if the checksun character will be shown in <code>getCode</code>.
     *
     * @return  Boolean
     */
    public boolean isShowCheckSum() {
        return showCheckSum;
    }

    /**
     * Activates, or not, the print of the checksum character.
     *
     * @param showCheckSum  Boolean
     */
    public void setShowCheckSum(boolean showCheckSum) {
        this.showCheckSum = showCheckSum;
    }
}
