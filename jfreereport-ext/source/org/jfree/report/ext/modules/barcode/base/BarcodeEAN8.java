package org.jfree.report.ext.modules.barcode.base;


import java.util.ArrayList;

import org.jfree.report.ext.modules.barcode.BarcodeIllegalLengthException;

/**
 * Encodes a string into EAN8 specifications
 * <p/>
 * Symbols allowed: 0-9 Start character: yes Stop character: yes Check character:
 * obligatory Code lenght limit: 7 data characters
 *
 * @author Mimil
 */
public class BarcodeEAN8 extends BarcodeEAN13
{

  private char checkSum;

  /**
   * Creates a new instance of BarcodeEAN8 , can only be used by derivated class
   */
  protected BarcodeEAN8 ()
  {
  }

  /**
   * Creates a new instance of BarcodeEAN8
   */
  public BarcodeEAN8 (final String code)
  {

    if (code == null)
    {
      throw new NullPointerException("Barcode code must be not null.");
    }
    if (code.length() != 7)
    {
      throw new BarcodeIllegalLengthException(this, 7, 7);
    }

    int check = 0;

    this.codeTable = new ArrayList();

    this.codeTable.add(START);   //add the start character


    for (int i = 0; i < code.length(); i++)
    {

      final int index = CHARTABLE.indexOf(code.charAt(i));

      if (index < 0)
      {//not found
        throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code EAN 8.");
      }
      else
      {
        if (i < 4)
        { //the number system digit + the manufacturer code  (the left part)
          this.codeTable.add(LEFTATABLE[index]);
        }
        else
        {    //the product code  (the right part)
          if (i == 4)
          {    //center guard
            this.codeTable.add(CENTER);
          }
          this.codeTable.add(RIGHTTABLE[index]);
        }

        //compute the check digit
        if (code.length() % 2 == 1)
        {
          if (i % 2 == 0)
          {
            check += index * 3;
          }
          else
          {
            check += index;
          }
        }
        else
        {
          if (i % 2 == 1)
          {
            check += index * 3;
          }
          else
          {
            check += index;
          }
        }
      }
    }

    final int tmpCheck = check % 10;
    if (tmpCheck == 0)
    {
      this.checkSum = (char) (tmpCheck + 48);
      this.codeTable.add(RIGHTTABLE[tmpCheck]);
    }
    else
    {
      this.checkSum = (char) (10 - tmpCheck + 48);
      this.codeTable.add(RIGHTTABLE[10 - tmpCheck]);
    }
    this.codeTable.add(STOP);      //add the stop character

    super.setCode(code);
  }

  /**
   * Returns the checksum character for this code.
   *
   * @return The check character
   */
  public char getCheckSum ()
  {
    return checkSum;
  }
}
