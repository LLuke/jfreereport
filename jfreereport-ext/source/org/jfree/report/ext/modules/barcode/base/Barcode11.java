package org.jfree.report.ext.modules.barcode.base;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jfree.report.ext.modules.barcode.Barcode;
import org.jfree.report.util.Log;

/**
 * Encodes a string into code11 specifications
 * <p/>
 * Symbols allowed: 0-9 - Start character: yes Stop character: yes Check character:
 * obligatory
 * <p/>
 * Also know as: - USD-8
 *
 * @author Mimil
 */
public class Barcode11 extends Barcode
{

  /**
   * If the code size is bigger than <code>KTRIGGER</code> the checksumK character will be
   * appended
   */
  private static final int KTRIGGER = 10;
  /**
   * Charaters allowed
   */
  private static final String CHARTABLE = "0123456789-";
  /**
   * Start and Stop symbol
   */
  private static final byte STARTSTOP[] = {0, 0, 1, 1, 0};

  private float narrowToWideMultiplier = 2;
  /**
   * First checksum character
   */
  private char checkSumC;
  /**
   * Seconce checksum character
   */
  private char checkSumK;
  /**
   * Table holding symbols to be drawn
   */
  private ArrayList codeTable;

  private boolean showCheckSum = false;

  private boolean isCheckSumNeeded = false;
  /**
   * Symbols allowed
   */
  private static final byte TABLE[][] = {
    {0, 0, 0, 0, 1},
    {1, 0, 0, 0, 1},
    {0, 1, 0, 0, 1},
    {1, 1, 0, 0, 0},
    {0, 0, 1, 0, 1},
    {1, 0, 1, 0, 0},
    {0, 1, 1, 0, 0},
    {0, 0, 0, 1, 1},
    {1, 0, 0, 1, 0},
    {1, 0, 0, 0, 0},
    {0, 0, 1, 0, 0}
  };

  /**
   * Creates a new instance of Barcode11, can only be used by derivated class
   */
  protected Barcode11 ()
  {
    codeTable = new ArrayList();
    codeTable.add(STARTSTOP);   //add the start character
  }

  /**
   * Creates a new instance of Barcode11
   */
  public Barcode11 (final String code)
  {
    super(code);

    codeTable = new ArrayList();
    codeTable.add(STARTSTOP);   //add the start character

    int tmpC = 0;
    int tmpK = 0;


    for (int i = 0, weightC = 1, weightK = 0; i < code.length(); i++, weightC++, weightK++)
    {
      final int index = CHARTABLE.indexOf(code.charAt(i));

      if (index < 0)
      {    //not found
        throw new IllegalArgumentException("The character '" + code.charAt(i) + "' is illegal in code 11.");
      }
      else
      {
        this.codeTable.add(TABLE[index]);

        /*System.out.println(((code.length()-weightC)%10+1) +"x"+ index);
        System.out.println(((code.length()-weightK)%9+1) +"x"+ index+"\n");*/

        tmpC += index * (code.length() - weightC) % 10 + 1;
        tmpK += index * (code.length() - weightK) % 9 + 1;

        if (weightC == 10)
        {
          weightC = 0;
        }
        if (weightK == 9)
        {
          weightK = 0;
        }
      }
    }

    tmpC = tmpC % 11;
    tmpK += tmpC;
    tmpK = tmpK % 11;

    this.checkSumC = CHARTABLE.charAt(tmpC);
    this.checkSumK = CHARTABLE.charAt(tmpK);

    this.codeTable.add(STARTSTOP);      //add the stop character

    super.setCode(code);
  }


  /**
   * Draws the barcode representation of the code on <code>graphics2D</code> target
   * according to <code>rectangle2D</code> window bounds.
   *
   * @param graphics2D  The drawable target
   * @param drawBounds The drawable bounds
   */
  public void draw (final Graphics2D graphics2D, final Rectangle2D drawBounds)
  {
    init(graphics2D, drawBounds);

    //7 of width each character (maximum)
    //nbchar-1 for the intercharacter
    final int nbChars = codeTable.size();
    float barcodeWidth = getMinWidth() * (nbChars * 3 + nbChars * 2 * this.getNarrowToWideMultiplier() + (nbChars - 1));
    //remove 1 minwidth for '0', '9' or '-'
    final String tmpCode = super.getCode();
    for (int i = 0; i < tmpCode.length(); i++)
    {
      if ((tmpCode.charAt(i) == '0') || (tmpCode.charAt(i) == '9') || (tmpCode.charAt(i) == '-'))
      {
        barcodeWidth -= getNarrowToWideMultiplier() * getMinWidth();
        barcodeWidth += getMinWidth();
      }
    }

    final Rectangle2D barcodeArea = computeBarcodeArea(drawBounds, barcodeWidth);
    final Rectangle2D codeArea = computeCodeArea(drawBounds, (float) barcodeArea.getMaxY());
    final Rectangle2D fullArea = createFullArea(drawBounds, codeArea, barcodeArea);

    printFullArea(graphics2D, fullArea);

    if (isShowCode())
    {
      printCode(graphics2D, fullArea, codeArea);
    }

    //now lets draw the barcode
    graphics2D.setColor(getBarcodeColor());

    final Point2D p = getBarcodePoint(fullArea, barcodeArea);
    float xPointer = (float) p.getX();
    final float yPointer = (float) p.getY();

    //LETS DRAW NOW I SAID !
    for (int i = 0; i < nbChars; i++)
    {
      final byte[] currentByte = (byte[]) codeTable.get(i);
      for (int j = 0; j < currentByte.length; j++)
      {
        if (j % 2 == 0)
        { //even == bar
          if (currentByte[j] == 0)
          { //narrow
            graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, getMinWidth(), getMinHeight()));

            xPointer += getMinWidth();
          }
          else
          {    //wide
            graphics2D.fill(new Rectangle2D.Float(xPointer, yPointer, getMinWidth() * getNarrowToWideMultiplier(), getMinHeight()));

            xPointer += getMinWidth() * getNarrowToWideMultiplier();
          }
        }
        else
        {  //odd == space
          if (currentByte[j] == 0)
          { //narrow
            xPointer += getMinWidth();
          }
          else
          {    //wide
            xPointer += getMinWidth() * getNarrowToWideMultiplier();
          }
        }
      }

      xPointer += getMinWidth(); //inter-character
    }

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

  /**
   * Returns the code of this barcode according to its options.
   *
   * @return The corresponding code
   */
  public String getCode ()
  {
    if (showCheckSum)
    {
      if (super.getCode().length() >= KTRIGGER)
      {
        return super.getCode() + checkSumC + checkSumK;
      }
      else
      {
        return super.getCode() + checkSumC;
      }
    }
    else
    {
      return super.getCode();
    }
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
   * Activates, or not, the print of checksum characters.
   *
   * @param showCheckSum Boolean
   */
  public void setShowCheckSum (final boolean showCheckSum)
  {
    this.showCheckSum = showCheckSum;
  }

  /**
   * Returns the first checksum character for this code.
   *
   * @return The check character C
   */
  public char getCheckSumC ()
  {
    return checkSumC;
  }

  /**
   * Returns the second checksum character for this code.
   *
   * @return The check character K
   */
  public char getCheckSumK ()
  {
    return checkSumK;
  }

  /**
   * Tells if checksums have to be used.
   *
   * @return Boolean
   */
  public boolean isCheckSumNeeded ()
  {
    return isCheckSumNeeded;
  }

  /**
   * Activates, or not, the use of checksum characters.
   *
   * @param checkSumNeeded Boolean
   */
  public void setCheckSumNeeded (final boolean checkSumNeeded)
  {
    if (checkSumNeeded == true && isCheckSumNeeded == false)
    {
      if (super.getCode().length() >= KTRIGGER)
      {
        codeTable.add(codeTable.size() - 1, TABLE[checkSumC - 48]);
        codeTable.add(codeTable.size() - 1, TABLE[checkSumK - 48]);
      }
      else
      {
        codeTable.add(codeTable.size() - 1, TABLE[checkSumC - 48]);
      }
    }
    else if (checkSumNeeded == false && isCheckSumNeeded == true)
    {
      Log.warn("Removing the CheckSum.");
      if (super.getCode().length() >= KTRIGGER)
      {
        codeTable.remove(codeTable.size() - 1);
        codeTable.remove(codeTable.size() - 1);
      }
      else
      {
        codeTable.remove(codeTable.size() - 1);
      }
    }
    this.isCheckSumNeeded = checkSumNeeded;
  }
}
