package org.jfree.report.ext.modules.barcode.base;

import org.jfree.report.ext.modules.barcode.Barcode;
import org.jfree.report.util.Log;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
//todo: use derivation! now i can
/**
 * Encodes a string into code25 interleaved specifications
 *
 * Symbols allowed: 0-9
 * Start character: yes
 * Stop character: yes
 * Check character: available
 *
 * Also know as:
 * - interleaved 2of5
 * - ITF25
 *
 * @author Mimil
 */
public class Barcode25Interleaved extends Barcode{

    /**Start symbol*/
    protected static byte STARTInterleaved[] = {1,0,1,0};
    /**Stop symbol*/
    protected static byte STOPInterleaved[] = {1,1,0,1};

    private char checkSum;

    private boolean checkSumNeeded = false;

    private float narrowToWideMultiplier = 3;

    private boolean showCheckSum = false;

    private boolean leadingZero = false;

    /**Table holding symbols to be drawn*/
    protected ArrayList codeTable;

    /**Allowed characters*/
    protected static String CHARTABLE = "0123456789";

    /**Allowed symbols*/
    protected static byte TABLE[][] = {
        {0,0,1,1,0},//0
        {1,0,0,0,1},
        {0,1,0,0,1},
        {1,1,0,0,0},
        {0,0,1,0,1},
        {1,0,1,0,0},
        {0,1,1,0,0},
        {0,0,0,1,1},
        {1,0,0,1,0},
        {0,1,0,1,0}//9
    };

    /**
     * Creates a new instance of Barcode25 Interleaved, can only be used by derivated class
     */
    protected Barcode25Interleaved() {}

    /**
     * Creates a new instance of Barcode25 Interleaved
     */
    public Barcode25Interleaved(String code) {
        super(code);
        int check = 0;

        this.codeTable = new ArrayList();


        if(code.length()%2 == 0) {  //if it is even then we add a leading '0' to make even the code + the checksum
            code = "0" + code;
            this.leadingZero = true;
        }

        for( int i=0; i<code.length(); i++) {

            int index = CHARTABLE.indexOf(code.charAt(i));

            if( index < 0) {//not found
                throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 25 Interleaved.");
            }else {
                this.codeTable.add(TABLE[index]);
                if(i%2 == 0) {
                    check += index*3;
                } else {
                    check += index;
                }
            }
        }

        int tmpCheck=check%10;
        if( tmpCheck == 0) {
            this.checkSum = (char)(tmpCheck+48);
            //this.codeTable.add(TABLE[tmpCheck]);
        } else {
            this.checkSum = (char)(10-tmpCheck+48);
            //this.codeTable.add(TABLE[10-tmpCheck]);
        }

        this.codeTable = this.mergeThemAll();

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


        //nbchar * ((2wide + 3narrow )*2(merge) +4start +4stop)
        int nbChars = this.codeTable.size();
        float barcodeWidth = this.getMinWidth() * ((nbChars-2)*(2*this.getNarrowToWideMultiplier() + 3)*2 +4 +4);


        Rectangle2D barcodeArea = this.computeBarcodeArea(rectangle2D, barcodeWidth);
        Rectangle2D codeArea = this.computeCodeArea(rectangle2D, (float)barcodeArea.getMaxY());
        Rectangle2D fullArea = this.createFullArea( rectangle2D, codeArea, barcodeArea);

        this.printFullArea(graphics2D, fullArea);

        if(this.isShowCode()) {
            this.printCode(graphics2D, fullArea, codeArea);
        }

        //now lets draw the barcode
        graphics2D.setColor(this.getBarcodeColor());

        Point2D p = this.getBarcodePoint(fullArea, barcodeArea);
        float xPointer = (float)p.getX();
        float yPointer = (float)p.getY();

        //DRAW START SYMBOL
        byte[] startByte = (byte[])codeTable.get(0);
        for(int j=0; j< startByte.length; j++) {

            if(startByte[j]==1) { //bar
                graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));

                xPointer += this.getMinWidth();
            } else {    //space
                xPointer += this.getMinWidth();
            }
        }

        //LETS DRAW NOW I SAID !
        for(int i=1; i<(nbChars-1); i++) {
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
        }

        //DRAW STOP SYMBOL
        byte[] stopByte = (byte[])codeTable.get(this.codeTable.size()-1);
        for(int j=0; j< stopByte.length; j++) {

            if(stopByte[j]==1) { //bar
                graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));

                xPointer += this.getMinWidth();
            } else {    //space
                xPointer += this.getMinWidth();
            }
        }
    }

    /**
     * Creates a new array of symbols. This array will be composed of the <code>STARTInterleaved</code> symbol,
     * code25 symbols concatened by 2 and the <code>STOPInterleaved</code> symbol
     *
     * @return  The new array of this barcode
     */
    public ArrayList mergeThemAll() {
        ArrayList tmpCodeTable = new ArrayList();

        tmpCodeTable.add(STARTInterleaved);

        for(int i=0; i<(this.codeTable.size()); i+=2) {
            tmpCodeTable.add(this.merge((byte[])this.codeTable.get(i), (byte[])this.codeTable.get(i+1)));
        }

        tmpCodeTable.add(STOPInterleaved);

        return tmpCodeTable;
    }

    /**
     * Concatenates 2 code25 symbols (one byte of the first symbol then one of the second one,...)
     *
     * @param b1    First symbol
     * @param b2    Second symbol
     * @return  The new concatened symbol
     */
    public byte[] merge(byte[] b1, byte[] b2) {
        if(b1.length != b2.length) {
            throw new IllegalArgumentException("Symbols have different size, unable to merge them.");
        }

        byte[] result = new byte[b1.length + b2.length];

        for (int i=0, j=0; i<b1.length; i++, j+=2) {
            result[j] = b1[i];
            result[j+1] = b2[i];
        }

        return result;
    }

    /**
     * Returns the code of this barcode according to its options.
     *
     * @return  The corresponding code
     */
    public String getCode() {
        if(this.showCheckSum) {
            return super.getCode() + this.getCheckSum();
        }else {
            return super.getCode();
        }
    }

    /**
     * Tells if the checksum have to be used.
     *
     * @return Boolean
     */
    public boolean isCheckSumNeeded() {
        return this.checkSumNeeded;
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

    /**
     * Activates, or not, the use of the checksum character.
     *
     * @param needCheckSum  Boolean
     */
    public void setCheckSumNeeded(boolean needCheckSum) {
        String oldCode = this.getCode();

        if(this.isCheckSumNeeded() == false && needCheckSum == true) {
            //todo make the checksum fucntion
            Log.warn("Re-enable checkSum is not yet possible.");
        } else if(this.isCheckSumNeeded() == true && needCheckSum == false) {
            Log.warn("Removing the CheckSum.");

            this.codeTable.remove(this.codeTable.size()-1);
            if(this.leadingZero == true) {
                this.codeTable.remove(1);
                super.setCode(oldCode.substring(1, oldCode.length()-1));
            } else {
                this.codeTable.add(1,TABLE[0]);
                super.setCode("0" + oldCode.substring(0, oldCode.length()-1));
            }

            this.checkSumNeeded = false;
            this.showCheckSum = false;
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
}
