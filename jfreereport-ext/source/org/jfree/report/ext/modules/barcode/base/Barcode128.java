package org.jfree.report.ext.modules.barcode.base;

import org.jfree.report.ext.modules.barcode.Barcode;
import org.jfree.report.util.Log;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: 7 avr. 2004
 * Time: 00:31:55
 * To change this template use File | Settings | File Templates.
 */
public class Barcode128 extends Barcode{

    private static byte[] STOP = {2, 3, 3, 1, 1, 1, 2};

    private ArrayList codeTable;

    private static byte[][] TABLE = {
        {2, 1, 2, 2, 2, 2},
        {2, 2, 2, 1, 2, 2},
        {2, 2, 2, 2, 2, 1},
        {1, 2, 1, 2, 2, 3},
        {1, 2, 1, 3, 2, 2},
        {1, 3, 1, 2, 2, 2},
        {1, 2, 2, 2, 1, 3},
        {1, 2, 2, 3, 1, 2},
        {1, 3, 2, 2, 1, 2},
        {2, 2, 1, 2, 1, 3},
        {2, 2, 1, 3, 1, 2},
        {2, 3, 1, 2, 1, 2},
        {1, 1, 2, 2, 3, 2},
        {1, 2, 2, 1, 3, 2},
        {1, 2, 2, 2, 3, 1},
        {1, 1, 3, 2, 2, 2},
        {1, 2, 3, 1, 2, 2},
        {1, 2, 3, 2, 2, 1},
        {2, 2, 3, 2, 1, 1},
        {2, 2, 1, 1, 3, 2},
        {2, 2, 1, 2, 3, 1},
        {2, 1, 3, 2, 1, 2},
        {2, 2, 3, 1, 1, 2},
        {3, 1, 2, 1, 3, 1},
        {3, 1, 1, 2, 2, 2},
        {3, 2, 1, 1, 2, 2},
        {3, 2, 1, 2, 2, 1},
        {3, 1, 2, 2, 1, 2},
        {3, 2, 2, 1, 1, 2},
        {3, 2, 2, 2, 1, 1},
        {2, 1, 2, 1, 2, 3},
        {2, 1, 2, 3, 2, 1},
        {2, 3, 2, 1, 2, 1},
        {1, 1, 1, 3, 2, 3},
        {1, 3, 1, 1, 2, 3},
        {1, 3, 1, 3, 2, 1},
        {1, 1, 2, 3, 1, 3},
        {1, 3, 2, 1, 1, 3},
        {1, 3, 2, 3, 1, 1},
        {2, 1, 1, 3, 1, 3},
        {2, 3, 1, 1, 1, 3},
        {2, 3, 1, 3, 1, 1},
        {1, 1, 2, 1, 3, 3},
        {1, 1, 2, 3, 3, 1},
        {1, 3, 2, 1, 3, 1},
        {1, 1, 3, 1, 2, 3},
        {1, 1, 3, 3, 2, 1},
        {1, 3, 3, 1, 2, 1},
        {3, 1, 3, 1, 2, 1},
        {2, 1, 1, 3, 3, 1},
        {2, 3, 1, 1, 3, 1},
        {2, 1, 3, 1, 1, 3},
        {2, 1, 3, 3, 1, 1},
        {2, 1, 3, 1, 3, 1},
        {3, 1, 1, 1, 2, 3},
        {3, 1, 1, 3, 2, 1},
        {3, 3, 1, 1, 2, 1},
        {3, 1, 2, 1, 1, 3},
        {3, 1, 2, 3, 1, 1},
        {3, 3, 2, 1, 1, 1},
        {3, 1, 4, 1, 1, 1},
        {2, 2, 1, 4, 1, 1},
        {4, 3, 1, 1, 1, 1},
        {1, 1, 1, 2, 2, 4},
        {1, 1, 1, 4, 2, 2},
        {1, 2, 1, 1, 2, 4},
        {1, 2, 1, 4, 2, 1},
        {1, 4, 1, 1, 2, 2},
        {1, 4, 1, 2, 2, 1},
        {1, 1, 2, 2, 1, 4},
        {1, 1, 2, 4, 1, 2},
        {1, 2, 2, 1, 1, 4},
        {1, 2, 2, 4, 1, 1},
        {1, 4, 2, 1, 1, 2},
        {1, 4, 2, 2, 1, 1},
        {2, 4, 1, 2, 1, 1},
        {2, 2, 1, 1, 1, 4},
        {4, 1, 3, 1, 1, 1},
        {2, 4, 1, 1, 1, 2},
        {1, 3, 4, 1, 1, 1},
        {1, 1, 1, 2, 4, 2},
        {1, 2, 1, 1, 4, 2},
        {1, 2, 1, 2, 4, 1},
        {1, 1, 4, 2, 1, 2},
        {1, 2, 4, 1, 1, 2},
        {1, 2, 4, 2, 1, 1},
        {4, 1, 1, 2, 1, 2},
        {4, 2, 1, 1, 1, 2},
        {4, 2, 1, 2, 1, 1},
        {2, 1, 2, 1, 4, 1},
        {2, 1, 4, 1, 2, 1},
        {4, 1, 2, 1, 2, 1},
        {1, 1, 1, 1, 4, 3},
        {1, 1, 1, 3, 4, 1},
        {1, 3, 1, 1, 4, 1},
        {1, 1, 4, 1, 1, 3},
        {1, 1, 4, 3, 1, 1},
        {4, 1, 1, 1, 1, 3},
        {4, 1, 1, 3, 1, 1},
        {1, 1, 3, 1, 4, 1},
        {1, 1, 4, 1, 3, 1},
        {3, 1, 1, 1, 4, 1},
        {4, 1, 1, 1, 3, 1},
        {2, 1, 1, 4, 1, 2},
        {2, 1, 1, 2, 1, 4},
        {2, 1, 1, 2, 3, 2}
    };

    private static int CODESET_AB_TO_C = 99;
    private static int CODESET_BC_TO_A = 101;
    private static int CODESET_CA_TO_B = 100;
    private static int SHIFT_AB = 98;
    private static int START_A = 103;
    private static int START_B = 104;
    private static int START_C = 105;



    /**
     * @deprecated
     * @param code
     */ 
    public Barcode128(String code) {
        super(code);
        this.codeTable = new ArrayList();

        int start = this.getFirstFourCharType(code);
        this.codeTable.add(TABLE[start]);   //the start character

        int currentSet;
        if(start == START_B) {
            currentSet = CODESET_CA_TO_B;
        } else if(start == START_C) {
            currentSet = CODESET_AB_TO_C;
        } else {    //START A
            currentSet = CODESET_BC_TO_A;
        }

        int count = start;  //the checksum



        for(int i=0, index=1; i<code.length(); i++, index++) {
            char tmpChar = code.charAt(i);
            int tmpCharInt = 0;

            if (tmpChar>=0 && tmpChar<128) {

                int tmpSet = this.getNextFourCharType(i, code, currentSet);
                int tmpCharSet = this.needToShift(tmpChar, currentSet);

                if(tmpSet != currentSet) {  //need to change the codeset
                    this.codeTable.add(TABLE[tmpSet]);
                    count += index*tmpSet;
                    index++;
                }
                currentSet = tmpSet;

                if(currentSet != CODESET_AB_TO_C) {
                    if(tmpCharSet != currentSet) {  // add the shift if necessary
                        this.codeTable.add(TABLE[tmpCharSet]);
                        count += index*tmpCharSet;
                        index++;
                    }
                    tmpCharInt = this.getCharIndex(tmpChar, tmpCharSet);
                    this.codeTable.add(TABLE[tmpCharInt]);
                    count += index*tmpCharInt;

                }else { //codeset C
                    tmpCharInt = this.getNumberIndex(code, i);
                    this.codeTable.add(TABLE[tmpCharInt]);
                    count += index*tmpCharInt;
                    index++;
                    i+=1;

                    /*tmpCharInt = this.getNumberIndex(code, i);
                    this.codeTable.add(TABLE[tmpCharInt]);
                    count += index*tmpCharInt;
                    i+=1;*/
                }
                /* } else { //add the char with the current codeset
                if(tmpCharSet != currentSet) {
                this.codeTable.add(TABLE[tmpCharSet]);
                }else {

                }
                }   */


            }else {
                Log.warn("The character '" + code.charAt(i) + "' is illegal in code 128.");
                throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 128.");
            }
        }

        this.codeTable.add(TABLE[count%103]);

        this.codeTable.add(STOP);

        super.setCode(code);

    }

    public void draw(Graphics2D graphics2D, Rectangle2D rectangle2D) {
        this.init(graphics2D, rectangle2D);



        //(nbchar-1)*11
        //13 for the stop
        int nbChars = this.codeTable.size();
        System.out.println(nbChars+"++++\n");
        float barcodeWidth = this.getMinWidth() * ((nbChars-1)*11 + 13);

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

        //LETS DRAW NOW I SAID !
        for(int i=0; i<nbChars; i++) {
            byte[] currentByte = (byte[])codeTable.get(i);
            for(int j=0; j< currentByte.length; j++) {
                if (j%2 == 0) { //even == bar
                    graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth()*currentByte[j], this.getMinHeight()));

                    xPointer += this.getMinWidth()*currentByte[j];
                } else {  //odd == space
                    xPointer += this.getMinWidth()*currentByte[j];
                }
            }
        }



    }


    private int getNumberIndex(String str, int pointer) {
        return Integer.parseInt(str.substring(pointer, pointer+2));
    }

    private int getCharIndex(char currentChar, int currentCodeSet) {
        if (currentCodeSet == CODESET_CA_TO_B) {
            return currentChar+32;
        }

        if(currentCodeSet == CODESET_BC_TO_A && currentChar<33) {
            return currentChar+64;
        }else {
            return currentChar+32;
        }
    }

    private int needToShift(char currentChar, int currentCodeSet) {
        if(currentCodeSet != CODESET_AB_TO_C) { //shift is not avalaible to CODESET C
            if(currentChar < 32 && currentCodeSet == CODESET_CA_TO_B) { //then shift
                return SHIFT_AB;
            }else if(currentChar < 32 && currentCodeSet == CODESET_BC_TO_A) {
                return currentCodeSet;
            }else if(currentChar >= 32 && currentCodeSet == CODESET_CA_TO_B) {
                return currentCodeSet;
            }else if(currentChar >= 32 && currentCodeSet == CODESET_BC_TO_A){
                return SHIFT_AB;
            }

        }/*else {
        return CODESET_AB_TO_C;
        } */

        return currentCodeSet; //CODE C
    }

    private int getNextFourCharType(int startIndex, String str, int currentSet) {
        //String exp = str.substring(startIndex, startIndex+4);
        String exp = str.substring(startIndex);

        if(Pattern.matches("^\\d{4,}", exp)) {
            return CODESET_AB_TO_C;
        }

        if(Pattern.matches("^\\W{4,}", exp)) {
            return CODESET_BC_TO_A;
        }

        //return CODESET_CA_TO_B;
        return currentSet;

    }

    private int getFirstFourCharType(String str) {
        //String exp = str.substring(0, 4);

        if(Pattern.matches("^\\d{4,}", str)) {
            System.out.println("------STARTC");
            return START_C;
        }

        if(Pattern.matches("^\\W{4,}", str)) {
            System.out.println("------STARTA");
            return START_A;
        }


        System.out.println("------STARTB");

        return START_B;
    }


}
