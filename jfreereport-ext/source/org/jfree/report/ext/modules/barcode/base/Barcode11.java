package org.jfree.report.ext.modules.barcode.base;

import org.jfree.report.ext.modules.barcode.Barcode;
import org.jfree.report.util.Log;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Encodes a string into code11 specifications
 *
 * Symbols allowed: 0-9 -
 * Start character: yes
 * Stop character: yes
 * Check character: obligatory
 *
 * Also know as:
 * - USD-8
 *
 * @author Mimil
 */
public class Barcode11 extends Barcode {

    /**If the code size is bigger than <code>KTRIGGER</code> the checksumK character will be appended*/
    protected static int KTRIGGER = 10;
    /**Charaters allowed*/
    protected static String CHARTABLE = "0123456789-";
    /**Start and Stop symbol*/
    protected static byte STARTSTOP[] = {0,0,1,1,0};

    private float narrowToWideMultiplier = 2;
    /**First checksum character*/
    private char checkSumC;
    /**Seconce checksum character*/
    private char checkSumK;
    /**Table holding symbols to be drawn*/
    protected ArrayList codeTable;

    private boolean showCheckSum = false;

    private boolean isCheckSumNeeded = false;
    /**Symbols allowed*/
    protected static byte TABLE[][] = {
        {0,0,0,0,1},
        {1,0,0,0,1},
        {0,1,0,0,1},
        {1,1,0,0,0},
        {0,0,1,0,1},
        {1,0,1,0,0},
        {0,1,1,0,0},
        {0,0,0,1,1},
        {1,0,0,1,0},
        {1,0,0,0,0},
        {0,0,1,0,0}
    };

    /**
     * Creates a new instance of Barcode11, can only be used by derivated class
     */
    protected Barcode11() {}

    /**
     * Creates a new instance of Barcode11
     */
    public Barcode11(String code) {
        super(code);

        this.codeTable = new ArrayList();
        int tmpC = 0;
        int tmpK = 0;

        this.codeTable.add(STARTSTOP);   //add the start character

        for( int i=0, weightC = 1, weightK = 0; i<code.length(); i++, weightC++, weightK++) {

            int index = CHARTABLE.indexOf(code.charAt(i));

            if(index < 0) {    //not found
                throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 11.");
            }else {
                this.codeTable.add(TABLE[index]);

                /*System.out.println(((code.length()-weightC)%10+1) +"x"+ index);
                System.out.println(((code.length()-weightK)%9+1) +"x"+ index+"\n");*/

                tmpC += index * (code.length()-weightC)%10+1;
                tmpK += index * (code.length()-weightK)%9+1;

                if(weightC==10){
                    weightC=0;
                }
                if(weightK==9){
                    weightK=0;
                }
            }
        }

        tmpC = tmpC%11;
        tmpK += tmpC;
        tmpK = tmpK%11;

        this.checkSumC = CHARTABLE.charAt(tmpC);
        this.checkSumK = CHARTABLE.charAt(tmpK);

        this.codeTable.add(STARTSTOP);      //add the stop character

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

        //7 of width each character (maximum)
        //nbchar-1 for the intercharacter
        int nbChars = this.codeTable.size();
        float barcodeWidth = this.getMinWidth() * (nbChars*3 +nbChars*2*this.getNarrowToWideMultiplier() + 1*(nbChars-1));
        //remove 1 minwidth for '0', '9' or '-'
        String tmpCode = super.getCode();
        for(int i=0; i<tmpCode.length(); i++) {
            if ((tmpCode.charAt(i)=='0') || (tmpCode.charAt(i)=='9') || (tmpCode.charAt(i)=='-')) {
                barcodeWidth -= this.getNarrowToWideMultiplier()*this.getMinWidth();
                barcodeWidth += this.getMinWidth();
            }
        }

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

    /**
     * Returns the code of this barcode according to its options.
     *
     * @return  The corresponding code
     */
    public String getCode() {
        if(this.showCheckSum) {
            if (super.getCode().length() >= KTRIGGER) {
                return super.getCode()+this.checkSumC+this.checkSumK;
            } else {
                return super.getCode()+this.checkSumC;
            }
        } else {
            return super.getCode();
        }
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
     * Activates, or not, the print of checksum characters.
     *
     * @param showCheckSum  Boolean
     */
    public void setShowCheckSum(boolean showCheckSum) {
        this.showCheckSum = showCheckSum;
    }

    /**
     * Returns the first checksum character for this code.
     *
     * @return  The check character C
     */
    public char getCheckSumC() {
        return checkSumC;
    }

    /**
     * Returns the second checksum character for this code.
     *
     * @return  The check character K
     */
    public char getCheckSumK() {
        return checkSumK;
    }

    /**
     * Tells if checksums have to be used.
     *
     * @return Boolean
     */
    public boolean isCheckSumNeeded() {
        return isCheckSumNeeded;
    }

    /**
     * Activates, or not, the use of checksum characters.
     *
     * @param checkSumNeeded  Boolean
     */
    public void setCheckSumNeeded(boolean checkSumNeeded) {
        if(checkSumNeeded == true && this.isCheckSumNeeded == false) {
            if(super.getCode().length() >= KTRIGGER) {
                this.codeTable.add(this.codeTable.size()-1, TABLE[this.checkSumC-48]);
                this.codeTable.add(this.codeTable.size()-1, TABLE[this.checkSumK-48]);
            } else {
                this.codeTable.add(this.codeTable.size()-1, TABLE[this.checkSumC-48]);
            }
        }else if(checkSumNeeded == false && this.isCheckSumNeeded == true) {
            Log.warn("Removing the CheckSum.");
            if(super.getCode().length() >= KTRIGGER) {
                this.codeTable.remove(this.codeTable.size()-1);
                this.codeTable.remove(this.codeTable.size()-1);
            } else {
                this.codeTable.remove(this.codeTable.size()-1);
            }
        }
    }
}
