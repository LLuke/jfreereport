package org.jfree.report.ext.modules.barcode.base;

import org.jfree.report.ext.modules.barcode.Barcode;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Encodes a string into code93 specifications
 *
 * Symbols allowed: 0-9 A-Z $ % * + - . / space
 * Start character: yes, in the symbols table (*)
 * Stop character: yes, in the symbols table (*)
 * Check character: obligatory
 *
 * Also know as:
 * - USS-93
 *
 * @author Mimil
 */
public class Barcode93 extends Barcode {

    /**First checksum character*/
    private char checkSumC;
    /**Second checksum character*/
    private char checkSumK;
    /**special character '$'*/
    protected static char ch1 = 0x80;
    /**special character '%'*/
    protected static char ch2 = 0x81;
    /**special character '/'*/
    protected static char ch3 = 0x82;
    /**special character '+'*/
    protected static char ch4 = 0x83;

    /**Allowed characters*/
    protected static String CHARTABLE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%"+ch1+ch2+ch3+ch4+"*";

    /**Table holding symbols to be drawn*/
    protected ArrayList codeTable;

    private boolean showCheckSum = false;
    /**Allowed symbols*/
    protected static byte TABLE[][] = {
        {1,0,0,0,1,0,1,0,0},
        {1,0,1,0,0,1,0,0,0},
        {1,0,1,0,0,0,1,0,0},
        {1,0,1,0,0,0,0,1,0},
        {1,0,0,1,0,1,0,0,0},
        {1,0,0,1,0,0,1,0,0},
        {1,0,0,1,0,0,0,1,0},
        {1,0,1,0,1,0,0,0,0},
        {1,0,0,0,1,0,0,1,0},
        {1,0,0,0,0,1,0,1,0},
        {1,1,0,1,0,1,0,0,0},
        {1,1,0,1,0,0,1,0,0},
        {1,1,0,1,0,0,0,1,0},
        {1,1,0,0,1,0,1,0,0},
        {1,1,0,0,1,0,0,1,0},
        {1,1,0,0,0,1,0,1,0},
        {1,0,1,1,0,1,0,0,0},
        {1,0,1,1,0,0,1,0,0},
        {1,0,1,1,0,0,0,1,0},
        {1,0,0,1,1,0,1,0,0},
        {1,0,0,0,1,1,0,1,0},
        {1,0,1,0,1,1,0,0,0},
        {1,0,1,0,0,1,1,0,0},
        {1,0,1,0,0,0,1,1,0},
        {1,0,0,1,0,1,1,0,0},
        {1,0,0,0,1,0,1,1,0},
        {1,1,0,1,1,0,1,0,0},
        {1,1,0,1,1,0,0,1,0},
        {1,1,0,1,0,1,1,0,0},
        {1,1,0,1,0,0,1,1,0},
        {1,1,0,0,1,0,1,1,0},
        {1,1,0,0,1,1,0,1,0},
        {1,0,1,1,0,1,1,0,0},
        {1,0,1,1,0,0,1,1,0},
        {1,0,0,1,1,0,1,1,0},
        {1,0,0,1,1,1,0,1,0},
        {1,0,0,1,0,1,1,1,0},
        {1,1,1,0,1,0,1,0,0},
        {1,1,1,0,1,0,0,1,0},
        {1,1,1,0,0,1,0,1,0},
        {1,0,1,1,0,1,1,1,0},
        {1,0,1,1,1,0,1,1,0},
        {1,1,0,1,0,1,1,1,0},
        {1,0,0,1,0,0,1,1,0},
        {1,1,1,0,1,1,0,1,0},
        {1,1,1,0,1,0,1,1,0},
        {1,0,0,1,1,0,0,1,0},
        {1,0,1,0,1,1,1,1,0}
    };

    /**
     * Creates a new instance of Barcode93, can only be used by derivated class
     */
    protected Barcode93() {}

    /**
     * Creates a new instance of Barcode93
     */
    public Barcode93(String code) {
        super(code);

        this.codeTable = new ArrayList();
        int checkC = 0;
        int checkK = 0;

        this.codeTable.add(TABLE[CHARTABLE.indexOf('*')]);   //add the start character

        for( int i=0; i<code.length(); i++) {

            int index = CHARTABLE.indexOf(code.charAt(i));

            if( (index < 0) || (index == CHARTABLE.length()-1)) {    //not found or '*' found
                throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 93.");
            }else {
                this.codeTable.add(TABLE[index]);

                checkC += index * ((code.length()-i)%20);
                checkK += index * ((code.length()-i+1)%15);
            }
        }

        checkC = checkC%47;
        checkK += checkC;
        checkK = checkK%47;

        this.checkSumC = CHARTABLE.charAt(checkC);
        this.codeTable.add(TABLE[checkC]);

        this.checkSumK = CHARTABLE.charAt(checkK%47);
        this.codeTable.add(TABLE[checkK]);


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
    public void draw(Graphics2D graphics2D, Rectangle2D rectangle2D) {
        this.init(graphics2D, rectangle2D);

        //9 bars for each characters
        //plus the last bar
        int nbChars = this.codeTable.size();
        float barcodeWidth = this.getMinWidth() * (nbChars*9 + 1);

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
                if(currentByte[j]==0) {  // spaces
                    xPointer += this.getMinWidth();
                } else if(currentByte[j]==1) {   //blacks
                    graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));
                    xPointer += this.getMinWidth();
                }
            }
        }
        graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));
    }

    /**
     * Returns the code of this barcode according to its options.
     *
     * @return  The corresponding code
     */
    public String getCode() {
        if(this.showCheckSum) {
            return super.getCode()+this.getCheckSumC()+this.getCheckSumK();
        } else {
            return super.getCode();
        }
    }

    /**
     * Returns the first checksum character for this barcode
     *
     * @return the check character
     */
    public char getCheckSumC() {
        return checkSumC;
    }

    /**
     * Returns the second checksum character for this barcode
     *
     * @return the check character
     */
    public char getCheckSumK() {
        return checkSumK;
    }

    /**
     * Tells if checksum characters have to be shown in <code>getCode</code>
     *
     * @return Boolean
     */
    public boolean isShowCheckSum() {
        return showCheckSum;
    }

    /**
     * Activates, or not, the print of check characters in <code>getCode</code>
     *
     * @param showCheckSum Boolean
     */
    public void setShowCheckSum(boolean showCheckSum) {
        this.showCheckSum = showCheckSum;
    }

}
