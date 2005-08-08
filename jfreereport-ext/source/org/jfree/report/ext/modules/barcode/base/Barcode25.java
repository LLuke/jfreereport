package org.jfree.report.ext.modules.barcode.base;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jfree.report.ext.modules.barcode.Barcode;
import org.jfree.util.Log;

/**
 * Encodes a string into code25 specifications
 * <p/>
 * Symbols allowed: 0-9 Start character: yes Stop character: yes Check character:
 * available
 * <p/>
 * Also know as: - 2of5 - standard 2of5 - industry 2of5
 *
 * @author Mimil
 */
public class Barcode25 extends Barcode
{
  /**
   * Charaters allowed
   */
  protected static final String CHARTABLE = "0123456789";

  /**
   * Stop symbol
   */
  protected static final byte START[] = {1, 1, 0, 1, 1, 0, 1, 0};

  /**
   * Start symbol
   */
  protected static final byte STOP[] = {1, 1, 0, 1, 0, 1, 1};

  private char checkSum;

  private float narrowToWideMultiplier = 3;

  private boolean checkSumNeeded = false;

  private boolean showCheckSum = false;

  /**
   * Table holding symbols to be drawn
   */
  private ArrayList codeTable;

  /**
   * Symbols allowed
   */
  protected static final byte TABLE[][] = {
    {0, 0, 1, 1, 0}, //0
    {1, 0, 0, 0, 1},
    {0, 1, 0, 0, 1},
    {1, 1, 0, 0, 0},
    {0, 0, 1, 0, 1},
    {1, 0, 1, 0, 0},
    {0, 1, 1, 0, 0},
    {0, 0, 0, 1, 1},
    {1, 0, 0, 1, 0},
    {0, 1, 0, 1, 0}//9
  };

  /**
   * Creates a new instance of Barcode25, can only be used by derivated class
   */
  protected Barcode25 ()
  {
  }

  /**
   * Creates a new instance of Barcode25
   */
  public Barcode25 (final String code)
  {
    super(code);

    int check = 0;

    this.codeTable = new ArrayList();

    this.codeTable.add(START);   //add the start character

    for (int i = 0; i < code.length(); i++)
    {

      final int index = CHARTABLE.indexOf(code.charAt(i));

      if (index < 0)
      {//not found
        throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 25.");
      }
      else
      {
        this.codeTable.add(TABLE[index]);
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
      //this.codeTable.add(TABLE[tmpCheck]);
    }
    else
    {
      this.checkSum = (char) (10 - tmpCheck + 48);
      //this.codeTable.add(TABLE[10-tmpCheck]);
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


    //nbchar * ((2wide + 3narrow + 6interWideToNarrow) -6start -7stop)
    final int nbChars = this.codeTable.size();
    final float barcodeWidth = this.getMinWidth() * ((nbChars - 2) * (2 * this.getNarrowToWideMultiplier() + 3 + 5) + 8 + 7);


    final Rectangle2D barcodeArea = this.computeBarcodeArea(rectangle2D, barcodeWidth);
    final Rectangle2D codeArea = this.computeCodeArea(rectangle2D, (float) barcodeArea.getMaxY());
    final Rectangle2D fullArea = this.createFullArea(rectangle2D, codeArea, barcodeArea);

    this.printFullArea(graphics2D, fullArea);

    /*if(this.isShowCode()) {
        this.printCode(graphics2D, fullArea, codeArea);
    } */

    //now lets draw the barcode
    graphics2D.setColor(this.getBarcodeColor());

    final Point2D p = this.getBarcodePoint(fullArea, barcodeArea);
    float xPointer = (float) p.getX();
    final float yPointer = (float) p.getY();

    //DRAW START SYMBOL
    final byte[] startByte = (byte[]) codeTable.get(0);
    for (int j = 0; j < startByte.length; j++)
    {

      if (startByte[j] == 1)
      { //bar
        graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));

        xPointer += this.getMinWidth();
      }
      else
      {    //space
        xPointer += this.getMinWidth();
      }
    }

    //LETS DRAW NOW I SAID !
    for (int i = 1; i < (nbChars - 1); i++)
    {
      final byte[] currentByte = (byte[]) codeTable.get(i);
      for (int j = 0; j < currentByte.length; j++)
      {

        if (currentByte[j] == 0)
        { //narrow
          graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));

          xPointer += this.getMinWidth();
        }
        else
        {    //wide
          graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth() * this.getNarrowToWideMultiplier(), this.getMinHeight()));

          xPointer += this.getMinWidth() * this.getNarrowToWideMultiplier();
        }

        xPointer += this.getMinWidth(); //inter character
      }
    }

    //DRAW STOP SYMBOL
    final byte[] stopByte = (byte[]) codeTable.get(this.codeTable.size() - 1);
    for (int j = 0; j < stopByte.length; j++)
    {

      if (stopByte[j] == 1)
      { //bar
        graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, this.getMinWidth(), this.getMinHeight()));

        xPointer += this.getMinWidth();
      }
      else
      {    //space
        xPointer += this.getMinWidth();
      }
    }


    if (this.isShowCode())
    {
      this.printCode(graphics2D, fullArea, codeArea);
    }

  }

  /**
   * Returns the code of this barcode according to its options.
   *
   * @return The corresponding code
   */
  public String getCode ()
  {
    if (this.showCheckSum)
    {
      return super.getCode() + this.getCheckSum();
    }
    else
    {
      return super.getCode();
    }

  }

  /**
   * Tells if the checksum have to be used.
   *
   * @return Boolean
   */
  public boolean isCheckSumNeeded ()
  {
    return checkSumNeeded;
  }

  /**
   * Activates, or not, the use of the checksum character.
   *
   * @param needCheckSum Boolean
   */
  public void setCheckSumNeeded (final boolean needCheckSum)
  {
    final String oldCode = super.getCode();

    if (this.checkSumNeeded == false && needCheckSum == true)
    {
      this.setCode(oldCode + this.getCheckSum());
      this.codeTable.add(this.codeTable.size() - 1, TABLE[CHARTABLE.indexOf(this.getCheckSum())]);
      this.checkSumNeeded = needCheckSum;
    }
    else if (this.checkSumNeeded == true && needCheckSum == false)
    {
      Log.warn("Removing the CheckSum.");
      this.codeTable.remove(this.codeTable.size() - 1);
      this.setCode(oldCode.substring(0, oldCode.length() - 1));
      this.checkSumNeeded = needCheckSum;
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
   * Tells if the checksun character will be shown in <code>getCode</code>.
   *
   * @return Boolean
   */
  public boolean isShowCheckSum ()
  {
    return showCheckSum;
  }

  /**
   * Activates, or not, the print of the checksum character.
   *
   * @param showCheckSum Boolean
   */
  public void setShowCheckSum (final boolean showCheckSum)
  {
    this.showCheckSum = showCheckSum;
  }

  /**
   * Returns the coeficient between narrow and wide bars.
   *
   * @return The coeficient
   */
  public float getNarrowToWideMultiplier ()
  {
    return narrowToWideMultiplier;
  }

  /**
   * Sets the multiplyer coefficient between narrow and wide bars for this barcode.
   *
   * @param narrowToWideMultiplier The new coeficient to set
   */
  public void setNarrowToWideMultiplier (final float narrowToWideMultiplier)
  {
    this.narrowToWideMultiplier = narrowToWideMultiplier;
  }
}
