package org.jfree.report.ext.modules.barcode.base;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jfree.report.ext.modules.barcode.Barcode;
import org.jfree.report.ext.modules.barcode.BarcodeIllegalLengthException;

/**
 * Encodes a string into EAN13 specifications
 * <p/>
 * Symbols allowed: 0-9 Start character: yes Stop character: yes Check character:
 * obligatory Code lenght limit: 12 data characters
 *
 * @author Mimil
 */
public class BarcodeEAN13 extends Barcode
        {

  /**
   * Allowed characters
   */
  protected static final String CHARTABLE = "0123456789";

  /**
   * Start symbol
   */
  protected static final byte START[] = {1, 0, 1};

  /**
   * Center symbol
   */
  protected static final byte CENTER[] = {0, 1, 0, 1, 0};

  /**
   * Stop symbol
   */
  protected static final byte STOP[] = {1, 0, 1};

  private char checkSum;

  /**
   * Table holding symbols to be drawn
   */
  private ArrayList codeTable;

  /**
   * Allowed symbols in the right part the the barcode
   */
  protected static final byte RIGHTTABLE[][] = {
    {1, 1, 1, 0, 0, 1, 0},
    {1, 1, 0, 0, 1, 1, 0},
    {1, 1, 0, 1, 1, 0, 0},
    {1, 0, 0, 0, 0, 1, 0},
    {1, 0, 1, 1, 1, 0, 0},
    {1, 0, 0, 1, 1, 1, 0},
    {1, 0, 1, 0, 0, 0, 0},
    {1, 0, 0, 0, 1, 0, 0},
    {1, 0, 0, 1, 0, 0, 0},
    {1, 1, 1, 0, 1, 0, 0}
  };

  /**
   * Allowed symbols in the left part the the barcode, version A (odd parity)
   */
  protected static final byte LEFTATABLE[][] = {//odd parity
    {0, 0, 0, 1, 1, 0, 1},
    {0, 0, 1, 1, 0, 0, 1},
    {0, 0, 1, 0, 0, 1, 1},
    {0, 1, 1, 1, 1, 0, 1},
    {0, 1, 0, 0, 0, 1, 1},
    {0, 1, 1, 0, 0, 0, 1},
    {0, 1, 0, 1, 1, 1, 1},
    {0, 1, 1, 1, 0, 1, 1},
    {0, 1, 1, 0, 1, 1, 1},
    {0, 0, 0, 1, 0, 1, 1}
  };

  /**
   * Allowed symbols in the left part the the barcode, version B (even parity)
   */
  protected static final byte LEFTBTABLE[][] = {//even paroty
    {0, 1, 0, 0, 1, 1, 1},
    {0, 1, 1, 0, 0, 1, 1},
    {0, 0, 1, 1, 0, 1, 1},
    {0, 1, 0, 0, 0, 0, 1},
    {0, 0, 1, 1, 1, 0, 1},
    {0, 1, 1, 1, 0, 0, 1},
    {0, 0, 0, 0, 1, 0, 1},
    {0, 0, 1, 0, 0, 0, 1},
    {0, 0, 0, 1, 0, 0, 1},
    {0, 0, 1, 0, 1, 1, 1}
  };

  /**
   * Allowed parity symbols
   */
  protected static final byte PARITY[][] = {//1 = odd and 0 = even
    {1, 1, 1, 1, 1, 1},
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
   * Creates a new instance of BarcodeEAN13 , can only be used by derivated class
   */
  protected BarcodeEAN13 ()
  {
    this.codeTable = new ArrayList();
  }

  protected ArrayList getCodeTable ()
  {
    return codeTable;
  }

  /**
   * Creates a new instance of BarcodeEAN13
   */
  public BarcodeEAN13 (final String code)
  {
    super(code);
    if (code.length() != 12)
    {
      throw new BarcodeIllegalLengthException(this, 12, 12);  //must have a lenght of 12 digits
    }


    int check = 0;
    byte currentParity[] = {};

    this.codeTable = new ArrayList();
    this.codeTable.add(START);   //add the start character

    for (int i = 0; i < code.length(); i++)
    {

      final int index = CHARTABLE.indexOf(code.charAt(i));

      if (index < 0)
      {//not found
        throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code EAN 13.");
      }
      else
      {
        if (i < 7)
        { //the number system digit + the manufacturer code  (the left part)
          if (i != 0)
          {
            if (currentParity[i - 1] == 1)
            {   //use odd parity table  (A)
              this.codeTable.add(LEFTATABLE[index]);
            }
            else
            { //else even parity table  (B)
              this.codeTable.add(LEFTBTABLE[index]);
            }
          }
          else
          {    //set firstDigit
            currentParity = PARITY[index];
          }

        }
        else
        {    //the product code  (the right part)
          if (i == 7)
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
   * Draws the barcode representation of the code on <code>graphics2D</code> target
   * according to <code>rectangle2D</code> window bounds.
   *
   * @param graphics2D  The drawable target
   * @param rectangle2D The drawable bounds
   */
  public void draw (final Graphics2D graphics2D, final Rectangle2D rectangle2D)
  {
    this.init(graphics2D, rectangle2D);


    //(nbchar-3) * 7
    //+3 start
    //+3 stop
    //+5 center
    final int nbChars = this.codeTable.size();
    final float barcodeWidth = this.getMinWidth() * ((nbChars - 3) * 7) + 6 + 5;


    final Rectangle2D barcodeArea = this.computeBarcodeArea(rectangle2D, barcodeWidth);
    final Rectangle2D codeArea = this.computeCodeArea(rectangle2D, (float) barcodeArea.getMaxY());
    final Rectangle2D fullArea = this.createFullArea(rectangle2D, codeArea, barcodeArea);

    this.printFullArea(graphics2D, fullArea);

    if (this.isShowCode())
    {
      this.printCode(graphics2D, fullArea, codeArea);
    }

    //now lets draw the barcode
    graphics2D.setColor(this.getBarcodeColor());

    final Point2D p = this.getBarcodePoint(fullArea, barcodeArea);
    float xPointer = (float) p.getX();
    final float yPointer = (float) p.getY();



    //LETS DRAW NOW I SAID !
    for (int i = 0; i < nbChars; i++)
    {
      final byte[] currentByte = (byte[]) codeTable.get(i);
      for (int j = 0; j < currentByte.length; j++)
      {
        if (currentByte[j] == 1)
        { //bar
          graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));

          xPointer += this.getMinWidth();
        }
        else
        {    //space
          xPointer += this.getMinWidth();
        }

      }
    }
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

  /**
   * Returns the code of this barcode according to its options.
   *
   * @return The corresponding code
   */
  public String getCode ()
  {
    return super.getCode() + this.getCheckSum();
  }
}
