package org.jfree.report.ext.modules.barcode.base;

import java.util.ArrayList;

import org.jfree.report.ext.modules.barcode.BarcodeIllegalLengthException;
import org.jfree.util.Log;

/**
 * Encodes a string into UPCE specifications
 * <p/>
 * Symbols allowed: 0-9 Start character: yes Stop character: yes Check character:
 * obligatory Code lenght limit: 12 data characters
 *
 * @author Mimil
 */
public class BarcodeUPCE extends BarcodeEAN13
{

  /**
   * Allowed symbols, used when the code starts by a '0'
   */
  protected static final byte PARITY0[][] = {//1 = odd, 0 = even, if code starts by a '0'
    {0, 0, 0, 1, 1, 1},
    {0, 0, 1, 0, 1, 1},
    {0, 0, 1, 1, 0, 1},
    {0, 0, 1, 1, 1, 0},
    {0, 1, 0, 0, 1, 1},
    {0, 1, 1, 0, 0, 1},
    {0, 1, 1, 1, 0, 0},
    {0, 1, 0, 1, 0, 1},
    {0, 1, 0, 1, 1, 0},
    {0, 1, 1, 0, 1, 0}
  };

  /**
   * Allowed symbols, used when the code starts by a '1'
   */
  protected static final byte PARITY1[][] = {//1 = odd, 0 = even, if code starts by a '1'
    {1, 1, 1, 0, 0, 0},
    {1, 1, 0, 1, 0, 0},
    {1, 1, 0, 0, 1, 0},
    {1, 1, 0, 0, 0, 1},
    {1, 0, 1, 1, 0, 0},
    {1, 0, 0, 1, 1, 0},
    {1, 0, 0, 0, 1, 1},
    {1, 0, 1, 0, 1, 0},
    {1, 0, 1, 0, 0, 1},
    {1, 0, 0, 1, 0, 1}
  };

  /**
   * Stop symbol
   */
  protected static final byte STOP[] = {1};

  /**
   * Creates a new instance of BarcodeUPCE, can only be used by derivated class
   */
  protected BarcodeUPCE ()
  {
  }

  /**
   * Creates a new instance of BarcodeUPCE
   *
   * @deprecated
   */
  public BarcodeUPCE (String code)
  {
    super(code);
    final int check = this.getCheckSum() - 47;

    final ArrayList codeTable = getCodeTable();

    if (code.length() != 7)
    {
      throw new BarcodeIllegalLengthException(this, 7, 7);
    }

    final int firstChar = code.charAt(0);
    if ((firstChar == '0' || firstChar == '1') == false)
    {
      throw new IllegalArgumentException("Barcode UPC-E must start by a '0' or a '1'.");
    }

    byte currentParity[];
    if (firstChar == '0')
    {
      currentParity = PARITY0[check];
    }
    else
    {
      currentParity = PARITY1[check];
    }


    codeTable.add(START);   //add the start character

    code = code.substring(1);   //remove the leading '0' or '1'

    for (int i = 0; i < code.length(); i++)
    {
      final int index = CHARTABLE.indexOf(code.charAt(i));
      if (index < 0)
      {
        //not found
        Log.warn("The character '" + code.charAt(i) + "' is illegal in code UPC-E.");
        throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code UPC-E.");
      }
      else
      {
        //if(i < 7) { //the number system digit + the manufacturer code  (the left part)
        if (i != 0)
        {
          if (currentParity[i - 1] == 1)
          {   //use odd parity table  (A)
            codeTable.add(LEFTATABLE[index]);
          }
          else
          { //else even parity table  (B)
            codeTable.add(LEFTBTABLE[index]);
          }
        }
        else
        {    //set firstDigit
          currentParity = PARITY[index];
        }

        //} else {    //the product code  (the right part)
        //if(i == 7) {    //center guard
        //this.codeTable.add(CENTER);
        //}
        //this.codeTable.add(RIGHTTABLE[index]);
        //}


      }

    }
    //this.codeTable.add(RIGHTTABLE[check]);
    codeTable.add(CENTER);
    codeTable.add(STOP);

    super.setCode(code);



    /*int firstChar = 0;

    if(code == null) {
        throw new NullPointerException("Barcode code must be not null.");
    }
    if(code.length() != 7) {
        throw new BarcodeIllegalLenghtException(this, 7, 7);
    }
    if((firstChar = (code.charAt(0))-47) != (0 | 1)) {
        throw new IllegalArgumentException("Barcode UPC-E must start by a '0' or a '1'.");
    }


    int check = 0;

    byte currentParity[] = {};

    this.codeTable = new ArrayList();

    this.codeTable.add(START);   //add the start character

    for( int i=0; i<code.length(); i++) {   //we have to know the check sum to choose the parity encoding

        int index = CHARTABLE.indexOf(code.charAt(i));

        if(index < 0) {//not found
            Log.warn("The character '" + code.charAt(i) + "' is illegal in code UPC-E.");
            throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code UPC-E.");

        }else {
            //compute the check digit
            if(code.length()%2 == 1) {
                if(i%2 == 0) {
                    check += index*3;
                } else {
                    check += index;
                }
            } else {
                if(i%2 == 1) {
                    check += index*3;
                } else {
                    check += index;
                }
            }
        }
    }

    check = check%10;
    if( check != 0) {
        check = 10-check;
    }
    if(firstChar == 0) {
        currentParity = PARITY0[check];
    } else {
        currentParity = PARITY1[check];
    }

    this.checkSum = (char)(check+48);

    code = code.substring(1);   //remove the leading '0' or '1'



    for( int i=0; i<code.length(); i++) {

        int index = CHARTABLE.indexOf(code.charAt(i));

        if(index < 0) {//not found
            Log.warn("The character '" + code.charAt(i) + "' is illegal in code EAN 13.");
            throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code EAN 13.");

        }else {
            //if(i < 7) { //the number system digit + the manufacturer code  (the left part)
            if(i != 0) {
                if(currentParity[i-1] == 1) {   //use odd parity table  (A)
                    this.codeTable.add(LEFTATABLE[index]);
                }else { //else even parity table  (B)
                    this.codeTable.add(LEFTBTABLE[index]);
                }
            } else {    //set firstDigit
                currentParity = PARITY[index];
            }

            //} else {    //the product code  (the right part)
            //if(i == 7) {    //center guard
            //this.codeTable.add(CENTER);
            //}
            //this.codeTable.add(RIGHTTABLE[index]);
            //}


        }

    }
    //this.codeTable.add(RIGHTTABLE[check]);
    this.codeTable.add(CENTER);
    this.codeTable.add(STOP);

    super.setCode(code);*/
  }
  /*
  public char getCheckSum() {
      return checkSum;
  } */
}
